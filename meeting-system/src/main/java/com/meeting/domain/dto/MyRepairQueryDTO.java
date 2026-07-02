package com.meeting.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:csc
 * @createTime:2023-07-25 16:11
 * @description:TODO
 */

@Data
public class MyRepairQueryDTO implements Serializable {
    private String labName;
    private Long userId;
    private String faultDesc;
    private Integer repairStatus;
    private Date startTime;
    private Date endTime;
    private static final long serialVersionUID = 1L;
}
