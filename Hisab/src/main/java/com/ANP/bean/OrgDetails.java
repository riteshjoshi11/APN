package com.ANP.bean;

import javax.validation.constraints.Min;

public class OrgDetails {
    @Min(value = 1, message = "orgDetailId is Mandatory")
    private long orgDetailId;
    private String mobile1;
    private String email;
    private String mobile2;
    private String gstNumber;
    private String panNumber;
    private String companyType;
    private String businessNature;
    private String numberOfEmployees;
    private String extraDetails;
    private String accountDetails;
    private String billingAddress;
    private String cAName;
    private String cAEmail;
    private String cAMobile;

    @Min(value = 1, message = "orgID is Mandatory")
    private long orgId;

    private boolean autoEmailGSTReport=false;

    private int companyTypeInt;
    private int businessNatureInt;
    private int numberOfEmployeesInt;
    private String website;

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public long getOrgDetailId() {
        return orgDetailId;
    }

    public void setOrgDetailId(long orgDetailId) {
        this.orgDetailId = orgDetailId;
    }

    public int getCompanyTypeInt() {
        return companyTypeInt;
    }

    public void setCompanyTypeInt(int companyTypeInt) {
        this.companyTypeInt = companyTypeInt;
    }

    public int getBusinessNatureInt() {
        return businessNatureInt;
    }

    public void setBusinessNatureInt(int businessNatureInt) {
        this.businessNatureInt = businessNatureInt;
    }

    public int getNumberOfEmployeesInt() {
        return numberOfEmployeesInt;
    }

    public void setNumberOfEmployeesInt(int numberOfEmployeesInt) {
        this.numberOfEmployeesInt = numberOfEmployeesInt;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getBusinessNature() {
        return businessNature;
    }

    public void setBusinessNature(String businessNature) {
        this.businessNature = businessNature;
    }

    public String getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(String numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public String getExtraDetails() {
        return extraDetails;
    }

    public void setExtraDetails(String extraDetails) {
        this.extraDetails = extraDetails;
    }

    public String getAccountDetails() {
        return accountDetails;
    }

    public void setAccountDetails(String accountDetails) {
        this.accountDetails = accountDetails;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getcAName() {
        return cAName;
    }

    public void setcAName(String cAName) {
        this.cAName = cAName;
    }

    public String getcAEmail() {
        return cAEmail;
    }

    public void setcAEmail(String cAEmail) {
        this.cAEmail = cAEmail;
    }

    public String getcAMobile() {
        return cAMobile;
    }

    public void setcAMobile(String cAMobile) {
        this.cAMobile = cAMobile;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public boolean isAutoEmailGSTReport() {
        return autoEmailGSTReport;
    }

    public void setAutoEmailGSTReport(boolean autoEmailGSTReport) {
        this.autoEmailGSTReport = autoEmailGSTReport;
    }

    @Override
    public String toString() {
        return "OrgDetails{" +
                "orgDetailId=" + orgDetailId +
                ", mobile1='" + mobile1 + '\'' +
                ", email='" + email + '\'' +
                ", mobile2='" + mobile2 + '\'' +
                ", gstNumber='" + gstNumber + '\'' +
                ", panNumber='" + panNumber + '\'' +
                ", companyType='" + companyType + '\'' +
                ", businessNature='" + businessNature + '\'' +
                ", numberOfEmployees='" + numberOfEmployees + '\'' +
                ", extraDetails='" + extraDetails + '\'' +
                ", accountDetails='" + accountDetails + '\'' +
                ", billingAddress='" + billingAddress + '\'' +
                ", cAName='" + cAName + '\'' +
                ", cAEmail='" + cAEmail + '\'' +
                ", cAMobile='" + cAMobile + '\'' +
                ", orgId=" + orgId +
                ", autoEmailGSTReport=" + autoEmailGSTReport +
                ", companyTypeInt=" + companyTypeInt +
                ", businessNatureInt=" + businessNatureInt +
                ", numberOfEmployeesInt=" + numberOfEmployeesInt +
                ", website='" + website + '\'' +
                '}';
    }
}
