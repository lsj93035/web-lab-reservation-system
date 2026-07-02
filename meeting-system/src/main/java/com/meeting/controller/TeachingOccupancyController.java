package com.meeting.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.meeting.domain.SysTeachingOccupancy;
import com.meeting.domain.dto.TeachingOccupancyAddDTO;
import com.meeting.service.TeachingOccupancyService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 教学占用Controller
 */
@RestController
@RequestMapping("/teaching")
public class TeachingOccupancyController extends BaseController {

    @Resource
    private TeachingOccupancyService teachingOccupancyService;

    /**
     * 查询教学占用列表
     */
    @PreAuthorize("@ss.hasPermi('lab:teaching:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysTeachingOccupancy sysTeachingOccupancy) {
        startPage();
        List<SysTeachingOccupancy> list = teachingOccupancyService.selectOccupancyList(sysTeachingOccupancy);
        return getDataTable(list);
    }

    /**
     * 新增教学占用
     */
    @PreAuthorize("@ss.hasPermi('lab:teaching:add')")
    @Log(title = "教学占用", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TeachingOccupancyAddDTO dto) {
        Long userId = SecurityUtils.getUserId();
        return teachingOccupancyService.insertOccupancy(dto, userId);
    }

    /**
     * 删除教学占用
     */
    @PreAuthorize("@ss.hasPermi('lab:teaching:remove')")
    @Log(title = "教学占用", businessType = BusinessType.DELETE)
    @DeleteMapping("/{occupyIds}")
    public AjaxResult remove(@PathVariable Integer[] occupyIds) {
        return toAjax(teachingOccupancyService.deleteOccupancyByIds(occupyIds));
    }

    /**
     * 获取教学占用详细信息
     */
    @PreAuthorize("@ss.hasPermi('lab:teaching:query')")
    @GetMapping(value = "/{occupyId}")
    public AjaxResult getInfo(@PathVariable("occupyId") Integer occupyId) {
        return AjaxResult.success(teachingOccupancyService.selectOccupancyById(occupyId));
    }
}
