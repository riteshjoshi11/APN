package com.ANP.repository;

import com.ANP.bean.*;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
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
    public ResponseEntity<InputStreamResource> fetchPdf(String filePath, long orgId, String loggedInEmployeeID) {
        try {
            Path pdfPath = Paths.get(filePath);
            byte[] pdf = Files.readAllBytes(pdfPath);
            ByteArrayInputStream pdfToByte = new ByteArrayInputStream(pdf);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Content-Disposition", "attachment; filename=" + pdfPath.getFileName().toString());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfToByte));
        } catch (IOException e) {
            throw new CustomAppException("The requested PDF does not exist or not readable", "SERVER.FETCH_PDF.PDFIOISSUE", HttpStatus.EXPECTATION_FAILED);
        }
    }

    /*
     * This method will take fullFilePath as parameter return the excel
     * orgId and loggedInEmployeeId is used in future
     */
    public ResponseEntity<InputStreamResource> fetchExcel(String filePath, long orgid, String loggedInEmployeeID) {
        try {
            Path excelPath = Paths.get(filePath);
            byte[] excel = Files.readAllBytes(excelPath);
            ByteArrayInputStream excelToByte = new ByteArrayInputStream(excel);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add("Content-Disposition", "attachment; filename=" + excelPath.getFileName().toString());

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(excelToByte));
        } catch (IOException e) {
            throw new CustomAppException("Problem reading excel file on server", "SERVER.FETCH_EXCEL.IOISSUE", HttpStatus.EXPECTATION_FAILED);
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

    public List<TransactionReportBean> listTxnReport(long orgID, List<SearchParam> searchParam, String orderBy, int noOfRecordsToShow, int startIndex) {
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
                " p_txnrpt_send_email where p_txnrpt_send_email.p_txn_reports_id=report.id) emails from p_txn_reports report where orgid=:orgID " +
                ANPUtils.getWhereClause(searchParam) + " order by  " + orderBy + " limit  :noOfRecordsToShow"
                + " offset :startIndex", param, new ListTransactionReportMapper());
    }

    /*
        Get the Generated Date of Last Successful Report for a orgId and EmployeeId
     */
    public java.util.Date getLastSuccessfulBackupDate(Long orgId, String employeeID) {
        if (orgId == null || orgId.longValue() < 1 || ANPUtils.isNullOrEmpty(employeeID)) {
            throw new CustomAppException("OrgID or EmployeeID is null", "SERVER.getLastSuccessfulBackupDate.INVALID_PARAM", HttpStatus.BAD_REQUEST);
        }

        String sql = "select todate from p_txn_reports where orgid=? and employeeid=? and reportstatus='"
                + ReportBean.reportStatusEnum.GENERATED.toString() + "' order by generatedate desc limit 1";
        try {
            return namedParameterJdbcTemplate.getJdbcTemplate().queryForObject(sql, new Object[]{orgId, employeeID}, java.util.Date.class);
        } catch (IncorrectResultSizeDataAccessException e) {
            //no need to handle this exception - this means that there is no result.
        }
        return null;
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
            if (!ANPUtils.isNullOrEmpty(emailsInCSVFormat)) {
                String elements[] = emailsInCSVFormat.split(",");
                if (elements != null && elements.length > 0) {
                    emails = Arrays.asList(elements);
                }
            }
            reportBean.setToEmailList(emails);
            return reportBean;
        }
    }

    private static final class ListTransactionReportMapper implements RowMapper<TransactionReportBean> {
        public TransactionReportBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            TransactionReportBean reportBean = new TransactionReportBean();
            reportBean.setExcelFilePath(rs.getString("report.excelfilepath"));
            reportBean.setPdfFilePath(rs.getString("report.pdffilepath"));
//            reportBean.setFromEmail(rs.getString("report.fromemail"));
            reportBean.setGenerateDate(rs.getTimestamp("report.generatedate"));
            reportBean.setReportStatus(rs.getString("reportstatus"));
            reportBean.setOrgId(rs.getLong("report.orgid"));
            reportBean.setReportId(rs.getLong("report.id"));
            reportBean.setTimePeriod(rs.getString("report.period"));
            reportBean.setFromDate(rs.getDate("report.fromdate"));
            reportBean.setToDate(rs.getDate("report.todate"));
            reportBean.setReportFormat(rs.getString("report.format"));
            reportBean.setType(rs.getString("report.type"));
            String emailsInCSVFormat = rs.getString("emails");
            List<String> emails = null;
            if (!ANPUtils.isNullOrEmpty(emailsInCSVFormat)) {
                String elements[] = emailsInCSVFormat.split(",");
                if (elements != null && elements.length > 0) {
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
    public void updateReport_status(ReportBean reportBean, String reportTableName) {
        if (reportBean.getReportId() <= 0) {
            throw new CustomAppException("ReportID cannot be 0 or blank", "SERVER.updateGSTReport_status.INVALID_PARAM", HttpStatus.EXPECTATION_FAILED);
        }

        if (reportBean.getOrgId() <= 0) {
            throw new CustomAppException("OrgID cannot be 0 or blank", "SERVER.updateGSTReport_status.INVALID_PARAM", HttpStatus.EXPECTATION_FAILED);
        }

        if (ANPUtils.isNullOrEmpty(reportBean.getReportStatus())) {
            throw new CustomAppException("Report Status blank or empty", "SERVER.updateGSTReport_status.INVALID_PARAM", HttpStatus.EXPECTATION_FAILED);
        }

        namedParameterJdbcTemplate.update("update " + reportTableName + " set reportstatus = :reportStatus" +
                " where orgid = :orgId and id = :reportId", new BeanPropertySqlParameterSource(reportBean));
    }

    /*
    This method will perform the udpates for the file path
    * Mandatory orgId, reportID, either pdfFilePath or ExcelFilePath
    *
    */
    public void updateReport_filepath(ReportBean reportBean, String reportTableName) {
        if (reportBean.getReportId() <= 0) {
            throw new CustomAppException("ReportID cannot be 0 or blank", "SERVER.UPDATE_REPORT_FILEPATH.INVALID_REPORTID", HttpStatus.BAD_REQUEST);
        }

        if (reportBean.getOrgId() <= 0) {
            throw new CustomAppException("OrgID cannot be 0 or blank", "SERVER.UPDATE_REPORT_FILEPATH.INVALID_ORGID", HttpStatus.BAD_REQUEST);
        }

        String updateQueryStr = "";

        if (ANPUtils.isNullOrEmpty(reportBean.getPdfFilePath()) && ANPUtils.isNullOrEmpty(reportBean.getExcelFilePath())) {
            throw new CustomAppException("Report File Path (Excel & PDF) blank or empty", "SERVER.updateGSTReport_filepath.INVALID_PARAM", HttpStatus.EXPECTATION_FAILED);
        } else if (!ANPUtils.isNullOrEmpty(reportBean.getPdfFilePath()) && !ANPUtils.isNullOrEmpty(reportBean.getExcelFilePath())) {
            updateQueryStr = " pdffilepath= :pdfFilePath , excelfilepath= :excelFilePath ";
        } else if (!ANPUtils.isNullOrEmpty(reportBean.getPdfFilePath())) {
            updateQueryStr = " pdffilepath= :pdfFilePath ";
        } else {
            updateQueryStr = " excelfilepath= :excelFilePath ";
        }
        namedParameterJdbcTemplate.update("update " + reportTableName + " set " + updateQueryStr +
                " where orgid = :orgId and id = :reportId", new BeanPropertySqlParameterSource(reportBean));
    }

    public List<String> getFrequentlyUsedGSTEmail(long orgId, String loggedInEmployeeId) {
        return namedParameterJdbcTemplate.getJdbcTemplate().query("select email from (select email,count(email) " +
                " from p_gst_reports rpt, p_gstrpt_send_email email where rpt.orgid=" + orgId +
                " and rpt.id=email.p_gst_reports_id group by email order by count(email) desc ) t limit 2", new RowMapper<String>() {
            public String mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getString(1);
            }
        });
    }
    /*
    * The create transaction report
     */
    public long createTxnReport(TransactionReportBean reportBean) {
        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update("insert into " + ANPConstants.DB_TBL_TXN_REPORT + " (orgid," +
                        " reportstatus,period,fromdate,todate,format,type) " +
                        " values(:orgId,:reportStatus,:timePeriod,:fromDate,:toDate, :reportFormat,:type)",
                new BeanPropertySqlParameterSource(reportBean), holder);
        long generatedOrgKey = holder.getKey().longValue();
        System.out.println("createTxnReport: Generated Key=" + generatedOrgKey);
        return generatedOrgKey;
    }

    /*
     * This method is little different than other 'insert' method
     * The reason is that it will make multiple entry into the table with same reference ID
     */
    public void createEmailEntryForTxn(TransactionReportBean reportBean) {
        String sql = "insert into p_txnrpt_send_email(email,p_txn_reports_id) values(?,?) ";
        namedParameterJdbcTemplate.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {
                String email = (reportBean.getToEmailList()).get(i);
                ps.setString(1, email);
                ps.setLong(2, reportBean.getReportId());
            }
            @Override
            public int getBatchSize() {
                return (reportBean.getToEmailList()).size();
            }
        });
    }

    public void coreLogicReportGeneration(GSTReportBean gstReportBean, String dateFrom, String dateTo) {

        List<SearchParam> searchParamList = new ArrayList<>();
        SearchParam searchParam = new SearchParam();
        searchParam.setCondition("and");
        searchParam.setFieldName("date");
        searchParam.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        searchParam.setSoperator(">");
        searchParam.setValue(dateFrom);

        SearchParam searchParam1 = new SearchParam();
        searchParam1.setCondition("and");
        searchParam1.setFieldName("date");
        searchParam1.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        searchParam1.setSoperator("<");
        searchParam1.setValue(dateTo);


        searchParamList.add(searchParam);
        searchParamList.add(searchParam1);

        XSSFWorkbook workbook = new XSSFWorkbook();
        try{
            XSSFSheet sheet = workbook.createSheet("Expense");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Bill Date");
            row.createCell(1).setCellValue("Party Name");
            row.createCell(2).setCellValue("Bill No");
            row.createCell(3).setCellValue("Party GST NO");
            row.createCell(4).setCellValue("NET");
            row.createCell(5).setCellValue("CGSTe");
            row.createCell(6).setCellValue("SGST");
            row.createCell(7).setCellValue("IGST");
            row.createCell(8).setCellValue("OTHER");
            row.createCell(9).setCellValue("Total");
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("orgId", gstReportBean.getOrgId());
            namedParameterJdbcTemplate.query("select exp.date, exp.description, exp.orderamount, exp.cgst," +
                    " exp.igst, exp.sgst, exp.extra, exp.totalamount, exp.topartyname," +
                    "   exp.topartygstno from generalexpense exp " +
                    " where exp.orgid = :orgId and exp.includeinreport " +
                    "= true and (exp.isdeleted is null or exp.isdeleted <> true) " + ANPUtils.getWhereClause(searchParamList), param, new ResultSetExtractor<String>() {
                public String extractData(ResultSet rs) throws SQLException {
                    while (rs.next()) {
                        int rowNum = sheet.getLastRowNum();
                        System.out.println("rownumber is" + rowNum);
                        rowNum++;
                        Row row = sheet.createRow(rowNum);
                        row.createCell(0).setCellValue(rs.getString("exp.date"));
                        row.createCell(1).setCellValue(rs.getString("exp.topartyname"));
                        row.createCell(2).setCellValue(rs.getString("exp.description"));
                        row.createCell(3).setCellValue(rs.getString("exp.topartygstno"));
                        row.createCell(4).setCellValue(rs.getDouble("exp.orderamount"));
                        row.createCell(5).setCellValue(rs.getDouble("exp.cgst"));
                        row.createCell(6).setCellValue(rs.getDouble("exp.igst"));
                        row.createCell(7).setCellValue(rs.getDouble("exp.sgst"));
                        row.createCell(8).setCellValue(rs.getDouble("exp.extra"));
                        row.createCell(9).setCellValue(rs.getDouble("exp.totalamount"));
                    }


                    // This return is useless, using it because method wont allow us to return void
                    return "hello";
                }
            });

            XSSFSheet sheet1 = workbook.createSheet("purchase");
            Row row1 = sheet1.createRow(0);
            row1.createCell(0).setCellValue("Bill Date");
            row1.createCell(1).setCellValue("Party Name");
            row1.createCell(2).setCellValue("Party State");
            row1.createCell(3).setCellValue("Bill No");
            row1.createCell(4).setCellValue("Party GST NO");
            row1.createCell(5).setCellValue("NET");
            row1.createCell(6).setCellValue("CGST");
            row1.createCell(7).setCellValue("SGST");
            row1.createCell(8).setCellValue("IGST");
            row1.createCell(9).setCellValue("OTHER");
            row1.createCell(10).setCellValue("Total");

            namedParameterJdbcTemplate.query("select pur.date, pur.billno, pur.orderamount, pur.cgst," +
                    " pur.igst, pur.sgst, pur.extra, pur.totalamount, customer.name," +
                    "  customer.state, customer.gstin from customer, purchasefromvendor pur " +
                    " where customer.id = pur.fromcustomerid and pur.orgid = :orgId and pur.includeinreport " +
                    "= true and (pur.isdeleted is null or pur.isdeleted <> true) " + ANPUtils.getWhereClause(searchParamList), param, new ResultSetExtractor<String>() {
                public String extractData(ResultSet rs) throws SQLException {
                    while (rs.next()) {
                        int rowNum = sheet1.getLastRowNum();
                        System.out.println("rownumber is" + rowNum);
                        rowNum++;
                        Row row = sheet1.createRow(rowNum);
                        row.createCell(0).setCellValue(rs.getString("pur.date"));
                        row.createCell(1).setCellValue(rs.getString("customer.name"));
                        row.createCell(2).setCellValue(rs.getString("customer.state"));
                        row.createCell(3).setCellValue(rs.getString("pur.billno"));
                        row.createCell(4).setCellValue(rs.getString("customer.gstin"));
                        row.createCell(5).setCellValue(rs.getDouble("pur.orderamount"));
                        row.createCell(6).setCellValue(rs.getDouble("pur.cgst"));
                        row.createCell(7).setCellValue(rs.getDouble("pur.igst"));
                        row.createCell(8).setCellValue(rs.getDouble("pur.sgst"));
                        row.createCell(9).setCellValue(rs.getDouble("pur.extra"));
                        row.createCell(10).setCellValue(rs.getDouble("pur.totalamount"));
                    }


                    // This return is useless, using it because method wont allow us to return void
                    return "hello";
                }
            });

            XSSFSheet sheet2 = workbook.createSheet("sale");
            Row row2 = sheet2.createRow(0);
            row2.createCell(0).setCellValue("Bill Date");
            row2.createCell(1).setCellValue("Party Name");
            row2.createCell(2).setCellValue("Party State");
            row2.createCell(3).setCellValue("Bill No");
            row2.createCell(4).setCellValue("Party GST NO");
            row2.createCell(5).setCellValue("NET");
            row2.createCell(6).setCellValue("CGST");
            row2.createCell(7).setCellValue("SGST");
            row2.createCell(8).setCellValue("IGST");
            row2.createCell(9).setCellValue("OTHER");
            row2.createCell(10).setCellValue("Total");

            namedParameterJdbcTemplate.query("select cus.date, cus.invoiceno, cus.orderamount, cus.cgst," +
                    " cus.igst, cus.sgst, cus.extra, cus.totalamount, customer.name," +
                    "  customer.state, customer.gstin from customer, customerinvoice cus " +
                    " where customer.id = cus.tocustomerid and cus.orgid = :orgId and cus.includeinreport " +
                    "= true and (cus.isdeleted is null or cus.isdeleted <> true) " + ANPUtils.getWhereClause(searchParamList), param, new ResultSetExtractor<String>() {
                public String extractData(ResultSet rs) throws SQLException {
                    while (rs.next()) {
                        int rowNum = sheet2.getLastRowNum();
                        System.out.println("rownumber is" + rowNum);
                        rowNum++;
                        Row row = sheet2.createRow(rowNum);
                        row.createCell(0).setCellValue(rs.getString("cus.date"));
                        row.createCell(1).setCellValue(rs.getString("customer.name"));
                        row.createCell(2).setCellValue(rs.getString("customer.state"));
                        row.createCell(3).setCellValue(rs.getString("cus.invoiceno"));
                        row.createCell(4).setCellValue(rs.getString("customer.gstin"));
                        row.createCell(5).setCellValue(rs.getDouble("cus.orderamount"));
                        row.createCell(6).setCellValue(rs.getDouble("cus.cgst"));
                        row.createCell(7).setCellValue(rs.getDouble("cus.igst"));
                        row.createCell(8).setCellValue(rs.getDouble("cus.sgst"));
                        row.createCell(9).setCellValue(rs.getDouble("cus.extra"));
                        row.createCell(10).setCellValue(rs.getDouble("cus.totalamount"));
                    }


                    // This return is useless, using it because method wont allow us to return void
                    return "hello";
                }
            });
        }catch(Exception e){
            throw new CustomAppException("SomeException has occured", "SERVER.Report_Logic.NOTAVAILABLE", HttpStatus.EXPECTATION_FAILED);
        }
        finally {
            try (FileOutputStream out = new FileOutputStream(gstReportBean.getExcelFilePath())) {
                workbook.write(out);
                workbook.close();
            } catch (FileNotFoundException fileNotFoundException) {
                throw new CustomAppException("File was not found", "SERVER.Report_Logic.NOTAVAILABLE", HttpStatus.EXPECTATION_FAILED);
            }
            catch (IOException ioException) {
                throw new CustomAppException("could'nt write into file", "SERVER.Report_Logic.NOTAVAILABLE", HttpStatus.EXPECTATION_FAILED);
            }
        }
    }

    public void updateP_ReportGST(GSTReportBean gstReportBean){
        Map<String,Object> param = new HashMap<>();
        param.put("orgId", gstReportBean.getOrgId());
        param.put("excelFilePath", gstReportBean.getExcelFilePath());
        param.put("forMonth",gstReportBean.getForMonth());
        param.put("reportid", gstReportBean.getReportId());
        namedParameterJdbcTemplate.update("update p_gst_reports set orgid = :orgId, excelfilepath = :excelFilePath" +
                " , formonth =:forMonth where id = :reportid",param);
    }
}

