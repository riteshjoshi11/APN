package com.ANP.repository;

import com.ANP.bean.RetailSale;
import com.ANP.bean.SystemConfigurationBean;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SystemUtilityDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public Map<String,String> getSystemConfigurationMap()
    {
        Map<String, Object> param = new HashMap<>();
        Map<String,String> systemConfigMap = new HashMap<>();
        List<SystemConfigurationBean> commaSeperatedList = namedParameterJdbcTemplate.query("select `key`, `value` from systemconfigurations ",param, new SystemConfigMapper());
        int listIndex = 0;
        for(Object listIterator : commaSeperatedList)
        {
            System.out.println(listIterator);
            systemConfigMap.put(commaSeperatedList.get(listIndex).getKey(),commaSeperatedList.get(listIndex).getValue());
            System.out.println(systemConfigMap.values());
            listIndex++;
        }

        //uISystemDAO.actualDeletion(purgeTableList,15);

        return systemConfigMap;

    }
    public static final class SystemConfigMapper implements RowMapper<SystemConfigurationBean>
    {
        public SystemConfigurationBean mapRow (ResultSet rs, int rowNum) throws SQLException
        {
            SystemConfigurationBean systemConfigurationBean = new SystemConfigurationBean();
            systemConfigurationBean.setKey(rs.getString("key"));
            systemConfigurationBean.setValue(rs.getString("value"));
            //l1.add(rs.getString("value"));
            return systemConfigurationBean;
        }
    }

}
