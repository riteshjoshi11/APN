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
}
