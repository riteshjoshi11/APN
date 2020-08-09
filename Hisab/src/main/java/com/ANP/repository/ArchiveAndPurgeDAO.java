package com.ANP.repository;

import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Scheduled(cron="${cron.expressionForArchiveAndPurge}")
    @Transactional(rollbackFor = Exception.class)
    public void invokeArchiveAndPurgeProcess()
    {
        System.out.println("archive and purge procedure call");
        /*
        Map<String,String> systemConfigMap ;
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
        }*/

    }

    @Scheduled(cron="${cron.expressionForOrgDataControl}")
    @Transactional(rollbackFor = Exception.class)
    public void controlOrgDataGrowth()
    {
        Map<String,String> systemConfigMap ;
        systemConfigMap = systemConfigurationReaderDAO.getSystemConfigurationMap();
        String commaSeperatedDataControlTableList = systemConfigMap.get("CONTROL.ORG.DATA.GROWTH.TABLENAME");
        String controlOrgDataDeleteDays = systemConfigMap.get("CONTROL.ORG.DATA.GROWTH.AUTOMATICDELETEAFTERDAYS");
        String PremiumControlOrgDataDeleteDays = systemConfigMap.get("CONTROL.ORG.DATA.GROWTH.PREMIUMDELETEDAYS");

        String automaticDeleteOnNoOfTransactions = systemConfigMap.get("CONTROL.ORG.DATA.GROWTH.'CONTROL.ORG.DATA.GROWTH.AUTOMATICDELETEONNUMBEROFTRANSACTION'");
        String premiumDeleteOnNoOfTransactions = systemConfigMap.get("CONTROL.ORG.DATA.GROWTH.PREMIUMCDELETEONNUMBEROFTRANSACTION");

        if(ANPUtils.isNullOrEmpty(commaSeperatedDataControlTableList)) {
            System.err.println("The configuration for ArchiveAndPurge table is not given. So stopping the process here");
            throw new CustomAppException("ArchivePurge.archiveandpurgetablelist","SERVER.SYSTEM_CONFIG.INVALIDVALUE", HttpStatus.EXPECTATION_FAILED);
        }

        if(ANPUtils.isNullOrEmpty(controlOrgDataDeleteDays)) {
            System.out.println("Delete days are not configured so setting to default 200");
            controlOrgDataDeleteDays =  "200";
        }

        if(ANPUtils.isNullOrEmpty(PremiumControlOrgDataDeleteDays)) {
            System.out.println("Delete days are not configured so setting to default 365");
            PremiumControlOrgDataDeleteDays =  "365";
        }

        if(ANPUtils.isNullOrEmpty(automaticDeleteOnNoOfTransactions)) {
            System.out.println("Delete transactions for non premium accounts are not configured so setting to default 200");
            automaticDeleteOnNoOfTransactions =  "200";
        }

        if(ANPUtils.isNullOrEmpty(premiumDeleteOnNoOfTransactions)) {
            System.out.println("Delete transactions for non premium accounts are not configured so setting to default 1000");
            premiumDeleteOnNoOfTransactions =  "1000";
        }

        int premiumNoDeleteDays;
        int noDeleteDays;
        try {
            noDeleteDays = Integer.parseInt(controlOrgDataDeleteDays);
            premiumNoDeleteDays = Integer.parseInt(premiumDeleteOnNoOfTransactions);
        }
        catch(Exception e)
        {
            throw new CustomAppException("Delete days invalid in control organization data","SERVER.CONTROLORGDATA_SYSTEM_CONFIG.INVALIDVALUE", HttpStatus.EXPECTATION_FAILED);
        }

        int automaticDeleteTransactions;
        int premiumDeleteTransactions;
        try {
            automaticDeleteTransactions = Integer.parseInt(automaticDeleteOnNoOfTransactions);
            premiumDeleteTransactions = Integer.parseInt(PremiumControlOrgDataDeleteDays);
        }
        catch(Exception e)
        {
            throw new CustomAppException("delete on transactions invalid in control organization data","SERVER.CONTROLORGDATA_SYSTEM_CONFIG.INVALIDVALUE", HttpStatus.EXPECTATION_FAILED);
        }

        List<String> resultDataControlTableList = Arrays.asList(commaSeperatedDataControlTableList.split("\\s*,\\s*"));

        //Deleting data for NON PREMIUM users DATE WISE
        controlOrgDataGrowthDateWise(noDeleteDays,premiumNoDeleteDays,resultDataControlTableList);
        //Deleting data for NON PREMIUM users TRANSACTION WISE
        controlOrgDataGrowthTransactionWise(automaticDeleteTransactions,premiumDeleteTransactions,resultDataControlTableList);

        }


    // This is a method for OrgDataControlDateWise
    private void controlOrgDataGrowthDateWise(int regularDeleteAfterDays, int premiumDeleteAfterDays , List<String> resultDataControlTableList)
    {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("ControlOrgDataGrowthDateWise_Procedure");
        Map<String, Object> inParamMap = new HashMap<>();


        for(String tableName : resultDataControlTableList)
        {
            if(!ANPUtils.isNullOrEmpty(tableName)) {

                System.out.println("Now we are going to call ControlOrgDataGrowthDateWise Procedure for table/object[" + tableName
                        + "]DeleteAfterNumberOfDays[" + regularDeleteAfterDays + "]");

                inParamMap.put("RegularDaysAfterDelete", regularDeleteAfterDays);
                inParamMap.put("TableName", tableName);
                inParamMap.put("PremiumDaysAfterDelete", premiumDeleteAfterDays);
                SqlParameterSource in = new MapSqlParameterSource(inParamMap);
                Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
                System.out.println("result = "+ simpleJdbcCallResult);
            }
        }
    }

    // This is a method for OrgDataControlTransactionWise

    private void controlOrgDataGrowthTransactionWise(int deleteAfterTransactions, int premiumDeleteAfterTransactions , List<String> resultDataControlTableList)
    {
        SimpleJdbcCall simpleJdbcCall1 = new SimpleJdbcCall(jdbcTemplate).withProcedureName("ControlOrgDataGrowthTransactionWise_Procedure");
        Map<String, Object> inParamMap1 = new HashMap<>();


        for(String tableName : resultDataControlTableList)
        {
            if(!ANPUtils.isNullOrEmpty(tableName)) {

                System.out.println("Now we are going to call ControlOrgDataGrowthTransactionWise Procedure for table/object[" + tableName
                        + "]DeleteAfterNumberOfTransactions[" + premiumDeleteAfterTransactions + "]");

                inParamMap1.put("RegularNoOfTxn", deleteAfterTransactions);
                inParamMap1.put("TableName", tableName);
                inParamMap1.put("PremiumNoOFTxn", premiumDeleteAfterTransactions);
                SqlParameterSource in = new MapSqlParameterSource(inParamMap1);
                Map<String, Object> simpleJdbcCallResult = simpleJdbcCall1.execute(in);
                System.out.println("result = "+ simpleJdbcCallResult);
            }
        }
    }
}

