package com.ANP.bean;

import java.math.BigDecimal;

public class AccountBean extends CommonAttribute {

    private String ownerid;
    private String accountnickname;
    private String type;
    private String details;
    private String createdbyid;
    private BigDecimal currentbalance;
    private BigDecimal lastbalance;
    private String createdate;
    private long accountId;
    private BigDecimal initialBalance;



    public long getAccountId(){return  accountId;}

    public void setAccountId(long accountId){this.accountId = accountId;}

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getAccountnickname() {
        return accountnickname;
    }

    public void setAccountnickname(String accountnickname) {
        this.accountnickname = accountnickname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    public String getCreatedbyid() {
        return createdbyid;
    }

    public void setCreatedbyid(String createdbyid) {
        this.createdbyid = createdbyid;
    }

    public BigDecimal getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(BigDecimal currentbalance) {
        this.currentbalance = currentbalance;
    }

    public BigDecimal getLastbalance() {
        return lastbalance;
    }

    public void setLastbalance(BigDecimal lastbalance) {
        this.lastbalance = lastbalance;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}
