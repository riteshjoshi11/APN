package com.ANP.bean;

import java.math.BigDecimal;
import java.util.Date;

public class RetailSale extends CommonAttribute {
    private long retailSaleId;
    private String notes;
    private BigDecimal amount;
    private String fromemployeeid;
    private int fromaccountid;
    private Date date;
    private boolean includeincalc = true;
    private EmployeeBean fromEmployee;

    public long getRetailSaleId() {
        return retailSaleId;
    }

    public void setRetailSaleId(long retailSaleId) {
        this.retailSaleId = retailSaleId;
    }

    public RetailSale()
    {
        fromEmployee = new EmployeeBean();
    }

    public EmployeeBean getFromEmployee() {
        return this.fromEmployee;
    }

    public void setFromEmployee(EmployeeBean fromEmployee) {
        this.fromEmployee = fromEmployee;
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

    public BigDecimal getAmount() { return amount; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }

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
