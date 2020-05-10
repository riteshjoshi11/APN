package com.ANP.bean;

import java.util.List;

public class IntermediateLoginBean {
    private boolean loginVerified;
    private List<Organization> organizationList;
    private String loginType;

    public boolean isLoginVerified() {
        return loginVerified;
    }

    public void setLoginVerified(boolean loginVerified) {
        this.loginVerified = loginVerified;
    }

    public List<Organization> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<Organization> organizationList) {
        this.organizationList = organizationList;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
