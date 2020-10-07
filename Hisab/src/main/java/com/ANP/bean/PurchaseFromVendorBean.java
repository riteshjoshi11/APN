package com.ANP.bean;

import java.math.BigDecimal;
import java.util.Date;

public class PurchaseFromVendorBean extends CommonAttribute {

    private long purchaseID;
    private long fromAccountId;
    private String fromCustomerId;
    private java.util.Date date;
    private BigDecimal orderAmount;
    private BigDecimal CGST;
    private BigDecimal SGST;
    private BigDecimal IGST;
    private BigDecimal extra;
    private BigDecimal totalAmount;
    private String note;
    private boolean includeInReport;
    private String billNo;
    private boolean includeInCalc=true;

    private CustomerBean customerBean;

    public PurchaseFromVendorBean() {
        customerBean = new CustomerBean();
    }

    public CustomerBean getCustomerBean() {
        return customerBean;
    }

    public void setCustomerBean(CustomerBean customerBean) {
        this.customerBean = customerBean;
    }

    public long getPurchaseID() {
        return purchaseID;
    }

    public void setPurchaseID(long purchaseID) {
        this.purchaseID = purchaseID;
    }

    public String getFromCustomerId() {
        return fromCustomerId;
    }

    public void setFromCustomerId(String fromCustomerId) {
        this.fromCustomerId = fromCustomerId;
    }



    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public boolean isIncludeInCalc() {
        return includeInCalc;
    }

    public void setIncludeInCalc(boolean includeInCalc) {
        this.includeInCalc = includeInCalc;
    }

    public long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public BigDecimal getOrderAmount() { return orderAmount; }

    public void setOrderAmount(BigDecimal orderAmount) { this.orderAmount = orderAmount; }

    public BigDecimal getCGST() { return CGST; }

    public void setCGST(BigDecimal CGST) { this.CGST = CGST; }

    public BigDecimal getSGST() { return SGST; }

    public void setSGST(BigDecimal SGST) { this.SGST = SGST; }

    public BigDecimal getIGST() { return IGST; }

    public void setIGST(BigDecimal IGST) { this.IGST = IGST; }

    public BigDecimal getExtra() { return extra; }

    public void setExtra(BigDecimal extra) { this.extra = extra; }

    public BigDecimal getTotalAmount() { return totalAmount; }

    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isIncludeInReport() {
        return includeInReport;
    }

    public void setIncludeInReport(boolean includeInReport) {
        this.includeInReport = includeInReport;
    }


}