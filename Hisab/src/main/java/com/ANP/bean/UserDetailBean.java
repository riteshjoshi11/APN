package com.ANP.bean;

public class UserDetailBean {
    //accountbean
    private String ownerid;
    private String accountnickname;
    private String type;
    private String details;
    private String createdbyid;
    private double currentbalance;
    private double lastbalance;
    private long accountId;

    //Employeebean
    private String employeeId;
    private String first;
    private String last;
    private String mobile;
    private String loginusername;
    private double currentsalarybalance;
    private double lastsalarybalance;
    private boolean loginrequired;

    //OrganizationBean
    private long orgId;
    private String orgName;
    private String city;
    private String state;
    private String clientId;

    private String mobile2;

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

    public double getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(double currentbalance) {
        this.currentbalance = currentbalance;
    }

    public double getLastbalance() {
        return lastbalance;
    }

    public void setLastbalance(double lastbalance) {
        this.lastbalance = lastbalance;
    }

    //employee

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

    //Organization
    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public boolean getLoginrequired(){ return loginrequired; }

    public void setLoginrequired(boolean loginrequired){
        this.loginrequired = loginrequired;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type=type;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
