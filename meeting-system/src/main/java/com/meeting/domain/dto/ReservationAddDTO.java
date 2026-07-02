package com.meeting.domain.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 预约提交DTO
 */
@Data
public class ReservationAddDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 预约编号（修改时使用） */
    private Integer reserveId;

    /** 实验室编号 */
    private Integer labId;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    /** 实验备注 */
    private String remark;
}
