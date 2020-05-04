package com.ANP.bean;


public class EmployeeSalary extends CommonAttribute {
    private String toEmployeeid;
    private double amount;
    private String details;


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

