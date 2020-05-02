package com.ANP.bean;

/*
This bean is mainly used for building/showing dashboard related items
 */
public class DashboardBean {

    private double cashWithYou=0.0;

    private double unpaidExpense=0.0;
    private double paidExpense=0.0;
    private double totalExpense=0.0;

    private double totalCashInHand=0.0;
    private double needToCollect=0.0;
    //auto calculated - no need to set
    private double totalAssets=0.0;

    private double totalSalaryDue=0.0;
    private double needToPay=0.0;
    //auto calculated - no need to set
    private double totalLiability=0.0;

    //auto calculated - no need to set
    private double businessHealth=0.0;

    public double getCashWithYou() {
        return cashWithYou;
    }

    public void setCashWithYou(double cashWithYou) {
        this.cashWithYou = cashWithYou;
    }

    public double getUnpaidExpense() {
        return unpaidExpense;
    }

    public void setUnpaidExpense(double unpaidExpense) {
        this.unpaidExpense = unpaidExpense;
    }

    public double getPaidExpense() {
        return paidExpense;
    }

    public void setPaidExpense(double paidExpense) {
        this.paidExpense = paidExpense;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public double getTotalCashInHand() {
        return totalCashInHand;
    }

    public void setTotalCashInHand(double totalCashInHand) {
        this.totalCashInHand = totalCashInHand;
    }

    public double getTotalAssets() {
        return (this.needToCollect + this.totalCashInHand);
     }

    public double getNeedToPay() {
        return needToPay;
    }

    public void setNeedToPay(double needToPay) {
        this.needToPay = needToPay;
    }

    public double getTotalLiability() {
        this.totalLiability = (this.totalExpense + this.totalSalaryDue + this.needToPay);
        return totalLiability ;
    }

    public double getBusinessHealth() {
        this.businessHealth =  (this.totalAssets-this.totalLiability);
        return businessHealth ;
    }

    public double getNeedToCollect() {
        return needToCollect;
    }

    public void setNeedToCollect(double needToCollect) {
        this.needToCollect = needToCollect;
    }

    public double getTotalSalaryDue() {
        return totalSalaryDue;
    }

    public void setTotalSalaryDue(double totalSalaryDue) {
        this.totalSalaryDue = totalSalaryDue;
    }
}
