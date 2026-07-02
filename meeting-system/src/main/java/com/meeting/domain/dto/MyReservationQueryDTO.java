package com.meeting.domain.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 我的预约查询DTO
 */
@Data
public class MyReservationQueryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 实验室名称 */
    private String labName;

    /** 所在楼宇 */
    private String building;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 审核状态 */
    private Integer auditStatus;

    /** 预约人编号 */
    private Long userId;

    /** 页码 */
    private Integer pageNum;

    /** 每页数量 */
    private Integer pageSize;
}
