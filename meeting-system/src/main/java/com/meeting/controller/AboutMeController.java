package com.meeting.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.meeting.domain.SysReservation;
import com.meeting.service.ReservationService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 预约记录Controller
 */
@RestController
@RequestMapping("/aboutMe")
public class AboutMeController extends BaseController {

    @Resource
    private ReservationService reservationService;

    /**
     * 查询与我有关的预约
     */
    @PreAuthorize("@ss.hasPermi('lab:aboutme:list')")
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        Long userId = SecurityUtils.getUserId();
        List<SysReservation> list = reservationService.listAboutMeReservations(userId);
        return getDataTable(list);
    }

    /**
     * 获取预约详细信息
     */
    @PreAuthorize("@ss.hasPermi('lab:aboutme:query')")
    @GetMapping(value = "/{reserveId}")
    public AjaxResult getInfo(@PathVariable("reserveId") Integer reserveId) {
        return AjaxResult.success(reservationService.selectReservationById(reserveId));
    }
}
