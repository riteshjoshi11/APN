package com.ANP.controller;


import com.ANP.bean.City;
import com.ANP.bean.ExpenseCategory;
import com.ANP.repository.MasterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/expenseCategory")
public class ExpenseCategoryController {

    @Autowired
    private MasterDao masterDao;

 @GetMapping(path = "/get", produces = "application/json")
        public List<ExpenseCategory> getCity()
        {
            return masterDao.getExpenseCategory();
        }

    @PostMapping(path = "/create", produces = "application/json" )
    public ResponseEntity createCustomer(@RequestBody ExpenseCategory obj) {
        masterDao.createExpenseCat(obj);
        return new ResponseEntity<>("Success", HttpStatus.OK);
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



