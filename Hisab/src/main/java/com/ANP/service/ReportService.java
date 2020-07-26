package com.ANP.service;

import com.ANP.bean.GSTReportBean;
import com.ANP.repository.ReportDAO;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReportService {
    @Autowired
    ReportDAO reportDAO;

    public void createGSTReportRecord(GSTReportBean reportBean) {
        //validate
        //create gstreport
        if(reportBean!=null && ANPConstants.GST_REPORT_CURRENT_MONTH.equalsIgnoreCase(reportBean.getForMonth())) {
            reportBean.setForMonth(ANPUtils.getMonthPart(new Date()));
        } else if(ANPConstants.GST_REPORT_PREVIOUS_MONTH.equalsIgnoreCase(reportBean.getForMonth())) {
            reportBean.setForMonth(ANPUtils.getMonthPart(ANPUtils.getPreviousMonth()));
        }
        reportDAO.createGSTReport(reportBean);
    }

}
