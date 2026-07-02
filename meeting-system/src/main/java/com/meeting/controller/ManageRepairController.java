package com.meeting.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.meeting.domain.SysRepair;
import com.meeting.domain.dto.EditRepairDTO;
import com.meeting.service.RepairService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;

/**
 * 维修管理Controller
 */
@RestController
@RequestMapping("/manage-repair")
public class ManageRepairController extends BaseController {

    @Resource
    private RepairService repairService;

    /**
     * 查询报修列表
     */
    @PreAuthorize("@ss.hasPermi('lab:managerepair:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysRepair sysRepair) {
        startPage();
        List<SysRepair> list = repairService.listManageRepair(sysRepair);
        return getDataTable(list);
    }

    /**
     * 修改报修状态
     */
    @PreAuthorize("@ss.hasPermi('lab:managerepair:edit')")
    @Log(title = "维修管理", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public AjaxResult editStatus(@RequestBody EditRepairDTO dto) {
        return toAjax(repairService.updateRepairStatus(dto));
    }

    /**
     * 导出报修列表
     */
    @PreAuthorize("@ss.hasPermi('lab:managerepair:export')")
    @Log(title = "维修管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysRepair sysRepair) {
        List<SysRepair> list = repairService.listManageRepair(sysRepair);
        // Excel导出逻辑由框架处理
    }
}
