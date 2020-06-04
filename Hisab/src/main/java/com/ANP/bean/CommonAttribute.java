package com.ANP.bean;


import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;

import java.util.Date;

public class CommonAttribute {

    @ApiModelProperty(hidden = true)
    protected String statusMessage;
    protected String createdbyId;
    @ApiModelProperty(hidden = true)
    protected java.util.Date createDate;
    @Min(value = 1, message = "orgid is mandatory")
    protected long orgId; //TODO remove 1

    @ApiModelProperty(hidden = true)
    protected String userID = "E1"; //TODo values will be set after log in from Token hardcoded till implementation

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

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
