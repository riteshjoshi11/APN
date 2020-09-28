package com.ANP.bean;

import javax.validation.constraints.Min;
import java.util.Collection;

public class SystemBean {

    @Min(value=1, message = "OrgID is mandatory")
    private long orgID;
    private Collection<UIItem> selectedDeleteOptions;

    public long getOrgID() {
        return orgID;
    }

    public void setOrgID(long orgID) {
        this.orgID = orgID;
    }

    public Collection<UIItem> getSelectedDeleteOptions() {
        return selectedDeleteOptions;
    }

    public void setSelectedDeleteOptions(Collection<UIItem> selectedDeleteOptions) {
        this.selectedDeleteOptions = selectedDeleteOptions;
    }

    @Override
    public String toString() {
        return "SystemBean{" +
                "orgID=" + orgID +
                ", selectedDeleteOptions=" + selectedDeleteOptions +
                '}';
    }
}
