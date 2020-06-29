package com.ANP.controller;


import com.ANP.bean.City;
import com.ANP.bean.EmployeeType;
import com.ANP.bean.ExpenseCategory;
import com.ANP.repository.UIListDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/uiList")
/*
This controller is mainly for handling UI Related Elements e.g Expense Category, City etc
 */
public class UIListController {

    @Autowired
    private UIListDAO cityDao;

    @Autowired
    private UIListDAO UIListDAO;


    @PostMapping(path = "/getCities", produces = "application/json")
    public List<City> getCity() {
        return cityDao.getCity();
    }

    @PostMapping(path = "/createCity", produces = "application/json")
    public ResponseEntity createCity(@RequestBody City obj) {
        cityDao.createCity(obj);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/getExpenseCategories", produces = "application/json")
    public List<ExpenseCategory> getCategory() {
        return UIListDAO.getExpenseCategory();
    }

    @PostMapping(path = "/createExpenseCategory", produces = "application/json")
    public ResponseEntity createExpenseCategory(@RequestBody ExpenseCategory obj) {
        UIListDAO.createExpenseCat(obj);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/getEmployeeType", produces = "application/json")
    public List<EmployeeType> getEmployee() {
        return UIListDAO.getEmployeeType();
    }

}



