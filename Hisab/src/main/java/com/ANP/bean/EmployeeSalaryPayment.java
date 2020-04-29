package com.ANP.bean;

import java.util.Date;

public class EmployeeSalaryPayment extends CommonAttribute {
    private long fromAccountId;
    private java.util.Date transferDate;
    private double amount;
    private String details;
    private String toEmployeeId;
    private String fromEmployeeId;

    public long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
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

    public String getToEmployeeId() {
        return toEmployeeId;
    }

    public void setToEmployeeId(String toEmployeeId) {
        this.toEmployeeId = toEmployeeId;
    }

    public String getFromEmployeeId() {
        return fromEmployeeId;
    }

    public void setFromEmployeeId(String fromEmployeeId) {
        this.fromEmployeeId = fromEmployeeId;
    }
}
