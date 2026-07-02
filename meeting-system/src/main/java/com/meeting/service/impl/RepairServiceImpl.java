package com.meeting.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.meeting.mapper.SysLabMapper;
import com.meeting.mapper.SysRepairMapper;
import com.meeting.domain.SysLab;
import com.meeting.domain.SysRepair;
import com.meeting.domain.dto.EditRepairDTO;
import com.meeting.domain.dto.MyRepairQueryDTO;
import com.meeting.service.RepairService;

/**
 * 设备报修Service业务层处理
 */
@Service
public class RepairServiceImpl implements RepairService {

    @Resource
    private SysRepairMapper sysRepairMapper;

    @Resource
    private SysLabMapper sysLabMapper;

    @Override
    public int insertRepair(SysRepair sysRepair) {
        return sysRepairMapper.insertRepair(sysRepair);
    }

    @Override
    public List<SysRepair> listMyRepair(MyRepairQueryDTO queryDTO) {
        return sysRepairMapper.listMyRepair(queryDTO);
    }

    @Override
    public List<SysRepair> listManageRepair(SysRepair sysRepair) {
        return sysRepairMapper.listManageRepair(sysRepair);
    }

    @Override
    public SysRepair selectRepairById(Integer repairId) {
        return sysRepairMapper.selectRepairById(repairId);
    }

    @Override
    @Transactional
    public int updateRepairStatus(EditRepairDTO dto) {
        // 1. 更新报修状态
        SysRepair repair = new SysRepair();
        repair.setRepairId(dto.getRepairId());
        repair.setRepairStatus(dto.getNewRepairStatus());
        repair.setRemark(dto.getRemark());
        int rows = sysRepairMapper.updateRepairStatus(repair);

        // 2. 如果需要更新实验室状态
        if (rows > 0 && dto.getLabStatus() != null) {
            SysRepair oldRepair = sysRepairMapper.selectRepairById(dto.getRepairId());
            if (oldRepair != null) {
                SysLab lab = new SysLab();
                lab.setLabId(oldRepair.getLabId());
                lab.setStatus(dto.getLabStatus());
                sysLabMapper.changeLabStatus(lab);
            }
        }

        return rows;
    }

    @Override
    public int deleteRepairById(Integer repairId) {
        return sysRepairMapper.deleteRepairById(repairId);
    }
}
