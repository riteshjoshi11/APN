package com.ANP.repository;

public class Expense{
private java.util.Date date;
private String Category;
private String Description;
private double amount;
private String toPartyName;
private int orgId;
private int createdById;
private int FromAccountID;
private String ToAccountID;

public java.util.Date getDate(){
        return date;
        }

public void setDate(java.util.Date date){
        this.date=date;
        }

public String getCategory(){
        return Category;
        }

public void setCategory(String Category){
        this.Category=Category;
        }

public String getDescription(){
        return Description;
        }

public void setDescription(String Description){
        this.Description=Description;
        }

public double getAmount(){
        return amount;
        }

public void setAmount(double amount){
        this.amount=amount;
        }

public String getTopartyname(){
        return toPartyName;
        }

public void setTopartyname(String toPartyName){
        this.toPartyName=toPartyName;
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

public int getFromaccountid(){
        return FromAccountID;
        }

public void setFromaccountid(int FromAccountID){
        this.FromAccountID=FromAccountID;
        }

public String getToaccountid(){
        return ToAccountID;
        }

public void setToaccountid(String ToAccountID){
        this.ToAccountID=ToAccountID;
        }
}
