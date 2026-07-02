package com.meeting.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.meeting.domain.SysRepair;
import com.meeting.domain.dto.MyRepairQueryDTO;
import com.meeting.service.RepairService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 设备报修Controller
 */
@RestController
@RequestMapping("/repair")
public class RepairController extends BaseController {

    @Resource
    private RepairService repairService;

    /**
     * 提交报修
     */
    @PreAuthorize("@ss.hasPermi('lab:repair:add')")
    @Log(title = "设备报修", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysRepair sysRepair) {
        Long userId = SecurityUtils.getUserId();
        sysRepair.setUserId(userId.intValue());
        sysRepair.setCreateBy(String.valueOf(userId));
        return toAjax(repairService.insertRepair(sysRepair));
    }

    /**
     * 查询我的报修列表
     */
    @PreAuthorize("@ss.hasPermi('lab:myrepair:list')")
    @GetMapping("/myList")
    public TableDataInfo myList(MyRepairQueryDTO queryDTO) {
        startPage();
        queryDTO.setUserId(SecurityUtils.getUserId());
        List<SysRepair> list = repairService.listMyRepair(queryDTO);
        return getDataTable(list);
    }

    /**
     * 获取报修详细信息
     */
    @PreAuthorize("@ss.hasPermi('lab:myrepair:query')")
    @GetMapping(value = "/{repairId}")
    public AjaxResult getInfo(@PathVariable("repairId") Integer repairId) {
        return AjaxResult.success(repairService.selectRepairById(repairId));
    }

    /**
     * 导出我的报修列表
     */
    @PreAuthorize("@ss.hasPermi('lab:myrepair:export')")
    @Log(title = "我的报修", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(MyRepairQueryDTO queryDTO) {
        queryDTO.setUserId(SecurityUtils.getUserId());
        List<SysRepair> list = repairService.listMyRepair(queryDTO);
        // Excel导出逻辑由框架处理
    }
}
