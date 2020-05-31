package com.ANP.bean;

import java.util.Date;

public class PaymentReceivedBean extends CommonAttribute {

    private long paymentReceivedID;
    private long fromAccountID;
    private String fromCustomerID;

    private long toAccountID;
    private String toEmployeeID;

    private String paymentType;
    private java.util.Date receivedDate;
    private double amount;
    private boolean includeInCalc;
    private String details;

    private CustomerBean customerBean;
    private EmployeeBean employeeBean;

    public PaymentReceivedBean() {
        this.customerBean = new CustomerBean();
        this.employeeBean = new EmployeeBean();
    }

    public CustomerBean getCustomerBean() {
        return customerBean;
    }

    public void setCustomerBean(CustomerBean customerBean) {
        this.customerBean = customerBean;
    }

    public EmployeeBean getEmployeeBean() {
        return employeeBean;
    }

    public void setEmployeeBean(EmployeeBean employeeBean) {
        this.employeeBean = employeeBean;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isIncludeInCalc() {
        return includeInCalc;
    }

    public void setIncludeInCalc(boolean includeInCalc) {
        this.includeInCalc = includeInCalc;
    }

    public long getToAccountID() {
        return toAccountID;
    }

    public void setToAccountID(long toAccountID) {
        this.toAccountID = toAccountID;
    }

    public long getPaymentReceivedID() {
        return paymentReceivedID;
    }

    public void setPaymentReceivedID(long paymentReceivedID) {
        this.paymentReceivedID = paymentReceivedID;
    }

    public long getFromAccountID() {
        return fromAccountID;
    }

    public void setFromAccountID(long fromAccountID) {
        this.fromAccountID = fromAccountID;
    }

    public String getFromCustomerID() {
        return fromCustomerID;
    }

    public void setFromCustomerID(String fromCustomerID) {
        this.fromCustomerID = fromCustomerID;
    }

    public String getToEmployeeID() {
        return toEmployeeID;
    }

    public void setToEmployeeID(String toEmployeeID) {
        this.toEmployeeID = toEmployeeID;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
