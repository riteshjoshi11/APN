package com.ANP.controller;

import com.ANP.bean.*;
import com.ANP.repository.*;
import com.ANP.service.AccountingHandler;
import com.ANP.util.ANPConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.time.Instant;
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
    PurchaseFromVendorDAO purchaseFromVendorDAO;

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

    private static final Logger logger = LoggerFactory.getLogger(AccountingController.class);


    @PostMapping(path = "/createSalesEntry", produces = "application/json")
    public ResponseEntity createCustomerInvoice(@Valid @RequestBody CustomerInvoiceBean customerInvoiceBean) {
        logger.trace("Entering createCustomerInvoice(): CustomerInvoiceBean : " + customerInvoiceBean);
        Instant start = Instant.now();
        accountingHandler.createCustomerInvoice(customerInvoiceBean);
        logger.trace("Exiting createCustomerInvoice() : Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/createPurchaseEntry", produces = "application/json")
    public ResponseEntity createPurchaseFromVendor(@Valid @RequestBody PurchaseFromVendorBean purchaseFromVendorBean) {
        logger.trace("Entering createPurchaseFromVendor()");
        Instant start = Instant.now();
        accountingHandler.createVendorPurchase(purchaseFromVendorBean);
        logger.trace("Exiting createPurchaseEntry() : Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/createGeneralExpense", produces = "application/json")
    public ResponseEntity createExpense(@Valid @RequestBody Expense expense) {
        logger.trace("Entering createExpense(): Expense=" + expense);
        Instant start = Instant.now();
        accountingHandler.createExpense(expense);
        logger.trace("Exiting createExpense() : Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/createPayToVendor", produces = "application/json")
    public ResponseEntity createPayToVendor(@Valid @RequestBody PayToVendorBean payToVendorBean) {
        logger.trace("Entering createPayToVendor(): payToVendorBean: " + payToVendorBean);
        Instant start = Instant.now();
        accountingHandler.createPayToVendor(payToVendorBean);
        if (payToVendorBean.isCreatePurchaseEntryAlso()) {
            PurchaseFromVendorBean purchaseFromVendorBean = new PurchaseFromVendorBean();
            purchaseFromVendorBean.setFromAccountId(payToVendorBean.getToAccountID());
            purchaseFromVendorBean.setFromCustomerId(payToVendorBean.getToCustomerID());
            purchaseFromVendorBean.setOrgId(payToVendorBean.getOrgId());
            purchaseFromVendorBean.setDate(payToVendorBean.getPaymentDate());
            purchaseFromVendorBean.setCreatedbyId(payToVendorBean.getCreatedbyId());
            purchaseFromVendorBean.setTotalAmount(payToVendorBean.getAmount());
            accountingHandler.createVendorPurchase(purchaseFromVendorBean);
        }
        logger.trace("Exiting createPayToVendor() : Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/createPaymentReceived", produces = "application/json")
    public ResponseEntity createPaymentReceived(@Valid @RequestBody PaymentReceivedBean paymentReceivedBean) {
        logger.trace("Entering createPaymentReceived(): paymentReceivedBean=" + paymentReceivedBean );
        Instant start = Instant.now();
        accountingHandler.createPaymentReceived(paymentReceivedBean);
        if (paymentReceivedBean.isCreateSaleEntryAlso()) {
            CustomerInvoiceBean customerInvoiceBean = new CustomerInvoiceBean();
            customerInvoiceBean.setCreatedbyId(paymentReceivedBean.getCreatedbyId());
            customerInvoiceBean.setDate(paymentReceivedBean.getReceivedDate());
            customerInvoiceBean.setToCustomerId(paymentReceivedBean.getFromCustomerID());
            customerInvoiceBean.setToAccountId(paymentReceivedBean.getFromAccountID());
            customerInvoiceBean.setOrgId(paymentReceivedBean.getOrgId());
            customerInvoiceBean.setTotalAmount(paymentReceivedBean.getAmount());
            accountingHandler.createCustomerInvoice(customerInvoiceBean);
        }
        logger.trace("Exiting createPaymentReceived() : Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/createInternalTransfer", produces = "application/json")
    public ResponseEntity createInternalTransfer(@Valid @RequestBody InternalTransferBean internalTransferBean) {
        logger.trace("Entering createInternalTransfer(): internalTransferBean:" + internalTransferBean);
        Instant start = Instant.now();
        accountingHandler.createInternalTransfer(internalTransferBean);
        logger.trace("Exiting createInternalTransfer() : Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/createRetailSale", produces = "application/json")
    public ResponseEntity createRetailSale(@Valid @RequestBody RetailSale retailSale) {
        logger.trace("Entering createRetailSale(): RetailSale:" + retailSale);
        Instant start = Instant.now();
        accountingHandler.createRetailSale(retailSale);
        logger.trace("Exiting createRetailSale() : Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/getAccountsByNickName", produces = "application/json")
    public List<AccountBean> getAccountsByNickName(@RequestBody AccountBean accountBean) {
        logger.trace("Entering getAccountsByNickName()");
        Instant start = Instant.now();
        List<AccountBean> accountBeans = accountDAO.searchAccounts(accountBean);
        logger.trace("Exiting getAccountsByNickName(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return accountBeans;
    }

    @PostMapping(path = "/searchExpenseByToPartyName", produces = "application/json")
    public List<Expense> searchExpenseByToPartyName(String toPartyname, @RequestParam long orgId) {
        logger.trace("Entering searchExpenseByToPartyName()");
        Instant start = Instant.now();
        List<Expense> expenses = expenseDao.findExpenseByToPartyName(toPartyname, orgId);
        logger.trace("Exiting searchExpenseByToPartyName(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return expenses;
    }

    /*
        TODO Paras: Please complete this method
     */

    @PostMapping(path = "/updateIncludeInCAReport", produces = "application/json")
    public ResponseEntity updateIncludeInCAReport(@RequestParam long expenseID, @RequestParam boolean CASwtich) {
        logger.trace("Entering updateIncludeInCAReport()");
        Instant start = Instant.now();
        expenseDao.updateIncludeInCAReport(expenseID, CASwtich);
        logger.trace("Exiting updateIncludeInCAReport(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/listPurchasesPaged", produces = "application/json")
    public List<PurchaseFromVendorBean> listPurchasesPaged(@Valid @RequestBody ListParametersBean listParametersBean) {
        logger.trace("Entering listPurchasesPaged()");
        Instant start = Instant.now();
        List<PurchaseFromVendorBean> purchaseFromVendorBeans = purchaseFromVendorDAO.listPurchasesPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
        logger.trace("Exiting listPurchasesPaged(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return purchaseFromVendorBeans;
    }


    @PostMapping(path = "/listExpensesPaged", produces = "application/json")
    public List<Expense> listExpensesPaged(@Valid @RequestBody ListParametersBean listParametersBean) {
        logger.trace("Entering listExpensesPaged()");
        Instant start = Instant.now();
        List<Expense> expenseList = expenseDao.listExpensesPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
        logger.trace("Exiting listExpensesPaged(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return expenseList;
    }

    @PostMapping(path = "/listSalesPaged", produces = "application/json")
    public List<CustomerInvoiceBean> listSalesPaged(@Valid @RequestBody ListParametersBean listParametersBean) {
        logger.trace("Entering listSalesPaged() : param=" + listParametersBean);
        Instant start = Instant.now();
        List<CustomerInvoiceBean> customerInvoiceBeanList = customerInvoiceDAO.listSalesPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
        logger.trace("Exiting listSalesPaged(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return customerInvoiceBeanList;

    }

    @PostMapping(path = "/listPayToVendorPaged", produces = "application/json")
    public List<PayToVendorBean> listPayToVendorPaged(@Valid @RequestBody ListParametersBean listParametersBean) {
        logger.trace("Entering listPayToVendorPaged(): Param=" + listParametersBean);
        Instant start = Instant.now();
        List<PayToVendorBean> payToVendorBeanList = payToVendorDAO.listPayToVendorPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
        logger.trace("Exiting listPayToVendorPaged(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return payToVendorBeanList;
    }

    @PostMapping(path = "/listInternalTransfer", produces = "application/json")
    public List<InternalTransferBean> listInternalTransfer(@Valid @RequestBody ListParametersBean listParametersBean) {
        logger.trace("Entering listInternalTransfer() : param=" + listParametersBean);
        Instant start = Instant.now();
        List<InternalTransferBean> payToVendorBeanList = internalTransferDAO.listInternalTransfer(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
        logger.trace("Exiting listInternalTransfer(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return payToVendorBeanList;
    }

    @PostMapping(path = "/listRetailEntryPaged", produces = "application/json")
    public List<RetailSale> listRetailEntryPaged(@Valid @RequestBody ListParametersBean listParametersBean) {
        logger.trace("Entering listRetailEntryPaged() : param=" + listParametersBean);
        Instant start = Instant.now();
        List<RetailSale> retailSaleList = retailSaleDAO.listRetailEntryPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
        logger.trace("Exiting listRetailEntryPaged(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return retailSaleList;
    }

    @PostMapping(path = "/listPaymentReceivedPaged", produces = "application/json")
    public List<PaymentReceivedBean> listPaymentReceivedPaged(@Valid @RequestBody ListParametersBean listParametersBean) {
        logger.trace("Entering listPaymentReceivedPaged() : param=" + listParametersBean);
        Instant start = Instant.now();
        List<PaymentReceivedBean> paymentReceivedBeanList = paymentReceivedDAO.listPaymentReceivedPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
        logger.trace("Exiting listPaymentReceivedPaged(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return paymentReceivedBeanList;
    }

    @PostMapping(path = "/listCustomerAuditPaged", produces = "application/json")
    public List<CustomerAuditBean> listCustomerAuditPaged(@Valid @RequestBody ListParametersBean listParametersBean) {
        logger.trace("Entering listCustomerAuditPaged() : param=" + listParametersBean);
        Instant start = Instant.now();
        List<CustomerAuditBean> customerAuditBeanList = customerAuditDao.listCustomerAudit(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
        logger.trace("Exiting listCustomerAuditPaged(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return customerAuditBeanList;
    }

    @PostMapping(path = "/listEmployeeAuditPaged", produces = "application/json")
    public List<EmployeeAuditBean> listEmployeeAuditPaged(@Valid @RequestBody ListParametersBean listParametersBean) {
        logger.trace("Entering listEmployeeAuditPaged() : param=" + listParametersBean);
        Instant start = Instant.now();

        List<EmployeeAuditBean> employeeAuditBeanList = employeeAuditDao.listEmployeeAudit(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
        logger.trace("Exiting listEmployeeAuditPaged(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return employeeAuditBeanList;
    }

    @PostMapping(path = "/makeExpenseUnpaidToPaid", produces = "application/json")
    public ResponseEntity makeExpenseUnpaidToPaid(@Valid @RequestBody Expense expense) {
        logger.trace("Entering makeExpenseUnpaidToPaid()");
        Instant start = Instant.now();
        accountingHandler.makeExpenseUnpaidToPaid(expense);
        logger.trace("Exiting makeExpenseUnpaidToPaid(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/deleteSingleTransaction", produces = "application/json")
    public ResponseEntity deleteSingleTransaction(String transactionName, long orgID, long recordID, String loggedInUserName) {
        logger.trace("Entering deleteSingleTransaction() : orgId[" + orgID + "] recordID[" + recordID
                + "] transactionName[" + transactionName + "] loggedInUserName[" + loggedInUserName + "]");
        Instant start = Instant.now();
        switch (transactionName) {
            case ANPConstants.TRANSACTION_NAME_SALE:
                accountingHandler.deleteCustomerInvoice(orgID, recordID, loggedInUserName);
                break;
            case ANPConstants.TRANSACTION_NAME_PURCHASE:
                accountingHandler.deleteVendorPurchase(orgID, recordID, loggedInUserName);
                break;
            case ANPConstants.TRANSACTION_NAME_PAY_TO_VENDOR:
                accountingHandler.deletePayToVendor(orgID, recordID, loggedInUserName);
                break;
            case ANPConstants.TRANSACTION_NAME_PAYMENT_RECEIVED:
                accountingHandler.deletePaymentReceived(orgID, recordID, loggedInUserName);
                break;
            case ANPConstants.TRANSACTION_NAME_EXPENSE:
                accountingHandler.deleteExpense(orgID, recordID, loggedInUserName);
                break;
            case ANPConstants.TRANSACTION_NAME_INTERNAL_TRANSFER:
                accountingHandler.deleteInternalTransfer(orgID, recordID, loggedInUserName);
                break;
            case ANPConstants.TRANSACTION_NAME_RETAIL_SALE:
                accountingHandler.deleteRetailSale(orgID, recordID, loggedInUserName);
                break;
            default:
        }
        logger.trace("Exiting deleteSingleTransaction(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/updateSales", produces = "application/json")
    public ResponseEntity updateSales(@RequestBody CustomerInvoiceBean customerInvoiceBean) {
        logger.trace("Entering updateSales(): customerInvoiceBean:" + customerInvoiceBean);
        Instant start = Instant.now();
        customerInvoiceDAO.updateSales(customerInvoiceBean);
        logger.trace("Exiting updateSales(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/updatePurchase", produces = "application/json")
    public ResponseEntity updatePurchase(@RequestBody PurchaseFromVendorBean purchaseFromVendorBean) {
        logger.trace("Entering updatePurchase()");
        Instant start = Instant.now();
        purchaseFromVendorDAO.updatePurchase(purchaseFromVendorBean);
        logger.trace("Exiting updatePurchase(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/updateExpense", produces = "application/json")
    public ResponseEntity updateExpense(@RequestBody Expense expense) {
        logger.trace("Entering updateExpense() : expense: " + expense);
        Instant start = Instant.now();
        accountingHandler.updateExpense(expense);
        logger.trace("Exiting updateExpense(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/updatePayToVendor", produces = "application/json")
    public ResponseEntity updatePayToVendor(@RequestBody PayToVendorBean payToVendorBean) {
        logger.trace("Entering updatePayToVendor(): " + payToVendorBean);
        Instant start = Instant.now();
        payToVendorDAO.updatePayToVendor(payToVendorBean);
        logger.trace("Exiting updatePayToVendor(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/updatePaymentReceived", produces = "application/json")
    public ResponseEntity updatePaymentReceived(@RequestBody PaymentReceivedBean paymentReceivedBean) {
        logger.trace("Entering updatePaymentReceived(): paymentReceivedBean: " + paymentReceivedBean);
        Instant start = Instant.now();
        paymentReceivedDAO.updatePaymentReceived(paymentReceivedBean);
        logger.trace("Exiting updatePaymentReceived(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/updateInternalTransfer", produces = "application/json")
    public ResponseEntity updateInternalTransfer(@RequestBody InternalTransferBean internalTransferBean) {
        logger.trace("Entering updateInternalTransfer(): internalTransferBean: " + internalTransferBean);
        Instant start = Instant.now();
        internalTransferDAO.updateInternalTransfer(internalTransferBean);
        logger.trace("Exiting updateInternalTransfer(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/updateRetailSale", produces = "application/json")
    public ResponseEntity updateRetailSale(@RequestBody RetailSale retailSale) {
        logger.trace("Entering updateRetailSale(): retailSale: " + retailSale);
        Instant start = Instant.now();
        retailSaleDAO.updateRetailSale(retailSale);
        logger.trace("Exiting updateRetailSale(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    /*
     Will be invoked by UI Before Update
     */

    @GetMapping(path = "/getExpenseById", produces = "application/json")
    public Expense getExpenseById(@RequestParam Long orgId, @RequestParam Long expenseId) {
        logger.trace("Entering getExpenseById()");
        Instant start = Instant.now();
        Expense expense = expenseDao.getExpenseById(orgId, expenseId);
        logger.trace("Exiting getExpenseById(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return expense;
    }

    @PostMapping(path = "/listEmployeeSalaryAuditPaged", produces = "application/json")
    public List<EmployeeAuditBean> listEmployeeSalaryAuditPaged(@Valid @RequestBody ListParametersBean listParametersBean) {
        logger.trace("Entering listInternalTransfer() : Param=" + listParametersBean);
        Instant start = Instant.now();
        List<EmployeeAuditBean> employeeAuditBeanList = employeeAuditDao.listEmployeeSalaryAudit(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
        logger.trace("Exiting listInternalTransfer(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return employeeAuditBeanList;
    }

    @GetMapping(path = "/getSalesById", produces = "application/json")
    public CustomerInvoiceBean getSalesById(@RequestParam Long orgId, @RequestParam Long salesId) {
        logger.trace("Entering getSalesById()");
        Instant start = Instant.now();
        CustomerInvoiceBean invoiceBean = customerInvoiceDAO.getSalesById(orgId, salesId);
        logger.trace("Exiting getSalesById(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return invoiceBean;
    }

    @GetMapping(path = "/getPurchaseById", produces = "application/json")
    public PurchaseFromVendorBean getPurchaseById(@RequestParam Long orgId, @RequestParam Long purchaseId) {
        logger.trace("Entering getPurchaseById()");
        Instant start = Instant.now();
        PurchaseFromVendorBean purchaseFromVendorBean = purchaseFromVendorDAO.getPurchaseById(orgId, purchaseId);
        logger.trace("Exiting getPurchaseById(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return purchaseFromVendorBean;
    }

    @GetMapping(path = "/getInternalTransferId", produces = "application/json")
    public InternalTransferBean getInternalTransferId(@RequestParam Long orgId, @RequestParam Long internalId) {
        logger.trace("Entering getInternalTransferId()");
        Instant start = Instant.now();
        InternalTransferBean internalTransferBean = internalTransferDAO.getInternalTransferId(orgId, internalId);
        logger.trace("Exiting getInternalTransferId(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return internalTransferBean;
    }

    @GetMapping(path = "/getPaymentReceivedById", produces = "application/json")
    public PaymentReceivedBean getPaymentReceivedById(@RequestParam Long orgId, @RequestParam Long paymentReceivedID) {
        logger.trace("Entering getPaymentReceivedById()");
        Instant start = Instant.now();
        PaymentReceivedBean paymentReceivedBean = paymentReceivedDAO.getPaymentReceivedById(orgId, paymentReceivedID);
        logger.trace("Exiting getPaymentReceivedById(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return paymentReceivedBean;
    }

    @GetMapping(path = "/getPayToVendorById", produces = "application/json")
    public PayToVendorBean getPayToVendorById(@RequestParam Long orgId, @RequestParam Long payToVendorId) {
        logger.trace("Entering getPayToVendorById()");
        Instant start = Instant.now();
        PayToVendorBean payToVendorBean = payToVendorDAO.getPayToVendorById(orgId, payToVendorId);
        logger.trace("Exiting getPayToVendorById(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return payToVendorBean;
    }

    @GetMapping(path = "/getRetailSaleById", produces = "application/json")
    public RetailSale getRetailSaleById(@RequestParam Long orgId, @RequestParam Long retailSaleId) {
        logger.trace("Entering getRetailSaleById()");
        Instant start = Instant.now();
        RetailSale retailSale = retailSaleDAO.getRetailSaleById(orgId, retailSaleId);
        logger.trace("Exiting getRetailSaleById(): Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");
        return retailSale;
    }
}