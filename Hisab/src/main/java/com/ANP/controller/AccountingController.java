package com.ANP.controller;

import com.ANP.bean.CustomerInvoiceBean;
import com.ANP.bean.EmployeeBean;
import com.ANP.bean.Expense;
import com.ANP.bean.PurchaseFromVendorBean;
import com.ANP.repository.ExpenseDAO;
import com.ANP.service.AccountingHandler;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/accounting")
public class AccountingController {

    @Autowired
    AccountingHandler accountingHandler;

    @Autowired
    private ExpenseDAO expenseDao;

    @PostMapping(path = "/createSalesEntry", produces = "application/json")
    public ResponseEntity createCustomerInvoice(@RequestBody CustomerInvoiceBean customerInvoiceBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isCustomerInvoiceCreated = accountingHandler.createCustomerInvoice(customerInvoiceBean);
        if (isCustomerInvoiceCreated) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }


    @PostMapping(path = "/createPurchaseEntry", produces = "application/json")
    public ResponseEntity createPurchaseFromVendor(@RequestBody PurchaseFromVendorBean purchaseFromVendorBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isPurchaseFromVendorCreated = accountingHandler.createVendorPurchase(purchaseFromVendorBean);
        if (isPurchaseFromVendorCreated) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }


    @GetMapping(path = "/getGeneralExpense", produces = "application/json")
    public List<Expense> getExpense() {
        return expenseDao.getExpenses();
    }

    @PostMapping(path = "/createGeneralExpense", produces = "application/json")
    public ResponseEntity createExpense(@RequestBody Expense expense) {
        expenseDao.createExpense(expense);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @ApiOperation("Search based on category,to party name and org")
    @PostMapping(path = "/searchGeneralExpense", produces = "application/json")
    public List<Expense> searchExpense(@RequestBody Expense expense) {
        List<Expense> expenses = expenseDao.findExpense(expense);
        return expenses;
    }

}
