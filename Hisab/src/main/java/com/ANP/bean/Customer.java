package com.ANP.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer{


    private long id;
    @JsonProperty("name")
        private String name;
private String city;
private java.util.Date dob;
private int orgId;
private String GSTIN;
private String transporter;

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

    public java.util.Date getDob(){
        return dob;
        }

public void setDob(java.util.Date dob){
        this.dob=dob;
        }

        public int getOrgId() {
                return orgId;
        }

        public void setOrgId(int orgId) {
                this.orgId = orgId;
        }

        public long getId() {
                return id;
        }

        public void setId(long id) {
                this.id = id;
        }

    public String getGSTIN() {
        return GSTIN;
    }

    public void setGSTIN(String GSTIN) {
        this.GSTIN = GSTIN;
    }

    public String getTransporter() {
        return transporter;
    }

    public void setTransporter(String transporter) {
        this.transporter = transporter;
    }
}