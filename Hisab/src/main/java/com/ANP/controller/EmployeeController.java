package com.ANP.controller;

import com.ANP.service.CustomerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {

    @Autowired
    private CustomerHandler customerHandler ;


}
