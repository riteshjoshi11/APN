package com.ANP.service;

import com.ANP.bean.*;
import com.ANP.repository.*;
import com.ANP.util.ANPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
    InternalTransferDAO internalTransferDAO ;

    @Autowired
    RetailSaleDAO retailSaleDAO;

    @Autowired
    ExpenseDAO expenseDAO;

    @Autowired
    CalculationTrackerDAO calculationTrackerDAO;

    /*
     * Create a invoice for a customer (Sale Entry)
     * Only Affects the customer balance
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createCustomerInvoice(CustomerInvoiceBean customerInvoiceBean) {
        customerInvoiceDAO.createInvoice(customerInvoiceBean);
        //accountDAO.updateAccountBalance(customerInvoiceBean.getToAccountId(), customerInvoiceBean.getTotalAmount(), "SUBTRACT");
        if(customerInvoiceBean.isIncludeInCalc()) {
            CustomerAuditBean customerAuditBean = new CustomerAuditBean();
            customerAuditBean.setOrgId(customerInvoiceBean.getOrgId());
            customerAuditBean.setCustomerid(customerInvoiceBean.getToCustomerId());
            customerAuditBean.setAccountid(customerInvoiceBean.getToAccountId());
            customerAuditBean.setAmount(customerInvoiceBean.getTotalAmount());
            customerAuditBean.setType(ANPConstants.CUSTOMER_AUDIT_TYPE_SALE);
            customerAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
            customerAuditBean.setTransactionDate(customerInvoiceBean.getDate());
            customerAuditBean.setOtherPartyName("-");
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
        if(purchaseFromVendorBean.isIncludeInCalc()) {
            CustomerAuditBean customerAuditBean = new CustomerAuditBean();
            customerAuditBean.setOrgId(purchaseFromVendorBean.getOrgId());
            customerAuditBean.setCustomerid(purchaseFromVendorBean.getFromCustomerId());
            customerAuditBean.setAccountid(purchaseFromVendorBean.getFromAccountId());
            customerAuditBean.setAmount(purchaseFromVendorBean.getTotalAmount());
            customerAuditBean.setType(ANPConstants.CUSTOMER_AUDIT_TYPE_PURCHASE);
            customerAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            customerAuditBean.setTransactionDate(purchaseFromVendorBean.getDate());
            customerAuditBean.setOtherPartyName("-");
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
        if(payToVendorBean.isIncludeInCalc()) {
            CustomerAuditBean customerAuditBean = new CustomerAuditBean();
            customerAuditBean.setOrgId(payToVendorBean.getOrgId());
            customerAuditBean.setCustomerid(payToVendorBean.getToCustomerID());
            customerAuditBean.setAccountid(payToVendorBean.getToAccountID());
            customerAuditBean.setAmount(payToVendorBean.getAmount());
            customerAuditBean.setType(ANPConstants.CUSTOMER_AUDIT_TYPE_PAYTOVENDOR);
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
            employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_PAY);
            employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
            employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_VENDORPAY);
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
    @Transactional(rollbackFor = Exception.class)
    public boolean createPaymentReceived(PaymentReceivedBean paymentReceivedBean) {
        paymentReceivedDAO.createPaymentReceived(paymentReceivedBean);
        //accountDAO.updateAccountBalance(paymentReceivedBean.getFromAccountID(), paymentReceivedBean.getAmount(), "ADD");
        if(paymentReceivedBean.isIncludeInCalc()) {
            CustomerAuditBean customerAuditBean = new CustomerAuditBean();
            customerAuditBean.setOrgId(paymentReceivedBean.getOrgId());
            customerAuditBean.setCustomerid(paymentReceivedBean.getFromCustomerID());
            customerAuditBean.setAccountid(paymentReceivedBean.getFromAccountID());
            customerAuditBean.setAmount(paymentReceivedBean.getAmount());
            customerAuditBean.setType(ANPConstants.CUSTOMER_AUDIT_TYPE_PAYMENTRCVDFROMVENDOR);
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
            employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_RCVD);
            employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_CUSTOMER_RCVD);
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
        if(internalTransferBean.isIncludeInCalc()) {
            EmployeeAuditBean fromEmployeeAuditBean = new EmployeeAuditBean();
            fromEmployeeAuditBean.setOrgId(internalTransferBean.getOrgId());
            fromEmployeeAuditBean.setEmployeeid(internalTransferBean.getFromEmployeeID());
            fromEmployeeAuditBean.setAccountid(internalTransferBean.getFromAccountID());
            fromEmployeeAuditBean.setAmount(internalTransferBean.getAmount());
            fromEmployeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_PAY);
            fromEmployeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
            fromEmployeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_INTERNAL);
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
            toEmployeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_RCVD);
            toEmployeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            toEmployeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_INTERNAL);
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
        if(retailSale.isIncludeincalc()) {
            EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
            employeeAuditBean.setOrgId(retailSale.getOrgId());
            employeeAuditBean.setEmployeeid(retailSale.getFromemployeeid());
            employeeAuditBean.setAccountid(retailSale.getFromaccountid());
            employeeAuditBean.setAmount(retailSale.getAmount());
            employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_RCVD);
            employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_RETAILSALE);
            employeeAuditBean.setOtherPartyName("-");
            employeeAuditBean.setTransactionDate(retailSale.getDate());
            accountDAO.updateEmployeeAccountBalance(employeeAuditBean);
        }
        return true ;
    }

    /*
     * Create an Expense
     * Expense only updates the employee Balance
     */

    @Transactional(rollbackFor = Exception.class)
    public boolean createExpense(Expense expense) {
        expenseDAO.createExpense(expense);
        if(expense.isPaid()) {
            calculationTrackerDAO.updatePaidExpenseBalance(expense.getOrgId(), expense.getTotalAmount(), "ADD");

            //From Employee Balance is only subtracted (Debited) when Paid Expense is created
            //accountDAO.updateAccountBalance(expense.getFromAccountID(), expense.getTotalAmount(), "SUBTRACT");
            if (expense.isIncludeInCalc()) {
                EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
                employeeAuditBean.setOrgId(expense.getOrgId());
                employeeAuditBean.setEmployeeid(expense.getFromEmployeeID());
                employeeAuditBean.setAccountid(expense.getFromAccountID());
                employeeAuditBean.setAmount(expense.getTotalAmount());
                employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_PAY);
                employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
                employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_EXPENSE);
                employeeAuditBean.setOtherPartyName(expense.getToPartyName());
                employeeAuditBean.setTransactionDate(expense.getDate());
                accountDAO.updateEmployeeAccountBalance(employeeAuditBean);
            } else {
                calculationTrackerDAO.updateUnPaidExpenseBalance(expense.getOrgId(), expense.getTotalAmount(), "ADD");
            }
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean makeExpenseUnpaidToPaid(Expense expense) {
        if(expense.isIncludeInCalc()) {
        //Subtract from unpaid expense Balance
        calculationTrackerDAO.updateUnPaidExpenseBalance(expense.getOrgId(),expense.getTotalAmount(),"SUBTRACT");
        //Add into the Paid Expense Balance
        calculationTrackerDAO.updatePaidExpenseBalance(expense.getOrgId(), expense.getTotalAmount(), "ADD");
        //Subtract from Employee Balance
        // accountDAO.updateAccountBalance(expense.getFromAccountID(), expense.getTotalAmount(), "SUBTRACT");
            EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
            employeeAuditBean.setOrgId(expense.getOrgId());
            employeeAuditBean.setEmployeeid(expense.getFromEmployeeID());
            employeeAuditBean.setAccountid(expense.getFromAccountID());
            employeeAuditBean.setAmount(expense.getTotalAmount());
            employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_PAY);
            employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
            employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_EXPENSE);
            employeeAuditBean.setOtherPartyName(expense.getToPartyName());
            employeeAuditBean.setTransactionDate(expense.getDate());
            accountDAO.updateEmployeeAccountBalance(employeeAuditBean);
        }
        //Make status change in the Expense Table from Unpaid to Paid
            expenseDAO.updateExpenseStatus(expense.getExpenseId(),true);
        return true;
    }
}
