package com.ANP.bean;


import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;

import java.util.Date;

public class CommonAttribute {

    protected String createdbyId;
    protected String createdByEmpoyeeName;

    @ApiModelProperty(hidden = true)
    protected java.util.Date createDate;

    @Min(value = 1, message = "OrgID is Mandatory")
    protected long orgId;

    private boolean forceCreate=true;

    public boolean isForceCreate() {
        return forceCreate;
    }

    public void setForceCreate(boolean forceCreate) {
        this.forceCreate = forceCreate;
    }

    public String getCreatedbyId() {
        return createdbyId;
    }

    public void setCreatedbyId(String createdbyId) {
        this.createdbyId = createdbyId;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public java.util.Date getCreateDate() {
        return createDate;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getCreatedByEmpoyeeName() { return createdByEmpoyeeName; }

    public void setCreatedByEmpoyeeName(String createdByEmpoyeeName) { this.createdByEmpoyeeName = createdByEmpoyeeName; }
}
