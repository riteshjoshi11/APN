package com.ANP.controller;

import java.util.Collection;
import java.util.List;

import com.ANP.bean.CustomerBean;
import com.ANP.bean.PurchaseFromVendorBean;
import com.ANP.bean.SearchParam;
import com.ANP.service.CustomerHandler;
import com.ANP.repository.CustomerDao;
import com.ANP.repository.PurchaseFromVendorDAO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity createCustomer(@RequestBody CustomerBean customerBean) {
        boolean isCustomerCreated = customerHandler.createCustomer(customerBean);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    /*
     * This is one of the important method for the UI to list customer and vendor with their account balance
     */
    @PostMapping(path = "/listCustomerANDVendorWithBalance", produces = "application/json")
    public List<CustomerBean> listCustomerANDVendorWithBalance(long orgID, Collection<SearchParam> searchParams,
                                                               String orderBy, int pageStartIndex, int pageEndIndex) {
        return customerDao.listCustomerVendorsWithBalance(orgID,searchParams,orderBy, pageStartIndex,pageEndIndex);
    }


}



