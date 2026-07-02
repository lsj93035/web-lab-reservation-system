package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 设备报修对象 lab_equipment_repair
 * * @author ruoyi
 */
public class LabEquipmentRepair extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报修ID */
    private Long repairId;

    /** 实验室ID */
    private Long labId;

    /** 设备名称 */
    private String equipmentName;

    /** 报修人ID */
    private Long reporterId;

    /** 故障描述 */
    private String issueDescription;

    /** 状态 (0待处理 1维修中 2已修复) */
    private String status;

    /** 处理人ID */
    private Long handlerId;

    /** 处理备注 */
    private String handleRemark;

    public void setRepairId(Long repairId) 
    {
        this.repairId = repairId;
    }

    public Long getRepairId() 
    {
        return repairId;
    }
    public void setLabId(Long labId) 
    {
        this.labId = labId;
    }

    public Long getLabId() 
    {
        return labId;
    }
    public void setEquipmentName(String equipmentName) 
    {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentName() 
    {
        return equipmentName;
    }
    public void setReporterId(Long reporterId) 
    {
        this.reporterId = reporterId;
    }

    public Long getReporterId() 
    {
        return reporterId;
    }
    public void setIssueDescription(String issueDescription) 
    {
        this.issueDescription = issueDescription;
    }

    public String getIssueDescription() 
    {
        return issueDescription;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setHandlerId(Long handlerId) 
    {
        this.handlerId = handlerId;
    }

    public Long getHandlerId() 
    {
        return handlerId;
    }
    public void setHandleRemark(String handleRemark) 
    {
        this.handleRemark = handleRemark;
    }

    public String getHandleRemark() 
    {
        return handleRemark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("repairId", getRepairId())
            .append("labId", getLabId())
            .append("equipmentName", getEquipmentName())
            .append("reporterId", getReporterId())
            .append("issueDescription", getIssueDescription())
            .append("status", getStatus())
            .append("handlerId", getHandlerId())
            .append("handleRemark", getHandleRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}