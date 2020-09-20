package com.ANP.bean;

import java.util.Date;

public class EmployeeSalaryPayment extends CommonAttribute {
    private long fromAccountId;
    private java.util.Date transferDate;
    private double amount;
    private String details;
    private String toEmployeeId;
    private String fromEmployeeId;
    private long salaryPaymentID;
    private String toEmployeeName;
    private String fromEmployeeName;
    private EmployeeBean toEmployeeBean;
    private EmployeeBean fromEmployeeBean;
    private boolean createSalaryDueAlso=false;

    public EmployeeSalaryPayment() {
        this.toEmployeeBean=new EmployeeBean();
        this.fromEmployeeBean=new EmployeeBean();
    }


    public String getFromEmployeeName() {
        return fromEmployeeName;
    }

    public void setFromEmployeeName(String fromEmployeeName) {
        this.fromEmployeeName = fromEmployeeName;
    }

    public String getToEmployeeName() {
        return toEmployeeName;
    }

    public void setToEmployeeName(String toEmployeeName) {
        this.toEmployeeName = toEmployeeName;
    }

    public EmployeeBean getToEmployeeBean() {
        return toEmployeeBean;
    }

    public void setToEmployeeBean(EmployeeBean toEmployeeBean) {
        this.toEmployeeBean = toEmployeeBean;
    }

    public EmployeeBean getFromEmployeeBean() {
        return fromEmployeeBean;
    }

    public void setFromEmployeeBean(EmployeeBean fromEmployeeBean) {
        this.fromEmployeeBean = fromEmployeeBean;
    }

    private boolean includeInCalc;

    public long getSalaryPaymentID() {
        return salaryPaymentID;
    }

    public void setSalaryPaymentID(long salaryPaymentID) {
        this.salaryPaymentID = salaryPaymentID;
    }

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

    public boolean isIncludeInCalc() {
        return includeInCalc;
    }

    public void setIncludeInCalc(boolean includeInCalc) {
        this.includeInCalc = includeInCalc;
    }

    public boolean isCreateSalaryDueAlso() {
        return createSalaryDueAlso;
    }

    public void setCreateSalaryDueAlso(boolean createSalaryDueAlso) {
        this.createSalaryDueAlso = createSalaryDueAlso;
    }
}
