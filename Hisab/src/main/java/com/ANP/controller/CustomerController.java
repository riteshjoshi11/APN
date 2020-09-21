package com.ANP.controller;

import java.util.List;

import com.ANP.bean.CustomerBean;
import com.ANP.bean.EmployeeBean;
import com.ANP.bean.ListParametersBean;
import com.ANP.service.CustomerHandler;
import com.ANP.repository.CustomerDAO;
import com.ANP.repository.PurchaseFromVendorDAO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/customer")
public class CustomerController {

    @Autowired
    private CustomerDAO customerDao;

    @Autowired
    private CustomerHandler customerHandler;

    @Autowired
    private PurchaseFromVendorDAO billDao;

    @PostMapping(path = "/create", produces = "application/json")
    public ResponseEntity createCustomer(@Valid @RequestBody CustomerBean customerBean) {
        customerHandler.createCustomer(customerBean);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/updateCustomer", produces = "application/json")
    public ResponseEntity updateCustomer(@Valid @RequestBody CustomerBean customerBean) {
        customerHandler.updateCustomer(customerBean);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    /*
     * This is one of the important method for the UI to list customer and vendor with their account balance
     */
    @PostMapping(path = "/listCustomerANDVendorWithBalancePaged", produces = "application/json")
    public List<CustomerBean> listCustomerANDVendorWithBalancePaged(@Valid @RequestBody ListParametersBean listParametersBean) {
        return customerDao.listCustomerANDVendorWithBalancePaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
    }

    @GetMapping(path = "/getCustomerById", produces = "application/json")
    public CustomerBean getCustomerById(@RequestParam Long orgId, @RequestParam String customerId) {
       return customerDao.getCustomerById(orgId,customerId);
    }
    @PostMapping(path = "/updateSendPaymentReminders", produces = "application/json")
    public ResponseEntity updateSendPaymentReminders(@RequestBody CustomerBean customerBean) {
        customerDao.updateSendPaymentReminders(customerBean);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}