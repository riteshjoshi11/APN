package com.ANP.controller;

import com.ANP.bean.*;
import com.ANP.repository.CustomerInvoiceDAO;
import com.ANP.repository.ExpenseDAO;
import com.ANP.repository.PurchaseFromVendorDAO;
import com.ANP.repository.ReportDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/report")
public class ReportController {

    @Autowired
    ReportDAO reportDao;

    @PostMapping(path = "/reportPdf", produces = "application/json")
    public ResponseEntity<InputStreamResource> fetchPdf(String filePath1, long orgId, String loggedInEmployeeID) throws  Exception{
        return reportDao.fetchPdf(filePath1,orgId,loggedInEmployeeID);
    }

    @PostMapping(path = "/reportExcel", produces = "application/json")
    public ResponseEntity<InputStreamResource> fetchExcel(String filePath1, long orgId, String loggedInEmployeeID) throws  Exception{
        return reportDao.fetchExcel(filePath1,orgId,loggedInEmployeeID);
    }


    @PostMapping(path = "/createGSTReport", produces = "application/json")
    public ResponseEntity createGSTReport(@RequestBody ReportBean reportBean)
    {
        reportDao.createGSTReport(reportBean);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/listGSTReport", produces = "application/json")
    public List<ReportBean> listGSTReport(@Valid @RequestBody ListParametersBean listParametersBean) {

        return reportDao.listGSTReport(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());

    }

}
