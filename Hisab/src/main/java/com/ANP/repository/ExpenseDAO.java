package com.ANP.repository;

import com.ANP.bean.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Repository
public class ExpenseDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public List<Expense> getExpenses() {

        List<Expense> list = new ArrayList<Expense>();
        Expense obj1 = new Expense();

        obj1.setTotalAmount(101);
        obj1.setOrgId(1);
        obj1.setDate(new Date());

        list.add(obj1);
        return list;

    }


    public int createExpense(Expense expense) {
        return namedParameterJdbcTemplate.update(
                "INSERT INTO generalexpense(date,Category,Description,totalamount,toPartyName,orgId,createdById,FromAccountID,fromemployeeid,IncludeInCalc,includeinreport,orderamount,cgst,sgst,igst,extra,topartygstno,topartymobileno)" +
                        "VALUES(:date,:category,:description,:totalAmount,:toPartyName,:orgId,:createdById,:FromAccountID,:fromEmployeeID,:IncludeInCalc,:includeInReport,:orderAmount,:CGST,SGST,:IGST,:extra,:toPartyGSTNO,:toPartyMobileNO);",
                new BeanPropertySqlParameterSource(expense));
    }


    public List<Expense> findExpense(Expense expense) {
        String whereCondition = "";
        if (null != expense.getCategory()) {
            whereCondition = "category = :category";
        } else if (null != expense.getToPartyName()) {
            if (!"".equals(whereCondition)) {
                whereCondition = whereCondition + " And ";
            }
            whereCondition = whereCondition + "toPartyName = :toPartyName";
        } else if (0 != expense.getOrgId()) {
            if (!"".equals(whereCondition)) {
                whereCondition = whereCondition + " And ";
            }
            whereCondition = whereCondition + "orgId =:orgId";
        }


        return namedParameterJdbcTemplate.query(
                "select * from expense where " + whereCondition,
                new BeanPropertySqlParameterSource(expense), new ExpenseMapper());


    }

    private static final class ExpenseMapper implements RowMapper<Expense> {
        public Expense mapRow(ResultSet rs, int rowNum) throws SQLException {
            Expense obj = new Expense();
            obj.setExpenseId(rs.getInt("id"));
            obj.setCategory(rs.getString("category"));
            obj.setTotalAmount(rs.getFloat("amount"));
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
                "select topartyname,topartygstno,topartymobileno from expense where orgid=" + orgId
                        + "and topartyname like %' + toPartyname + '%",
                new BeanPropertySqlParameterSource(new Expense()), new ExpenseMapperLimited());
    }

    /*
         TODO Paras: Please complete this method.
         you need to update Expense Table:IncludeInCAReport field with the passed value
     */
    public boolean updateIncludeInCAReport(long expenseID, boolean caSwitch) {
        return false;
    }

    /*
          TODO Paras: Please complete this method.
          you need to update Expense Table:includeInCalc field with the passed value
    */
    public boolean updateIncludeInCalc(long expenseID,boolean includeInCalcSwtich) {
        return false;
    }


}
