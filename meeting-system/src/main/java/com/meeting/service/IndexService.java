package com.meeting.service;

import java.util.Map;

/**
 * 首页统计Service接口
 */
public interface IndexService {

    /**
     * 获取首页统计数据
     * @param userId 当前用户ID
     * @param roles 用户角色列表
     * @return 统计数据Map
     */
    Map<String, Object> getStatData(Long userId, String roles);
}
