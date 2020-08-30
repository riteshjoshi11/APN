package com.ANP.repository;

import com.ANP.bean.PermissionBean;
import com.ANP.bean.RetailSale;
import com.ANP.bean.SystemConfigurationBean;
import com.ANP.util.ANPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

    SystemConfigurationReaderDAO() {
        System.out.println("Calling system configuration dao");
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
       @TODO Ritesh : Please write a method to query your permission related data
       Build PermissionBean for each role type
       Finally - create/build a map with key as RoleType and Value as PermissionBean.
     */
    public Map<Integer, PermissionBean> getPermissionBeanMap() {
        //@TODO RITESH : write a query here
        return namedParameterJdbcTemplate.query("select empTypeId,  permissionId, pe.name  from role_permission rp,permission pe,employeetype et where rp.empTypeId=et.id and rp.permissionId =pe.id ",

                new ResultSetExtractor<Map<Integer, PermissionBean>>() {
                    @Override
                    public Map<Integer, PermissionBean> extractData(ResultSet rs) throws SQLException,
                            DataAccessException {
                        Map<Integer, PermissionBean> returnMap = new HashMap<Integer, PermissionBean>();
                        try {
                            System.out.println("in extrect methord ..." + rs.getFetchSize());
                            while (rs.next()) {
                                System.out.println("1111 ...");
                                Integer key = rs.getInt(1);
                                System.out.println("2222 ...");

                                // String value = rs.getString("<>");
                                PermissionBean permissionBean = returnMap.get(key);
                                if (permissionBean == null) {
                                    permissionBean = new PermissionBean();
                                    returnMap.put(key, permissionBean);
                                }
                                String permissionName = rs.getString(3);
                                Boolean permissionTrueFalse = new Boolean(true); //if entry present in table it means have permission

                                System.out.println("Permission bean .." + permissionBean + " name " + permissionName + " true flase  " + permissionTrueFalse);
                                if (!ANPUtils.isNullOrEmpty(permissionName)) {
                                    permissionBean.addEntryIntoMap(permissionName, Boolean.TRUE);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        return returnMap;

                    }
                });

    }
}//end class
