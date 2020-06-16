package com.ANP.bean;

public class Organization {

    private long orgId;
    private String orgName;
    private String city;
    private String state;

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
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String toString() {
        return "" + "orgName[" + orgName + "] city[" + city + "] state[" + state + "]" ;
    }
}
