package com.meeting.mapper;

import java.util.List;
import java.util.Date;
import org.apache.ibatis.annotations.Param;
import com.meeting.domain.SysReservation;
import com.meeting.domain.dto.MyReservationQueryDTO;

/**
 * 预约信息Mapper接口
 */
public interface SysReservationMapper {

    /**
     * 查询可用实验室列表（用于预约选择）
     */
    List<SysReservation> selectLabSelect(SysReservation sysReservation);

    /**
     * 查询预约列表
     */
    List<SysReservation> selectReservationList(SysReservation sysReservation);

    /**
     * 查询某实验室某天的已预约时间段
     */
    List<SysReservation> queryReservationTimes(@Param("labId") Integer labId, @Param("date") Date date);

    /**
     * 查询冲突的预约记录
     */
    List<SysReservation> queryConflictingReservations(@Param("labId") Integer labId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 查询冲突的预约记录（排除自身，用于修改）
     */
    List<SysReservation> queryConflictingReservationsExcludeSelf(@Param("labId") Integer labId, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("reserveId") Integer reserveId);

    /**
     * 新增预约
     */
    int insertReservation(SysReservation sysReservation);

    /**
     * 查询我的预约列表
     */
    List<SysReservation> listMyReservations(MyReservationQueryDTO queryDTO);

    /**
     * 查询单个预约详情
     */
    SysReservation selectReservationById(Integer reserveId);

    /**
     * 修改预约
     */
    int updateReservation(SysReservation sysReservation);

    /**
     * 删除预约
     */
    int deleteReservationById(Integer reserveId);

    /**
     * 查询与我有关的预约（我创建的）
     */
    List<SysReservation> listAboutMeReservations(Long userId);

    /**
     * 查询待审核的预约列表（教师审批用）
     */
    List<SysReservation> listPendingReservations(Long userId);

    /**
     * 审批预约
     */
    int auditReservation(SysReservation sysReservation);

    /**
     * 查询某实验室的预约数量
     */
    int countReservationsByLabId(Integer labId);

    /**
     * 统计待审批预约数量
     */
    int countPendingAudit();

    /**
     * 统计用户预约数量
     */
    int countByUserId(@Param("userId") Long userId);

    /**
     * 统计今日预约数量
     */
    int countTodayReservations();
}
