package com.ANP.controller;

import com.ANP.bean.ListParametersBean;
import com.ANP.bean.PurchaseFromVendorBean;
import com.ANP.repository.ReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
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
}
