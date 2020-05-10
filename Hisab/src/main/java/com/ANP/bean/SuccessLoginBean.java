package com.ANP.bean;


public class SuccessLoginBean {
    private Organization organization;
    private AccountBean accountBean;
    private EmployeeBean employeeBean;

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public AccountBean getAccountBean() {
        return accountBean;
    }

    public void setAccountBean(AccountBean accountBean) {
        this.accountBean = accountBean;
    }

    public EmployeeBean getEmployeeBean() {
        return employeeBean;
    }

    public void setEmployeeBean(EmployeeBean employeeBean) {
        this.employeeBean = employeeBean;
    }

    String LoggedInUserID; //=Employee: username

    String LoggedInUserType; //=Customer/Employee

    String loggedInUserAccountID;//Account:AccountID

    String LoggedInEmployeeID;//EMployee:EmployeeID


/*

    public String getOrgName() {
        return orgName;
    }

    public String getLoggedInUserID() {
        return LoggedInUserID;
    }

    public void setLoggedInUserID(String loggedInUserID) {
        LoggedInUserID = loggedInUserID;
    }

    public String getLoggedInUserType() {
        return LoggedInUserType;
    }

    public void setLoggedInUserType(String loggedInUserType) {
        LoggedInUserType = loggedInUserType;
    }

    public String getLoggedInUserAccountID() {
        return loggedInUserAccountID;
    }

    public void setLoggedInUserAccountID(String loggedInUserAccountID) {
        this.loggedInUserAccountID = loggedInUserAccountID;
    }

    public String getLoggedInEmployeeID() {
        return LoggedInEmployeeID;
    }

    public void setLoggedInEmployeeID(String loggedInEmployeeID) {
        LoggedInEmployeeID = loggedInEmployeeID;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    public String getOrgID() {
        return OrgID;
    }

    public void setOrgID(String orgID) {
        OrgID = orgID;
    }

 */
}
