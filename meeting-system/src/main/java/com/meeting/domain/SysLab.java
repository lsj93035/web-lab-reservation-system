package com.meeting.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 实验室信息表
 */
@Data
public class SysLab implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 实验室编号 */
    private Integer labId;

    /** 实验室名称 */
    private String labName;

    /** 所在楼宇 */
    private String building;

    /** 容纳人数 */
    private Integer capacity;

    /** 状态(0正常 1维修) */
    private Integer status;

    /** 设备清单 */
    private String equipment;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /** 备注 */
    private String remark;
}
