package com.meeting.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.meeting.mapper.SysLabMapper;
import com.meeting.domain.SysLab;
import com.meeting.service.LabService;

/**
 * 实验室信息Service业务层处理
 */
@Service
public class LabServiceImpl implements LabService {

    @Resource
    private SysLabMapper sysLabMapper;

    @Override
    public List<SysLab> selectLabList(SysLab sysLab) {
        return sysLabMapper.selectLabList(sysLab);
    }

    @Override
    public SysLab selectLabById(Integer labId) {
        return sysLabMapper.selectLabById(labId);
    }

    @Override
    public int insertLab(SysLab sysLab) {
        return sysLabMapper.insertLab(sysLab);
    }

    @Override
    public int updateLab(SysLab sysLab) {
        return sysLabMapper.updateLab(sysLab);
    }

    @Override
    public int deleteLabById(Integer labId) {
        return sysLabMapper.deleteLabById(labId);
    }

    @Override
    public int deleteLabByIds(Integer[] labIds) {
        return sysLabMapper.deleteLabByIds(labIds);
    }

    @Override
    public int changeLabStatus(SysLab sysLab) {
        return sysLabMapper.changeLabStatus(sysLab);
    }

    @Override
    public List<String> selectBuildingList() {
        return sysLabMapper.selectBuildingList();
    }
}
