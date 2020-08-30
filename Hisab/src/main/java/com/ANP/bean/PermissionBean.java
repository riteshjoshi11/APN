package com.ANP.bean;

import java.util.HashMap;
import java.util.Map;

public class PermissionBean {

    private Map<String, Boolean> permissionMap = new HashMap<String, Boolean>();

    public Map<String, Boolean> getPermissionMap() {
        return permissionMap;
    }

    public void setPermissionMap(Map<String, Boolean> permissionMap) {
        this.permissionMap = permissionMap;
    }

    public PermissionBean() {
        this.permissionMap = new HashMap<String, Boolean>();
    }

    public boolean canShowCustomerBalance() {
        return permissionMap.get("customer.balance.view");
    }

    public Boolean addEntryIntoMap(String key, Boolean value) {
        return permissionMap.put(key, value);
    }

//    public String getPermissionName() {
//        return permissionName;
//    }
//
//    public void setPermissionName(String permissionName) {
//        this.permissionName = permissionName;
//    }
}
