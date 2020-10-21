package com.ANP.bean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

public class PhoneBookListingBean {
    @Min(value = 1, message = "OrgID is Mandatory")
    private long orgId;
    @NotBlank(message = "employeeId name is mandatory")
    private String employeeId;
    private String mobileNumber;

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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public List<RawPhonebookContact> getRawPhonebookContacts() {
        return rawPhonebookContacts;
    }

    public void setRawPhonebookContacts(List<RawPhonebookContact> rawPhonebookContacts) {
        this.rawPhonebookContacts = rawPhonebookContacts;
    }
}
