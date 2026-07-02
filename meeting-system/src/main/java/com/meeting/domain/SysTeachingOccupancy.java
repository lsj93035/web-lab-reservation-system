package com.meeting.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;

/**
 * 教学占用表
 */
@Data
public class SysTeachingOccupancy implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 占用编号 */
    @Excel(name = "占用编号")
    private Integer occupyId;

    /** 教师编号 */
    private Integer teacherId;

    /** 实验室编号 */
    private Integer labId;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 占用日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "占用日期", width = 20, dateFormat = "yyyy-MM-dd")
    private Date occupyDate;

    /** 使用时段(如08:00-10:00) */
    @Excel(name = "使用时段")
    private String timeSlot;

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

    /** 教师姓名（关联查询） */
    private String teacherName;
}
