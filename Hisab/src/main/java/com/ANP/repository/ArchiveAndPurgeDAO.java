package com.ANP.repository;

import com.ANP.util.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public class ArchiveAndPurgeDAO {

    @Autowired
    SystemUtilityDAO systemUtilityDAO;

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Transactional(rollbackFor = Exception.class)
    public void actualDeletion()
    {
        Map<String,String> systemConfigMap ;
        int noOfDaysAfterDelete;
        systemConfigMap = systemUtilityDAO.getSystemConfigurationMap();
        String commaSeperatedArchiveList = systemConfigMap.get("archiveandpurgetablelist");
        String deleteDays = systemConfigMap.get("daysafterdeletelist");
        int noDeleteDays;
        List<String> resultArchiveTableList = Arrays.asList(commaSeperatedArchiveList.split("\\s*,\\s*"));
        try {
            noDeleteDays = Integer.parseInt(deleteDays);
        }
        catch(Exception e)
        {
            throw new CustomAppException("delete Days invalid","SERVER.SYSTEM_CONFIG.INVALIDVALUE", HttpStatus.EXPECTATION_FAILED);
        }
        if(noDeleteDays < 7){
            noDeleteDays = 7;
        }
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("PurgeProcess_Procedure");
        Map<String, Object> inParamMap = new HashMap<>();

        Iterator<String> it1 = resultArchiveTableList.iterator();

        for(Object listIterator : resultArchiveTableList)
        {
            if(!listIterator.equals("")) {

                System.out.println("no fo days" + noDeleteDays);
                System.out.println("object" + listIterator);
                inParamMap.put("noofdaysafterdeletion", noDeleteDays);
                inParamMap.put("purgearchivetablename", listIterator);
                SqlParameterSource in = new MapSqlParameterSource(inParamMap);
                Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
                System.out.println("result = "+ simpleJdbcCallResult);
            }
        }
    }
}
