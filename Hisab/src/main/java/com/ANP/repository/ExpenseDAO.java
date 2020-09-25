package com.ANP.repository;

import com.ANP.bean.EmployeeBean;
import com.ANP.bean.Expense;
import com.ANP.bean.PurchaseFromVendorBean;
import com.ANP.bean.SearchParam;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

@Repository
public class ExpenseDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public int createExpense(Expense expense) {
        if (!expense.isForceCreate()) {
            isDuplicateSuspect(expense);
        }
        return namedParameterJdbcTemplate.update(
                "INSERT INTO generalexpense(date,Category,Description,totalamount,toPartyName,orgId,createdById,FromAccountID,fromemployeeid,includeinreport,orderamount,cgst,sgst,igst,extra,topartygstno,topartymobileno,paid)" +
                        "VALUES(:date,:category,:description,:totalAmount,:toPartyName,:orgId,:createdbyId,:FromAccountID,:fromEmployeeID,:includeInReport,:orderAmount,:CGST,:SGST,:IGST,:extra,:toPartyGSTNO,:toPartyMobileNO,:paid);",
                new BeanPropertySqlParameterSource(expense));
    }

    public List<Expense> listExpensesPaged(long orgId, Collection<SearchParam> searchParams,
                                           String orderBy, int noOfRecordsToShow, int startIndex) {
        if (startIndex == 0) {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgId);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);

        if (ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "id desc";
        }
        return namedParameterJdbcTemplate.query(
                "select exp.*, e.first,e.last, " +
                        "(select concat(`first`,' ',`last`,'[',`mobile`,']') from employee e where e.id = exp.createdbyid) as createdByEmployeeName " +
                        " from generalexpense exp, employee e where exp.fromemployeeid=e.id and exp.orgid=:orgID " +
                        "and (exp.isdeleted is null or exp.isdeleted <> true) " +
                        ANPUtils.getWhereClause(searchParams) + " order by  " + orderBy + "  limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new FullExpenseMapper());
    }

    private static final class FullExpenseMapper implements RowMapper<Expense> {
        public Expense mapRow(ResultSet rs, int rowNum) throws SQLException {
            Expense obj = new Expense();
            obj.setExpenseId(rs.getInt("exp.id"));
            obj.setCategory(rs.getString("exp.category"));
            obj.setTotalAmount(rs.getFloat("exp.totalamount"));
            obj.setOrgId(rs.getLong("exp.orgid"));
            obj.setIncludeInReport(rs.getBoolean("exp.includeinreport"));
            obj.setCreatedbyId(rs.getString("exp.createdbyid"));
            obj.setOrderAmount(rs.getDouble("exp.orderamount"));
            obj.setCGST(rs.getDouble("exp.cgst"));
            obj.setSGST(rs.getDouble("exp.sgst"));
            obj.setIGST(rs.getDouble("exp.igst"));
            obj.setExtra(rs.getDouble("exp.extra"));
            obj.setToPartyName(rs.getString("exp.topartyname"));
            obj.setDate(rs.getTimestamp("exp.date"));
            obj.setToPartyGSTNO(rs.getString("exp.topartygstno"));
            obj.setToPartyMobileNO(rs.getString("exp.topartymobileno"));
            obj.setEmpFirstName(rs.getString("e.first"));
            obj.setEmpLastName(rs.getString("e.last"));
            obj.setPaid(rs.getBoolean("paid"));
            obj.setCreateDate(rs.getTimestamp("exp.createdate"));
            obj.setFromAccountID(rs.getLong("exp.fromaccountid"));
            obj.setFromEmployeeID(rs.getString("exp.fromemployeeid"));
            obj.setCreatedByEmpoyeeName(rs.getString("createdByEmployeeName"));

            return obj;
        }
    }

    private static final class ExpenseMapperLimited implements RowMapper<Expense> {
        public Expense mapRow(ResultSet rs, int rowNum) throws SQLException {
            Expense obj = new Expense();
            obj.setToPartyName(rs.getString("topartyname"));
            obj.setToPartyGSTNO(rs.getString("topartygstno"));
            obj.setToPartyMobileNO(rs.getString("topartymobileno"));
            return obj;
        }
    }

    public List<Expense> findExpenseByToPartyName(String toPartyName, long orgId) {
        String toPartyNameCriteria = "";
        if (!ANPUtils.isNullOrEmpty(toPartyName)) {
            toPartyNameCriteria = " and topartyname like '%" + toPartyName + "%'";
        }
        return namedParameterJdbcTemplate.query(
                "select topartyname,topartygstno,topartymobileno from generalexpense where orgid=" + orgId
                        + toPartyNameCriteria,
                new BeanPropertySqlParameterSource(new Expense()), new ExpenseMapperLimited());
    }

    /*
         you need to update Expense Table:IncludeInCAReport field with the passed value
     */
    public boolean updateIncludeInCAReport(long expenseID, boolean caSwitch) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", expenseID);
        parameterSource.addValue("caSwitch", caSwitch);
        if (namedParameterJdbcTemplate.update("update generalexpense set includeinreport = :caSwitch where id = :id", parameterSource) != 0) {
            return true;
        } else {
            return false;
        }
    }


    /*
        you need to update Expense Table:includeInCalc field with the passed value
  */
    public boolean updateExpenseStatus(long expenseID, boolean paidStatus) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", expenseID);
        parameterSource.addValue("paidStatus", paidStatus);
        if (namedParameterJdbcTemplate.update("update generalexpense set paid = :paidStatus where id = :id", parameterSource) != 0) {
            return true; //operation success
        }
        return false;
    }

    private void isDuplicateSuspect(Expense expense) {
        //Do a count(*) query and if you found count>0 then throw this error else nothing
        Map<String, Object> params = new HashMap<>();
        params.put("orgid", expense.getOrgId());
        params.put("fromemployeeid", expense.getFromEmployeeID());

        long actualamount = (long) (expense.getTotalAmount());
        params.put("amount", actualamount);

        Integer count = namedParameterJdbcTemplate.queryForObject("select count(*) from ( SELECT  floor(totalamount) " +
                " as totalamount ,id FROM generalexpense where orgid=:orgid and fromemployeeid=:fromemployeeid and (isdeleted is null or isdeleted<> true) " +
                "  order by id desc limit 1) expense where totalamount = :amount", params, Integer.class);
        System.out.println(count);
        if (count > 0) {
            throw new CustomAppException("The created expense looks like duplicate", "SERVER.CREATE_GENERAL_EXPENSE.DUPLICATE_SUSPECT", HttpStatus.CONFLICT);
        }
    }

    public List<Expense> pdfListExpensePaged(long orgId, Collection<SearchParam> searchParams,
                                             String orderBy, int noOfRecordsToShow, int startIndex) {
        if (startIndex == 0) {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgId);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);

        if (ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "id desc";
        }

        List<Expense> expenseList = namedParameterJdbcTemplate.query(
                "select exp.* from generalexpense exp  where exp.orgid=:orgID " +
                        "and exp.includeinreport = true and (exp.isdeleted is null or exp.isdeleted <> true) " +
                        ANPUtils.getWhereClause(searchParams) + " order by  " + orderBy + "  limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new PDFExpenseMapper());


        com.itextpdf.text.List list;
        try {

            String file1 = "f:/";
            Document document = new Document(PageSize.A4, 20, 20, 20, 20);
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            PdfWriter.getInstance(document, new FileOutputStream(file1 + "expense" + (new Date().getTime() / 1000) + ".pdf"));
            // PdfWriter.getInstance(document, new FileOutputStream(file1 +  "purchase" + dateFormat.format(new Date()) + ".pdf" ));
            int index = 0;
            document.open();
            for (Object listIterator : expenseList) {
                list = new com.itextpdf.text.List(false);
                list.setListSymbol("");
                list.add(new ListItem("Name :  "));
                list.add(new ListItem("Date:  " + expenseList.get(index).getDate().toString()));
                list.add(new ListItem("Total Amount:  " + expenseList.get(index).getTotalAmount()));
                list.add(new ListItem("Extra:  " + expenseList.get(index).getExtra()));
                list.add(new ListItem("IGST:  " + expenseList.get(index).getIGST()));
                list.add(new ListItem("CGST:  " + expenseList.get(index).getCGST()));
                list.add(new ListItem("SGST:  " + expenseList.get(index).getSGST()));
                document.add(list);
                document.add(new Paragraph("\n"));
                index++;
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expenseList;
    }

    private static final class PDFExpenseMapper implements RowMapper<Expense> {
        public Expense mapRow(ResultSet rs, int rowNum) throws SQLException {
            Expense obj = new Expense();
            obj.setExpenseId(rs.getInt("exp.id"));
            obj.setCategory(rs.getString("exp.category"));
            obj.setTotalAmount(rs.getFloat("exp.totalamount"));
            obj.setOrgId(rs.getLong("exp.orgid"));
            obj.setIncludeInReport(rs.getBoolean("exp.includeinreport"));
            obj.setCreatedbyId(rs.getString("exp.createdbyid"));
            obj.setOrderAmount(rs.getDouble("exp.orderamount"));
            obj.setCGST(rs.getDouble("exp.cgst"));
            obj.setSGST(rs.getDouble("exp.sgst"));
            obj.setIGST(rs.getDouble("exp.igst"));
            obj.setExtra(rs.getDouble("exp.extra"));
            obj.setToPartyName(rs.getString("exp.topartyname"));
            obj.setDate(rs.getTimestamp("exp.date"));
            obj.setToPartyGSTNO(rs.getString("exp.topartygstno"));
            obj.setToPartyMobileNO(rs.getString("exp.topartymobileno"));
            obj.setPaid(rs.getBoolean("exp.paid"));
            obj.setCreateDate(rs.getTimestamp("exp.createdate"));
            return obj;
        }
    }

    /*
     * also there is big mistake here the current logic will update all the expense for an organization.
     * Always include primary key for the update.
     */

    public void updateExpense(Expense expense) {
        namedParameterJdbcTemplate.update("update generalexpense set cgst = :CGST," +
                        "sgst=:SGST, igst=:IGST, description = :description, date=:date,category=:category, " +
                        "orderamount=:orderAmount, includeinreport=:includeInReport,extra=:extra,topartygstno=:toPartyGSTNO," +
                        "topartymobileno=:toPartyMobileNO where orgid = :orgId and id = :expenseId",
                new BeanPropertySqlParameterSource(expense));
    }

    public Expense getExpenseById(Long orgId, Long expenseId) {
        List<SearchParam> searchParams = new ArrayList<>();
        SearchParam param = new SearchParam();
        param.setCondition("and");
        param.setFieldName("exp.id");
        param.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        param.setSoperator("=");
        param.setValue("" + expenseId);
        searchParams.add(param);
        List<Expense> expenseList = listExpensesPaged(orgId, searchParams, "", 1, 1);
        if (expenseList != null && !expenseList.isEmpty()) {
            return expenseList.get(0);
        }
        return null;
    }
}