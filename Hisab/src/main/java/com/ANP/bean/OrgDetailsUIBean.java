package com.ANP.bean;

import java.util.ArrayList;
import java.util.List;

public class OrgDetailsUIBean {
    private List<UIItem> noOfEmployeeList ;
    private List<UIItem> businessNatureList ;
    private List<UIItem> companyTypeList ;

    public OrgDetailsUIBean() {
        this.noOfEmployeeList = new ArrayList<UIItem>();
        this.businessNatureList =new ArrayList<UIItem>();
        this.companyTypeList = new ArrayList<UIItem>();
    }

    public List<UIItem> getNoOfEmployeeList() {
        return noOfEmployeeList;
    }

    public void setNoOfEmployeeList(List<UIItem> noOfEmployeeList) {
        this.noOfEmployeeList = noOfEmployeeList;
    }

    public List<UIItem> getBusinessNatureList() {
        return businessNatureList;
    }

    public void setBusinessNatureList(List<UIItem> businessNatureList) {
        this.businessNatureList = businessNatureList;
    }

    public List<UIItem> getCompanyTypeList() {
        return companyTypeList;
    }

    public void setCompanyTypeList(List<UIItem> companyTypeList) {
        this.companyTypeList = companyTypeList;
    }
}
