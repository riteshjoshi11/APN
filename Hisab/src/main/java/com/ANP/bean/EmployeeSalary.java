package com.ANP.bean;


public class EmployeeSalary extends CommonAttribute {
    private String toEmployeeid;
    private double amount;
    private String details;
    private long salaryID;
    private boolean includeInCalc;

    public boolean isIncludeInCalc() {
        return includeInCalc;
    }

    public void setIncludeInCalc(boolean includeInCalc) {
        this.includeInCalc = includeInCalc;
    }

    public String getToEmployeeid() {
        return toEmployeeid;
    }

    public void setToEmployeeid(String toEmployeeid) {
        this.toEmployeeid = toEmployeeid;
    }

    public long getSalaryID() {
        return salaryID;
    }

    public void setSalaryID(long salaryID) {
        this.salaryID = salaryID;
    }

    public String getToEmployeeId() {
        return toEmployeeid;
    }

    public void setToEmployeeId(String toemployeeid) {
        this.toEmployeeid = toemployeeid;
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

    public void setOrgid(int orgid) {
        super.orgId = orgid;
    }

}

