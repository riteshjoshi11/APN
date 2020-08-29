package com.ANP.bean;

import java.util.Map;

public class PermissionBean {
    boolean isShowEmployeePersonalbalance;
    boolean isShowSalaryBal;
    boolean isshowDashBoard;
    boolean isEmployeeAccountView;


    private Map<String, Boolean> permission;
    String permissionname;
    boolean ispermission;


    public boolean canshowCustimerbal() {
        return permission.get("customerbalanceview");
    }

}
