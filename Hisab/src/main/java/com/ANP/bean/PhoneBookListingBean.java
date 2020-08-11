package com.ANP.bean;

import java.util.List;

public class PhoneBookListingBean {
    private long orgId;
    private String employeeId;
    private int noOfRecordsToShow;
    private int startIndex;
    private List<RawPhonebookContact> rawPhonebookContacts;

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

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public List<RawPhonebookContact> getRawPhonebookContacts() {
        return rawPhonebookContacts;
    }

    public void setRawPhonebookContacts(List<RawPhonebookContact> rawPhonebookContacts) {
        this.rawPhonebookContacts = rawPhonebookContacts;
    }
}
