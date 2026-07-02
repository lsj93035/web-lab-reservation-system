package com.meeting.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;

/**
 * 预约信息表
 */
@Data
public class SysReservation implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 预约编号 */
    @Excel(name = "预约编号")
    private Integer reserveId;

    /** 预约人编号 */
    private Integer userId;

    /** 实验室编号 */
    private Integer labId;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    /** 审核状态(0待审 1通过 2驳回) */
    @Excel(name = "审核状态", readConverterExp = "0=待审核,1=已通过,2=已驳回")
    private Integer auditStatus;

    /** 实验备注 */
    @Excel(name = "实验备注")
    private String remark;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    // ========== 非数据库字段，用于查询和显示 ==========

    /** 实验室名称（关联查询） */
    private String labName;

    /** 所在楼宇（关联查询） */
    private String building;

    /** 设备清单（关联查询） */
    private String equipment;

    /** 预约人姓名（关联查询） */
    private String userName;
}
