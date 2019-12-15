package com.ANP.bean;

public class Customer{
private String name;
private int city;
private java.util.Date dob;
private int orgId;
public String getName(){
        return name;
        }

public void setName(String name){
        this.name=name;
        }

public int getCity(){
        return city;
        }

public void setCity(int city){
        this.city=city;
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
}