package com.meeting.domain.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 教学占用添加DTO
 */
@Data
public class TeachingOccupancyAddDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 实验室编号 */
    private Integer labId;

    /** 课程名称 */
    private String courseName;

    /** 占用日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date occupyDate;

    /** 使用时段(如08:00-10:00) */
    private String timeSlot;
}
