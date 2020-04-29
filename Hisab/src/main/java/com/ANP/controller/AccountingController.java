package com.ANP.controller;

import com.ANP.bean.CustomerInvoiceBean;
import com.ANP.bean.EmployeeBean;
import com.ANP.bean.PurchaseFromVendorBean;
import com.ANP.service.AccountingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/accounting")
public class AccountingController {

    @Autowired
    AccountingHandler accountingHandler;

    @PostMapping(path = "/createSalesEntry", produces = "application/json")
    public ResponseEntity createCustomerInvoice(@RequestBody CustomerInvoiceBean customerInvoiceBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isCustomerInvoiceCreated = accountingHandler.createCustomerInvoice(customerInvoiceBean);
        if (isCustomerInvoiceCreated) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }


    @PostMapping(path = "/createPurchaseEntry", produces = "application/json")
    public ResponseEntity createPurchaseFromVendor(@RequestBody PurchaseFromVendorBean purchaseFromVendorBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isPurchaseFromVendorCreated = accountingHandler.createVendorPurchase(purchaseFromVendorBean);
        if (isPurchaseFromVendorCreated) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }

}
