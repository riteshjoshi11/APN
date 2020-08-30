package com.ANP.bean;

import com.ANP.util.ANPUtils;

public class RolePermission {

    private long empTypeId;
    private String Permission;

    public long getEmpTypeId() {
        return empTypeId;
    }

    public void setEmpTypeId(long empTypeId) {
        this.empTypeId = empTypeId;
    }

    public String getPermission() {
        return Permission;
    }

    public void setPermission(String permission) {
        Permission = permission;
    }
}