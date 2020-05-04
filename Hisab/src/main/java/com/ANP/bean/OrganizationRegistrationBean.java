package com.ANP.bean;

/*
As the organization registration creates default first employee so creating this bean.
 */
public class OrganizationRegistrationBean {
    Organization orgBean ;
    EmployeeBean employeeBean;

    public OrganizationRegistrationBean() {
        orgBean = new Organization();
        employeeBean = new EmployeeBean();
    }

    public Organization getOrgBean() {
        return orgBean;
    }

    public void setOrgBean(Organization orgBean) {
        this.orgBean = orgBean;
    }

    public EmployeeBean getEmployeeBean() {
        return employeeBean;
    }

    public void setEmployeeBean(EmployeeBean employeeBean) {
        this.employeeBean = employeeBean;
    }
}
