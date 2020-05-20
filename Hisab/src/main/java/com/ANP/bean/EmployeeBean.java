package com.ANP.bean;

public class EmployeeBean extends CommonAttribute {

    private String employeeId;
    private String first;
    private String last;
    private String mobile;
    private String loginusername;
    private String type;
    private double currentsalarybalance;
    private double lastsalarybalance;
    private float initialSalaryBalance;
    private float currentAccountBalance;
    private boolean loginrequired;
    private long accountId;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    /*
        This method will be used for displaying in the UI
         */
    public String getDisplayName() {
        return this.first + " " + this.last;
    }

    public float getCurrentAccountBalance() {
        return currentAccountBalance;
    }

    public void setCurrentAccountBalance(float currentAccountBalance) {
        this.currentAccountBalance = currentAccountBalance;
    }

    public float getInitialSalaryBalance() {
        return initialSalaryBalance;
    }

    public void setInitialSalaryBalance(float initialSalaryBalance) {
        this.initialSalaryBalance = initialSalaryBalance;
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

    public String getLoginusername(){
        return loginusername;
    }

    public void setLoginusername(String loginusername){
        this.loginusername=loginusername;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type=type;
    }

    public double getCurrentsalarybalance(){
        return currentsalarybalance;
    }

    public void setCurrentsalarybalance(double currentsalarybalance){
        this.currentsalarybalance=currentsalarybalance;
    }

    public double getLastsalarybalance(){
        return lastsalarybalance;
    }

    public void setLastsalarybalance(double lastsalarybalance){
        this.lastsalarybalance=lastsalarybalance;
    }

    public boolean getLoginrequired(){ return loginrequired; }

    public void setLoginrequired(boolean loginrequired){
        this.loginrequired = loginrequired;
    }


}
