package com.ANP.bean;


public class EmployeeSalary extends CommonAttribute {
    private String toEmployeeID;
    private double amount;
    private String details;
    private long salaryID;
    private boolean includeInCalc;

    public String getToEmployeeID() {
        return toEmployeeID;
    }

    public void setToEmployeeID(String toEmployeeID) {
        this.toEmployeeID = toEmployeeID;
    }

    public boolean isIncludeInCalc() {
        return includeInCalc;
    }

    public void setIncludeInCalc(boolean includeInCalc) {
        this.includeInCalc = includeInCalc;
    }


    public long getSalaryID() {
        return salaryID;
    }

    public void setSalaryID(long salaryID) {
        this.salaryID = salaryID;
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

