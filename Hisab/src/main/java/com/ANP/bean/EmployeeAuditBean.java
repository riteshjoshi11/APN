package com.ANP.bean;

public class EmployeeAuditBean extends CommonAttribute {
    private String employeeid;

    private long accountid;
    private String type;
    private double amount;

    private String otherparty;
    private double newbalance;
    private double previousbalance;
    private String operation;
    private String otherPartyName;
    private String forWhat;
    private EmployeeBean employeeBean;


    public EmployeeAuditBean() {
        this.employeeBean = new EmployeeBean();
    }

    public EmployeeBean getEmployeeBean() {
        return employeeBean;
    }

    public void setEmployeeBean(EmployeeBean employeeBean) {
        this.employeeBean = employeeBean;
    }


    public String getForWhat() {
        return forWhat;
    }

    public void setForWhat(String forWhat) {
        this.forWhat = forWhat;
    }

    public String getOtherPartyName() {
        return otherPartyName;
    }

    //WE allow only 80 chars for otherParty
    public void setOtherPartyName(String pOtherPartyName) {
        if(pOtherPartyName!=null && pOtherPartyName.trim().length()>80) {
            this.otherPartyName=(pOtherPartyName.trim()).substring(0,80);
        } else {
            this.otherPartyName = pOtherPartyName;
        }
    }

    public String getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(String employeeid) {
        this.employeeid = employeeid;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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


