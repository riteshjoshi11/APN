package com.ANP.controller;

import java.util.List;

import com.ANP.bean.CustomerBean;
import com.ANP.bean.PurchaseFromVendorBean;
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

    @GetMapping(path = "/get", produces = "application/json")
    public List<CustomerBean> getCustomer() {
        return customerDao.getCustomer();
    }

    @PostMapping(path = "/create", produces = "application/json")
    public ResponseEntity createCustomer(@RequestBody CustomerBean customerBean) {
        boolean isCustomerCreated = customerHandler.createCustomer(customerBean);
        //int result= customerDao.createCustomer(customer);

        return new ResponseEntity<>("Success", HttpStatus.OK);

    }

    @ApiOperation(value = "search customer based on name and city")
    @PostMapping(path = "/search", produces = "application/json")
    public List<CustomerBean> searchCustomer(@RequestBody CustomerBean customerBean) {
        List<CustomerBean> customerBeans = customerDao.findByNameAndCity(customerBean);
        return customerBeans;
    }

}



