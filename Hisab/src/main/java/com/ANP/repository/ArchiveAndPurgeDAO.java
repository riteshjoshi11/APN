package com.ANP.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ArchiveAndPurgeDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Transactional(rollbackFor = Exception.class)
    public void actualDeletion(Map<String,String> commaSeperatedList , int noOfDaysAfterDelete)
    {
        String list = commaSeperatedList.get("archiveandpurgetablelist");
        List<String> result = Arrays.asList(list.split("\\s*,\\s*"));
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("PurgeProcess_Procedure");
        Map<String, Object> inParamMap = new HashMap<String, Object>();
        for(Object o : result)
        {
            if(!o.equals("")) {
                System.out.println("no fo days" + noOfDaysAfterDelete);
                System.out.println("object" + o);
                inParamMap.put("noofdaysafterdeletion", noOfDaysAfterDelete);
                inParamMap.put("purgearchivetablename", o);
                SqlParameterSource in = new MapSqlParameterSource(inParamMap);
                Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
                System.out.println(simpleJdbcCallResult);
            }
        }
    }
}
