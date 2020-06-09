package com.ANP.bean;

import com.ANP.util.ANPUtils;

import javax.validation.constraints.Pattern;
import java.util.Date;

public class Expense extends CommonAttribute {

    private long expenseId;
    private java.util.Date date;
    private String category;
    private String description;
    private double totalAmount;
    private String toPartyName;
    private long fromAccountID;
    private String fromEmployeeID;
    private boolean includeInCalc;
    private boolean includeInReport;
    private double orderAmount;
    private double CGST;
    private double SGST;
    private double IGST;
    private double extra;
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

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getToPartyName() {
        return toPartyName;
    }

    public void setToPartyName(String toPartyName) {
        if(!ANPUtils.isNullOrEmpty(toPartyName))
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

    public boolean isIncludeInCalc() {
        return includeInCalc;
    }

    public void setIncludeInCalc(boolean includeInCalc) {
        this.includeInCalc = includeInCalc;
    }

    public boolean isIncludeInReport() {
        return includeInReport;
    }

    public void setIncludeInReport(boolean includeInReport) {
        this.includeInReport = includeInReport;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public double getCGST() {
        return CGST;
    }

    public void setCGST(double CGST) {
        this.CGST = CGST;
    }

    public double getSGST() {
        return SGST;
    }

    public void setSGST(double SGST) {
        this.SGST = SGST;
    }

    public double getIGST() {
        return IGST;
    }

    public void setIGST(double IGST) {
        this.IGST = IGST;
    }

    public double getExtra() {
        return extra;
    }

    public void setExtra(double extra) {
        this.extra = extra;
    }
}
