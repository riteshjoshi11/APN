package com.ANP.service;

import com.ANP.bean.PermissionBean;
import com.ANP.repository.SystemConfigurationReaderDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RoleTypeBeanSingleton {

    @Autowired
    SystemConfigurationReaderDAO systemConfigurationReaderDAO;

    private static final Logger logger = LoggerFactory.getLogger(RoleTypeBeanSingleton.class);
    private Map<Integer, PermissionBean> roleTypeBeanMap = null;


    public PermissionBean getPermissionBean(Integer employeeType) {
        logger.trace("Entering getPermissionBean() : employeeType:" + employeeType);

        if (roleTypeBeanMap == null) {
            logger.info("Initializing the Permission Data...");
            roleTypeBeanMap = systemConfigurationReaderDAO.getPermissionBeanMap();
            logger.info("Initialization complete" + roleTypeBeanMap);
        }

        PermissionBean permissionBean = this.roleTypeBeanMap.get(employeeType);
        logger.trace("Exiting getPermissionBean() : permissionBean:" + permissionBean);
        return  permissionBean;
    }
}
