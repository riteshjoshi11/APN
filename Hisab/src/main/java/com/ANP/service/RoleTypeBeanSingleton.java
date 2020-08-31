package com.ANP.service;

import com.ANP.bean.PermissionBean;
import com.ANP.repository.SystemConfigurationReaderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RoleTypeBeanSingleton {

    @Autowired
    SystemConfigurationReaderDAO systemConfigurationReaderDAO;

    private Map<Integer, PermissionBean> roleTypeBeanMap = null;

    public PermissionBean getPermissionBean(Integer employeeType) {
        if (roleTypeBeanMap == null) {
            roleTypeBeanMap = systemConfigurationReaderDAO.getPermissionBeanMap();
        }
        return this.roleTypeBeanMap.get(employeeType);
    }

}
