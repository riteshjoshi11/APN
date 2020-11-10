package com.ANP.bean;

import com.ANP.util.ANPUtils;

import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;

public class Expense extends CommonAttribute {

    private long expenseId;
    private java.util.Date date;
    private String category;
    private String description;
    private BigDecimal totalAmount;
    private String toPartyName;
    private long fromAccountID;
    private String fromEmployeeID;
    private boolean includeInReport;
    private BigDecimal orderAmount;
    private BigDecimal CGST;
    private BigDecimal SGST;
    private BigDecimal IGST;
    private BigDecimal extra;
    @Pattern(regexp = "\\s*|.{15}", message = "GSTIN should be of 15 characters")
    private String toPartyGSTNO;
    @Pattern(regexp = "\\s*|.{10}", message = "To Party Mobile no. should be of 10 digits")
    private String toPartyMobileNO;
    private boolean paid;
    private String empFirstName;
    private String empLastName;

    public String getEmpFirstName() {
        return empFirstName;
    }

    public void setEmpFirstName(String empFirstName) {
        this.empFirstName = empFirstName;
    }

    public String getEmpLastName() {
        return empLastName;
    }

    public void setEmpLastName(String empLastName) {
        this.empLastName = empLastName;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getToPartyGSTNO() {
        return toPartyGSTNO;
    }

    public void setToPartyGSTNO(String toPartyGSTNO) {
        this.toPartyGSTNO = toPartyGSTNO;
    }

    public String getToPartyMobileNO() {
        return toPartyMobileNO;
    }

    public void setToPartyMobileNO(String toPartyMobileNO) {
        this.toPartyMobileNO = toPartyMobileNO;
    }

    public long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(long expenseId) {
        this.expenseId = expenseId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToPartyName() {
        return toPartyName;
    }

    public void setToPartyName(String toPartyName) {
        if (!ANPUtils.isNullOrEmpty(toPartyName))
            this.toPartyName = toPartyName.toUpperCase();
    }

    public long getFromAccountID() {
        return fromAccountID;
    }

    public void setFromAccountID(long fromAccountID) {
        this.fromAccountID = fromAccountID;
    }

    public String getFromEmployeeID() {
        return fromEmployeeID;
    }

    public void setFromEmployeeID(String fromEmployeeID) {
        this.fromEmployeeID = fromEmployeeID;
    }


    public boolean isIncludeInReport() {
        return includeInReport;
    }

    public void setIncludeInReport(boolean includeInReport) {
        this.includeInReport = includeInReport;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    @Override
    public String toString() {
        return "Expense{" +
                "expenseId=" + expenseId +
                ", date=" + date +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", totalAmount=" + totalAmount +
                ", toPartyName='" + toPartyName + '\'' +
                ", fromAccountID=" + fromAccountID +
                ", fromEmployeeID='" + fromEmployeeID + '\'' +
                ", includeInReport=" + includeInReport +
                ", orderAmount=" + orderAmount +
                ", CGST=" + CGST +
                ", SGST=" + SGST +
                ", IGST=" + IGST +
                ", extra=" + extra +
                ", toPartyGSTNO='" + toPartyGSTNO + '\'' +
                ", toPartyMobileNO='" + toPartyMobileNO + '\'' +
                ", paid=" + paid +
                ", empFirstName='" + empFirstName + '\'' +
                ", empLastName='" + empLastName + '\'' +
                '}';
    }
}
