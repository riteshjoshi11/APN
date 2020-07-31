package com.ANP.controller;

import com.ANP.bean.*;
import com.ANP.repository.CustomerInvoiceDAO;
import com.ANP.repository.ExpenseDAO;
import com.ANP.repository.PurchaseFromVendorDAO;
import com.ANP.repository.ReportDAO;
import com.ANP.service.ReportService;
import com.ANP.util.ANPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/report")
public class ReportController {

    @Autowired
    ReportDAO reportDao;

    @Autowired
    ReportService reportService;

    @PostMapping(path = "/downloadPDF", produces = "application/json")
    public ResponseEntity<InputStreamResource> fetchPdf(String filePath, long orgId, String loggedInEmployeeID) throws  Exception{
        return reportDao.fetchPdf(filePath,orgId,loggedInEmployeeID);
    }

    @PostMapping(path = "/downloadExcel", produces = "application/json")
    public ResponseEntity<InputStreamResource> fetchExcel(String filePath, long orgId, String loggedInEmployeeID) throws  Exception{
        return reportDao.fetchExcel(filePath,orgId,loggedInEmployeeID);
    }


    @PostMapping(path = "/createGSTReport", produces = "application/json")
    public ResponseEntity createGSTReport(@Valid @RequestBody GSTReportBean reportBean)
    {
        reportBean.setMode("Manual");
        long reportID = reportService.createGSTReportRecord(reportBean);
        reportBean.setGenerateDate(new Date());
        /*
         START - Temporary code to be removed
         */
        reportBean.setReportId(reportID);
        reportBean.setPdfFilePath("/home/ec2-user/gst_reports/Error Code Testing Plan.pdf");
        reportBean.setReportStatus((ReportBean.reportStatusEnum.GENERATED).toString());
        reportDao.updateGSTReport_filepath(reportBean, ANPConstants.DB_TBL_GST_REPORT);
        reportDao.updateGSTReport_status(reportBean, ANPConstants.DB_TBL_GST_REPORT);
        /* END */
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/listGSTReport", produces = "application/json")
    public List<GSTReportBean> listGSTReport(@Valid @RequestBody ListParametersBean listParametersBean) {
        listParametersBean.setNoOfRecordsToShow(3);
        return reportDao.listGSTReport(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
    }

    /*
        This method will be used for sending email. This will be a async method. It will just create entry into the email table
        * toEmailList: Should have some value
        * reportID
        *
     */
    @PostMapping(path = "/emailGSTReport", produces = "application/json")
    public ResponseEntity emailGSTReport(@Valid @RequestBody GSTReportBean reportBean) {
        reportDao.createEmailEntry(reportBean);
        return new ResponseEntity<>("Success",HttpStatus.OK);
    }

    @PostMapping(path = "/getFrequentlyUsedEmail", produces = "application/json")
    public List<String> getFrequentlyUsedEmail(@RequestParam  long orgId, @RequestParam  String loggedInEmployeeId) {
        return reportDao.getFrequentlyUsedEmail(orgId, loggedInEmployeeId);
    }

    @PostMapping(path = "/createTransactionReport", produces = "application/json")
    public ResponseEntity createTransactionReport(@Valid @RequestBody TransactionReportBean reportBean)
    {

        long reportID = reportService.createTxnReportRecord(reportBean);
        reportBean.setGenerateDate(new Date());
        /*
         START - Temporary code to be removed
         */
        reportBean.setReportId(reportID);
        reportBean.setPdfFilePath("/home/ec2-user/gst_reports/Error Code Testing Plan.pdf");
        reportBean.setReportStatus((ReportBean.reportStatusEnum.GENERATED).toString());
        reportDao.updateGSTReport_filepath(reportBean,ANPConstants.DB_TBL_TXN_REPORT);
        reportDao.updateGSTReport_status(reportBean,ANPConstants.DB_TBL_TXN_REPORT);
        /* END */
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
  }
