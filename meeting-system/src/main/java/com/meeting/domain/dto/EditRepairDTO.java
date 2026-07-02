package com.meeting.domain.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * 修改报修状态DTO
 */
@Data
public class EditRepairDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 报修单号 */
    private Integer repairId;

    /** 新的维修状态(0待处理 1维修中 2已修复) */
    private Integer newRepairStatus;

    /** 旧的维修状态 */
    private Integer oldRepairStatus;

    /** 实验室状态(0正常 1维修) */
    private Integer labStatus;

    /** 报修完成留言 */
    private String remark;
}
