package com.ANP.bean;

import java.util.HashMap;
import java.util.Map;

public class PermissionBean {

    private Map<String, Boolean> permissionMap;
    private String permissionName;


    public PermissionBean() {
        this.permissionMap = new HashMap<>();
    }

    public boolean canShowCustomerBalance() {
        return permissionMap.get("customer.balance.view");
    }

    public boolean addEntryIntoMap(String key, Boolean value) {
        return permissionMap.put(key, value);
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}
