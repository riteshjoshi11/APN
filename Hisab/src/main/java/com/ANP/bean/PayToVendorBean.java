package com.ANP.bean;

import java.math.BigDecimal;
import java.util.Date;

public class PayToVendorBean extends CommonAttribute {

    private long payToVendorID;
    private long fromAccountID;
    private String fromEmployeeID;
    private long toAccountID;
    private String toCustomerID;
    private java.util.Date paymentDate;
    private BigDecimal amount;
    private String details;
    private boolean includeInCalc = true;
    private CustomerBean customerBean;
    private EmployeeBean employeeBean;
    //For Audit Entry
    private String fromPartyName;
    private String toPartyName;
    private boolean createPurchaseEntryAlso=false;


    public PayToVendorBean() {
        this.customerBean =  new CustomerBean();
        this.employeeBean = new EmployeeBean();
    }

    public String getFromPartyName() {
        return fromPartyName;
    }

    public void setFromPartyName(String fromPartyName) {
        this.fromPartyName = fromPartyName;
    }

    public String getToPartyName() {
        return toPartyName;
    }

    public void setToPartyName(String toPartyName) {
        this.toPartyName = toPartyName;
    }

    public EmployeeBean getEmployeeBean() {
        return employeeBean;
    }

    public void setEmployeeBean(EmployeeBean employeeBean) {
        this.employeeBean = employeeBean;
    }



    public CustomerBean getCustomerBean() {
        return customerBean;
    }

    public void setCustomerBean(CustomerBean customerBean) {
        this.customerBean = customerBean;
    }

    public boolean isIncludeInCalc() {
        return includeInCalc;
    }

    public void setIncludeInCalc(boolean includeInCalc) {
        this.includeInCalc = includeInCalc;
    }

    public long getPayToVendorID() {
        return payToVendorID;
    }

    public void setPayToVendorID(long payToVendorID) {
        this.payToVendorID = payToVendorID;
    }

    public long getFromAccountID() {
        return fromAccountID;
    }

    public void setFromAccountID(long fromAccountID) {
        this.fromAccountID = fromAccountID;
    }

    public String getFromEmployeeID() {
        return fromEmployeeID;
    }

    public void setFromEmployeeID(String fromEmployeeID) {
        this.fromEmployeeID = fromEmployeeID;
    }

    public long getToAccountID() {
        return toAccountID;
    }

    public void setToAccountID(long toAccountID) {
        this.toAccountID = toAccountID;
    }

    public String getToCustomerID() {
        return toCustomerID;
    }

    public void setToCustomerID(String toCustomerID) {
        this.toCustomerID = toCustomerID;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() { return amount; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isCreatePurchaseEntryAlso() {
        return createPurchaseEntryAlso;
    }

    public void setCreatePurchaseEntryAlso(boolean createPurchaseEntryAlso) { this.createPurchaseEntryAlso = createPurchaseEntryAlso; }
}
