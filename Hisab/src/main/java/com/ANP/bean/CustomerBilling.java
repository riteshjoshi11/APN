package com.ANP.bean;

public class CustomerBilling{
private java.util.Date date;
private double amount;
private double CGST;
private double SGST;
private double IGST;
private double extra;
private double total;
private int orgId;
private int createdById;

public java.util.Date getDate(){
        return date;
        }

public void setDate(java.util.Date date){
        this.date=date;
        }

public double getAmount(){
        return amount;
        }

public void setAmount(double amount){
        this.amount=amount;
        }

public double getCgst(){
        return CGST;
        }

public void setCgst(double CGST){
        this.CGST=CGST;
        }

public double getSgst(){
        return SGST;
        }

public void setSgst(double SGST){
        this.SGST=SGST;
        }

public double getIgst(){
        return IGST;
        }

public void setIgst(double IGST){
        this.IGST=IGST;
        }

public double getExtra(){
        return extra;
        }

public void setExtra(double extra){
        this.extra=extra;
        }

public double getTotal(){
        return total;
        }

public void setTotal(double total){
        this.total=total;
        }

public int getOrgid(){
        return orgId;
        }

public void setOrgid(int orgId){
        this.orgId=orgId;
        }

public int getCreatedbyid(){
        return createdById;
        }

public void setCreatedbyid(int createdById){
        this.createdById=createdById;
        }
        }