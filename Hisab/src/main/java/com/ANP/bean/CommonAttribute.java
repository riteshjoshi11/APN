package com.ANP.bean;


import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class CommonAttribute {

    @ApiModelProperty(hidden = true)
    protected String statusMessage;
  //  @ApiModelProperty(hidden = true)
    protected String createdbyId;
    @ApiModelProperty(hidden = true)
    protected java.util.Date createDate;
 //   @ApiModelProperty(hidden = true)
    protected long orgId; //TODO remove 1

    //protected boolean includeInCalc;

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


    //public boolean getIncludeInCalc(){ return includeInCalc;}

    //public void setIncludeInCalc(){ this.includeInCalc= includeInCalc;}


}
