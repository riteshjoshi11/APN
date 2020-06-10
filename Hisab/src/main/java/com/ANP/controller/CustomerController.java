package com.ANP.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ANP.bean.CustomerBean;
import com.ANP.bean.ListParametersBean;
import com.ANP.bean.PurchaseFromVendorBean;
import com.ANP.bean.SearchParam;
import com.ANP.service.CustomerHandler;
import com.ANP.repository.CustomerDao;
import com.ANP.repository.PurchaseFromVendorDAO;
import com.ANP.util.ANPConstants;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/customer")
public class CustomerController {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerHandler customerHandler;

    @Autowired
    private PurchaseFromVendorDAO billDao;

    @PostMapping(path = "/create", produces = "application/json")
    public ResponseEntity createCustomer(@Valid @RequestBody CustomerBean customerBean) {
        customerHandler.createCustomer(customerBean);
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
}