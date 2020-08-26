package com.ANP.bean;


public class SuccessLoginBean {
    private Organization organization;
    private AccountBean accountBean;
    private EmployeeBean employeeBean;
    private PermissionBean permissionBean;

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

    public PermissionBean getPermissionBean() {
        return permissionBean;
    }

    public void setPermissionBean(PermissionBean permissionBean) {
        this.permissionBean = permissionBean;
    }
}
