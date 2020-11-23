package com.ANP.service;

import com.ANP.bean.*;
import com.ANP.repository.*;
import com.ANP.util.ANPConstants;
import com.ANP.util.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AccountingHandler {

    @Autowired
    PurchaseFromVendorDAO purchaseFromVendorDAO;

    @Autowired
    CustomerInvoiceDAO customerInvoiceDAO;

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    PayToVendorDAO payToVendorDAO;

    @Autowired
    PaymentReceivedDAO paymentReceivedDAO;

    @Autowired
    InternalTransferDAO internalTransferDAO;

    @Autowired
    RetailSaleDAO retailSaleDAO;

    @Autowired
    ExpenseDAO expenseDAO;

    @Autowired
    CalculationTrackerDAO calculationTrackerDAO;

    @Autowired
    CommonDAO commonDAO;

    @Autowired
    LocaleHandlerService localeHandlerService;

    /*
     * Create a invoice for a customer (Sale Entry)
     * Only Affects the customer balance
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean createCustomerInvoice(CustomerInvoiceBean customerInvoiceBean) {
        customerInvoiceDAO.createInvoice(customerInvoiceBean);
        //accountDAO.updateAccountBalance(customerInvoiceBean.getToAccountId(), customerInvoiceBean.getTotalAmount(), "SUBTRACT");
        if (customerInvoiceBean.isIncludeInCalc()) {
            CustomerAuditBean customerAuditBean = new CustomerAuditBean();
            customerAuditBean.setOrgId(customerInvoiceBean.getOrgId());
            customerAuditBean.setCustomerid(customerInvoiceBean.getToCustomerId());
            customerAuditBean.setAccountid(customerInvoiceBean.getToAccountId());
            customerAuditBean.setAmount(customerInvoiceBean.getTotalAmount());
            customerAuditBean.setType("" + CustomerAuditBean.TRANSACTION_TYPE_ENUM.Sale);
            customerAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
            customerAuditBean.setTransactionDate(customerInvoiceBean.getDate());
            customerAuditBean.setOtherPartyName("");
            accountDAO.updateCustomerAccountBalance(customerAuditBean);
        }
        return true;
    }

    /*
     * Create a Bill Received, Purchase Entry
     * Only affects the vendor/customer balance
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createVendorPurchase(PurchaseFromVendorBean purchaseFromVendorBean) {
        purchaseFromVendorDAO.createBill(purchaseFromVendorBean);
        //  accountDAO.updateAccountBalance(purchaseFromVendorBean.getFromAccountId(), purchaseFromVendorBean.getTotalAmount(), "ADD");
        if (purchaseFromVendorBean.isIncludeInCalc()) {
            CustomerAuditBean customerAuditBean = new CustomerAuditBean();
            customerAuditBean.setOrgId(purchaseFromVendorBean.getOrgId());
            customerAuditBean.setCustomerid(purchaseFromVendorBean.getFromCustomerId());
            customerAuditBean.setAccountid(purchaseFromVendorBean.getFromAccountId());
            customerAuditBean.setAmount(purchaseFromVendorBean.getTotalAmount());
            customerAuditBean.setType("" + CustomerAuditBean.TRANSACTION_TYPE_ENUM.Purchase);
            customerAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            customerAuditBean.setTransactionDate(purchaseFromVendorBean.getDate());
            customerAuditBean.setOtherPartyName("");
            accountDAO.updateCustomerAccountBalance(customerAuditBean);
        }
        return true;
    }

    /*
     *   Logic:
     *   Payment To Vendor: 1. TOAccount (To Whom payment is made)  is SUBTRACTED 2. FromAccount(Employee who made payment) is SUBTRACTED
     *  Affect Customer + Employee both
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createPayToVendor(PayToVendorBean payToVendorBean) {
        payToVendorDAO.createPayToVendor(payToVendorBean);
        // accountDAO.updateAccountBalance(payToVendorBean.getToAccountID(), payToVendorBean.getAmount(), "SUBTRACT");

        //Update Customer Balance and Audit
        if (payToVendorBean.isIncludeInCalc()) {
            CustomerAuditBean customerAuditBean = new CustomerAuditBean();
            customerAuditBean.setOrgId(payToVendorBean.getOrgId());
            customerAuditBean.setCustomerid(payToVendorBean.getToCustomerID());
            customerAuditBean.setAccountid(payToVendorBean.getToAccountID());
            customerAuditBean.setAmount(payToVendorBean.getAmount());
            customerAuditBean.setType("" + CustomerAuditBean.TRANSACTION_TYPE_ENUM.Paid);
            customerAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
            customerAuditBean.setOtherPartyName(payToVendorBean.getFromPartyName()); //This will be opposite party
            customerAuditBean.setTransactionDate(payToVendorBean.getPaymentDate());
            accountDAO.updateCustomerAccountBalance(customerAuditBean);

            //Update Employee Balance and Audit
            //accountDAO.updateAccountBalance(payToVendorBean.getFromAccountID(), payToVendorBean.getAmount(), "SUBTRACT");

            EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
            employeeAuditBean.setOrgId(payToVendorBean.getOrgId());
            employeeAuditBean.setEmployeeid(payToVendorBean.getFromEmployeeID());
            employeeAuditBean.setAccountid(payToVendorBean.getFromAccountID());
            employeeAuditBean.setAmount(payToVendorBean.getAmount());
            // employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_PAY);
            employeeAuditBean.setType("" + EmployeeAuditBean.TRANSACTION_TYPE_ENUM.Supplier);
            employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
            //  employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_VENDORPAY);
            employeeAuditBean.setOtherPartyName(payToVendorBean.getToPartyName()); //This will be opposite party
            employeeAuditBean.setTransactionDate(payToVendorBean.getPaymentDate());
            accountDAO.updateEmployeeAccountBalance(employeeAuditBean);
        }
        return true;
    }

    /*
     *   Logic
     *   Payment Received 1. FromAccount (Customer who is paying)  is ADDED 2. ToAccount(Employee who received payment) is ADDED
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean createPaymentReceived(PaymentReceivedBean paymentReceivedBean) {
        paymentReceivedDAO.createPaymentReceived(paymentReceivedBean);
        if (paymentReceivedBean.isIncludeInCalc()) {
            CustomerAuditBean customerAuditBean = new CustomerAuditBean();
            customerAuditBean.setOrgId(paymentReceivedBean.getOrgId());
            customerAuditBean.setCustomerid(paymentReceivedBean.getFromCustomerID());
            customerAuditBean.setAccountid(paymentReceivedBean.getFromAccountID());
            customerAuditBean.setAmount(paymentReceivedBean.getAmount());
            customerAuditBean.setType("" + CustomerAuditBean.TRANSACTION_TYPE_ENUM.Received);
            customerAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            customerAuditBean.setOtherPartyName(paymentReceivedBean.getToPartyName()); //This will be opposite party
            customerAuditBean.setTransactionDate(paymentReceivedBean.getReceivedDate());
            accountDAO.updateCustomerAccountBalance(customerAuditBean);


            //accountDAO.updateAccountBalance(paymentReceivedBean.getToAccountID(), paymentReceivedBean.getAmount(), "ADD");
            EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
            employeeAuditBean.setOrgId(paymentReceivedBean.getOrgId());
            employeeAuditBean.setEmployeeid(paymentReceivedBean.getToEmployeeID());
            employeeAuditBean.setAccountid(paymentReceivedBean.getToAccountID());
            employeeAuditBean.setAmount(paymentReceivedBean.getAmount());
            //employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_RCVD);
            employeeAuditBean.setType("" + EmployeeAuditBean.TRANSACTION_TYPE_ENUM.Customer);
            employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            //employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_CUSTOMER_RCVD);
            employeeAuditBean.setOtherPartyName(paymentReceivedBean.getFromPartyName());
            employeeAuditBean.setTransactionDate(paymentReceivedBean.getReceivedDate());
            accountDAO.updateEmployeeAccountBalance(employeeAuditBean);
        }
        return true;
    }

    /*
     *   Logic
     *   Payment Received 1. FromAccount (Employee who is paying) is SUBTRACTED 2. ToAccount(Employee who received payment) is ADDED
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createInternalTransfer(InternalTransferBean internalTransferBean) {
        internalTransferDAO.createInternalTransfer(internalTransferBean);
        //Update From Employee (SUBTRACT)
        //accountDAO.updateAccountBalance(internalTransferBean.getFromAccountID(), internalTransferBean.getAmount(), "SUBTRACT");
        if (internalTransferBean.isIncludeInCalc()) {
            EmployeeAuditBean fromEmployeeAuditBean = new EmployeeAuditBean();
            fromEmployeeAuditBean.setOrgId(internalTransferBean.getOrgId());
            fromEmployeeAuditBean.setEmployeeid(internalTransferBean.getFromEmployeeID());
            fromEmployeeAuditBean.setAccountid(internalTransferBean.getFromAccountID());
            fromEmployeeAuditBean.setAmount(internalTransferBean.getAmount());
            fromEmployeeAuditBean.setType("" + EmployeeAuditBean.TRANSACTION_TYPE_ENUM.Staff_Transfer);
            fromEmployeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
            fromEmployeeAuditBean.setOtherPartyName(internalTransferBean.getToPartyName()); //This will be opposite party
            fromEmployeeAuditBean.setTransactionDate(internalTransferBean.getReceivedDate());
            accountDAO.updateEmployeeAccountBalance(fromEmployeeAuditBean);

            //Update TO Employee (ADD)
            EmployeeAuditBean toEmployeeAuditBean = new EmployeeAuditBean();
            toEmployeeAuditBean.setOrgId(internalTransferBean.getOrgId());
            toEmployeeAuditBean.setEmployeeid(internalTransferBean.getToEmployeeID());
            toEmployeeAuditBean.setAccountid(internalTransferBean.getToAccountID());
            toEmployeeAuditBean.setAmount(internalTransferBean.getAmount());
            toEmployeeAuditBean.setType("" + EmployeeAuditBean.TRANSACTION_TYPE_ENUM.Staff_Transfer);
            toEmployeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            toEmployeeAuditBean.setOtherPartyName(internalTransferBean.getFromPartyName());
            toEmployeeAuditBean.setTransactionDate(internalTransferBean.getReceivedDate());
            accountDAO.updateEmployeeAccountBalance(toEmployeeAuditBean);
        }
        return true;
    }

    /*
     * Retail Sale only updates the employee Balance
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createRetailSale(RetailSale retailSale) {
        retailSaleDAO.createRetailSale(retailSale);
        // accountDAO.updateAccountBalance(retailSale.getFromaccountid(), retailSale.getAmount(), "ADD");
        if (retailSale.isIncludeincalc()) {
            EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
            employeeAuditBean.setOrgId(retailSale.getOrgId());
            employeeAuditBean.setEmployeeid(retailSale.getFromemployeeid());
            employeeAuditBean.setAccountid(retailSale.getFromaccountid());
            employeeAuditBean.setAmount(retailSale.getAmount());
            //employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_RCVD);
            employeeAuditBean.setType("" + EmployeeAuditBean.TRANSACTION_TYPE_ENUM.Retail);
            employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            //employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_RETAILSALE);
            employeeAuditBean.setOtherPartyName("");
            employeeAuditBean.setTransactionDate(retailSale.getDate());
            accountDAO.updateEmployeeAccountBalance(employeeAuditBean);
        }
        return true;
    }

    /*
     * Create an Expense
     * Expense only updates the employee Balance
     */

    @Transactional(rollbackFor = Exception.class)
    public boolean createExpense(Expense expense) {
        expenseDAO.createExpense(expense);
        //From Employee Balance is only subtracted (Debited) when Paid Expense is created
        //accountDAO.updateAccountBalance(expense.getFromAccountID(), expense.getTotalAmount(), "SUBTRACT");
        if (expense.isPaid()) {
            //do it only when expense type is [paid]
            calculationTrackerDAO.updatePaidExpenseBalance(expense.getOrgId(), expense.getTotalAmount(), "ADD");
            //Update Employee Balance only when type is [Paid]
            EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
            employeeAuditBean.setOrgId(expense.getOrgId());
            employeeAuditBean.setEmployeeid(expense.getFromEmployeeID());
            employeeAuditBean.setAccountid(expense.getFromAccountID());
            employeeAuditBean.setAmount(expense.getTotalAmount());
            //employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_PAY);
            employeeAuditBean.setType("" + EmployeeAuditBean.TRANSACTION_TYPE_ENUM.Expense);
            employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
            //employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_EXPENSE);
            employeeAuditBean.setOtherPartyName(expense.getToPartyName());
            employeeAuditBean.setTransactionDate(expense.getDate());
            accountDAO.updateEmployeeAccountBalance(employeeAuditBean);
        } else {
            //do it only when expense type is unpaid
            calculationTrackerDAO.updateUnPaidExpenseBalance(expense.getOrgId(), expense.getTotalAmount(), "ADD");
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean makeExpenseUnpaidToPaid(Expense expense) {
        //Subtract from unpaid expense Balance
        calculationTrackerDAO.updateUnPaidExpenseBalance(expense.getOrgId(), expense.getTotalAmount(), "SUBTRACT");
        //Add into the Paid Expense Balance
        calculationTrackerDAO.updatePaidExpenseBalance(expense.getOrgId(), expense.getTotalAmount(), "ADD");
        //Subtract from Employee Balance
        // accountDAO.updateAccountBalance(expense.getFromAccountID(), expense.getTotalAmount(), "SUBTRACT");
        EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
        employeeAuditBean.setOrgId(expense.getOrgId());
        employeeAuditBean.setEmployeeid(expense.getFromEmployeeID());
        employeeAuditBean.setAccountid(expense.getFromAccountID());
        employeeAuditBean.setAmount(expense.getTotalAmount());
        // employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_PAY);
        employeeAuditBean.setType("" + EmployeeAuditBean.TRANSACTION_TYPE_ENUM.Expense);

        employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
        // employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_EXPENSE);
        employeeAuditBean.setOtherPartyName(expense.getToPartyName());
        employeeAuditBean.setTransactionDate(expense.getDate());
        accountDAO.updateEmployeeAccountBalance(employeeAuditBean);

        //Make status change in the Expense Table from Unpaid to Paid
        expenseDAO.updateExpenseStatus(expense.getExpenseId(), true);
        return true;
    }


    /*
     * Delete a invoice for a customer (Sale Entry)
     * Only Affects the customer balance
     * It will reverse the sale
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCustomerInvoice(long orgId, long invoiceId, String loggedInUserName) {

        List<CustomerInvoiceBean> invoiceBeans = customerInvoiceDAO.listSalesPaged(orgId, getSearchParamsListForDelete("cusinv.id", invoiceId), null, 1, 0);

        if (invoiceBeans == null || invoiceBeans.isEmpty()) {
            throw new CustomAppException("ID NOT VALID", "SERVER.DELETE_INVOICE.INVALID_ID", HttpStatus.EXPECTATION_FAILED);
        }

        CustomerInvoiceBean customerInvoiceBean = invoiceBeans.get(0);

        if (customerInvoiceBean.isIncludeInCalc()) {
            CustomerAuditBean customerAuditBean = new CustomerAuditBean();
            customerAuditBean.setOrgId(customerInvoiceBean.getOrgId());
            customerAuditBean.setCustomerid(customerInvoiceBean.getToCustomerId());
            customerAuditBean.setAccountid(customerInvoiceBean.getToAccountId());
            customerAuditBean.setAmount(customerInvoiceBean.getTotalAmount());
            customerAuditBean.setType("" + CustomerAuditBean.TRANSACTION_TYPE_ENUM.D_Sale);
            customerAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            customerAuditBean.setTransactionDate(customerInvoiceBean.getDate());
            customerAuditBean.setOtherPartyName("");
            accountDAO.updateCustomerAccountBalance(customerAuditBean);
        }

        commonDAO.softDeleteSingleRecord(ANPConstants.DB_TBL_customerinvoice, orgId, invoiceId);
        return true;
    }

    /*
     * Delete a Bill Received, Purchase Entry
     * Only affects the vendor/customer balance
     * It will reverse the Purchase
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteVendorPurchase(long orgId, long billId, String loggedInUserName) {

        List<PurchaseFromVendorBean> purchaseBeans = purchaseFromVendorDAO.listPurchasesPaged(orgId, getSearchParamsListForDelete("p.id", billId), null, 1, 0);
        if (purchaseBeans == null || purchaseBeans.isEmpty()) {
            throw new CustomAppException("ID NOT VALID", "SERVER.DELETE_PURCHASE.INVALID_ID", HttpStatus.EXPECTATION_FAILED);
        }
        PurchaseFromVendorBean purchaseFromVendorBean = purchaseBeans.get(0);
        if (purchaseFromVendorBean.isIncludeInCalc()) {
            CustomerAuditBean customerAuditBean = new CustomerAuditBean();
            customerAuditBean.setOrgId(purchaseFromVendorBean.getOrgId());
            customerAuditBean.setCustomerid(purchaseFromVendorBean.getFromCustomerId());
            customerAuditBean.setAccountid(purchaseFromVendorBean.getFromAccountId());
            customerAuditBean.setAmount(purchaseFromVendorBean.getTotalAmount());
            customerAuditBean.setType("" + CustomerAuditBean.TRANSACTION_TYPE_ENUM.D_Purchase);
            customerAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT); //opposite to purchase
            customerAuditBean.setTransactionDate(purchaseFromVendorBean.getDate());
            customerAuditBean.setOtherPartyName("");
            accountDAO.updateCustomerAccountBalance(customerAuditBean);
        }
        commonDAO.softDeleteSingleRecord(ANPConstants.DB_TBL_purchasefromvendor, orgId, billId);
        return true;
    }

    /*
     *   Logic
     *   Payment Received 1. FromAccount (Customer who is paying)  is ADDED 2. ToAccount(Employee who received payment) is ADDED
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePaymentReceived(long orgId, long paymentRcvdId, String loggedInUserName) {
        List<PaymentReceivedBean> paymentReceivedBeans = paymentReceivedDAO.listPaymentReceivedPaged(orgId, getSearchParamsListForDelete("prcvd.id", paymentRcvdId), null, 1, 0);
        if (paymentReceivedBeans == null || paymentReceivedBeans.isEmpty()) {
            throw new CustomAppException("ID NOT VALID", "SERVER.DELETE_PAYMENT_RECEIVED.INVALID_ID", HttpStatus.EXPECTATION_FAILED);
        }

        PaymentReceivedBean paymentReceivedBean = paymentReceivedBeans.get(0);

        if (paymentReceivedBean.isIncludeInCalc()) {
            CustomerAuditBean customerAuditBean = new CustomerAuditBean();
            customerAuditBean.setOrgId(paymentReceivedBean.getOrgId());
            customerAuditBean.setCustomerid(paymentReceivedBean.getFromCustomerID());
            customerAuditBean.setAccountid(paymentReceivedBean.getFromAccountID());
            customerAuditBean.setAmount(paymentReceivedBean.getAmount());
            customerAuditBean.setType("" + CustomerAuditBean.TRANSACTION_TYPE_ENUM.D_Received);
            customerAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
            customerAuditBean.setOtherPartyName(""); //This will be opposite party
            customerAuditBean.setTransactionDate(paymentReceivedBean.getReceivedDate());
            accountDAO.updateCustomerAccountBalance(customerAuditBean);

            EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
            employeeAuditBean.setOrgId(paymentReceivedBean.getOrgId());
            employeeAuditBean.setEmployeeid(paymentReceivedBean.getToEmployeeID());
            employeeAuditBean.setAccountid(paymentReceivedBean.getToAccountID());
            employeeAuditBean.setAmount(paymentReceivedBean.getAmount());
            // employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_DELETE_RCVD);
            employeeAuditBean.setType("" + EmployeeAuditBean.TRANSACTION_TYPE_ENUM.D_Customer);
            employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
            //   employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_CUSTOMER_RCVD);
            employeeAuditBean.setOtherPartyName("");
            employeeAuditBean.setTransactionDate(paymentReceivedBean.getReceivedDate());
            accountDAO.updateEmployeeAccountBalance(employeeAuditBean);
        }
        commonDAO.softDeleteSingleRecord(ANPConstants.DB_TBL_paymentreceived, orgId, paymentRcvdId);
        return true;
    }

    /*
     *   Logic:
     *   Payment To Vendor: 1. TOAccount (To Whom payment is made)  is SUBTRACTED 2. FromAccount(Employee who made payment) is SUBTRACTED
     *  Affect Customer + Employee both
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePayToVendor(long orgId, long paymentID, String loggedInUserName) {

        List<PayToVendorBean> payToVendorBeans = payToVendorDAO.listPayToVendorPaged(orgId, getSearchParamsListForDelete("paytov.id", paymentID), null, 1, 0);
        if (payToVendorBeans == null || payToVendorBeans.isEmpty()) {
            throw new CustomAppException("ID NOT VALID", "SERVER.DELETE_PAY_TO_VENDOR.INVALID_ID", HttpStatus.EXPECTATION_FAILED);
        }
        PayToVendorBean payToVendorBean = payToVendorBeans.get(0);

        //Update Customer Balance and Audit
        if (payToVendorBean.isIncludeInCalc()) {
            CustomerAuditBean customerAuditBean = new CustomerAuditBean();
            customerAuditBean.setOrgId(payToVendorBean.getOrgId());
            customerAuditBean.setCustomerid(payToVendorBean.getToCustomerID());
            customerAuditBean.setAccountid(payToVendorBean.getToAccountID());
            customerAuditBean.setAmount(payToVendorBean.getAmount());
            customerAuditBean.setType("" + CustomerAuditBean.TRANSACTION_TYPE_ENUM.D_Paid);
            customerAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            customerAuditBean.setOtherPartyName(""); //This will be opposite party
            customerAuditBean.setTransactionDate(payToVendorBean.getPaymentDate());
            accountDAO.updateCustomerAccountBalance(customerAuditBean);

            //Update Employee Balance and Audit
            EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
            employeeAuditBean.setOrgId(payToVendorBean.getOrgId());
            employeeAuditBean.setEmployeeid(payToVendorBean.getFromEmployeeID());
            employeeAuditBean.setAccountid(payToVendorBean.getFromAccountID());
            employeeAuditBean.setAmount(payToVendorBean.getAmount());
            //employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_DELETE_PAY);
            employeeAuditBean.setType("" + EmployeeAuditBean.TRANSACTION_TYPE_ENUM.D_Supplier);
            employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            // employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_VENDORPAY);
            employeeAuditBean.setOtherPartyName(""); //This will be opposite party
            employeeAuditBean.setTransactionDate(payToVendorBean.getPaymentDate());
            accountDAO.updateEmployeeAccountBalance(employeeAuditBean);
        }
        commonDAO.softDeleteSingleRecord(ANPConstants.DB_TBL_paytovendor, orgId, paymentID);
        return true;
    }

    /*
     *   Logic
     *   Payment Received 1. FromAccount (Employee who is paying) is SUBTRACTED 2. ToAccount(Employee who received payment) is ADDED
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteInternalTransfer(long orgId, long paymentID, String loggedInUserName) {

        List<InternalTransferBean> internalTransferBeans = internalTransferDAO.listInternalTransfer(orgId, getSearchParamsListForDelete("internal.id", paymentID), null, 1, 0);
        if (internalTransferBeans == null || internalTransferBeans.isEmpty()) {
            throw new CustomAppException("ID NOT VALID", "SERVER.DELETE_INTERNAL_TRANSFER.INVALID_ID", HttpStatus.EXPECTATION_FAILED);
        }

        InternalTransferBean internalTransferBean = internalTransferBeans.get(0);

        if (internalTransferBean.isIncludeInCalc()) {
            EmployeeAuditBean fromEmployeeAuditBean = new EmployeeAuditBean();
            fromEmployeeAuditBean.setOrgId(internalTransferBean.getOrgId());
            fromEmployeeAuditBean.setEmployeeid(internalTransferBean.getFromEmployeeID());
            fromEmployeeAuditBean.setAccountid(internalTransferBean.getFromAccountID());
            fromEmployeeAuditBean.setAmount(internalTransferBean.getAmount());
            // fromEmployeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_DELETE_PAY);
            fromEmployeeAuditBean.setType("" + EmployeeAuditBean.TRANSACTION_TYPE_ENUM.D_Staff_Transfer);
            fromEmployeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            //fromEmployeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_INTERNAL);
            fromEmployeeAuditBean.setOtherPartyName(internalTransferBean.getToPartyName()); //This will be opposite party
            fromEmployeeAuditBean.setTransactionDate(internalTransferBean.getReceivedDate());
            accountDAO.updateEmployeeAccountBalance(fromEmployeeAuditBean);

            //accountDAO.updateAccountBalance(internalTransferBean.getToAccountID(), internalTransferBean.getAmount(), "ADD");
            //Update TO Employee (ADD)
            EmployeeAuditBean toEmployeeAuditBean = new EmployeeAuditBean();
            toEmployeeAuditBean.setOrgId(internalTransferBean.getOrgId());
            toEmployeeAuditBean.setEmployeeid(internalTransferBean.getToEmployeeID());
            toEmployeeAuditBean.setAccountid(internalTransferBean.getToAccountID());
            toEmployeeAuditBean.setAmount(internalTransferBean.getAmount());
            //toEmployeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_DELETE_RCVD);
            toEmployeeAuditBean.setType("" + EmployeeAuditBean.TRANSACTION_TYPE_ENUM.D_Staff_Transfer);
            toEmployeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
            //toEmployeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_INTERNAL);
            toEmployeeAuditBean.setOtherPartyName(internalTransferBean.getFromPartyName());
            toEmployeeAuditBean.setTransactionDate(internalTransferBean.getReceivedDate());
            accountDAO.updateEmployeeAccountBalance(toEmployeeAuditBean);
        }
        commonDAO.softDeleteSingleRecord(ANPConstants.DB_TBL_internaltransfer, orgId, paymentID);
        return true;
    }

    /*
     * Retail Sale only updates the employee Balance
     * Delete
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRetailSale(long orgId, long paymentID, String loggedInUserName) {

        List<RetailSale> retailSales = retailSaleDAO.listRetailEntryPaged(orgId, getSearchParamsListForDelete("retail.id", paymentID), null, 1, 0);
        if (retailSales == null || retailSales.isEmpty()) {
            throw new CustomAppException("ID NOT VALID", "SERVER.DELETE_RETAIL_SALE.INVALID_ID", HttpStatus.EXPECTATION_FAILED);
        }

        RetailSale retailSale = retailSales.get(0);

        if (retailSale.isIncludeincalc()) {
            EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
            employeeAuditBean.setOrgId(retailSale.getOrgId());
            employeeAuditBean.setEmployeeid(retailSale.getFromemployeeid());
            employeeAuditBean.setAccountid(retailSale.getFromaccountid());
            employeeAuditBean.setAmount(retailSale.getAmount());
            //employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_DELETE_RCVD);
            employeeAuditBean.setType("" + EmployeeAuditBean.TRANSACTION_TYPE_ENUM.D_Retail);
            employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
            //employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_RETAILSALE);
            employeeAuditBean.setOtherPartyName("");
            employeeAuditBean.setTransactionDate(retailSale.getDate());
            accountDAO.updateEmployeeAccountBalance(employeeAuditBean);
        }
        commonDAO.softDeleteSingleRecord(ANPConstants.DB_TBL_retailsale, orgId, paymentID);
        return true;
    }

    /*
     * Create an Expense
     * Expense only updates the employee Balance
     */

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteExpense(long orgId, long paymentID, String loggedInUserName) {
        List<Expense> expenseList = expenseDAO.listExpensesPaged(orgId, getSearchParamsListForDelete("exp.id", paymentID), null, 1, 0);
        if (expenseList == null || expenseList.isEmpty()) {
            throw new CustomAppException("ID NOT VALID", "SERVER.DELETE_EXPENSE.INVALID_ID", HttpStatus.EXPECTATION_FAILED);
        }

        Expense expense = expenseList.get(0);

        if (expense.isPaid()) {
            calculationTrackerDAO.updatePaidExpenseBalance(expense.getOrgId(), expense.getTotalAmount(), "SUBTRACT");

            EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
            employeeAuditBean.setOrgId(expense.getOrgId());
            employeeAuditBean.setEmployeeid(expense.getFromEmployeeID());
            employeeAuditBean.setAccountid(expense.getFromAccountID());
            employeeAuditBean.setAmount(expense.getTotalAmount());
            //employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_DELETE_PAY);
            employeeAuditBean.setType("" + EmployeeAuditBean.TRANSACTION_TYPE_ENUM.valueOf("D_Expense"));

            employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            //employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_EXPENSE);
            employeeAuditBean.setOtherPartyName(expense.getToPartyName());
            employeeAuditBean.setTransactionDate(expense.getDate());
            accountDAO.updateEmployeeAccountBalance(employeeAuditBean);
        } else {
            calculationTrackerDAO.updateUnPaidExpenseBalance(expense.getOrgId(), expense.getTotalAmount(), "SUBTRACT");
        }

        commonDAO.softDeleteSingleRecord(ANPConstants.DB_TBL_generalexpense, orgId, paymentID);
        return true;
    }


    private List<SearchParam> getSearchParamsListForDelete(String fieldName, long value) {
        SearchParam searchParam = new SearchParam();
        searchParam.setFieldType("int");
        searchParam.setFieldName(fieldName);
        searchParam.setCondition("AND");
        searchParam.setSoperator("=");
        searchParam.setValue("" + value);
        List searchParams = new ArrayList<SearchParam>();
        searchParams.add(searchParam);
        return searchParams;
    }

    @Transactional
    public void updateExpense(Expense expense) {
        if (expense == null || expense.getOrgId() <= 0 || expense.getExpenseId() <= 0) {
            throw new CustomAppException("OrgId or ExpenseID Invalid", "SERVER.UPDATE_EXPENSE.INVALID_PARAM", HttpStatus.BAD_REQUEST);
        }
        //First get Expense and Compare
        Expense existingExpense = expenseDAO.getExpenseById(expense.getOrgId(), expense.getExpenseId());

        if (existingExpense == null || existingExpense.getExpenseId() <= 0) {
            throw new CustomAppException("Could not find existing expense to be updated", "SERVER.UPDATE_EXPENSE.INVALID_OLD_EXPENSE", HttpStatus.EXPECTATION_FAILED);
        }

        if (existingExpense.isPaid() != expense.isPaid()) { //change detected
            //Expense is made to "Paid"
            if (expense.isPaid()) {
                this.makeExpenseUnpaidToPaid(expense);
            }
        }
        expenseDAO.updateExpense(expense);
    } //end method

}
