package com.ANP.bean;

public class EmployeeAuditBean {
    private String employeeid;
    private long accountid;
    private String type;
    private double amount;
    private String forwhat;
    private String otherparty;
    private double newbalance;
    private double previousbalance;

    public String getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(String employeeid) {
        this.employeeid = employeeid;
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

    public String getForwhat() {
        return forwhat;
    }

    public void setForwhat(String forwhat) {
        this.forwhat = forwhat;
    }

    public String getOtherparty() {
        return otherparty;
    }

    public void setOtherparty(String otherparty) {
        this.otherparty = otherparty;
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


