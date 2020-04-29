package com.ANP.controller;

import com.ANP.bean.Customer;
import com.ANP.bean.EmployeeBean;
import com.ANP.bean.EmployeeSalary;
import com.ANP.bean.EmployeeSalaryPayment;
import com.ANP.service.EmployeeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {

    @Autowired
    private EmployeeHandler employeeHandler;

    @PostMapping(path = "/createEmployee", produces = "application/json")
    public ResponseEntity createEmployee(@RequestBody EmployeeBean employeeBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isEmployeeCreated = employeeHandler.createEmployee(employeeBean);
        if (isEmployeeCreated) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }

    @PostMapping(path = "/createEmpSalaryPayment", produces = "application/json")
    public ResponseEntity createEmployeeSalary(@RequestBody EmployeeSalary employeeSalaryBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isEmployeeSalCreated = employeeHandler.createSalary(employeeSalaryBean);
        if (isEmployeeSalCreated) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }


    @PostMapping(path = "/createEmpSalary", produces = "application/json")
    public ResponseEntity createEmployeeSalaryPayment(@RequestBody EmployeeSalaryPayment employeeSalaryPayment) {
        ResponseEntity<String> responseEntity = null;
        boolean isEmployeeSalPaymentCreated = employeeHandler.createSalaryPayment(employeeSalaryPayment);
        if (isEmployeeSalPaymentCreated) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }

}
