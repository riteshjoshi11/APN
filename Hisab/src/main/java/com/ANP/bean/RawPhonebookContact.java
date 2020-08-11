package com.ANP.bean;

public class RawPhonebookContact {
    protected String contactName;
    protected String key="";
    protected String value="";
    private boolean isDeleted;

    public enum PHONEBOOK_CONTACT_TYPE {
        WEBSITE,
        PHONE,
        EMAIL
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return ((contactName.hashCode()) * (key.hashCode()) * (value.hashCode()));
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null) return false;
        if(! (obj instanceof RawPhonebookContact)) return false;

        RawPhonebookContact contact = (RawPhonebookContact) obj;
        if(this.contactName.equalsIgnoreCase(contact.getContactName()) &&
                this.key.equalsIgnoreCase(contact.getKey()) &&
                this.value.equalsIgnoreCase(contact.getValue()) ) {
            return true;
        }
        return false;
    }
}
