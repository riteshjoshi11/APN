package com.ANP.bean;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerInvoiceBean extends CommonAttribute {
    private long invoiceID;
    private long toAccountId;
    private String toCustomerId;
    private java.util.Date date;
    private BigDecimal orderAmount;
    private BigDecimal CGST;
    private BigDecimal SGST;
    private BigDecimal IGST;
    private BigDecimal extra;
    private BigDecimal totalAmount;
    private String note;

    private String invoiceNo;

    private boolean includeInReport;
    private boolean includeInCalc = true;

    public CustomerBean getCustomerBean() {
        return customerBean;
    }

    public void setCustomerBean(CustomerBean customerBean) {
        this.customerBean = customerBean;
    }

    public CustomerInvoiceBean() {
        this.customerBean = new CustomerBean();
    }

    private CustomerBean customerBean;


    public long getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(long invoiceID) {
        this.invoiceID = invoiceID;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getToCustomerId() {
        return toCustomerId;
    }

    public void setToCustomerId(String toCustomerId) {
        this.toCustomerId = toCustomerId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getCGST() {
        return CGST;
    }

    public void setCGST(BigDecimal CGST) {
        this.CGST = CGST;
    }

    public BigDecimal getSGST() {
        return SGST;
    }

    public void setSGST(BigDecimal SGST) {
        this.SGST = SGST;
    }

    public BigDecimal getIGST() {
        return IGST;
    }

    public void setIGST(BigDecimal IGST) {
        this.IGST = IGST;
    }

    public BigDecimal getExtra() {
        return extra;
    }

    public void setExtra(BigDecimal extra) {
        this.extra = extra;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public boolean isIncludeInReport() {
        return includeInReport;
    }

    public void setIncludeInReport(boolean includeInReport) {
        this.includeInReport = includeInReport;
    }

    public boolean isIncludeInCalc() {
        return includeInCalc;
    }

    public void setIncludeInCalc(boolean includeInCalc) {
        this.includeInCalc = includeInCalc;
    }

    @Override
    public String toString() {
        return "CustomerInvoiceBean{" +
                "invoiceID=" + invoiceID +
                ", toAccountId=" + toAccountId +
                ", toCustomerId='" + toCustomerId + '\'' +
                ", date=" + date +
                ", orderAmount=" + orderAmount +
                ", CGST=" + CGST +
                ", SGST=" + SGST +
                ", IGST=" + IGST +
                ", extra=" + extra +
                ", totalAmount=" + totalAmount +
                ", note='" + note + '\'' +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", includeInReport=" + includeInReport +
                ", includeInCalc=" + includeInCalc +
                ", customerBean=" + customerBean +
                ", createdbyId='" + createdbyId + '\'' +
                ", orgId=" + orgId +
                '}';
    }
}
