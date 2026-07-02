package com.meeting.service;

import java.util.List;
import com.meeting.domain.SysRepair;
import com.meeting.domain.dto.EditRepairDTO;
import com.meeting.domain.dto.MyRepairQueryDTO;

/**
 * 设备报修Service接口
 */
public interface RepairService {

    /**
     * 新增报修
     */
    int insertRepair(SysRepair sysRepair);

    /**
     * 查询我的报修列表
     */
    List<SysRepair> listMyRepair(MyRepairQueryDTO queryDTO);

    /**
     * 查询管理报修列表
     */
    List<SysRepair> listManageRepair(SysRepair sysRepair);

    /**
     * 查询单个报修详情
     */
    SysRepair selectRepairById(Integer repairId);

    /**
     * 修改报修状态
     */
    int updateRepairStatus(EditRepairDTO dto);

    /**
     * 删除报修
     */
    int deleteRepairById(Integer repairId);
}
