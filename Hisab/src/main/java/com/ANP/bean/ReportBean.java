package com.ANP.bean;

import javax.validation.constraints.Min;
import java.util.Date;


public class ReportBean {
    private long reportId;
    private Date generateDate;
    private Date generateBy;
    private String pdfFilePath;
    private String excelFilePath;
    private String reportStatus="Waiting"; //processing, generated, error

    @Min(value = 1, message = "OrgID is Mandatory")
    private long orgId;

    private String fromEmail;

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
