package com.ANP.bean;

import java.util.Date;

public class RetailSale extends CommonAttribute {
    private String notes;
    private double amount;
    private String fromemployeeid;
    private int fromaccountid;
    private Date date;
    private boolean includeincalc;
    public EmployeeBean fromEmployeeBean;


    public RetailSale()
    {
        fromEmployeeBean = new EmployeeBean();
    }

    public EmployeeBean getFromEmployee() {
        return fromEmployeeBean;
    }

    public void setFromEmployee(EmployeeBean fromEmployee) {
        this.fromEmployeeBean = fromEmployee;
    }

    public boolean isIncludeincalc() {
        return includeincalc;
    }

    public void setIncludeincalc(boolean includeincalc) {
        this.includeincalc = includeincalc;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFromemployeeid() {
        return fromemployeeid;
    }

    public void setFromemployeeid(String fromemployeeid) {
        this.fromemployeeid = fromemployeeid;
    }

    public int getFromaccountid() {
        return fromaccountid;
    }

    public void setFromaccountid(int fromaccountid) {
        this.fromaccountid = fromaccountid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
