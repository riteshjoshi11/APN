package com.ANP.service;

import com.ANP.bean.GSTReportBean;
import com.ANP.bean.Organization;
import com.ANP.bean.ReportBean;
import com.ANP.bean.TransactionReportBean;
import com.ANP.repository.CommonDAO;
import com.ANP.repository.ReportDAO;
import com.ANP.repository.SystemConfigurationReaderDAO;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ReportService {
    @Autowired
    ReportDAO reportDAO;
    @Autowired
    SystemConfigurationReaderDAO systemConfigurationReaderDAO;


    @Autowired
    CommonDAO commonDAO ;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public long createGSTReportRecord(GSTReportBean reportBean) {
        //validate
        //create gstreport
        if(reportBean!=null && ANPConstants.GST_REPORT_CURRENT_MONTH.equalsIgnoreCase(reportBean.getForMonth())) {
            reportBean.setForMonth(ANPUtils.getMonthPart(new Date()));
        } else if(ANPConstants.GST_REPORT_PREVIOUS_MONTH.equalsIgnoreCase(reportBean.getForMonth())) {
            reportBean.setForMonth(ANPUtils.getMonthPart(ANPUtils.getPreviousMonth()));
        }
       return reportDAO.createGSTReport(reportBean);
    }

    public long createTxnReportRecord(TransactionReportBean reportBean) {
        //calculate from date and to date in case user has selected from last backup date to now.
        reportBean.setReportStatus(ReportBean.reportStatusEnum.WAITING.toString());
        Date lastBackupDate = null;
        if( (TransactionReportBean.PeriodOptionsEnum.FROM_LAST_BACKUP.toString()).equalsIgnoreCase(reportBean.getTimePeriod())) {
            lastBackupDate = reportDAO.getLastSuccessfulBackupDate(reportBean.getOrgId(), reportBean.getGeneratedByEmployeeId());
            if(lastBackupDate==null) {
                //get the company registration date
                lastBackupDate = commonDAO.getOrgCreationDate(reportBean.getOrgId());
            }
            if(lastBackupDate==null) {
                    throw new CustomAppException("Could not determine last backup date","SERVER.createTxnReportRecord.LASTBACKUPDATE", HttpStatus.EXPECTATION_FAILED);
            }
            reportBean.setFromDate(ANPUtils.addOneDay(lastBackupDate));
            reportBean.setToDate(new Date());
        } else if(TransactionReportBean.PeriodOptionsEnum.BETWEEN_DATES.toString().equalsIgnoreCase(reportBean.getTimePeriod())) {
            if(reportBean.getFromDate()==null || reportBean.getToDate()==null) {
                throw new CustomAppException("From Date or To Date is not set","SERVER.createTxnReportRecord.INVALID_PARAM", HttpStatus.BAD_REQUEST);
            } else if( (reportBean.getFromDate().compareTo(reportBean.getToDate()) ) > 0 ) {
                throw new CustomAppException("From Date is greater than ToDate","SERVER.createTxnReportRecord.INVALID_PARAM", HttpStatus.BAD_REQUEST);
            }
        }
        return reportDAO.createTxnReport(reportBean);
    }

    /*
     *  Paras complete this code
     *  in the GSTReportBean consider that you only have - 1. OrgID, 2. For Month
     *  You need to generate GST report for OrgID and forMonth (In format of "August 2020")
     *  "August 2020" means 1 Aug 2020 to 31 Aug 2020 (this you need to calculate)
     *  Generate PDF Files Name using Random Logic (using Date), Update in DB the names of the files for that record (Write a private method here)
     *  Call you Report DAO methods that you prepared for Report data writing
     *  In One Excel - provide data of Sale/Purchase and Expense on different sheets (sheet named accordingly)
     *  While in PDF format - you can do one table after another
     * Once generated updated the status to generated (ReportBean.reportStatusEnum.GENERATED)
     * Please update - ReportBean.reportStatusEnum.Error in case of error (do not ignore the errors in the catch blocks - just update the status)
     *
     */
    public void processGSTReport(GSTReportBean gstReportBean) {

        if(ANPUtils.isNullOrEmpty(gstReportBean.getForMonth()))
        {
            throw new CustomAppException("Date cannot be null", "SERVER.REPORT_HANDLER.NOTAVAILABLE", HttpStatus.EXPECTATION_FAILED);
        }
        if(gstReportBean.getReportId()<=0)
        {
            throw new CustomAppException("Report Id cannot be 0", "SERVER.REPORT_HANDLER.NOTAVAILABLE", HttpStatus.EXPECTATION_FAILED);
        }
        String monthYearMix = gstReportBean.getForMonth().trim();

        //Fetching year
        String year = monthYearMix.substring(monthYearMix.length()-4);
        //Fetching Month
        String month = monthYearMix.substring(0,monthYearMix.length()-5);


        //Using map to get start date and last date
        Map<String,String> dateMap;
        dateMap = monthNum(month.toUpperCase(),year);

        String startDate = dateMap.get("startDate");
        String lastDate = dateMap.get("lastDate");

        //Generating excelFileName using date logic.
        String excelName = generateEXCELName();
        Map<String,String> systemConfigMap ;
        systemConfigMap = systemConfigurationReaderDAO.getSystemConfigurationMap();
        //Appending excelFileName
        String excelPath = systemConfigMap.get("REPORT.PATH") + excelName;
        gstReportBean.setExcelFilePath(excelPath);

        //Updating status in p_gst_table
        updateP_ReportGST(gstReportBean);
        System.out.println("WE are here1");
        reportDAO.coreLogicReportGeneration(gstReportBean,startDate,lastDate);
    }

    public  void updateP_ReportGST(GSTReportBean gstReportBean) {

        reportDAO.updateP_ReportGST(gstReportBean);

    }


    public Map<String, String> monthNum(String month, String year){
        Map<String,String> dateMap= new HashMap<>();
        String startDate = null;
        String lastDate = null;
        switch(month)
        {
           case "JANUARY": startDate = year+"-01-01"; lastDate = year+"-01-31"; break;
           case "FEBRUARY": startDate = year+"-02-01"; lastDate = year+"-02-28"; break;
           case "MARCH": startDate = year+"-03-01"; lastDate = year+"-03-31"; break;
           case "APRIL": startDate = year+"-04-01"; lastDate = year+"-04-30"; break;
           case "MAY": startDate = year+"-05-01"; lastDate = year+"-05-31"; break;
           case "JUNE": startDate = year+"-06-01"; lastDate = year+"-06-30"; break;
           case "JULY": startDate = year+"-07-01"; lastDate = year+"-07-31"; break;
           case "AUGUST": startDate = year+"-08-01"; lastDate = year+"-08-31"; break;
           case "SEPTEMBER": startDate = year+"-09-01"; lastDate = year+"-09-30"; break;
           case "OCTOBER": startDate = year+"-10-01"; lastDate = year+"-10-31"; break;
           case "NOVEMBER": startDate = year+"-11-01"; lastDate = year+"-11-30"; break;
           case "DECEMBER": startDate = year+"-12-01"; lastDate = year+"-12-31"; break;
        }

        //FOR LEAP YEAR
        int year1 = Integer.parseInt(year);
        if(month.equalsIgnoreCase("FEBRUARY")) {
            if (year1 % 4 == 0) {
                lastDate = year + "-02-29";
            }
        }

        if(ANPUtils.isNullOrEmpty(startDate)||ANPUtils.isNullOrEmpty(lastDate))
        {
            throw new CustomAppException("Date is not in correct format", "SERVER.REPORT_HANDLER.WRONGFORMAT", HttpStatus.EXPECTATION_FAILED);
        }

        dateMap.put("startDate",startDate);
        dateMap.put("lastDate",lastDate);
        return dateMap;
        }

    public String generateEXCELName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssS");
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDateTime = dateTime.format(formatter);


        Random rand = new Random();
        int randomNumber = (rand.nextInt(89999) + 10000);
        String randomNumberString = Integer.toString(randomNumber);
        return (formattedDateTime + "-" + randomNumberString+".xlsx");


    }

}


