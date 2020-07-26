package com.ANP.bean;

import java.util.ArrayList;
import java.util.List;

public class GSTReportBean extends ReportBean {
    private String mode;
    private String forMonth;
    private List<String> toEmailList;

    public GSTReportBean() {
        this.toEmailList = new ArrayList<String>();
    }

    public List<String> getToEmailList() {
        return toEmailList;
    }

    public void setToEmailList(List<String> toEmailList) {
        this.toEmailList = toEmailList;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getForMonth() {
        return forMonth;
    }

    public void setForMonth(String forMonth) {
        this.forMonth = forMonth;
    }


}
