package com.meeting.service.impl;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.meeting.mapper.SysLabMapper;
import com.meeting.mapper.SysRepairMapper;
import com.meeting.mapper.SysReservationMapper;
import com.meeting.service.IndexService;

/**
 * 首页统计Service实现
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Resource
    private SysLabMapper labMapper;

    @Resource
    private SysReservationMapper reservationMapper;

    @Resource
    private SysRepairMapper repairMapper;

    @Override
    public Map<String, Object> getStatData(Long userId, String roles) {
        Map<String, Object> statData = new HashMap<>();

        // 实验室总数（所有角色都需要）
        int labTotal = labMapper.countLabTotal();
        statData.put("labTotal", labTotal);

        if (roles.contains("admin")) {
            // 管理员：今日预约数、待审批预约、待处理报修
            int todayReservations = reservationMapper.countTodayReservations();
            int pendingAudit = reservationMapper.countPendingAudit();
            int pendingRepair = repairMapper.countPendingRepair();

            statData.put("todayReservations", todayReservations);
            statData.put("pendingAudit", pendingAudit);
            statData.put("pendingRepair", pendingRepair);

        } else if (roles.contains("teacher")) {
            // 教师：待审批预约、我的预约数
            int pendingAudit = reservationMapper.countPendingAudit();
            int myReservations = reservationMapper.countByUserId(userId);
            int pendingRepair = repairMapper.countPendingRepair();

            statData.put("pendingAudit", pendingAudit);
            statData.put("myReservations", myReservations);
            statData.put("pendingRepair", pendingRepair);

        } else if (roles.contains("student")) {
            // 学生：我的预约数、我的报修数
            int myReservations = reservationMapper.countByUserId(userId);
            int myRepairs = repairMapper.countByUserId(userId);
            int availableLabs = labMapper.countLabTotal(); // 可用实验室（简化处理）

            statData.put("myReservations", myReservations);
            statData.put("myRepairs", myRepairs);
            statData.put("availableLabs", availableLabs);
        }

        return statData;
    }
}
