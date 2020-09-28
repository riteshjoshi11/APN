package com.ANP.controller;


import com.ANP.bean.*;
import com.ANP.repository.SystemConfigurationReaderDAO;
import com.ANP.repository.UIListDAO;
import com.ANP.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @Autowired
    EmailUtil emailUtil;

    Map<Integer,UIItem> SYSTEM_RESET_OPTION_MAP = null;

    //Initializer block
    {
        System.out.println("Initializing Map...");
        SYSTEM_RESET_OPTION_MAP = new HashMap<Integer,UIItem>();
        SYSTEM_RESET_OPTION_MAP.put(1, new UIItem("1", "Retain GST Transaction(s)"));
        SYSTEM_RESET_OPTION_MAP.put(2, new UIItem("2", "Retain Staff Salary Transaction(s)"));
        SYSTEM_RESET_OPTION_MAP.put(3, new UIItem("3", "Retain Staff Acccount Balance(s)"));
        SYSTEM_RESET_OPTION_MAP.put(4, new UIItem("4", "Retain Customer/Supplier Balance(s)"));
        SYSTEM_RESET_OPTION_MAP.put(5, new UIItem("5", "Retain Staff Salary Balance(s)"));
    }

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
    public List<UIItem> getEmployee() {
        return UIListDAO.getEmployeeType();
    }

    @PostMapping(path = "/getOrgDetailsUILists", produces = "application/json")
    public OrgDetailsUIBean getOrgDetailsUILists() {
        return UIListDAO.getOrgDetailsUILists();
    }

    @PostMapping(path = "/getSystemResetScreenOptions", produces = "application/json")
    public Collection<UIItem> getSystemResetScreenOptions() {
        return SYSTEM_RESET_OPTION_MAP.values();
    }
/*
    @PostMapping(path = "/getTest", produces = "application/json")
    public void getTest() {
        emailUtil.sendEmail(null,null,null,null,null);
    }

 */
}



