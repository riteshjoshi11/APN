package com.ANP.bean;

import java.util.Date;

public class TransactionReportBean extends ReportBean {
    private String timePeriod;
    private String type;

    public enum PeriodOptionsEnum {
        FROM_LAST_BACKUP,
        BETWEEN_DATES
    }


    public enum ReportTypeEnum {
        SIMPLE,
        DETAILED
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }
}
