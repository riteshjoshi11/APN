package com.ANP.controller;


import com.ANP.bean.City;
import com.ANP.repository.MasterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/city")
public class CityController {

    @Autowired
    private MasterDao cityDao;

 @GetMapping(path = "/get", produces = "application/json")
        public List<City> getCity()
        {
            return cityDao.getCity();
        }

    @PostMapping(path = "/create", produces = "application/json" )
    public ResponseEntity createCustomer(@RequestBody City obj) {
        cityDao.createCity(obj);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }





//        @PostMapping(path = "/", consumes = "application/json", produces = "application/json")
//        public ResponseEntity<Object> addEmployee (@RequestBody Employee employee)
//        {
//            Integer id = employeeDao.getAllEmployees().getEmployeeList().size() + 1;
//            employee.setCustomerID(id);
//
//            employeeDao.addEmployee(employee);
//
//            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                    .path("/{id}")
//                    .buildAndExpand(employee.getCustomerID())
//                    .toUri();
//
//            return ResponseEntity.created(location).build();
//        }
//    }
}



