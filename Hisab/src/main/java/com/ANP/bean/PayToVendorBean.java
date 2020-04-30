package com.ANP.bean;

import java.util.Date;

public class PayToVendorBean extends CommonAttribute {

    private long payToVendorID;
    private long fromAccountID;
    private String fromEmployeeID;
    private long toAccountID;
    private String toCustomerID;
    private java.util.Date paymentDate;
    private double amount;
    private String details;

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
