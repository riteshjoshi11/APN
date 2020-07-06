package com.ANP.controller;

import com.ANP.bean.ListParametersBean;
import com.ANP.bean.PurchaseFromVendorBean;
import com.ANP.bean.ReportBean;
import com.ANP.repository.ReportDao;
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
    ReportDao reportDao;

    @PostMapping(path = "/reportPdf", produces = "application/json")
    public ResponseEntity<InputStreamResource> fetchPdf() throws  Exception{
        return reportDao.fetchPdf();
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
