package com.ANP.repository;

import com.ANP.bean.RetailSale;
import com.ANP.bean.SystemConfigurationBean;
import com.ANP.util.ANPUtils;
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
public class SystemConfigurationReaderDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Map<String,String> getSystemConfigurationMap()
    {
        List<Map<String,String>> listOfMaps = namedParameterJdbcTemplate.query("select `key`, `value` from systemconfigurations ", new HashMap<String,String>(), new SystemConfigMapper());
        if(listOfMaps!=null && !listOfMaps.isEmpty()) {
            return listOfMaps.get(0);
        }
        return new HashMap<>();
    }
    public static final class SystemConfigMapper implements RowMapper<Map<String,String>> {
        public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String, String> returnMap = new HashMap<String, String>();
            String key = rs.getString("key");
            String value = rs.getString("value");

            if (!ANPUtils.isNullOrEmpty(key) && !ANPUtils.isNullOrEmpty(value)) {
                returnMap.put(key, value);
            }
            return returnMap;
        }
    }

}//end class
