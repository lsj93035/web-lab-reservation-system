package com.meeting.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.meeting.domain.SysLab;
import com.meeting.service.LabService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;

/**
 * 实验室信息Controller
 */
@RestController
@RequestMapping("/lab")
public class LabController extends BaseController {

    @Resource
    private LabService labService;

    /**
     * 查询实验室列表
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,student,teacher')")
    @GetMapping("/list")
    public TableDataInfo list(SysLab sysLab) {
        startPage();
        List<SysLab> list = labService.selectLabList(sysLab);
        return getDataTable(list);
    }

    /**
     * 获取实验室详细信息
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,student,teacher')")
    @GetMapping(value = "/{labId}")
    public AjaxResult getInfo(@PathVariable("labId") Integer labId) {
        return AjaxResult.success(labService.selectLabById(labId));
    }

    /**
     * 新增实验室
     */
    @PreAuthorize("@ss.hasPermi('lab:lab:add')")
    @Log(title = "实验室管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysLab sysLab) {
        return toAjax(labService.insertLab(sysLab));
    }

    /**
     * 修改实验室
     */
    @PreAuthorize("@ss.hasPermi('lab:lab:edit')")
    @Log(title = "实验室管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysLab sysLab) {
        return toAjax(labService.updateLab(sysLab));
    }

    /**
     * 删除实验室
     */
    @PreAuthorize("@ss.hasPermi('lab:lab:remove')")
    @Log(title = "实验室管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{labIds}")
    public AjaxResult remove(@PathVariable Integer[] labIds) {
        return toAjax(labService.deleteLabByIds(labIds));
    }

    /**
     * 修改实验室状态
     */
    @PreAuthorize("@ss.hasPermi('lab:lab:edit')")
    @Log(title = "实验室管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysLab sysLab) {
        return toAjax(labService.changeLabStatus(sysLab));
    }

    /**
     * 获取楼宇列表
     */
    @GetMapping("/buildingList")
    public AjaxResult getBuildingList() {
        return AjaxResult.success(labService.selectBuildingList());
    }
}
