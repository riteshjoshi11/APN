package com.ANP.service;

import com.ANP.bean.CustomerInvoiceBean;
import com.ANP.bean.PurchaseFromVendorBean;
import com.ANP.repository.AccountDAO;
import com.ANP.repository.CustomerInvoiceDAO;
import com.ANP.repository.PurchaseFromVendorDAO;
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

    /*
     *   TODO: Joshi please complete this method
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createCustomerInvoice(CustomerInvoiceBean customerInvoiceBean) {
        customerInvoiceDAO.createInvoice(customerInvoiceBean);
        accountDAO.updateAccountBalance(customerInvoiceBean.getToAccountId(),customerInvoiceBean.getTotalAmount(), "SUBTRACT");
        return false;
    }

    /*
     *   TODO: Joshi please complete this method
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createVendorPurchase(PurchaseFromVendorBean purchaseFromVendorBean) {
        purchaseFromVendorDAO.createBill(purchaseFromVendorBean);
        accountDAO.updateAccountBalance(purchaseFromVendorBean.getFromAccountId(),purchaseFromVendorBean.getTotalAmount(),"ADD");
        return false;
    }
}
