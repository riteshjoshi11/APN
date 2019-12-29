package com.ANP.controller;


import java.util.List;

import com.ANP.bean.Customer;
import com.ANP.repository.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



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

    @PostMapping(path = "/create", produces = "application/json" )
    public ResponseEntity createCustomer(@RequestBody Customer customer) {
        customerDao.createCustomer(customer);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping(path = "/search", produces = "application/json" )
    public List<Customer> searchCustomer(@RequestBody Customer customer) {
       List <Customer>customers =customerDao.findByNameAndCity(customer);
        return customers;
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



