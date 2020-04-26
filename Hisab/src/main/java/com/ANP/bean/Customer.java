package com.ANP.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Customer extends  CommonAttribute{

        private String name;
        private String city;
        private String gstin;
        private String transporter;
        private String mobile1;
        private String mobile2;
        private String firmname;
        private String billingadress;
        private int orgid;

    public double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(double initialBalance) {
        this.initialBalance = initialBalance;
    }

    private String createdbyid;
        private java.util.Date createdate;
    private double initialBalance = 0;




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

    public int getOrgid() {
        return orgid;
    }

    public void setOrgid(int orgid) {
        this.orgid = orgid;
    }

    public String getCreatedbyid() {
        return createdbyid;
    }

    public void setCreatedbyid(String createdbyid) {
        this.createdbyid = createdbyid;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getName(){
        return name;
        }

public void setName(String name){
        this.name=name;
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