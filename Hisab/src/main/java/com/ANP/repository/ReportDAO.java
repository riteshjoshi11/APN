package com.ANP.repository;

import com.ANP.bean.CustomerBean;
import com.ANP.bean.ReportBean;
import com.ANP.bean.SearchParam;
import com.ANP.util.ANPUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
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
    public ResponseEntity<InputStreamResource> fetchPdf(String filePath, long orgid, String loggedInEmployeeID) throws Exception {
        String filepath = ("f:/" + "generatedpdf" + (new Date().getTime() / 1000) + ".pdf");
        Document document = new Document(PageSize.A4, 20, 20, 20, 20);
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        PdfWriter.getInstance(document, new FileOutputStream(filepath));
        document.open();
        document.add(new Paragraph("hello world"));
        document.close();

        Path pdfPath = Paths.get(filepath);
        byte[] pdf = Files.readAllBytes(pdfPath);
        ByteArrayInputStream c = new ByteArrayInputStream(pdf);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add("Content-Disposition", "inline; filename=ParasGenerated.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(c));
    }

    /*
     * This method will take fullFilePath as parameter return the excel
     * orgId and loggedInEmployeeId is used in future
     */

    public ResponseEntity<InputStreamResource> fetchExcel(String filePath, long orgid, String loggedInEmployeeID) throws Exception {
        //@TODO Paras please write code for Excel download
        return null;
    }

    public int createGSTReport(ReportBean reportBean) {
        return namedParameterJdbcTemplate.update("insert into p_gst_report(id,orgid,toemails,pdffilepath,excelfilepath," +
                "fromemail,`mode`,reportstatus,formonth) values(:reportId, :orgId, :toEmails, :pdfFilePath, :excelFilePath, :fromEmail," +
                " :mode, :reportStatus, :forMonth)", new BeanPropertySqlParameterSource(reportBean));
    }


    public List<ReportBean> listGSTReport(long orgID, Collection<SearchParam> searchParams,
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

    private static final class ListGSTReportMapper implements RowMapper<ReportBean> {
        public ReportBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            ReportBean reportBean = new ReportBean();
            reportBean.setExcelFilePath(rs.getString("excelfilepath"));
            reportBean.setForMonth(rs.getString("formonth"));
            reportBean.setFromEmail(rs.getString("fromemail"));
            reportBean.setGenerateDate(rs.getTimestamp("generatedate"));
            reportBean.setMode(rs.getString("mode"));
            reportBean.setReportStatus(rs.getString("reportstatus"));
            reportBean.setOrgId(rs.getLong("orgid"));
            reportBean.setReportId(rs.getLong("id"));
            reportBean.setToEmails(rs.getString("toemails"));
            reportBean.setPdfFilePath(rs.getString("pdffilepath"));

            return reportBean;
        }
    }
}
