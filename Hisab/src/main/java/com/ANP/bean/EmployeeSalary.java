package com.ANP.bean;


import java.math.BigDecimal;

public class EmployeeSalary extends CommonAttribute {
    private String toEmployeeID;
    private BigDecimal amount;
    private String details;
    private long salaryID;
    private boolean includeInCalc;
    private EmployeeBean employeeBean;

    public EmployeeSalary() {
        this.employeeBean=new EmployeeBean();
    }

    public EmployeeBean getEmployeeBean() {
        return employeeBean;
    }

    public void setEmployeeBean(EmployeeBean employeeBean) {
        this.employeeBean = employeeBean;
    }

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

    public BigDecimal getAmount() { return amount; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }



}

