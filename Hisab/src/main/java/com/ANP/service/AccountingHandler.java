package com.ANP.service;

import com.ANP.bean.CustomerInvoiceBean;
import com.ANP.bean.PurchaseFromVendorBean;
import com.ANP.repository.CustomerInvoiceDAO;
import com.ANP.repository.PurchaseFromVendorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountingHandler {

    @Autowired
    PurchaseFromVendorDAO purchaseFromVendorDAO;

    @Autowired
    CustomerInvoiceDAO customerInvoiceDAO;

    /*
     *   TODO: Joshi please complete this method
     */
    public boolean createCustomerInvoice(CustomerInvoiceBean customerInvoiceBean) {
        customerInvoiceDAO.createInvoice(customerInvoiceBean);
        return false;
    }

    /*
     *   TODO: Joshi please complete this method
     */
    public boolean createVendorPurchase(PurchaseFromVendorBean purchaseFromVendorBean) {
        purchaseFromVendorDAO.createBill(purchaseFromVendorBean);
        return false;
    }
}
