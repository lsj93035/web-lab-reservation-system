package com.meeting.mapper;

import java.util.List;
import java.util.Date;
import org.apache.ibatis.annotations.Param;
import com.meeting.domain.SysTeachingOccupancy;

/**
 * 教学占用Mapper接口
 */
public interface SysTeachingOccupancyMapper {

    /**
     * 查询教学占用列表
     */
    List<SysTeachingOccupancy> selectOccupancyList(SysTeachingOccupancy sysTeachingOccupancy);

    /**
     * 查询冲突的教学占用（区间重叠判定）
     */
    List<SysTeachingOccupancy> queryConflictingOccupancy(@Param("labId") Integer labId, @Param("occupyDate") Date occupyDate, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 新增教学占用
     */
    int insertOccupancy(SysTeachingOccupancy sysTeachingOccupancy);

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
