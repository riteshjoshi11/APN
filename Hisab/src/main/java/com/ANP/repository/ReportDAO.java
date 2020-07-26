package com.ANP.repository;

import com.ANP.bean.CustomerBean;
import com.ANP.bean.GSTReportBean;
import com.ANP.bean.ReportBean;
import com.ANP.bean.SearchParam;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


@Repository
public class ReportDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /*
     * This method will take fullFilePath as parameter return the pdf
     * orgId and loggedInEmployeeId is used in future
     */
    public ResponseEntity<InputStreamResource> fetchPdf (String filePath, long orgId, String loggedInEmployeeID) throws Exception {
        try {
            Path pdfPath = Paths.get(filePath);
            byte[] pdf = Files.readAllBytes(pdfPath);
            ByteArrayInputStream pdfToByte = new ByteArrayInputStream(pdf);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Content-Disposition", "inline; filename= "+ pdfPath.getFileName().toString());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfToByte));
        }
        catch (NoSuchFileException e) {
            throw new CustomAppException("No PDF With that name","SERVER.FETCH_PDF.DOESNOTEXIST", HttpStatus.EXPECTATION_FAILED);
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
            headers.add("Content-Disposition", "inline; filename= "+ excelPath.getFileName().toString());

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(excelToByte));
            }
                catch (NoSuchFileException e) {
                throw new CustomAppException("No Excel File With that name","SERVER.FETCH_EXCEL.DOESNOTEXIST", HttpStatus.EXPECTATION_FAILED);
            }
    }

    public long createGSTReport(GSTReportBean reportBean) {
        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("insert into p_gst_reports(orgid," +
                "`mode`,reportstatus,formonth) values(:orgId,:mode,:reportStatus,:forMonth)", new BeanPropertySqlParameterSource(reportBean),holder);
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
        if (ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "id desc";
        }
        return namedParameterJdbcTemplate.query("select p_gst_report.*" +
                        " from p_gst_report where orgid=:orgID " +
                        ANPUtils.getWhereClause(searchParams) + " order by  " + orderBy + "  limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new ListGSTReportMapper());
    }

    private static final class ListGSTReportMapper implements RowMapper<GSTReportBean> {
        public GSTReportBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            GSTReportBean reportBean = new GSTReportBean();
            reportBean.setExcelFilePath(rs.getString("excelfilepath"));
            reportBean.setForMonth(rs.getString("formonth"));
            reportBean.setFromEmail(rs.getString("fromemail"));
            reportBean.setGenerateDate(rs.getTimestamp("generatedate"));
            reportBean.setMode(rs.getString("mode"));
            reportBean.setReportStatus(rs.getString("reportstatus"));
            reportBean.setOrgId(rs.getLong("orgid"));
            reportBean.setReportId(rs.getLong("id"));
            //           // reportBean.setToEmails(rs.getString("toemails"));
            reportBean.setPdfFilePath(rs.getString("pdffilepath"));
            return reportBean;
        }
    }
}
