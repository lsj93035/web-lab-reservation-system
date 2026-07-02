package com.meeting.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.meeting.mapper.SysLabMapper;
import com.meeting.mapper.SysReservationMapper;
import com.meeting.mapper.SysTeachingOccupancyMapper;
import com.meeting.domain.SysLab;
import com.meeting.domain.SysReservation;
import com.meeting.domain.SysTeachingOccupancy;
import com.meeting.domain.dto.AuditDTO;
import com.meeting.domain.dto.MyReservationQueryDTO;
import com.meeting.domain.dto.ReservationAddDTO;
import com.meeting.service.ReservationService;
import com.meeting.utils.RedisLock;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 预约信息Service业务层处理
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    @Resource
    private SysReservationMapper sysReservationMapper;

    @Resource
    private SysLabMapper sysLabMapper;

    @Resource
    private SysTeachingOccupancyMapper sysTeachingOccupancyMapper;

    @Resource
    private RedisLock redisLock;

    @Override
    public List<SysReservation> selectLabSelect(SysReservation sysReservation) {
        return sysReservationMapper.selectLabSelect(sysReservation);
    }

    @Override
    public List<SysReservation> selectReservationList(SysReservation sysReservation) {
        return sysReservationMapper.selectReservationList(sysReservation);
    }

    @Override
    public List<SysReservation> queryReservationTimes(Integer labId, String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = sdf.parse(date);
            return sysReservationMapper.queryReservationTimes(labId, parsedDate);
        } catch (Exception e) {
            throw new RuntimeException("日期解析失败", e);
        }
    }

    @Override
    @Transactional
    public AjaxResult addReservation(ReservationAddDTO dto, Long userId) {
        // 1. 参数校验
        if (dto.getLabId() == null) {
            return AjaxResult.error("请选择实验室");
        }
        if (dto.getStartDate() == null || dto.getEndDate() == null) {
            return AjaxResult.error("请选择预约时间");
        }
        if (dto.getStartDate().after(dto.getEndDate())) {
            return AjaxResult.error("开始时间不能晚于结束时间");
        }
        if (dto.getStartDate().before(new Date())) {
            return AjaxResult.error("预约时间不能早于当前时间");
        }

        // 2. 检查实验室是否存在且状态正常
        SysLab lab = sysLabMapper.selectLabById(dto.getLabId());
        if (lab == null) {
            return AjaxResult.error("实验室不存在");
        }
        if (lab.getStatus() != null && lab.getStatus() == 1) {
            return AjaxResult.error("实验室正在维修中，无法预约");
        }

        // 3. 检查教学占用冲突（教学占用有最高优先级）
        // 将 startDate 转为纯日期（去掉时分秒），匹配 DB 中 DATE 类型的 occupy_date 列
        Date occupyDate;
        try {
            SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
            occupyDate = dateOnly.parse(dateOnly.format(dto.getStartDate()));
        } catch (Exception e) {
            return AjaxResult.error("日期解析失败");
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String startTime = timeFormat.format(dto.getStartDate());
        String endTime = timeFormat.format(dto.getEndDate());
        List<SysTeachingOccupancy> occupancyList = sysTeachingOccupancyMapper.queryConflictingOccupancy(
                dto.getLabId(), occupyDate, startTime, endTime);
        if (occupancyList != null && !occupancyList.isEmpty()) {
            return AjaxResult.error("该时段已被教学占用，无法预约");
        }

        // 4. 使用Redis分布式锁防止并发冲突
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String lockName = sdf.format(dto.getStartDate()) + dto.getLabId();
        boolean locked = redisLock.tryLock("reservation:" + lockName, 10000);
        if (!locked) {
            return AjaxResult.error("系统繁忙，请稍后重试");
        }

        try {
            // 5. 双重检查：再次查询冲突
            List<SysReservation> conflicts = sysReservationMapper.queryConflictingReservations(
                    dto.getLabId(), dto.getStartDate(), dto.getEndDate());
            if (conflicts != null && !conflicts.isEmpty()) {
                return AjaxResult.error("该时段已被预约，请选择其他时间");
            }

            // 6. 插入预约记录
            SysReservation reservation = new SysReservation();
            reservation.setUserId(userId.intValue());
            reservation.setLabId(dto.getLabId());
            reservation.setStartDate(dto.getStartDate());
            reservation.setEndDate(dto.getEndDate());
            reservation.setAuditStatus(0); // 待审核
            reservation.setRemark(dto.getRemark());
            reservation.setCreateBy(String.valueOf(userId));

            int rows = sysReservationMapper.insertReservation(reservation);
            if (rows > 0) {
                return AjaxResult.success("预约提交成功，等待审核");
            }
            return AjaxResult.error("预约提交失败");
        } finally {
            redisLock.unlock("reservation:" + lockName);
        }
    }

    @Override
    public List<SysReservation> listMyReservations(MyReservationQueryDTO queryDTO) {
        return sysReservationMapper.listMyReservations(queryDTO);
    }

    @Override
    public SysReservation selectReservationById(Integer reserveId) {
        return sysReservationMapper.selectReservationById(reserveId);
    }

    @Override
    @Transactional
    public AjaxResult updateReservation(ReservationAddDTO dto, Long userId) {
        if (dto.getReserveId() == null) {
            return AjaxResult.error("预约编号不能为空");
        }

        // 查询原预约
        SysReservation old = sysReservationMapper.selectReservationById(dto.getReserveId());
        if (old == null) {
            return AjaxResult.error("预约记录不存在");
        }
        if (!old.getUserId().equals(userId.intValue())) {
            return AjaxResult.error("只能修改自己的预约");
        }
        if (old.getAuditStatus() != null && old.getAuditStatus() != 0) {
            return AjaxResult.error("只能修改待审核状态的预约");
        }

        // 如果时间有变化，检查冲突
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            if (!dto.getStartDate().equals(old.getStartDate()) || !dto.getEndDate().equals(old.getEndDate())) {
                List<SysReservation> conflicts = sysReservationMapper.queryConflictingReservationsExcludeSelf(
                        dto.getLabId(), dto.getStartDate(), dto.getEndDate(), dto.getReserveId());
                if (conflicts != null && !conflicts.isEmpty()) {
                    return AjaxResult.error("该时段已被预约，请选择其他时间");
                }
            }
        }

        SysReservation reservation = new SysReservation();
        reservation.setReserveId(dto.getReserveId());
        reservation.setStartDate(dto.getStartDate());
        reservation.setEndDate(dto.getEndDate());
        reservation.setRemark(dto.getRemark());
        reservation.setUpdateBy(String.valueOf(userId));

        int rows = sysReservationMapper.updateReservation(reservation);
        if (rows > 0) {
            return AjaxResult.success("修改成功");
        }
        return AjaxResult.error("修改失败");
    }

    @Override
    public AjaxResult cancelReservation(Integer reserveId, Long userId) {
        SysReservation old = sysReservationMapper.selectReservationById(reserveId);
        if (old == null) {
            return AjaxResult.error("预约记录不存在");
        }
        if (!old.getUserId().equals(userId.intValue())) {
            return AjaxResult.error("只能撤销自己的预约");
        }
        if (old.getAuditStatus() != null && old.getAuditStatus() != 0) {
            return AjaxResult.error("只能撤销待审核状态的预约");
        }

        int rows = sysReservationMapper.deleteReservationById(reserveId);
        if (rows > 0) {
            return AjaxResult.success("撤销成功");
        }
        return AjaxResult.error("撤销失败");
    }

    @Override
    public List<SysReservation> listAboutMeReservations(Long userId) {
        return sysReservationMapper.listAboutMeReservations(userId);
    }

    @Override
    public List<SysReservation> listPendingReservations(Long userId) {
        return sysReservationMapper.listPendingReservations(userId);
    }

    @Override
    public AjaxResult auditReservation(AuditDTO dto, Long userId) {
        if (dto.getReserveId() == null) {
            return AjaxResult.error("预约编号不能为空");
        }
        if (dto.getAuditStatus() == null || (dto.getAuditStatus() != 1 && dto.getAuditStatus() != 2)) {
            return AjaxResult.error("审核状态无效");
        }

        SysReservation old = sysReservationMapper.selectReservationById(dto.getReserveId());
        if (old == null) {
            return AjaxResult.error("预约记录不存在");
        }
        if (old.getAuditStatus() != null && old.getAuditStatus() != 0) {
            return AjaxResult.error("该预约已被审核");
        }

        SysReservation reservation = new SysReservation();
        reservation.setReserveId(dto.getReserveId());
        reservation.setAuditStatus(dto.getAuditStatus());
        reservation.setUpdateBy(String.valueOf(userId));

        int rows = sysReservationMapper.auditReservation(reservation);
        if (rows > 0) {
            String msg = dto.getAuditStatus() == 1 ? "审核通过" : "审核驳回";
            return AjaxResult.success(msg);
        }
        return AjaxResult.error("审核失败");
    }

}
