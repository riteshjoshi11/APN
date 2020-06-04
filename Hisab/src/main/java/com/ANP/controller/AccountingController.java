package com.ANP.controller;

import com.ANP.bean.*;
import com.ANP.repository.*;
import com.ANP.service.AccountingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @Autowired
    RetailSaleDAO retailSaleDAO;

    @Autowired
    InternalTransferDAO internalTransferDAO;

    @Autowired
    PayToVendorDAO payToVendorDAO;

    @Autowired
    CustomerInvoiceDAO customerInvoiceDAO;

    @Autowired
    PaymentReceivedDAO paymentReceivedDAO;

    @Autowired
    CustomerAuditDAO customerAuditDao;

    @Autowired
    EmployeeAuditDAO employeeAuditDao;


    @PostMapping(path = "/createSalesEntry", produces = "application/json")
    public ResponseEntity createCustomerInvoice(@Valid @RequestBody CustomerInvoiceBean customerInvoiceBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isCustomerInvoiceCreated = accountingHandler.createCustomerInvoice(customerInvoiceBean);
        if (isCustomerInvoiceCreated) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.EXPECTATION_FAILED);
        }
        return responseEntity;
    }


    @PostMapping(path = "/createPurchaseEntry", produces = "application/json")
    public ResponseEntity createPurchaseFromVendor(@Valid @RequestBody PurchaseFromVendorBean purchaseFromVendorBean) {
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
    public ResponseEntity createExpense(@Valid @RequestBody Expense expense) {
        accountingHandler.createExpense(expense);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/createPayToVendor", produces = "application/json")
    public ResponseEntity createPayToVendor(@Valid @RequestBody PayToVendorBean payToVendorBean) {
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
    public ResponseEntity createPaymentReceived(@Valid @RequestBody PaymentReceivedBean paymentReceivedBean) {
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
    public ResponseEntity createInternalTransfer(@Valid @RequestBody InternalTransferBean internalTransferBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isInternalTransferCreated = accountingHandler.createInternalTransfer(internalTransferBean);
        if (isInternalTransferCreated) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }

    @PostMapping(path = "/createRetailSale", produces = "application/json")
    public ResponseEntity createRetailSale(@Valid @RequestBody RetailSale retailSale) {
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Error", HttpStatus.EXPECTATION_FAILED);
        boolean created = accountingHandler.createRetailSale(retailSale);
        if(created) {
            responseEntity =  new ResponseEntity<>("Success", HttpStatus.OK);
        }
        return  responseEntity;
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
    public List<PurchaseFromVendorBean> listPurchasesPaged(@Valid @RequestBody ListParametersBean listParametersBean) {

        return purchaseFromVendorDAO.listPurchasesPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());

    }

    @PostMapping(path = "/listExpensesPaged", produces = "application/json")
    public List<Expense> listExpensesPaged(@Valid @RequestBody ListParametersBean listParametersBean) {

        return expenseDao.listExpensesPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
    }

    @PostMapping(path = "/listSalesPaged", produces = "application/json")
    public List<CustomerInvoiceBean> listSalesPaged(@Valid @RequestBody ListParametersBean listParametersBean)
    {
        return customerInvoiceDAO.listSalesPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
    }

    @PostMapping(path = "/listPayToVendorPaged", produces = "application/json")
    public List<PayToVendorBean> listPayToVendorPaged(@Valid @RequestBody ListParametersBean listParametersBean)
    {
        return payToVendorDAO.listPayToVendorPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
    }

    @PostMapping(path = "/listInternalTransfer", produces = "application/json")
    public List<InternalTransferBean> listInternalTransfer(@Valid @RequestBody ListParametersBean listParametersBean)
    {
        return internalTransferDAO.listInternalTransfer(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
    }

    @PostMapping(path = "/listRetailEntryPaged", produces = "application/json")
    public List<RetailSale> listRetailEntryPaged(@Valid @RequestBody ListParametersBean listParametersBean)
    {
        return retailSaleDAO.listRetailEntryPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
    }

    @PostMapping(path = "/listPaymentReceivedPaged", produces = "application/json")
    public List<PaymentReceivedBean> listPaymentReceivedPaged(@Valid @RequestBody ListParametersBean listParametersBean)
    {
        return paymentReceivedDAO.listPaymentReceivedPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
    }

    @PostMapping(path = "/listCustomerAuditPaged", produces = "application/json")
    public List<CustomerAuditBean> listCustomerAuditPaged(@Valid @RequestBody ListParametersBean listParametersBean)
    {
        return customerAuditDao.listCustomerAudit(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
    }

    @PostMapping(path = "/listEmployeeAuditPaged", produces = "application/json")
    public List<EmployeeAuditBean> listEmployeeAuditPaged(@Valid @RequestBody ListParametersBean listParametersBean)
    {
        return employeeAuditDao.listEmployeeAudit(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
    }

    @PostMapping(path = "/makeExpenseUnpaidToPaid", produces = "application/json")
    public ResponseEntity makeExpenseUnpaidToPaid(@Valid @RequestBody Expense expense) {
        ResponseEntity<String> responseEntity = null;
        if(accountingHandler.makeExpenseUnpaidToPaid(expense)) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.EXPECTATION_FAILED);
        }
        return responseEntity;
    }
  }