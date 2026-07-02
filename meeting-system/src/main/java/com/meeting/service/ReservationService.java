package com.meeting.service;

import java.util.List;
import com.meeting.domain.SysReservation;
import com.meeting.domain.dto.AuditDTO;
import com.meeting.domain.dto.MyReservationQueryDTO;
import com.meeting.domain.dto.ReservationAddDTO;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 预约信息Service接口
 */
public interface ReservationService {

    /**
     * 查询可用实验室列表
     */
    List<SysReservation> selectLabSelect(SysReservation sysReservation);

    /**
     * 查询预约列表
     */
    List<SysReservation> selectReservationList(SysReservation sysReservation);

    /**
     * 查询某实验室某天的已预约时间段
     */
    List<SysReservation> queryReservationTimes(Integer labId, String date);

    /**
     * 提交预约
     */
    AjaxResult addReservation(ReservationAddDTO dto, Long userId);

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
    AjaxResult updateReservation(ReservationAddDTO dto, Long userId);

    /**
     * 撤销预约
     */
    AjaxResult cancelReservation(Integer reserveId, Long userId);

    /**
     * 查询与我有关的预约
     */
    List<SysReservation> listAboutMeReservations(Long userId);

    /**
     * 查询待审核的预约列表
     */
    List<SysReservation> listPendingReservations(Long userId);

    /**
     * 审批预约
     */
    AjaxResult auditReservation(AuditDTO dto, Long userId);
}
