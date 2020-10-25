package com.ANP.bean;

import java.util.HashMap;
import java.util.Map;

public class PermissionBean {
    private Map<String, Boolean> permissionMap = new HashMap<String, Boolean>();
    private Boolean canShowDeliveryPage=Boolean.FALSE;

    public PermissionBean() {
        this.permissionMap = new HashMap<String, Boolean>();
    }

    //Employee Account Balance on the List Employee Screen & Account Page
    public Boolean getCanShowEmployeePersonalBalance() {
        if(permissionMap.get("view.other.emp.personalbalance")!=null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //Employee Salary Balance on the List Employee Screen & Account Page
    public Boolean getCanShowOtherEmployeeSalaryBalance() {
        if(permissionMap.get("view.other.emp.salarybalance")!=null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //Can Show Company Details Part after CashWithYou on the DashBoard
    public Boolean getCanShowDashboard() {
        if(permissionMap.get("view.dashboard.company.details")!=null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //Employee Account Details Page - When a user clicks on a employee on the list employee screen
    public Boolean getCanShowOtherEmployeePersonalBalancePage() {
        if(permissionMap.get("view.emp.personalbalance.page")!=null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //Employee Salary Details Page - When a user clicks on a employee on the list employee screen
    public Boolean getCanShowOtherEmployeeSalaryBalancePage() {
        if(permissionMap.get("view.emp.salarybalance.page")!=null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //Control Delivery Menu
    public Boolean getCanShowDeliveryMenu() {
        return this.canShowDeliveryPage;
    }

    //Control Delivery Menu
    public void setCanShowDeliveryMenu(Boolean canShowDeliveryMenu) {
        this.canShowDeliveryPage=canShowDeliveryMenu;
    }


    //Control GST Report Menu
    public Boolean getCanShowGSTReportMenu() {
        if(permissionMap.get("view.gst.report.page")!=null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //Control Transaction Report Menu
    public Boolean getCanShowTransactionReportMenu() {
        if(permissionMap.get("view.txn.report.page")!=null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //Control Employee Create/Update Employee Save Button
    public Boolean getCanShowCreateUpdateEmployeeButton() {
        if(permissionMap.get("create.employee")!=null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //Control Employee Create/Update Customer Save Button
    public Boolean getCanShowCreateCustomer() {
        if(permissionMap.get("create.customer")!=null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //Control Create Employee FingerPrint (When feature is available)
    public Boolean getCanShowEmployeeCreateFingerprint() {
        if(permissionMap.get("create.employee.fingerprint")!=null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //Control Authenticate Employee FingerPrint (When feature is available)
    public Boolean getCanShowEmployeeAuthenticateFingerprint() {
        if(permissionMap.get("authenticate.employee.fingerprint")!=null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    //Control Account Selection available on all the create Transactional forms (PaidByAccount, ReceivedByAccount)
    public Boolean getCanShowOtherAccountSelection() {
        if(permissionMap.get("view.other.account.selection")!=null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean getCanShowDeleteAuditButton() {
        if(permissionMap.get("view.audit.delete.button")!=null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    public Boolean getCanShowDeleteAuditAllButton() {
        if(permissionMap.get("view.audit.deleteall.button")!=null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean getCanEditOrganizationDetails() {
        if(permissionMap.get("edit.organization.details")!=null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean getCanShowPaymentReminder() {
         return Boolean.FALSE;
    }
    public void setPermissionMap(Map<String, Boolean> permissionMap) {
        this.permissionMap = permissionMap;
    }
    public Boolean addEntryIntoMap(String key, Boolean value) {
        return permissionMap.put(key, value);
    }


}
