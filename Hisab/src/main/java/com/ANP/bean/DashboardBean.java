package com.ANP.bean;

import java.math.BigDecimal;

/*
This bean is mainly used for building/showing dashboard related items
 */
public class DashboardBean {

    private BigDecimal cashWithYou = new BigDecimal("0.0");

    private BigDecimal unpaidExpense = new BigDecimal("0.0");
    private BigDecimal paidExpense = new BigDecimal("0.0");
    private BigDecimal totalExpense = new BigDecimal("0.0");

    private BigDecimal totalCashInHand = new BigDecimal("0.0");
    private BigDecimal needToCollect = new BigDecimal("0.0");
    //auto calculated - no need to set
    private BigDecimal totalAssets = new BigDecimal("0.0");


    private BigDecimal totalSalaryDue = new BigDecimal("0.0");
    private BigDecimal needToPay = new BigDecimal("0.0");

    //auto calculated - no need to set
    private BigDecimal totalLiability = new BigDecimal("0.0");

    //auto calculated - no need to set
    private BigDecimal businessHealth = new BigDecimal("0.0");

    public BigDecimal getCashWithYou() {
        return cashWithYou;
    }

    public void setCashWithYou(BigDecimal cashWithYou) {
        this.cashWithYou = cashWithYou;
    }

    public BigDecimal getUnpaidExpense() {
        return unpaidExpense;
    }

    public void setUnpaidExpense(BigDecimal unpaidExpense) {
        this.unpaidExpense = unpaidExpense;
    }

    public BigDecimal getPaidExpense() {
        return paidExpense;
    }

    public void setPaidExpense(BigDecimal paidExpense) {
        this.paidExpense = paidExpense;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getTotalCashInHand() {
        return totalCashInHand;
    }

    public void setTotalCashInHand(BigDecimal totalCashInHand) {
        this.totalCashInHand = totalCashInHand;
    }

    public BigDecimal getNeedToCollect() {
        return needToCollect.abs();
    }

    public BigDecimal getTotalAssets() {
        //need to collect is negative number and need to convert to positive
        return getNeedToCollect().add(getTotalCashInHand());
    }

    public BigDecimal getTotalLiability() {
        this.totalLiability = (getTotalExpense().add(getTotalSalaryDue())).add(getNeedToPay());
        return totalLiability ;
    }

    public BigDecimal getBusinessHealth() {
        this.businessHealth =  getTotalAssets().subtract(getTotalLiability()) ;
        return businessHealth ;
    }

    public BigDecimal getTotalSalaryDue() {
        return totalSalaryDue;
    }

    public void setTotalSalaryDue(BigDecimal totalSalaryDue) {
        this.totalSalaryDue = totalSalaryDue;
    }

    public BigDecimal getNeedToPay() {
        return needToPay;
    }

    public void setNeedToPay(BigDecimal needToPay) {
        this.needToPay = needToPay;
    }
    public void setNeedToCollect(BigDecimal needToCollect) {
        this.needToCollect = needToCollect;
    }

}
