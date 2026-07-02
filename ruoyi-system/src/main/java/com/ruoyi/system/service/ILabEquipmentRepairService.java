package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.LabEquipmentRepair;

/**
 * 设备报修Service接口
 */
public interface ILabEquipmentRepairService 
{
    /** 查询报修记录 */
    public LabEquipmentRepair selectLabEquipmentRepairById(Long repairId);

    /** 查询报修列表 */
    public List<LabEquipmentRepair> selectLabEquipmentRepairList(LabEquipmentRepair labEquipmentRepair);

    /** 新增报修 */
    public int insertLabEquipmentRepair(LabEquipmentRepair labEquipmentRepair);

    /** 修改报修（用于维修处理） */
    public int updateLabEquipmentRepair(LabEquipmentRepair labEquipmentRepair);
}