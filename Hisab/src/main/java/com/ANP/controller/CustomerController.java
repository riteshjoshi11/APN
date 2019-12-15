package com.ANP.controller;


import java.util.List;

import com.ANP.bean.Customer;
import com.ANP.repository.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping(path = "/customer")
public class CustomerController {

    @Autowired
    private CustomerDao customerDao;

 @GetMapping(path = "/get", produces = "application/json")
        public List<Customer> getCustomer()
        {
            return customerDao.getCustomer();
        }

//        @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
//        public ResponseEntity<Object> addEmployee (@RequestBody Employee employee)
//        {
//            Integer id = employeeDao.getAllEmployees().getEmployeeList().size() + 1;
//            employee.setId(id);
//
//            employeeDao.addEmployee(employee);
//
//            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                    .path("/{id}")
//                    .buildAndExpand(employee.getId())
//                    .toUri();
//
//            return ResponseEntity.created(location).build();
//        }
//    }
}



