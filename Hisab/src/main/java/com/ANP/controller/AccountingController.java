package com.ANP.controller;

import com.ANP.bean.*;
import com.ANP.repository.AccountDAO;
import com.ANP.repository.ExpenseDAO;
import com.ANP.repository.PurchaseFromVendorDAO;
import com.ANP.repository.RetailSaleDAO;
import com.ANP.service.AccountingHandler;
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

    @Autowired
    PurchaseFromVendorDAO purchaseFromVendorDAO ;



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

    @PostMapping(path = "/createGeneralExpense", produces = "application/json")
    public ResponseEntity createExpense(@RequestBody Expense expense) {
        expenseDao.createExpense(expense);
        return new ResponseEntity<>("Success", HttpStatus.OK);
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
    public List<Expense> searchExpenseByToPartyName(String toPartyname, @RequestParam long orgId) {
        List<Expense> expenses = expenseDao.findExpenseByToPartyName(toPartyname, orgId);
        return expenses;
    }

    /*
        TODO Paras: Please complete this method
     */

    @PostMapping(path = "/updateIncludeInCAReport", produces = "application/json")
    public ResponseEntity updateIncludeInCAReport(@RequestParam long expenseID, @RequestParam boolean CASwtich) {
        ResponseEntity<String> responseEntity = null;
        if(expenseDao.updateIncludeInCAReport(expenseID,CASwtich)){
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        }
        else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }

    @PostMapping(path = "/listPurchasesPaged", produces = "application/json")
    public List<PurchaseFromVendorBean> listPurchasesPaged(@RequestBody ListParametersBean listParametersBean) {
        return purchaseFromVendorDAO.listPurchasesPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());

    }

    @PostMapping(path = "/listExpensesPaged", produces = "application/json")
    public List<Expense> listExpensesPaged(@RequestBody ListParametersBean listParametersBean) {

        return expenseDao.listExpensesPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
    }


    @PostMapping(path = "/createRetailSale", produces = "application/json")
    public ResponseEntity createRetailSale(@RequestBody RetailSale retailSale) {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Error", HttpStatus.EXPECTATION_FAILED);
        boolean created = accountingHandler.createRetailSale(retailSale);
        if(created) {
            responseEntity =  new ResponseEntity<>("Success", HttpStatus.OK);
        }
        return  responseEntity;
    }
  }