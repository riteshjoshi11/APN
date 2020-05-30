package com.ANP.bean;

public class CustomerBean extends CommonAttribute {
    private String name;
    private String city;
    private String gstin;
    private String transporter;
    private String mobile1;
    private String mobile2;
    private String firmname;
    private String billingadress;
    private String customerID;
    private double initialBalance = 0;
    private boolean sendPaymentReminders;
    private String state;
    //This is account attribute
    private double accountBalance;


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
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

    public double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(double initialBalance) {
        this.initialBalance = initialBalance;
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
}