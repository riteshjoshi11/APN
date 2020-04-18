package com.ANP.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class LoginBean {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String OrgID;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String LoggedInUserID; //=Employee: username
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String LoggedInUserType; //=Customer/Employee
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String loggedInUserAccountID;//Account:AccountID
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String LoggedInEmployeeID;//EMployee:EmployeeID
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String companyId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String loginUserName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String password;

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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrgID() {
        return OrgID;
    }

    public void setOrgID(String orgID) {
        OrgID = orgID;
    }


}
