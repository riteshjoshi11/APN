package com.ANP.repository;

import com.ANP.bean.*;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;


@Repository
public class ReportDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /*
     * This method will take fullFilePath as parameter return the pdf
     * orgId and loggedInEmployeeId is used in future
     */
    public ResponseEntity<InputStreamResource> fetchPdf(String filePath, long orgId, String loggedInEmployeeID) throws Exception {
        try {
            Path pdfPath = Paths.get(filePath);
            byte[] pdf = Files.readAllBytes(pdfPath);
            ByteArrayInputStream pdfToByte = new ByteArrayInputStream(pdf);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Content-Disposition", "inline; filename= " + pdfPath.getFileName().toString());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfToByte));
        } catch (NoSuchFileException e) {
            throw new CustomAppException("No PDF With that name", "SERVER.FETCH_PDF.DOESNOTEXIST", HttpStatus.EXPECTATION_FAILED);
        }
    }

    /*
     * This method will take fullFilePath as parameter return the excel
     * orgId and loggedInEmployeeId is used in future
     */

    public ResponseEntity<InputStreamResource> fetchExcel(String filePath, long orgid, String loggedInEmployeeID) throws Exception {
        try {
            Path excelPath = Paths.get(filePath);
            byte[] excel = Files.readAllBytes(excelPath);
            ByteArrayInputStream excelToByte = new ByteArrayInputStream(excel);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add("Content-Disposition", "inline; filename= " + excelPath.getFileName().toString());

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(excelToByte));
        } catch (NoSuchFileException e) {
            throw new CustomAppException("No Excel File With that name", "SERVER.FETCH_EXCEL.DOESNOTEXIST", HttpStatus.EXPECTATION_FAILED);
        }
    }

    public long createGSTReport(GSTReportBean reportBean) {
        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("insert into p_gst_reports(orgid," +
                "`mode`,reportstatus,formonth) values(:orgId,:mode,:reportStatus,:forMonth)", new BeanPropertySqlParameterSource(reportBean), holder);
        long generatedOrgKey = holder.getKey().longValue();
        System.out.println("createGSTReport: Generated Key=" + generatedOrgKey);
        return generatedOrgKey;
    }


    public List<GSTReportBean> listGSTReport(long orgID, Collection<SearchParam> searchParams,
                                             String orderBy, int noOfRecordsToShow, int startIndex) {

        if (startIndex == 0) {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);
        orderBy = "id desc";
        //Please note that the email is getting concat'd here from other column
        return namedParameterJdbcTemplate.query("select report.*, (select GROUP_CONCAT(email) from" +
                " p_gstrpt_send_email where p_gstrpt_send_email.p_gst_reports_id=report.id) emails from p_gst_reports report where orgid=:orgID " +
                ANPUtils.getWhereClause(searchParams) + " order by  " + orderBy + " limit  :noOfRecordsToShow"
                + " offset :startIndex", param, new ListGSTReportMapper());
    }


    private static final class ListGSTReportMapper implements RowMapper<GSTReportBean> {
        public GSTReportBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            GSTReportBean reportBean = new GSTReportBean();
            reportBean.setExcelFilePath(rs.getString("report.excelfilepath"));
            reportBean.setPdfFilePath(rs.getString("report.pdffilepath"));
            reportBean.setForMonth(rs.getString("report.formonth"));
            reportBean.setFromEmail(rs.getString("report.fromemail"));
            reportBean.setGenerateDate(rs.getTimestamp("report.generatedate"));
            reportBean.setMode(rs.getString("report.mode"));
            reportBean.setReportStatus(rs.getString("reportstatus"));
            reportBean.setOrgId(rs.getLong("report.orgid"));
            reportBean.setReportId(rs.getLong("report.id"));

            String emailsInCSVFormat = rs.getString("emails");
            List<String> emails = null;
            if(!ANPUtils.isNullOrEmpty(emailsInCSVFormat)) {
                String elements[] = emailsInCSVFormat.split(",");
                if(elements!=null && elements.length>0) {
                    emails = Arrays.asList(elements);
                }
            }
            reportBean.setToEmailList(emails);
            return reportBean;
        }
    }

    public void createEmailEntry(GSTReportBean reportBean) {
        String sql = "insert into p_gstrpt_send_email(email,p_gst_reports_id) values(?,?) ";
        namedParameterJdbcTemplate.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {

                String myPojo = (reportBean.getToEmailList()).get(i);
                ps.setString(1, myPojo);
                ps.setLong(2, reportBean.getReportId());
            }

            @Override
            public int getBatchSize() {
                return (reportBean.getToEmailList()).size();
            }
        });
    }

    /*
     * This method will perform the udpates for the file path
     * Mandatory orgId, reportID, Status
     *
     */
    public void updateGSTReport_status(GSTReportBean gstReportBean) {
        if (gstReportBean.getReportId() <= 0) {
            throw new CustomAppException("ReportID cannot be 0 or blank", "SERVER.updateGSTReport_status.INVALID_PARAM", HttpStatus.EXPECTATION_FAILED);
        }

        if (gstReportBean.getOrgId() <= 0) {
            throw new CustomAppException("OrgID cannot be 0 or blank", "SERVER.updateGSTReport_status.INVALID_PARAM", HttpStatus.EXPECTATION_FAILED);
        }

        if (ANPUtils.isNullOrEmpty(gstReportBean.getReportStatus())) {
            throw new CustomAppException("Report Status blank or empty", "SERVER.updateGSTReport_status.INVALID_PARAM", HttpStatus.EXPECTATION_FAILED);
        }

        namedParameterJdbcTemplate.update("update p_gst_reports set reportstatus = :reportStatus" +
                " where orgid = :orgId and id = :reportId", new BeanPropertySqlParameterSource(gstReportBean));
    }

    /*
    This method will perform the udpates for the file path
    * Mandatory orgId, reportID, either pdfFilePath or ExcelFilePath
    *
    */
    public void updateGSTReport_filepath(GSTReportBean gstReportBean) {
        if (gstReportBean.getReportId() <= 0) {
            throw new CustomAppException("ReportID cannot be 0 or blank", "SERVER.updateGSTReport_filepath.INVALID_PARAM", HttpStatus.EXPECTATION_FAILED);
        }

        if (gstReportBean.getOrgId() <= 0) {
            throw new CustomAppException("OrgID cannot be 0 or blank", "SERVER.updateGSTReport_filepath.INVALID_PARAM", HttpStatus.EXPECTATION_FAILED);
        }

        String updateQueryStr = "";

        if (ANPUtils.isNullOrEmpty(gstReportBean.getPdfFilePath()) && ANPUtils.isNullOrEmpty(gstReportBean.getExcelFilePath())) {
            throw new CustomAppException("Report File Path (Excel & PDF) blank or empty", "SERVER.updateGSTReport_filepath.INVALID_PARAM", HttpStatus.EXPECTATION_FAILED);
        } else if (!ANPUtils.isNullOrEmpty(gstReportBean.getPdfFilePath()) && !ANPUtils.isNullOrEmpty(gstReportBean.getExcelFilePath())) {
            updateQueryStr = " pdffilepath= :pdfFilePath , excelfilepath= :excelFilePath ";
        } else if (!ANPUtils.isNullOrEmpty(gstReportBean.getPdfFilePath())) {
            updateQueryStr = " pdffilepath= :pdfFilePath ";
        } else {
            updateQueryStr = " excelfilepath= :excelFilePath ";
        }
        namedParameterJdbcTemplate.update("update p_gst_reports set " + updateQueryStr +
                " where orgid = :orgId and id = :reportId", new BeanPropertySqlParameterSource(gstReportBean));
    }

    public List<String> getFrequentlyUsedEmail(long orgId, String loggedInEmployeeId) {
        return namedParameterJdbcTemplate.getJdbcTemplate().query("select email from (select email,count(email) " +
                " from p_gst_reports rpt, p_gstrpt_send_email email where rpt.orgid=" + orgId +
                " and rpt.id=email.p_gst_reports_id group by email order by count(email) desc ) t limit 2", new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString(1);
            }
        });
    }

}
