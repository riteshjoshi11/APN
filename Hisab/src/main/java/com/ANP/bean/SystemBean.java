package com.ANP.bean;

import javax.validation.constraints.Min;

public class SystemBean {

    @Min(value=1, message = "orgid is mandatory")
    private long orgID;
    private boolean deleteCompanyData;
    private boolean deleteSalaryData;
    private boolean deleteAuditData;
    private boolean deleteEmployeeSalaryBalance;
    private boolean deleteEmployeeCompanyBalance;
    private boolean deleteCustomerBalance;

    public boolean isDeleteEmployeeSalaryBalance() {
        return deleteEmployeeSalaryBalance;
    }

    public void setDeleteEmployeeSalaryBalance(boolean deleteEmployeeSalaryBalance) {
        this.deleteEmployeeSalaryBalance = deleteEmployeeSalaryBalance;
    }

    public boolean isDeleteEmployeeCompanyBalance() {
        return deleteEmployeeCompanyBalance;
    }

    public void setDeleteEmployeeCompanyBalance(boolean deleteEmployeeCompanyBalance) {
        this.deleteEmployeeCompanyBalance = deleteEmployeeCompanyBalance;
    }

    public boolean isDeleteCustomerBalance() {
        return deleteCustomerBalance;
    }

    public void setDeleteCustomerBalance(boolean deleteCustomerBalance) {
        this.deleteCustomerBalance = deleteCustomerBalance;
    }

    public long getOrgID() {
        return orgID;
    }

    public void setOrgID(long orgID) {
        this.orgID = orgID;
    }

    public boolean isDeleteCompanyData() {
        return deleteCompanyData;
    }

    public void setDeleteCompanyData(boolean deleteCompanyData) {
        this.deleteCompanyData = deleteCompanyData;
    }

    public boolean isDeleteSalaryData() {
        return deleteSalaryData;
    }

    public void setDeleteSalaryData(boolean deleteSalaryData) {
        this.deleteSalaryData = deleteSalaryData;
    }

    public boolean isDeleteAuditData() {
        return deleteAuditData;
    }

    public void setDeleteAuditData(boolean deleteAuditData) {
        this.deleteAuditData = deleteAuditData;
    }


}
