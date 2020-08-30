package com.ANP.bean;

import com.ANP.util.ANPUtils;

public class Organization {

    private long orgId;
    private String orgName;
    private String city;
    private String state;
    private String clientId;

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgID) {
        this.orgId = orgID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrgName() {
        if(!ANPUtils.isNullOrEmpty(this.orgName)) {
            return (this.orgName.trim()).toUpperCase();
        }
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String toString() {
        return "" + "orgName[" + orgName + "] city[" + city + "] state[" + state + "]" ;
    }
}
