package com.ANP.bean;

import java.util.List;

public class PhoneBookListingBean {
    private long orgId;
    private String employeeId;
    private List<RawPhonebookContact> rawPhonebookContacts;

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
