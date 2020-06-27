package com.ANP.bean;

import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListParametersBean {
    private String orderBy;
    private int noOfRecordsToShow;
    private int startIndex;
    @Min(value=1, message = "orgid is mandatory")
    private long orgID;
    private List<SearchParam> searchParam;

    public ListParametersBean() {
        this.searchParam = new ArrayList<>();
    }
    public List<SearchParam> getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(List<SearchParam> searchParam) {
        this.searchParam = searchParam;
    }


    public long getOrgID() {
        return orgID;
    }

    public void setOrgID(long orgID) {
        this.orgID = orgID;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getNoOfRecordsToShow() {
        return noOfRecordsToShow;
    }

    public void setNoOfRecordsToShow(int noOfRecordsToShow) {
        this.noOfRecordsToShow = noOfRecordsToShow;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

}
