package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LabEquipmentRepairMapper;
import com.ruoyi.system.domain.LabEquipmentRepair;
import com.ruoyi.system.service.ILabEquipmentRepairService;

/**
 * 设备报修Service业务层处理实现类
 */
@Service
public class LabEquipmentRepairServiceImpl implements ILabEquipmentRepairService 
{
    @Autowired
    private LabEquipmentRepairMapper labEquipmentRepairMapper;

    /**
     * 查询设备报修
     */
    @Override
    public LabEquipmentRepair selectLabEquipmentRepairById(Long repairId)
    {
        return labEquipmentRepairMapper.selectLabEquipmentRepairById(repairId);
    }

    /**
     * 查询设备报修列表
     */
    @Override
    public List<LabEquipmentRepair> selectLabEquipmentRepairList(LabEquipmentRepair labEquipmentRepair)
    {
        return labEquipmentRepairMapper.selectLabEquipmentRepairList(labEquipmentRepair);
    }

    /**
     * 新增设备报修
     */
    @Override
    public int insertLabEquipmentRepair(LabEquipmentRepair labEquipmentRepair)
    {
        labEquipmentRepair.setCreateTime(DateUtils.getNowDate());
        return labEquipmentRepairMapper.insertLabEquipmentRepair(labEquipmentRepair);
    }

    /**
     * 修改设备报修
     */
    @Override
    public int updateLabEquipmentRepair(LabEquipmentRepair labEquipmentRepair)
    {
        labEquipmentRepair.setUpdateTime(DateUtils.getNowDate());
        return labEquipmentRepairMapper.updateLabEquipmentRepair(labEquipmentRepair);
    }
}