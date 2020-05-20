package com.ANP.service;

import com.ANP.bean.*;
import com.ANP.repository.*;
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


    /*
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createCustomerInvoice(CustomerInvoiceBean customerInvoiceBean) {
      //  customerInvoiceDAO.createInvoice(customerInvoiceBean);
      //  accountDAO.updateAccountBalance(customerInvoiceBean.getToAccountId(), customerInvoiceBean.getTotalAmount(), "SUBTRACT");
        return true;
    }

    /*
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createVendorPurchase(PurchaseFromVendorBean purchaseFromVendorBean) {
      //  purchaseFromVendorDAO.createBill(purchaseFromVendorBean);
      //  accountDAO.updateAccountBalance(purchaseFromVendorBean.getFromAccountId(), purchaseFromVendorBean.getTotalAmount(), "ADD");
        return true;
    }

    /*
     *   Logic:
     *   Payment To Vendor: 1. TOAccount (To Whom payment is made)  is SUBTRACTED 2. FromAccount(Employee who made payment) is SUBTRACTED
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createPayToVendor(PayToVendorBean payToVendorBean) {
        payToVendorDAO.createPayToVendor(payToVendorBean);
        //accountDAO.updateAccountBalance(payToVendorBean.getToAccountID(), payToVendorBean.getAmount(), "SUBTRACT");
        //accountDAO.updateAccountBalance(payToVendorBean.getFromAccountID(), payToVendorBean.getAmount(), "SUBTRACT");
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
        //accountDAO.updateAccountBalance(paymentReceivedBean.getToAccountID(), paymentReceivedBean.getAmount(), "ADD");
        return true;
    }

    /*
     *   Logic
     *   Payment Received 1. FromAccount (Employee who is paying) is SUBTRACTED 2. ToAccount(Employee who received payment) is ADDED
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createInternalTransfer(InternalTransferBean internalTransferBean) {
        internalTransferDAO.createInternalTransfer(internalTransferBean);
        //accountDAO.updateAccountBalance(internalTransferBean.getToAccountID(), internalTransferBean.getAmount(), "ADD");
        //accountDAO.updateAccountBalance(internalTransferBean.getFromAccountID(), internalTransferBean.getAmount(), "SUBTRACTED");
        return true;
    }

}
