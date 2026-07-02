package com.meeting.service.impl;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.meeting.domain.SysReservation;
import com.meeting.mapper.SysReservationMapper;
import com.meeting.mapper.SysTeachingOccupancyMapper;
import com.meeting.domain.SysTeachingOccupancy;
import com.meeting.domain.dto.TeachingOccupancyAddDTO;
import com.meeting.service.TeachingOccupancyService;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 教学占用Service业务层处理
 */
@Service
public class TeachingOccupancyServiceImpl implements TeachingOccupancyService {

    @Resource
    private SysTeachingOccupancyMapper sysTeachingOccupancyMapper;

    @Resource
    private SysReservationMapper sysReservationMapper;

    @Override
    public List<SysTeachingOccupancy> selectOccupancyList(SysTeachingOccupancy sysTeachingOccupancy) {
        return sysTeachingOccupancyMapper.selectOccupancyList(sysTeachingOccupancy);
    }

    @Override
    public AjaxResult insertOccupancy(TeachingOccupancyAddDTO dto, Long userId) {
        // 1. 参数校验
        if (dto.getLabId() == null) {
            return AjaxResult.error("请选择实验室");
        }
        if (dto.getCourseName() == null || dto.getCourseName().trim().isEmpty()) {
            return AjaxResult.error("请输入课程名称");
        }
        if (dto.getOccupyDate() == null) {
            return AjaxResult.error("请选择占用日期");
        }
        if (dto.getTimeSlot() == null || dto.getTimeSlot().trim().isEmpty()) {
            return AjaxResult.error("请输入使用时段");
        }

        // 2. 检查是否与已有教学占用冲突（区间重叠判定）
        String[] parts = dto.getTimeSlot().split("-");
        String startTime = parts.length > 0 ? parts[0].trim() : "";
        String endTime = parts.length > 1 ? parts[1].trim() : "";
        List<SysTeachingOccupancy> conflicts = sysTeachingOccupancyMapper.queryConflictingOccupancy(
                dto.getLabId(), dto.getOccupyDate(), startTime, endTime);
        if (conflicts != null && !conflicts.isEmpty()) {
            return AjaxResult.error("该时段已有教学占用");
        }

        // 2.5 反向检查：与已通过/待审核预约是否冲突
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(dto.getOccupyDate());
            Date resStart = sdf.parse(dateStr + " " + startTime);
            Date resEnd = sdf.parse(dateStr + " " + endTime);
            List<SysReservation> reservationConflicts = sysReservationMapper.queryConflictingReservations(
                    dto.getLabId(), resStart, resEnd);
            if (reservationConflicts != null && !reservationConflicts.isEmpty()) {
                return AjaxResult.error("该教室在此时间段已有预约，无法添加教学占用");
            }
        } catch (Exception e) {
            return AjaxResult.error("时间格式解析失败");
        }

        // 3. 插入教学占用记录
        SysTeachingOccupancy occupancy = new SysTeachingOccupancy();
        occupancy.setTeacherId(userId.intValue());
        occupancy.setLabId(dto.getLabId());
        occupancy.setCourseName(dto.getCourseName());
        occupancy.setOccupyDate(dto.getOccupyDate());
        occupancy.setTimeSlot(dto.getTimeSlot());
        occupancy.setCreateBy(String.valueOf(userId));

        int rows = sysTeachingOccupancyMapper.insertOccupancy(occupancy);
        if (rows > 0) {
            return AjaxResult.success("添加成功");
        }
        return AjaxResult.error("添加失败");
    }

    @Override
    public int deleteOccupancyById(Integer occupyId) {
        return sysTeachingOccupancyMapper.deleteOccupancyById(occupyId);
    }

    @Override
    public int deleteOccupancyByIds(Integer[] occupyIds) {
        return sysTeachingOccupancyMapper.deleteOccupancyByIds(occupyIds);
    }

    @Override
    public SysTeachingOccupancy selectOccupancyById(Integer occupyId) {
        return sysTeachingOccupancyMapper.selectOccupancyById(occupyId);
    }
}
