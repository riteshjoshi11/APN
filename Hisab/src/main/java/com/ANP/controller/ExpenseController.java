package com.ANP.controller;

import com.ANP.bean.Customer;
import com.ANP.bean.Expense;
import com.ANP.repository.CustomerDao;
import com.ANP.repository.ExpenseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/expense")
public class ExpenseController {

    @Autowired
    private ExpenseDao expenseDao;

    @GetMapping(path = "/get", produces = "application/json")
    public List<Expense> getExpense() {
        return expenseDao.getExpenses();
    }

    @PostMapping(path = "/create", produces = "application/json")
    public ResponseEntity createCustomer(@RequestBody Expense expense) {
        expenseDao.createExpense(expense);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping(path = "/search", produces = "application/json")
    public List<Expense> searchCustomer(@RequestBody Expense expense) {
        List<Expense> expenses = expenseDao.findByNameAndCity(expense);
        return expenses;
    }
}
