package com.meeting.controller;

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.meeting.service.IndexService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 首页统计Controller
 */
@RestController
@RequestMapping("/system/index")
public class IndexController extends BaseController {

    @Resource
    private IndexService indexService;

    /**
     * 获取首页统计数据
     */
    @PreAuthorize("@ss.hasAnyRoles('admin,teacher,student')")
    @GetMapping("/stat")
    public AjaxResult getStatData() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long userId = loginUser.getUserId();
        String roles = "";
        if (loginUser.getUser() != null
                && loginUser.getUser().getRoles() != null
                && !loginUser.getUser().getRoles().isEmpty()) {
            roles = loginUser.getUser().getRoles().get(0).getRoleKey();
        }
        Map<String, Object> statData = indexService.getStatData(userId, roles);
        return AjaxResult.success(statData);
    }
}
