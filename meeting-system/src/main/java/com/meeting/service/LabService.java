package com.meeting.service;

import java.util.List;
import com.meeting.domain.SysLab;

/**
 * 实验室信息Service接口
 */
public interface LabService {

    /**
     * 查询实验室列表
     */
    List<SysLab> selectLabList(SysLab sysLab);

    /**
     * 查询单个实验室
     */
    SysLab selectLabById(Integer labId);

    /**
     * 新增实验室
     */
    int insertLab(SysLab sysLab);

    /**
     * 修改实验室
     */
    int updateLab(SysLab sysLab);

    /**
     * 删除实验室
     */
    int deleteLabById(Integer labId);

    /**
     * 批量删除实验室
     */
    int deleteLabByIds(Integer[] labIds);

    /**
     * 修改实验室状态
     */
    int changeLabStatus(SysLab sysLab);

    /**
     * 查询所有楼宇列表
     */
    List<String> selectBuildingList();
}
