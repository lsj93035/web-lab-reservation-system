package com.ruoyi.web.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.LabEquipmentRepair;
import com.ruoyi.system.service.ILabEquipmentRepairService;

/**
 * 设备报修Controller
 */
@RestController
@RequestMapping("/system/repair")
public class LabEquipmentRepairController extends BaseController
{
    @Autowired
    private ILabEquipmentRepairService labEquipmentRepairService;

    /**
     * 查询设备报修列表
     */
    @GetMapping("/list")
    public TableDataInfo list(LabEquipmentRepair labEquipmentRepair)
    {
        startPage();
        List<LabEquipmentRepair> list = labEquipmentRepairService.selectLabEquipmentRepairList(labEquipmentRepair);
        return getDataTable(list);
    }

    /**
     * 新增保存设备报修
     */
    @PostMapping
    public AjaxResult add(@RequestBody LabEquipmentRepair labEquipmentRepair)
    {
        return toAjax(labEquipmentRepairService.insertLabEquipmentRepair(labEquipmentRepair));
    }

    /**
     * 修改保存设备报修
     */
    @PutMapping
    public AjaxResult edit(@RequestBody LabEquipmentRepair labEquipmentRepair)
    {
        return toAjax(labEquipmentRepairService.updateLabEquipmentRepair(labEquipmentRepair));
    }
}
