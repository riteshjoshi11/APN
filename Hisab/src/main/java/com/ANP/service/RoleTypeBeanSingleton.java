package com.ANP.service;

import com.ANP.bean.PermissionBean;
import com.ANP.repository.SystemConfigurationReaderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RoleTypeBeanSingleton {

    @Autowired
    SystemConfigurationReaderDAO systemConfigurationReaderDAO;

    private Map<Integer, PermissionBean> roleTypeBeanMap = null;

    public RoleTypeBeanSingleton() {
        roleTypeBeanMap = new HashMap<>();
        initiazeMap();
    }

    private void initiazeMap() {
        roleTypeBeanMap = systemConfigurationReaderDAO.getPermissionBeanMap();
    }

    public PermissionBean getPermissionBean(Integer employeeType) {
        return this.roleTypeBeanMap.get(employeeType);
    }

}
