package com.ANP.bean;

import java.util.Collection;
import java.util.Date;

/*
 * Main bean to be returned to UI
 */
public class PhonebookBean {
    protected Long orgId;
    protected String employeeId;
    protected Date lastSyncDate;
    protected String syncStatus;

    public enum SYNC_STATUS_ENUM {
        Syncing,
        Fully_Synced
    }

    /*
      This list is basically a processed contactList.
     */
    protected Collection<ProcessedContact> processedContactList;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Date getLastSyncDate() {
        return lastSyncDate;
    }

    public void setLastSyncDate(Date lastSyncDate) {
        this.lastSyncDate = lastSyncDate;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String lastSyncStatus) {
        this.syncStatus = lastSyncStatus;
    }

    public Collection<ProcessedContact> getProcessedContactList() {
        return processedContactList;
    }

    public void setProcessedContactList(Collection<ProcessedContact> processedContactList) {
        this.processedContactList = processedContactList;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
