package com.ANP.repository;

import com.ANP.bean.PermissionBean;
import com.ANP.bean.RetailSale;
import com.ANP.bean.SystemConfigurationBean;
import com.ANP.service.LoginHandler;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SystemConfigurationReaderDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(SystemConfigurationReaderDAO.class);

    SystemConfigurationReaderDAO() {
        logger.trace("Calling system configuration dao");
    }

    public Map<String, String> getSystemConfigurationMap() {

        return namedParameterJdbcTemplate.query("select `key`, `value` from systemconfigurations ",
                new ResultSetExtractor<Map<String, String>>() {
                    @Override
                    public Map<String, String> extractData(ResultSet rs) throws SQLException,
                            DataAccessException {
                        Map<String, String> returnMap = new HashMap<String, String>();
                        while (rs.next()) {
                            String key = rs.getString("key");
                            String value = rs.getString("value");

                            if (!ANPUtils.isNullOrEmpty(key) && !ANPUtils.isNullOrEmpty(value)) {
                                returnMap.put(key, value);
                            }
                        }
                        return returnMap;
                    }
                });
    }

    /*
       Please write a method to query your permission related data
       Build PermissionBean for each role type
       Finally - create/build a map with key as RoleType and Value as PermissionBean.
     */
    public Map<Integer, PermissionBean> getPermissionBeanMap() {
        return namedParameterJdbcTemplate.query("select empTypeId,  permissionId, pe.name  from role_permission rp,permission pe,employeetype et where rp.empTypeId=et.id and rp.permissionId =pe.id ",
                new ResultSetExtractor<Map<Integer, PermissionBean>>() {
                    @Override
                    public Map<Integer, PermissionBean> extractData(ResultSet rs) throws SQLException,
                            DataAccessException {
                        Map<Integer, PermissionBean> returnMap = new HashMap<Integer, PermissionBean>();
                        try {
                            while (rs.next()) {
                                Integer key = rs.getInt("empTypeId");
                                PermissionBean permissionBean = returnMap.get(key);
                                if (permissionBean == null) {
                                    permissionBean = new PermissionBean();
                                    returnMap.put(key, permissionBean);
                                }
                                String permissionName = rs.getString("pe.name");
                                Boolean permissionTrueFalse = new Boolean(true); //if entry present in table it means have permission

                                logger.trace("Permission bean .." + permissionBean + " name " + permissionName + " true flase  " + permissionTrueFalse);
                                if (!ANPUtils.isNullOrEmpty(permissionName)) {
                                    permissionBean.addEntryIntoMap(permissionName, Boolean.TRUE);
                                }
                            }
                        } catch (Exception e) {
                            throw new CustomAppException("Error: System configuration issue", "SERVER.SECURITY.PERMISSION.BADCONFIGURATION", HttpStatus.EXPECTATION_FAILED);
                        }
                        return returnMap;
                    }
                });
    }
}//end class
