package com.ANP.controller;

import com.ANP.bean.*;
import com.ANP.repository.ReportDAO;
import com.ANP.service.ReportService;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(path = "/downloadPDF", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> fetchPdf(@RequestParam String filePath, @RequestParam long orgId,
                                                        @RequestParam String loggedInEmployeeID) {
        return reportDao.fetchPdf(filePath, orgId, loggedInEmployeeID);
    }

    @GetMapping(path = "/downloadExcel", produces = "application/json")
    public ResponseEntity<InputStreamResource> fetchExcel(@RequestParam String filePath, @RequestParam long orgId,
                                                          @RequestParam String loggedInEmployeeID) {
        return reportDao.fetchExcel(filePath, orgId, loggedInEmployeeID);
    }


    @PostMapping(path = "/createGSTReport", produces = "application/json")
    public ResponseEntity createGSTReport(@Valid @RequestBody GSTReportBean reportBean) {
        reportBean.setMode("Manual");
        long reportID = reportService.createGSTReportRecord(reportBean);
        reportBean.setGenerateDate(new Date());
        /*
         START - Temporary code to be removed
         */
        String testModeOTP = System.getProperty("TestModeOTP");
        if (!ANPUtils.isNullOrEmpty(testModeOTP)) {
            reportBean.setReportId(reportID);
            reportBean.setPdfFilePath("/home/ec2-user/gst_reports/Error Code Testing Plan.pdf");
            reportBean.setExcelFilePath("/home/ec2-user/gst_reports/PermissionGroup.csv");
            reportBean.setReportStatus((ReportBean.reportStatusEnum.GENERATED).toString());
            reportDao.updateReport_filepath(reportBean, ANPConstants.DB_TBL_GST_REPORT);
            reportDao.updateReport_status(reportBean, ANPConstants.DB_TBL_GST_REPORT);
        }
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
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/getFrequentlyUsedGSTEmail", produces = "application/json")
    public List<String> getFrequentlyUsedGSTEmail(@RequestParam long orgId, @RequestParam String loggedInEmployeeId) {
        return reportDao.getFrequentlyUsedGSTEmail(orgId, loggedInEmployeeId);
    }

    @PostMapping(path = "/createTransactionReport", produces = "application/json")
    public ResponseEntity createTransactionReport(@Valid @RequestBody TransactionReportBean reportBean) {
        long reportID = reportService.createTxnReportRecord(reportBean);
        /*
         START - Temporary code to be removed
         */
        reportBean.setReportId(reportID);
        reportBean.setPdfFilePath("/home/ec2-user/gst_reports/Error Code Testing Plan.pdf");
        reportBean.setExcelFilePath("/home/ec2-user/gst_reports/PermissionGroup.csv");
        reportBean.setReportStatus((ReportBean.reportStatusEnum.GENERATED).toString());
        reportBean.setGenerateDate(new Date());
        reportDao.updateReport_filepath(reportBean, ANPConstants.DB_TBL_TXN_REPORT);
        reportDao.updateReport_status(reportBean, ANPConstants.DB_TBL_TXN_REPORT);
        /* END */
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/listTransactionReport", produces = "application/json")
    public List<TransactionReportBean> listTransactionReport(@Valid @RequestBody ListParametersBean listParametersBean) {
        listParametersBean.setNoOfRecordsToShow(2);
        return reportDao.listTxnReport(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
    }

    @PostMapping(path = "/emailTransactionReport", produces = "application/json")
    public ResponseEntity emailTransactionReport(@Valid @RequestBody TransactionReportBean reportBean) {
        reportDao.createEmailEntryForTxn(reportBean);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/backupReportGeneration", produces = "application/json")
    public ResponseEntity backupReportGeneration(long orgId, String format, String dateFrom , String dateTo) {
        reportService.generateTransactionReport(orgId,format,dateFrom,dateTo);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
