package com.ANP.service;

import com.ANP.bean.GSTReportBean;
import com.ANP.bean.ReportBean;
import com.ANP.bean.TransactionReportBean;
import com.ANP.repository.CommonDAO;
import com.ANP.repository.ReportDAO;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReportService {
    @Autowired
    ReportDAO reportDAO;

    @Autowired
    CommonDAO commonDAO ;
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

    }
}
