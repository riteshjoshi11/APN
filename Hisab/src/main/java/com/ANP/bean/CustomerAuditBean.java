package com.ANP.bean;

import java.util.Date;

public class CustomerAuditBean extends CommonAttribute {
    private long auditId;
    private String customerid;
    private long accountid;
    private String type;
    private double amount;
    private double newbalance;
    private double previousbalance;
    private String operation;
    private String otherPartyName;
    private CustomerBean customerBean;
    private Date transactionDate;

    public long getAuditId() {
        return auditId;
    }

    public void setAuditId(long auditId) {
        this.auditId = auditId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public CustomerAuditBean() {
        this.customerBean = new CustomerBean();
    }

    public CustomerBean getCustomerBean() {
        return customerBean;
    }

    public void setCustomerBean(CustomerBean customerBean) {
        this.customerBean = customerBean;
    }

    public String getOtherPartyName() {
        return this.otherPartyName;
    }

    //WE allow only 80 chars for otherParty
    public void setOtherPartyName(String pOtherPartyName) {
       if(pOtherPartyName!=null && pOtherPartyName.trim().length()>80) {
           this.otherPartyName=(pOtherPartyName.trim()).substring(0,80);
        } else {
           this.otherPartyName = pOtherPartyName;
       }
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public long getAccountid() {
        return accountid;
    }

    public void setAccountid(long accountid) {
        this.accountid = accountid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getNewbalance() {
        return newbalance;
    }

    public void setNewbalance(double newbalance) {
        this.newbalance = newbalance;
    }

    public double getPreviousbalance() {
        return previousbalance;
    }

    public void setPreviousbalance(double previousbalance) {
        this.previousbalance = previousbalance;
    }


}