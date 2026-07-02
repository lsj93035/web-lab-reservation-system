package com.meeting.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.meeting.domain.SysReservation;
import com.meeting.domain.dto.AuditDTO;
import com.meeting.domain.dto.MyReservationQueryDTO;
import com.meeting.domain.dto.ReservationAddDTO;
import com.meeting.service.ReservationService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 预约信息Controller
 */
@RestController
@RequestMapping("/reservation")
public class ReservationController extends BaseController {

    @Resource
    private ReservationService reservationService;

    /**
     * 查询可用实验室列表
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,student,teacher')")
    @GetMapping("/labSelect")
    public TableDataInfo labSelect(SysReservation sysReservation) {
        startPage();
        List<SysReservation> list = reservationService.selectLabSelect(sysReservation);
        return getDataTable(list);
    }

    /**
     * 查询预约列表
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,student,teacher')")
    @GetMapping("/list")
    public TableDataInfo list(SysReservation sysReservation) {
        startPage();
        List<SysReservation> list = reservationService.selectReservationList(sysReservation);
        return getDataTable(list);
    }

    /**
     * 查询某实验室某天的已预约时间段
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,student,teacher')")
    @GetMapping("/timeQueryByDate")
    public AjaxResult timeQueryByDate(@RequestParam Integer labId, @RequestParam String date) {
        List<SysReservation> list = reservationService.queryReservationTimes(labId, date);
        return AjaxResult.success(list);
    }

    /**
     * 提交预约
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,student,teacher')")
    @Log(title = "实验室预约", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ReservationAddDTO dto) {
        Long userId = SecurityUtils.getUserId();
        return reservationService.addReservation(dto, userId);
    }

    /**
     * 查询我的预约列表
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,student,teacher')")
    @GetMapping("/myList")
    public TableDataInfo myList(MyReservationQueryDTO queryDTO) {
        startPage();
        queryDTO.setUserId(SecurityUtils.getUserId());
        List<SysReservation> list = reservationService.listMyReservations(queryDTO);
        return getDataTable(list);
    }

    /**
     * 获取预约详细信息
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,student,teacher')")
    @GetMapping(value = "/{reserveId}")
    public AjaxResult getInfo(@PathVariable("reserveId") Integer reserveId) {
        return AjaxResult.success(reservationService.selectReservationById(reserveId));
    }

    /**
     * 修改预约
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,student,teacher')")
    @Log(title = "实验室预约", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReservationAddDTO dto) {
        Long userId = SecurityUtils.getUserId();
        return reservationService.updateReservation(dto, userId);
    }

    /**
     * 撤销预约
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,student,teacher')")
    @Log(title = "实验室预约", businessType = BusinessType.DELETE)
    @DeleteMapping("/{reserveId}")
    public AjaxResult remove(@PathVariable("reserveId") Integer reserveId) {
        Long userId = SecurityUtils.getUserId();
        return reservationService.cancelReservation(reserveId, userId);
    }

    /**
     * 查询与我有关的预约
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,student,teacher')")
    @GetMapping("/aboutMe")
    public TableDataInfo aboutMe() {
        startPage();
        Long userId = SecurityUtils.getUserId();
        List<SysReservation> list = reservationService.listAboutMeReservations(userId);
        return getDataTable(list);
    }

    /**
     * 查询待审核的预约列表
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,teacher')")
    @GetMapping("/pendingList")
    public TableDataInfo pendingList() {
        startPage();
        // 审批页面需查看所有用户的待审核预约，传 null 表示不过滤用户
        List<SysReservation> list = reservationService.listPendingReservations(null);
        return getDataTable(list);
    }

    /**
     * 审批预约
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,teacher')")
    @Log(title = "预约审批", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody AuditDTO dto) {
        Long userId = SecurityUtils.getUserId();
        return reservationService.auditReservation(dto, userId);
    }
}
