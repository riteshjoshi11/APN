package com.ANP.bean;


import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class EmployeeBean extends CommonAttribute {


    private String employeeId;
    @NotBlank(message = "first name is mandatory")
    @NotNull(message = "first name is mandatory")
    private String first;
    private String last;
    @Pattern(regexp = "\\s*|.{10}", message = "mobile no. should be of 10 digits")
    private String mobile;
    private String mobile2;
    private String type;
    private int typeInt;
    private BigDecimal currentsalarybalance;
    private BigDecimal lastsalarybalance;
    private BigDecimal initialSalaryBalance;
    private BigDecimal currentAccountBalance;
    private BigDecimal initialBalance;
    private boolean loginrequired;
    private long accountId;

    public int getTypeInt() {
        return typeInt;
    }

    public void setTypeInt(int typeInt) {
        this.typeInt = typeInt;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public enum EmployeeTypeEnum {
        SUPER_ADMIN(1),
        BusinessPartner(2),
        SalesPerson(3),
        Labour(4),
        Accountant(5),
        VIRTUAL(6),
        Default(7);

        private final int value;

        private EmployeeTypeEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
   }

    /*
        This method will be used for displaying in the UI
         */
    public String getDisplayName() {
        return this.first + " " + this.last;
    }

    public void setCurrentsalarybalance(BigDecimal currentsalarybalance) {
        this.currentsalarybalance = currentsalarybalance;
    }

    public void setLastsalarybalance(BigDecimal lastsalarybalance) {
        this.lastsalarybalance = lastsalarybalance;
    }

    public BigDecimal getInitialSalaryBalance() {
        return initialSalaryBalance;
    }

    public void setInitialSalaryBalance(BigDecimal initialSalaryBalance) {
        this.initialSalaryBalance = initialSalaryBalance;
    }

    public BigDecimal getCurrentAccountBalance() {
        return currentAccountBalance;
    }

    public void setCurrentAccountBalance(BigDecimal currentAccountBalance) {
        this.currentAccountBalance = currentAccountBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public boolean isLoginrequired() {
        return loginrequired;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirst(){
        return first;
    }

    public void setFirst(String first){
        this.first=first;
    }

    public String getLast(){
        return last;
    }

    public void setLast(String last){
        this.last=last;
    }

    public String getMobile(){
        return mobile;
    }

    public void setMobile(String mobile){
        this.mobile=mobile;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type=type;
    }

    public BigDecimal getCurrentsalarybalance() { return currentsalarybalance; }

    public BigDecimal getLastsalarybalance() { return lastsalarybalance; }

    public BigDecimal getInitialBalance() { return initialBalance; }

    public boolean getLoginrequired(){ return loginrequired; }

    public void setLoginrequired(boolean loginrequired){
        this.loginrequired = loginrequired;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    @Override
    public String toString() {
        return "EmployeeBean{" +
                "employeeId='" + employeeId + '\'' +
                ", first='" + first + '\'' +
                ", last='" + last + '\'' +
                ", mobile='" + mobile + '\'' +
                ", mobile2='" + mobile2 + '\'' +
                ", type='" + type + '\'' +
                ", typeInt=" + typeInt +
                ", currentsalarybalance=" + currentsalarybalance +
                ", lastsalarybalance=" + lastsalarybalance +
                ", initialSalaryBalance=" + initialSalaryBalance +
                ", currentAccountBalance=" + currentAccountBalance +
                ", initialBalance=" + initialBalance +
                ", loginrequired=" + loginrequired +
                ", accountId=" + accountId +
                '}';
    }
}
