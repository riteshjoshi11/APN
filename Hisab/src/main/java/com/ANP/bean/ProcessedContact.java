package com.ANP.bean;

import java.util.ArrayList;
import java.util.List;

/*
 * For each contactName - it will store multiple entries for PHONENO|EMAIL|WEBSITE
 */
public class ProcessedContact {
    protected String contactName;
    protected List<RawPhonebookContact> rawPhonebookContacts;

    public ProcessedContact() {
        this.rawPhonebookContacts = new ArrayList<>();
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public List<RawPhonebookContact> getRawPhonebookContacts() {
        return rawPhonebookContacts;
    }

    public void addARawPhonebookContact(RawPhonebookContact rawPhonebookContact) {
        this.rawPhonebookContacts.add(rawPhonebookContact);
    }

}
