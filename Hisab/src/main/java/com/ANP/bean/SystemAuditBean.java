package com.ANP.bean;

public class SystemAuditBean {
    private long orgId;
    private long recordId;
    private String identifier;
    private boolean deleteAll;

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isDeleteAll() {
        return deleteAll;
    }

    public void setDeleteAll(boolean deleteAll) {
        this.deleteAll = deleteAll;
    }


}
