package com.ANP.repository;

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
}//end class
