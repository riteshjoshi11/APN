package com.ANP.bean;

import java.util.Date;
import java.util.List;

public class PhonebookBean {
    protected long phonebookId;
    protected long orgId;
    protected String employeeId;
    protected Date lastSyncDate;
    protected String lastSyncStatus;
    protected List<PhonebookContacts> phonebookContacts;
}
