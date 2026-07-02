package com.meeting.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.meeting.domain.SysRepair;
import com.meeting.domain.dto.MyRepairQueryDTO;

/**
 * 设备报修Mapper接口
 */
public interface SysRepairMapper {

    /**
     * 新增报修
     */
    int insertRepair(SysRepair sysRepair);

    /**
     * 查询我的报修列表
     */
    List<SysRepair> listMyRepair(MyRepairQueryDTO queryDTO);

    /**
     * 查询管理报修列表（管理员用）
     */
    List<SysRepair> listManageRepair(SysRepair sysRepair);

    /**
     * 查询单个报修详情
     */
    SysRepair selectRepairById(Integer repairId);

    /**
     * 修改报修状态
     */
    int updateRepairStatus(SysRepair sysRepair);

    /**
     * 删除报修
     */
    int deleteRepairById(Integer repairId);

    /**
     * 统计待处理报修数量
     */
    int countPendingRepair();

    /**
     * 统计用户报修数量
     */
    int countByUserId(@Param("userId") Long userId);
}
