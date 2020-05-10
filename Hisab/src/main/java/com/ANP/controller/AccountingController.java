package com.ANP.controller;

import com.ANP.bean.*;
import com.ANP.repository.AccountDAO;
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
    ExpenseDAO expenseDao;

    @Autowired
    AccountDAO accountDAO;


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

    @PostMapping(path = "/createPayToVendor", produces = "application/json")
    public ResponseEntity createPayToVendor(@RequestBody PayToVendorBean payToVendorBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isPayToVendorCreated = accountingHandler.createPayToVendor(payToVendorBean);
        if (isPayToVendorCreated) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failuer", HttpStatus.OK);
        }
        return responseEntity;
    }


    @PostMapping(path = "/createPaymentReceived", produces = "application/json")
    public ResponseEntity createPaymentReceived(@RequestBody PaymentReceivedBean paymentReceivedBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isPaymentReceivedCreated = accountingHandler.createPaymentReceived(paymentReceivedBean);
        if (isPaymentReceivedCreated) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }


    @PostMapping(path = "/createInternalTransfer", produces = "application/json")
    public ResponseEntity createInternalTransfer(@RequestBody InternalTransferBean internalTransferBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isInternalTransferCreated = accountingHandler.createInternalTransfer(internalTransferBean);
        if (isInternalTransferCreated) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }

    @PostMapping(path = "/getAccountsByNickName", produces = "application/json")
    public List<AccountBean> getAccountsByNickName(@RequestBody AccountBean accountBean) {
        return accountDAO.searchAccounts(accountBean);
    }

    @PostMapping(path = "/searchExpenseByToPartyName", produces = "application/json")
    public List<Expense> searchExpenseByToPartyName(@RequestParam String toPartyname, @RequestParam long orgId) {
        List<Expense> expenses = expenseDao.findExpenseByToPartyName(toPartyname, orgId);
        return expenses;
    }

    /*
        TODO Paras: Please complete this method
     */

    @PostMapping(path = "/updateIncludeInCAReport", produces = "application/json")
    public ResponseEntity updateIncludeInCAReport(@RequestParam long expenseID, @RequestParam boolean CASwtich) {
        expenseDao.updateIncludeInCAReport(expenseID,CASwtich);
        return  new ResponseEntity<>("Success", HttpStatus.OK);
    }
}