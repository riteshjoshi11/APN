package com.ANP.bean;

import java.math.BigDecimal;

public class CalculationTrackerBean extends CommonAttribute {

    private BigDecimal unPaidExpense;
    private BigDecimal paidExpense;
    private BigDecimal totalExpense ;

    public BigDecimal getUnPaidExpense() {
        return unPaidExpense;
    }

    public void setUnPaidExpense(BigDecimal unPaidExpense) {
        this.unPaidExpense = unPaidExpense;
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
}
