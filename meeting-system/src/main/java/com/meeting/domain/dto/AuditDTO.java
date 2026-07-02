package com.meeting.domain.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * 审批操作DTO
 */
@Data
public class AuditDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 预约编号 */
    private Integer reserveId;

    /** 审核状态(1通过 2驳回) */
    private Integer auditStatus;
}
