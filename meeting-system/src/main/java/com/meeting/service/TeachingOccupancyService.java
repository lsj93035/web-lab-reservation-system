package com.meeting.service;

import java.util.List;
import com.meeting.domain.SysTeachingOccupancy;
import com.meeting.domain.dto.TeachingOccupancyAddDTO;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 教学占用Service接口
 */
public interface TeachingOccupancyService {

    /**
     * 查询教学占用列表
     */
    List<SysTeachingOccupancy> selectOccupancyList(SysTeachingOccupancy sysTeachingOccupancy);

    /**
     * 新增教学占用
     */
    AjaxResult insertOccupancy(TeachingOccupancyAddDTO dto, Long userId);

    /**
     * 删除教学占用
     */
    int deleteOccupancyById(Integer occupyId);

    /**
     * 批量删除教学占用
     */
    int deleteOccupancyByIds(Integer[] occupyIds);

    /**
     * 查询单个教学占用详情
     */
    SysTeachingOccupancy selectOccupancyById(Integer occupyId);
}
