package com.meeting.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;

/**
 * 设备报修表
 */
@Data
public class SysRepair implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 报修单号 */
    @Excel(name = "报修单号")
    private Integer repairId;

    /** 实验室编号 */
    private Integer labId;

    /** 报修人编号 */
    private Integer userId;

    /** 故障描述 */
    @Excel(name = "故障描述")
    private String faultDesc;

    /** 维修状态(0待处理 1维修中 2已修复) */
    @Excel(name = "维修状态", readConverterExp = "0=待处理,1=维修中,2=已修复")
    private Integer repairStatus;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "提交时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

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

    // ========== 非数据库字段，用于查询和显示 ==========

    /** 实验室名称（关联查询） */
    private String labName;

    /** 所在楼宇（关联查询） */
    private String building;

    /** 报修人姓名（关联查询） */
    private String userName;
}
