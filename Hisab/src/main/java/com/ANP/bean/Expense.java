package com.ANP.bean;

import java.util.Date;

public class Expense extends CommonAttribute{
private int id;
private java.util.Date date;
private String Category;
private String Description;
private double amount;
private String toPartyName;
private int orgId;
private int createdById;
private int FromAccountID;
private String ToAccountID;
private boolean includeInCalc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIncludeInCalc() {
        return includeInCalc;
    }

    public void setIncludeInCalc(boolean includeInCalc) {
        this.includeInCalc = includeInCalc;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getToPartyName() {
        return toPartyName;
    }

    public void setToPartyName(String toPartyName) {
        this.toPartyName = toPartyName;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getCreatedById() {
        return createdById;
    }

    public void setCreatedById(int createdById) {
        this.createdById = createdById;
    }

    public int getFromAccountID() {
        return FromAccountID;
    }

    public void setFromAccountID(int fromAccountID) {
        FromAccountID = fromAccountID;
    }

    public String getToAccountID() {
        return ToAccountID;
    }

    public void setToAccountID(String toAccountID) {
        ToAccountID = toAccountID;
    }
}
