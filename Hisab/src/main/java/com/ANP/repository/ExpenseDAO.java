package com.ANP.repository;

import com.ANP.bean.Expense;
import com.ANP.bean.SearchParam;
import com.ANP.util.ANPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ExpenseDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public int createExpense(Expense expense) {
        return namedParameterJdbcTemplate.update(
                "INSERT INTO generalexpense(date,Category,Description,totalamount,toPartyName,orgId,createdById,FromAccountID,fromemployeeid,IncludeInCalc,includeinreport,orderamount,cgst,sgst,igst,extra,topartygstno,topartymobileno)" +
                        "VALUES(:date,:category,:description,:totalAmount,:toPartyName,:orgId,:createdbyId,:FromAccountID,:fromEmployeeID,:IncludeInCalc,:includeInReport,:orderAmount,:CGST,SGST,:IGST,:extra,:toPartyGSTNO,:toPartyMobileNO);",
                new BeanPropertySqlParameterSource(expense));
    }


    public List<Expense> listExpensesPaged(long orgId, Collection<SearchParam> searchParams,
                                           String orderBy, int noOfRecordsToShow, int startIndex) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgId);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);
        param.put("orderBy", orderBy);

        return namedParameterJdbcTemplate.query(
                "select exp.*, e.first,e.last from generalexpense exp, employee e where exp.fromemployeeid=e.id and exp.orgid=:orgID " +
                        ANPUtils.getWhereClause(searchParams) + " order by :orderBy limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new FullExpenseMapper());

    }

    private static final class FullExpenseMapper implements RowMapper<Expense> {
        public Expense mapRow(ResultSet rs, int rowNum) throws SQLException {
            Expense obj = new Expense();
            obj.setExpenseId(rs.getInt("id"));
            obj.setCategory(rs.getString("category"));
            obj.setTotalAmount(rs.getFloat("totalamount"));
            obj.setOrgId(rs.getLong("orgid"));
            obj.setIncludeInCalc(rs.getBoolean("includeincalc"));
            obj.setIncludeInReport(rs.getBoolean("includeinreport"));
            obj.setCreatedbyId(rs.getString("createdbyid"));
            obj.setOrderAmount(rs.getDouble("orderamount"));
            obj.setCGST(rs.getDouble("cgst"));
            obj.setSGST(rs.getDouble("sgst"));
            obj.setIGST(rs.getDouble("igst"));
            obj.setExtra(rs.getDouble("extra"));
            obj.setToPartyGSTNO(rs.getString("topartygstno"));
            obj.setToPartyMobileNO(rs.getString("topartymobileno"));
            obj.setEmpFirstName(rs.getString("first"));
            obj.setEmpLastName(rs.getString("last"));
            //TODO Paras: Please add all expense related fields (other than fromEmployeeID,fromAccountID) please note that you will also be adding
            // e.first and e.last into the expense Bean, I have created two corresponding fields
            // in the Exepense Bean (empFirstName, empLastName)
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

    public List<Expense> findExpenseByToPartyName(String toPartyname, long orgId) {
        return namedParameterJdbcTemplate.query(
                "select topartyname,topartygstno,topartymobileno from generalexpense where orgid=" + orgId
                        + " and topartyname like '%" + toPartyname + "%'",
                new BeanPropertySqlParameterSource(new Expense()), new ExpenseMapperLimited());
    }

    /*
         you need to update Expense Table:IncludeInCAReport field with the passed value
     */
    public boolean updateIncludeInCAReport(long expenseID, boolean caSwitch) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", expenseID);
        parameterSource.addValue("caSwitch", caSwitch);
       if (namedParameterJdbcTemplate.update("update generalexpense set includeinreport = :caSwitch where id = :id", parameterSource)!= 0) {
            return true;
        } else {
            return false;
        }
    }

    /*
          you need to update Expense Table:includeInCalc field with the passed value
    */
    public boolean updateIncludeInCalc(long expenseID,boolean includeInCalcSwtich) {

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", expenseID);
        parameterSource.addValue("includeInCalcSwtich", includeInCalcSwtich);
        if (namedParameterJdbcTemplate.update("update generalexpense set includeincalc = :includeInCalcSwtich where id = :id", parameterSource)!= 0) {
            return true;
        } else {
            return false;
        }
    }
}