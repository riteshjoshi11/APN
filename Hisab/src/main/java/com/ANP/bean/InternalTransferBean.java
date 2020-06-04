package com.ANP.bean;

import java.util.Date;

public class InternalTransferBean extends CommonAttribute {
    private long internalTransferID;
    private long fromAccountID;
    private long toAccountID;
    private java.util.Date receivedDate;
    private double amount;
    private String details;
    private String toEmployeeID;
    private String fromEmployeeID;
    private boolean includeInCalc;

    private EmployeeBean toEmployee;
    private EmployeeBean fromEmployee;

    //For Audit Entry
    private String fromPartyName;
    private String toPartyName;


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


    public InternalTransferBean()
    {
        this.toEmployee = new EmployeeBean();
        this.fromEmployee = new EmployeeBean();
    }
    public EmployeeBean getToEmployee() {
        return toEmployee;
    }

    public void setToEmployee(EmployeeBean toEmployee) {
        this.toEmployee = toEmployee;
    }

    public EmployeeBean getFromEmployee() {
        return fromEmployee;
    }

    public void setFromEmployee(EmployeeBean fromEmployee) {
        this.fromEmployee = fromEmployee;
    }

    public long getInternalTransferID() {
        return internalTransferID;
    }

    public void setInternalTransferID(long internalTransferID) {
        this.internalTransferID = internalTransferID;
    }

    public long getFromAccountID() {
        return fromAccountID;
    }

    public void setFromAccountID(long fromAccountID) {
        this.fromAccountID = fromAccountID;
    }

    public long getToAccountID() {
        return toAccountID;
    }

    public void setToAccountID(long toAccountID) {
        this.toAccountID = toAccountID;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getToEmployeeID() {
        return toEmployeeID;
    }

    public void setToEmployeeID(String toEmployeeID) {
        this.toEmployeeID = toEmployeeID;
    }

    public String getFromEmployeeID() {
        return fromEmployeeID;
    }

    public void setFromEmployeeID(String fromEmployeeID) {
        this.fromEmployeeID = fromEmployeeID;
    }

    public boolean isIncludeInCalc() {
        return includeInCalc;
    }

    public void setIncludeInCalc(boolean includeInCalc) {
        this.includeInCalc = includeInCalc;
    }
}
