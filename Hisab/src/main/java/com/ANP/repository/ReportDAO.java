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
import java.lang.reflect.Parameter;
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

    @Autowired
    SystemConfigurationReaderDAO systemConfigurationReaderDAO;

    @Autowired
    CustomerInvoiceDAO customerInvoiceDAO;

    @Autowired
    ExpenseDAO expenseDAO;

    @Autowired
    PurchaseFromVendorDAO purchaseFromVendorDAO;

    @Autowired
    PayToVendorDAO payToVendorDAO;

    @Autowired
    InternalTransferDAO internalTransferDAO;

    @Autowired
    RetailSaleDAO retailSaleDAO;

    @Autowired
    PaymentReceivedDAO paymentReceivedDAO;

    @Autowired
    EmployeeDAO employeeDAO;
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
                "`mode`,reportstatus,formonth) values(:orgId,:mode,'"+ReportBean.reportStatusEnum.WAITING.toString()+"',:forMonth)", new BeanPropertySqlParameterSource(reportBean), holder);
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
        return namedParameterJdbcTemplate.query("select report.*," +
                "(select concat(`first`,' ',`last`,'[',`mobile`,']') from employee where employee.id = report.generatedby) as createdByEmployeeName, " +
                " (select GROUP_CONCAT(email) from" +
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
        return namedParameterJdbcTemplate.query("select report.*," +
                "(select concat(`first`,' ',`last`,'[',`mobile`,']') from employee where employee.id = report.generatedby) as createdByEmployeeName, " +
                " (select GROUP_CONCAT(email) from" +
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
            reportBean.setGeneratedByName(rs.getString("createdByEmployeeName"));

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
            reportBean.setGeneratedByName(rs.getString("createdByEmployeeName"));
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
                        " values(:orgId,'"+ ReportBean.reportStatusEnum.WAITING.toString() +"',:timePeriod,:fromDate,:toDate, :reportFormat,:type)",
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

    public void generateGSTReport(GSTReportBean gstReportBean, String dateFrom, String dateTo) {
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
        FileOutputStream out = null;
        try {
            XSSFSheet sheet = workbook.createSheet("Expense");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Bill Date");
            row.createCell(1).setCellValue("Party Name");
            row.createCell(2).setCellValue("Bill No");
            row.createCell(3).setCellValue("Party GST NO");
            row.createCell(4).setCellValue("NET");
            row.createCell(5).setCellValue("CGST");
            row.createCell(6).setCellValue("SGST");
            row.createCell(7).setCellValue("IGST");
            row.createCell(8).setCellValue("OTHER");
            row.createCell(9).setCellValue("Total(Gross)");
            row.createCell(10).setCellValue("Details");

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("orgId", gstReportBean.getOrgId());
            namedParameterJdbcTemplate.query("select exp.date, exp.description, exp.orderamount, exp.cgst," +
                    " exp.igst, exp.sgst, exp.extra, exp.totalamount, exp.topartyname," +
                    "   exp.topartygstno, exp.description from generalexpense exp " +
                    " where exp.orgid = :orgId and exp.includeinreport " +
                    "= true and (exp.isdeleted is null or exp.isdeleted <> true) "
                    + ANPUtils.getWhereClause(searchParamList), param, new ResultSetExtractor<String>() {
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
                        row.createCell(6).setCellValue(rs.getDouble("exp.sgst"));
                        row.createCell(7).setCellValue(rs.getDouble("exp.igst"));
                        row.createCell(8).setCellValue(rs.getDouble("exp.extra"));
                        row.createCell(9).setCellValue(rs.getDouble("exp.totalamount"));
                        row.createCell(10).setCellValue(rs.getString("exp.description"));

                    }


                    // This return is useless, using it because method wont allow us to return void
                    return "";
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
            row1.createCell(10).setCellValue("Total(Gross)");
            row1.createCell(11).setCellValue("Details");


            namedParameterJdbcTemplate.query("select pur.date, pur.billno, pur.orderamount, pur.cgst," +
                    " pur.igst, pur.sgst, pur.extra, pur.totalamount,  pur.note, customer.name," +
                    "  customer.state, customer.gstin from customer, purchasefromvendor pur " +
                    " where customer.id = pur.fromcustomerid and pur.orgid = :orgId and pur.includeinreport " +
                    "= true and (pur.isdeleted is null or pur.isdeleted <> true) "
                    + ANPUtils.getWhereClause(searchParamList), param, new ResultSetExtractor<String>() {
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
                        row.createCell(7).setCellValue(rs.getDouble("pur.sgst"));
                        row.createCell(8).setCellValue(rs.getDouble("pur.igst"));
                        row.createCell(9).setCellValue(rs.getDouble("pur.extra"));
                        row.createCell(10).setCellValue(rs.getDouble("pur.totalamount"));
                        row.createCell(11).setCellValue(rs.getString("pur.note"));
                    }


                    // This return is useless, using it because method wont allow us to return void
                    return "";
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
            row2.createCell(10).setCellValue("Total(Gross)");
            row2.createCell(11).setCellValue("Details");


            namedParameterJdbcTemplate.query("select cus.date, cus.invoiceno, cus.orderamount, cus.cgst," +
                    " cus.igst, cus.sgst, cus.extra, cus.totalamount, cus.note, customer.name," +
                    "  customer.state, customer.gstin from customer, customerinvoice cus " +
                    " where customer.id = cus.tocustomerid and cus.orgid = :orgId and cus.includeinreport " +
                    "= true and (cus.isdeleted is null or cus.isdeleted <> true) "
                    + ANPUtils.getWhereClause(searchParamList), param, new ResultSetExtractor<String>() {
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
                        row.createCell(7).setCellValue(rs.getDouble("cus.sgst"));
                        row.createCell(8).setCellValue(rs.getDouble("cus.igst"));
                        row.createCell(9).setCellValue(rs.getDouble("cus.extra"));
                        row.createCell(10).setCellValue(rs.getDouble("cus.totalamount"));
                        row.createCell(10).setCellValue(rs.getString("cus.note"));

                    }


                    // This return is useless, using it because method wont allow us to return void
                    return "";
                }
            });

            out = new FileOutputStream(gstReportBean.getExcelFilePath());
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomAppException("Report cannot be generated", "SERVER.GENERATE_GST_REPORT.REPORT_GEN_ISSUE", HttpStatus.EXPECTATION_FAILED);
        } finally {

            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                }
            }
        }
    }


    /*
     * This method updates the Report Status and Excel File Path
     *
     */
    public void updateP_ReportGST(GSTReportBean gstReportBean) {
        Map<String, Object> param = new HashMap<>();

        param.put("excelFilePath", gstReportBean.getExcelFilePath());
        param.put("reportstatus", gstReportBean.getReportStatus());
        param.put("reportid", gstReportBean.getReportId());
        param.put("orgId", gstReportBean.getOrgId());

        //Updating ReportStatus and Excel File Path
        namedParameterJdbcTemplate.update("update p_gst_reports set reportstatus = :reportstatus , excelfilepath = :excelFilePath" +
                " where id = :reportid and  orgid = :orgId", param);

    }

    public void updateTransactionReport(TransactionReportBean transactionReportBean) {
        Map<String, Object> param = new HashMap<>();

        param.put("excelFilePath", transactionReportBean.getExcelFilePath());
        param.put("reportstatus", transactionReportBean.getReportStatus());
        param.put("reportid", transactionReportBean.getReportId());
        param.put("orgId", transactionReportBean.getOrgId());

        //Updating ReportStatus and Excel File Path
        namedParameterJdbcTemplate.update("update p_txn_reports set reportstatus = :reportstatus , excelfilepath = :excelFilePath" +
                " where id = :reportid and  orgid = :orgId", param);

    }


    public void canCreateMoreGSTReports(GSTReportBean gstReportBean){
        Map<String, Object> param = new HashMap<>();
        param.put("orgid", gstReportBean.getOrgId());
        param.put("formonth",gstReportBean.getForMonth());
        Integer countProcessing = namedParameterJdbcTemplate.queryForObject("select count(*) from p_gst_reports " +
                " where orgid = :orgid and (reportstatus = '"+ReportBean.reportStatusEnum.PROCESSING.toString()+
                "' or reportstatus =   '"+ReportBean.reportStatusEnum.WAITING.toString()+"')",param, Integer.class);



        if(countProcessing > 0)
        {
           throw new CustomAppException("Error: You cannot generate more reports, either cancel previous requests" +
                   " or wait for the them to be generated"
                   ,"SERVER.Can_Create_More_GST_Reports.ALREADYGENERATING", HttpStatus.EXPECTATION_FAILED);
        }

        Integer countGenerated = namedParameterJdbcTemplate.queryForObject("select count(*) from p_gst_reports "+
                "where orgid = :orgid and reportstatus = '"+ReportBean.reportStatusEnum.GENERATED.toString()+
                "' and forMonth = :formonth",param,
                Integer.class);
        System.out.println(countGenerated);
        System.out.println(countProcessing);
        Map<String,String> systemConfigMap = systemConfigurationReaderDAO.getSystemConfigurationMap();
        String maxNoOfReportsInAMonthInString = systemConfigMap.get("REPORT.MAX.REPORTS.FOR.ONE.MONTH");
        int maxNoOfReportsInAMonth;
        try {
            maxNoOfReportsInAMonth = Integer.parseInt(maxNoOfReportsInAMonthInString);
        }
        catch(Exception e)
        {
            throw new CustomAppException("Max Number Of Reports in a month are invalid","SERVER.CONTROLORGDATA_SYSTEM_CONFIG.INVALIDVALUE", HttpStatus.EXPECTATION_FAILED);
        }

        if (maxNoOfReportsInAMonth <= 0)
        {
            maxNoOfReportsInAMonth = 3;
        }

        if(countGenerated >= maxNoOfReportsInAMonth)
        {
            throw new CustomAppException("Error: You have reached the reports generation limit for the given month"
                    ,"SERVER.Can_Create_More_GST_Reports.LIMITREACHED", HttpStatus.EXPECTATION_FAILED);
        }
    }

    public void backupReportGeneration(long orgID, String dateFrom , String dateTo ,String filePath) {

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

        List<SearchParam> searchParamListRcvDate = new ArrayList<>();
        SearchParam searchParamRcvDate = new SearchParam();
        searchParamRcvDate.setCondition("and");
        searchParamRcvDate.setFieldName("rcvddate");
        searchParamRcvDate.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        searchParamRcvDate.setSoperator(">");
        searchParamRcvDate.setValue(dateFrom);

        SearchParam searchParamRcvDate1 = new SearchParam();
        searchParamRcvDate1.setCondition("and");
        searchParamRcvDate1.setFieldName("rcvddate");
        searchParamRcvDate1.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        searchParamRcvDate1.setSoperator("<");
        searchParamRcvDate1.setValue(dateTo);

        searchParamListRcvDate.add(searchParamRcvDate);
        searchParamListRcvDate.add(searchParamRcvDate1);

        searchParamList.add(searchParam);
        searchParamList.add(searchParam1);

        XSSFWorkbook workbook = new XSSFWorkbook();
        FileOutputStream out = null;

        String orderBy = "date desc";
        List<CustomerInvoiceBean> listSales =  customerInvoiceDAO.listSalesPaged(orgID,searchParamList,orderBy,500,1);
        List<PurchaseFromVendorBean> purchaseFromVendorBeanList = purchaseFromVendorDAO.listPurchasesPaged(orgID,searchParamList,orderBy,500,1);
        List<Expense> expenseList = expenseDAO.listExpensesPaged(orgID,searchParamList,orderBy,500,1);
        List<InternalTransferBean> internalTransferBeanList = internalTransferDAO.listInternalTransfer(orgID,searchParamListRcvDate,"rcvddate desc",500,1);
        List<RetailSale> retailSaleList = retailSaleDAO.listRetailEntryPaged(orgID,searchParamList,orderBy,500,1);
        List<PayToVendorBean> payToVendorBeanList = payToVendorDAO.listPayToVendorPaged(orgID,searchParamList,orderBy,500,1);
        List<PaymentReceivedBean> paymentReceivedBeanList = paymentReceivedDAO.listPaymentReceivedPaged(orgID,searchParamListRcvDate,"rcvddate desc",500,1);
        try {
            XSSFSheet sheet = workbook.createSheet("Sales");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Transaction Date");
            row.createCell(1).setCellValue("Order Amount");
            row.createCell(2).setCellValue("Total Amount");
            row.createCell(3).setCellValue("Invoice No.");
            row.createCell(4).setCellValue("CGST");
            row.createCell(5).setCellValue("SGST");
            row.createCell(6).setCellValue("IGST");

            for(CustomerInvoiceBean customerInvoiceBean : listSales)
            {
                int rowNum = sheet.getLastRowNum();
                System.out.println("rownumber is" + rowNum);
                rowNum++;
                row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(customerInvoiceBean.getDate());
                row.createCell(1).setCellValue(customerInvoiceBean.getOrderAmount().toString());
                row.createCell(2).setCellValue(customerInvoiceBean.getTotalAmount().toString());
                row.createCell(3).setCellValue(customerInvoiceBean.getInvoiceNo());
                row.createCell(4).setCellValue(customerInvoiceBean.getCGST().toString());
                row.createCell(5).setCellValue(customerInvoiceBean.getSGST().toString());
                row.createCell(6).setCellValue(customerInvoiceBean.getIGST().toString());
            }
            XSSFSheet sheet1 = workbook.createSheet("Purchase");
            Row row1 = sheet1.createRow(0);
            row1.createCell(0).setCellValue("Transaction Date");
            row1.createCell(1).setCellValue("Order Amount");
            row1.createCell(2).setCellValue("CGST");
            row1.createCell(3).setCellValue("SGST");
            row1.createCell(4).setCellValue("IGST");
            row1.createCell(5).setCellValue("Total Amount");
            row1.createCell(6).setCellValue("Bill no.");

            for(PurchaseFromVendorBean purchaseFromVendorBean : purchaseFromVendorBeanList)
            {
                int rowNum = sheet1.getLastRowNum();
                System.out.println("rownumber is" + rowNum);
                rowNum++;
                row1 = sheet1.createRow(rowNum);
                row1.createCell(0).setCellValue(purchaseFromVendorBean.getDate());
                row1.createCell(1).setCellValue(purchaseFromVendorBean.getOrderAmount().toString());
                row1.createCell(2).setCellValue(purchaseFromVendorBean.getCGST().toString());
                row1.createCell(3).setCellValue(purchaseFromVendorBean.getSGST().toString());
                row1.createCell(4).setCellValue(purchaseFromVendorBean.getIGST().toString());
                row1.createCell(5).setCellValue(purchaseFromVendorBean.getTotalAmount().toString());
                row1.createCell(6).setCellValue(purchaseFromVendorBean.getBillNo());
            }

            XSSFSheet sheet2 = workbook.createSheet("Expense");
            Row row2 = sheet2.createRow(0);
            row2.createCell(0).setCellValue("Transaction Date");
            row2.createCell(1).setCellValue("To Party Name");
            row2.createCell(2).setCellValue("Order Amount");
            row2.createCell(3).setCellValue("CGST");
            row2.createCell(4).setCellValue("SGST");
            row2.createCell(5).setCellValue("IGST");
            row2.createCell(6).setCellValue("To Party GST No.");
            row2.createCell(7).setCellValue("To Party Mobile No.");
            row2.createCell(8).setCellValue("Total Amount");

            for(Expense expense : expenseList)
            {
                int rowNum = sheet2.getLastRowNum();
                System.out.println("rownumber is" + rowNum);
                rowNum++;
                row2 = sheet2.createRow(rowNum);
                row2.createCell(0).setCellValue(expense.getDate());
                row2.createCell(1).setCellValue(expense.getToPartyName());
                row2.createCell(2).setCellValue(expense.getOrderAmount().toString());
                row2.createCell(3).setCellValue(expense.getCGST().toString());
                row2.createCell(4).setCellValue(expense.getSGST().toString());
                row2.createCell(5).setCellValue(expense.getIGST().toString());
                row2.createCell(6).setCellValue(expense.getToPartyGSTNO());
                row2.createCell(7).setCellValue(expense.getToPartyMobileNO());
                row2.createCell(8).setCellValue(expense.getTotalAmount().toString());
            }

            XSSFSheet sheet3 = workbook.createSheet("Internal Transfer");
            Row row3 = sheet3.createRow(0);
            row3.createCell(0).setCellValue("Received Date");
            row3.createCell(1).setCellValue("Amount");
            row3.createCell(2).setCellValue("Details");

            for(InternalTransferBean internalTransferBean : internalTransferBeanList)
            {
                int rowNum = sheet3.getLastRowNum();
                System.out.println("rownumber is" + rowNum);
                rowNum++;
                row3 = sheet3.createRow(rowNum);
                row3.createCell(0).setCellValue(internalTransferBean.getReceivedDate());
                row3.createCell(1).setCellValue(internalTransferBean.getAmount().toString());
                row3.createCell(2).setCellValue(internalTransferBean.getDetails());
            }

            XSSFSheet sheet4 = workbook.createSheet("Retail Sale");
            Row row4 = sheet4.createRow(0);
            row4.createCell(0).setCellValue("Transaction Date");
            row4.createCell(1).setCellValue("Amount");
            row4.createCell(2).setCellValue("Notes");

            for(RetailSale retailSale : retailSaleList)
            {
                int rowNum = sheet4.getLastRowNum();
                System.out.println("rownumber is" + rowNum);
                rowNum++;
                row4 = sheet4.createRow(rowNum);
                row4.createCell(0).setCellValue(retailSale.getDate());
                row4.createCell(1).setCellValue(retailSale.getAmount().toString());
                row4.createCell(2).setCellValue(retailSale.getNotes());
            }

            XSSFSheet sheet5 = workbook.createSheet("Pay To Vendor");
            Row row5 = sheet5.createRow(0);
            row5.createCell(0).setCellValue("Transaction Date");
            row5.createCell(1).setCellValue("Amount");
            row5.createCell(2).setCellValue("Details");

            for(PayToVendorBean payToVendorBean : payToVendorBeanList)
            {
                int rowNum = sheet5.getLastRowNum();
                System.out.println("rownumber is" + rowNum);
                rowNum++;
                row5 = sheet5.createRow(rowNum);
                row5.createCell(0).setCellValue(payToVendorBean.getPaymentDate());
                row5.createCell(1).setCellValue(payToVendorBean.getAmount().toString());
                row5.createCell(2).setCellValue(payToVendorBean.getDetails());
            }

            XSSFSheet sheet6 = workbook.createSheet("Payment Received");
            Row row6 = sheet6.createRow(0);
            row6.createCell(0).setCellValue("Transaction Date");
            row6.createCell(1).setCellValue("Amount");
            row6.createCell(2).setCellValue("Details");
            row6.createCell(3).setCellValue("Payment Type");

            for(PaymentReceivedBean paymentReceivedBean : paymentReceivedBeanList)
            {
                int rowNum = sheet6.getLastRowNum();
                System.out.println("rownumber is" + rowNum);
                rowNum++;
                row6 = sheet6.createRow(rowNum);
                row6.createCell(0).setCellValue(paymentReceivedBean.getReceivedDate());
                row6.createCell(1).setCellValue(paymentReceivedBean.getAmount().toString());
                row6.createCell(2).setCellValue(paymentReceivedBean.getDetails());
                row6.createCell(3).setCellValue(paymentReceivedBean.getPaymentType());
            }
            Map<String,Object> param = new HashMap<>();
            param.put("orgid",orgID);

            XSSFSheet sheet7 = workbook.createSheet("Employee With Their Balances");
            Row row7 = sheet7.createRow(0);



            System.out.println("WE are here 1");
            row7.createCell(0).setCellValue("First Name");
            row7.createCell(1).setCellValue("Last Name");
            row7.createCell(2).setCellValue("Mobile");
            row7.createCell(3).setCellValue("Salary Balance");
            row7.createCell(4).setCellValue("Current Balance Debit");
            row7.createCell(5).setCellValue("Current Balance Credit");

            namedParameterJdbcTemplate.query("select emp.first, emp.last, emp.mobile, emp.currentsalarybalance" +
                    ", acc.currentbalance from employee emp, account acc where emp.id = acc.ownerid and emp.orgid = :orgid and " +
                    "acc.type = 'EMPLOYEE' " , param, new ResultSetExtractor<String>() {
                public String extractData(ResultSet rs) throws SQLException {
                    while (rs.next()) {
                        int rowNum = sheet7.getLastRowNum();
                        System.out.println("rownumber is" + rowNum);
                        rowNum++;
                        Row row7 = sheet7.createRow(rowNum);

                        double credit = 0;
                        double debit = 0;
                        double currentBalance = rs.getDouble("acc.currentbalance");
                        if(currentBalance<0) {
                            debit = (currentBalance * (-1));
                            credit = 0;
                        } else {
                            debit = 0;
                            credit = currentBalance;
                        }
                        System.out.println("WE are here 1");
                        row7.createCell(0).setCellValue(rs.getString("emp.first"));
                        row7.createCell(1).setCellValue(rs.getString("emp.last"));
                        row7.createCell(2).setCellValue(rs.getString("emp.mobile"));
                        row7.createCell(3).setCellValue(rs.getString("emp.currentsalarybalance"));
                        row7.createCell(4).setCellValue(debit);
                        row7.createCell(5).setCellValue(credit);
                    }
                    System.out.println("WE are here 1");
                    // This return is useless, using it because method wont allow us to return void
                    return "";
                }
            });

            XSSFSheet sheet8 = workbook.createSheet("Customers With Their Balances");
            Row row8 = sheet8.createRow(0);
            System.out.println("WE are here 2");
            row8.createCell(0).setCellValue("Name/Firmname");
            row8.createCell(1).setCellValue("Mobile1");
            row8.createCell(2).setCellValue("Mobile2");
            row8.createCell(3).setCellValue("Current Balance Debit");
            row8.createCell(4).setCellValue("Current Balance Credit");

            namedParameterJdbcTemplate.query("select cus.name, cus.firmname, cus.mobile1, cus.mobile2" +
                    ", acc.currentbalance from customer cus, account acc where cus.id = acc.ownerid and cus.orgid = :orgid and " +
                    "acc.type = 'CUSTOMER' ", param, new ResultSetExtractor<String>() {
                public String extractData(ResultSet rs) throws SQLException {
                    while (rs.next()) {
                        int rowNum = sheet8.getLastRowNum();

                        rowNum++;
                        Row row = sheet8.createRow(rowNum);

                        String nameOrFirmName = rs.getString("cus.name");
                        if(ANPUtils.isNullOrEmpty(nameOrFirmName)) {
                            nameOrFirmName = rs.getString("cus.firmname");
                        }
                        System.out.println("WE are here 2");
                        double credit = 0;
                        double debit = 0;
                        double currentBalance = rs.getDouble("acc.currentbalance");
                        if(currentBalance<0) {
                            debit = (currentBalance * (-1));
                            credit = 0;
                        } else {
                            debit = 0;
                            credit = currentBalance;
                        }
                        System.out.println("WE are here 2");
                        row.createCell(0).setCellValue(nameOrFirmName);
                        row.createCell(1).setCellValue(rs.getString("cus.mobile1"));
                        row.createCell(2).setCellValue(rs.getString("cus.mobile2"));
                        row.createCell(3).setCellValue(debit);
                        row.createCell(4).setCellValue(credit);
                    }
                    // This return is useless, using it because method wont allow us to return void
                    System.out.println("WE are here 2");
                    return "";
                }
            });


            out = new FileOutputStream(filePath);
            workbook.write(out);
    } catch (Exception e) {
            e.printStackTrace();
            throw new CustomAppException("Report cannot be generated", "SERVER.GENERATE_GST_REPORT.REPORT_GEN_ISSUE", HttpStatus.EXPECTATION_FAILED);
        }   finally {

        if (out != null) {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
        if (workbook != null) {
            try {
                workbook.close();
            } catch (Exception e) {
            }
        }
    }
    }

    public List<GSTReportBean> getUnprocessedGSTReportRequestBatch(Integer batchSize){
        Map<String,Object> param = new HashMap<>();
        param.put("batchSize",batchSize);
        return namedParameterJdbcTemplate.query("select id , orgid, formonth " +
                "  from p_gst_reports where reportstatus = '"+ ReportBean.reportStatusEnum.WAITING.toString() +"'" +
                " limit :batchSize offset 0",param,new GSTReportMapper());
    }

    private static final class GSTReportMapper implements RowMapper<GSTReportBean>{
        public GSTReportBean mapRow(ResultSet rs, int Rownum) throws SQLException{
            GSTReportBean gstReportBean = new GSTReportBean();
            gstReportBean.setOrgId(rs.getLong("orgid"));
            gstReportBean.setReportId(rs.getLong("id"));
            gstReportBean.setForMonth(rs.getString("formonth"));
            return gstReportBean;
        }
    }

    public List<TransactionReportBean> getUnprocessedTransactionReportRequestBatch(Integer batchSize){
        Map<String,Object> param = new HashMap<>();
        param.put("batchSize",batchSize);
        return namedParameterJdbcTemplate.query("select id , orgid, fromdate, todate " +
                "  from p_txn_reports where reportstatus = '"+ ReportBean.reportStatusEnum.WAITING.toString() +"'" +
                " limit :batchSize offset 0",param,new TransactionGSTReportMapper());
    }

    private static final class TransactionGSTReportMapper implements RowMapper<TransactionReportBean>{
        public TransactionReportBean mapRow(ResultSet rs, int rownum) throws SQLException{
            TransactionReportBean transactionReportBean = new TransactionReportBean();
            transactionReportBean.setOrgId(rs.getLong("orgid"));
            transactionReportBean.setReportId(rs.getLong("id"));
            transactionReportBean.setFromDate(rs.getDate("fromdate"));
            transactionReportBean.setToDate(rs.getDate("todate"));
            return transactionReportBean;
        }

    }

    public void updateGSTReportStatusProcessingInBatch(List<GSTReportBean> gstReportBeanList) {
        String sql = "update p_gst_reports set reportstatus = '"+ ReportBean.reportStatusEnum.PROCESSING +"'" +
                " where id = ? and orgid = ?  ";
        namedParameterJdbcTemplate.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {

                Long reportID = gstReportBeanList.get(i).getReportId();
                Long orgId = gstReportBeanList.get(i).getOrgId();
                ps.setLong(1, reportID);
                ps.setLong(2, orgId);
            }

            @Override
            public int getBatchSize() {
                return (gstReportBeanList.size());
            }
        });
    }

    public void updateTxnReportStatusProcessingInBatch(List<TransactionReportBean> transactionReportBeanList) {
        String sql = "update p_txn_reports set reportstatus = '"+ ReportBean.reportStatusEnum.PROCESSING +"'" +
                " where id = ? and orgid = ?  ";
        namedParameterJdbcTemplate.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                    Long reportID = transactionReportBeanList.get(i).getReportId();
                    Long orgId = transactionReportBeanList.get(i).getOrgId();
                    ps.setLong(1, reportID);
                    ps.setLong(2, orgId);
                }

            @Override
            public int getBatchSize() {
                    return (transactionReportBeanList.size());
                }
        });
    }
}


