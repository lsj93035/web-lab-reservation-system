package com.meeting.manager.factory;

import com.meeting.domain.dto.EditRepairDTO;
import com.meeting.domain.SysRepair;
import com.meeting.mapper.SysRepairMapper;
import com.meeting.mapper.SysLabMapper;
import com.meeting.domain.SysLab;
import com.ruoyi.common.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

/**
 * 异步任务工厂
 */
@Slf4j
public class AsyncFactory {

    /**
     * 更新报修状态及实验室状态
     */
    public static TimerTask saveRepairChange(final EditRepairDTO editRepairDTO) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    SysRepairMapper repairMapper = SpringUtils.getBean(SysRepairMapper.class);
                    SysLabMapper labMapper = SpringUtils.getBean(SysLabMapper.class);

                    // 更新报修状态
                    SysRepair repair = new SysRepair();
                    repair.setRepairId(editRepairDTO.getRepairId());
                    repair.setRepairStatus(editRepairDTO.getNewRepairStatus());
                    repair.setRemark(editRepairDTO.getRemark());
                    repairMapper.updateRepairStatus(repair);

                    // 如果需要更新实验室状态
                    if (editRepairDTO.getLabStatus() != null) {
                        SysRepair oldRepair = repairMapper.selectRepairById(editRepairDTO.getRepairId());
                        if (oldRepair != null) {
                            SysLab lab = new SysLab();
                            lab.setLabId(oldRepair.getLabId());
                            lab.setStatus(editRepairDTO.getLabStatus());
                            labMapper.changeLabStatus(lab);
                        }
                    }
                } catch (Exception e) {
                    log.error("更新报修状态异步任务失败: {}", e.getMessage());
                }
            }
        };
    }
}
