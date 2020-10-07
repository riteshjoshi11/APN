package com.ANP.bean;

import org.springframework.lang.Nullable;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@AtLeastOneNotEmpty(fields = {"name", "firmname"})
public class CustomerBean extends CommonAttribute {
    private String name;
    private String city;
    @Pattern(regexp = "\\s*|.{15}", message = "GSTIN should be of 15 characters")
    private String gstin;
    private String transporter;
    @Pattern(regexp = "\\s*|.{10}", message = "Mobile no. should be of 10 digits")
    private String mobile1;
    @Pattern(regexp = "\\s*|.{10}", message = "Mobile2 no. should be of 10 digits")
    private String mobile2;
    private String firmname;
    private String billingadress;
    private String customerID;
    private BigDecimal initialBalance = new BigDecimal("0");
    private boolean sendPaymentReminders;
    private String state;
    //This is account attribute
    private BigDecimal accountBalance;
    private long accountId;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public boolean isSendPaymentReminders() {
        return sendPaymentReminders;
    }

    public void setSendPaymentReminders(boolean sendPaymentReminders) {
        this.sendPaymentReminders = sendPaymentReminders;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }


    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getFirmname() {
        return firmname;
    }

    public void setFirmname(String firmname) {
        this.firmname = firmname;
    }

    public String getBillingadress() {
        return billingadress;
    }

    public void setBillingadress(String billingadress) {
        this.billingadress = billingadress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTransporter() {
        return transporter;
    }

    public void setTransporter(String transporter) {
        this.transporter = transporter;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }
}

