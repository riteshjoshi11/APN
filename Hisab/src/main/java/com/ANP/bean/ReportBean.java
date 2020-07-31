package com.ANP.bean;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ReportBean {
    private long reportId;
    private Date generateDate;
    private Date generateBy;
    private String pdfFilePath;
    private String excelFilePath;
    private String reportStatus= reportStatusEnum.WAITING.toString();
    private String reportFormat;

    @Min(value = 1, message = "OrgID is Mandatory")
    private long orgId;
    private String fromEmail;
    private List<String> toEmailList;
    private Date fromDate;
    private Date toDate;

    public enum reportStatusEnum {
        WAITING,
        PROCESSING,
        GENERATED,
        ERROR
    }


    public ReportBean() {
        this.toEmailList = new ArrayList<String>();
    }



    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getReportFormat() {
        return reportFormat;
    }

    public void setReportFormat(String reportFormat) {
        this.reportFormat = reportFormat;
    }

    public List<String> getToEmailList() {
        return toEmailList;
    }

    public void setToEmailList(List<String> toEmailList) {
        this.toEmailList = toEmailList;
    }

    public Date getGenerateBy() {
        return generateBy;
    }

    public void setGenerateBy(Date generateBy) {
        this.generateBy = generateBy;
    }

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public Date getGenerateDate() {
        return generateDate;
    }

    public void setGenerateDate(Date generateDate) {
        this.generateDate = generateDate;
    }

    public String getPdfFilePath() {
        return pdfFilePath;
    }

    public void setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }

    public String getExcelFilePath() {
        return excelFilePath;
    }

    public void setExcelFilePath(String excelFilePath) {
        this.excelFilePath = excelFilePath;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }
}
