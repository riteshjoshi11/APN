package com.ANP.repository;

import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
/*
* This process is offline process based on the scheduler
* This process will kick-off as per the configured schedule
* This will actually archive and purge/delete organization data based on the table names and number of days after soft deletion configuration parameters
 */
@EnableScheduling
public class ArchiveAndPurgeDAO {

    @Autowired
    SystemConfigurationReaderDAO systemConfigurationReaderDAO;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Scheduled(cron="${cron.expression}")
    @Transactional(rollbackFor = Exception.class)
    public void invokeArchiveAndPurgeProcess()
    {
        System.out.println("archive and purge procedure call");
        /*
        Map<String,String> systemConfigMap ;
        int noOfDaysAfterDelete;
        systemConfigMap = systemConfigurationReaderDAO.getSystemConfigurationMap();
        String commaSeperatedArchiveList = systemConfigMap.get("ArchivePurge.archiveandpurgetablelist");
        String deleteDays = systemConfigMap.get("ArchivePurge.DeleteAfterNumberOfDays");

        if(ANPUtils.isNullOrEmpty(commaSeperatedArchiveList)) {
            System.err.println("The configuration for ArchiveAndPurge table is not given. So stopping the process here");
            throw new CustomAppException("ArchivePurge.archiveandpurgetablelist","SERVER.SYSTEM_CONFIG.INVALIDVALUE", HttpStatus.EXPECTATION_FAILED);
        }

        if(ANPUtils.isNullOrEmpty(deleteDays)) {
            System.out.println("Delete days are not configured so setting to default 7");
            deleteDays =  "7";
        }


        int noDeleteDays;
        try {
            noDeleteDays = Integer.parseInt(deleteDays);
        }
        catch(Exception e)
        {
            throw new CustomAppException("delete Days invalid","SERVER.SYSTEM_CONFIG.INVALIDVALUE", HttpStatus.EXPECTATION_FAILED);
        }

        if(noDeleteDays < 7){
            System.out.println("Delete days are not allowed less than 7 days...so setting it to default 7 days");
            noDeleteDays = 7;
        }

        List<String> resultArchiveTableList = Arrays.asList(commaSeperatedArchiveList.split("\\s*,\\s*"));

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("ArchiveAndPurgeProcess_Procedure");
        Map<String, Object> inParamMap = new HashMap<>();


        for(String tableName : resultArchiveTableList)
        {
            if(!ANPUtils.isNullOrEmpty(tableName)) {

                System.out.println("Now we are going to call ArchiveAndPurge Procedure for table/object[" + tableName
                        + "]DeleteAfterNumberOfDays[" + noDeleteDays + "]");

                inParamMap.put("noofdaysafterdeletion", noDeleteDays);
                inParamMap.put("purgearchivetablename", tableName);
                SqlParameterSource in = new MapSqlParameterSource(inParamMap);
                Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
                System.out.println("result = "+ simpleJdbcCallResult);
            }
        }
    */
    }
}
