package com.ANP.repository;

import com.ANP.bean.PurchaseFromVendorBean;
import com.ANP.bean.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class PurchaseFromVendorDAO {

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


    /*
        TODO: Joshi i have corrected this method, you need to run the latest 29April version of DB patch
        No need to change anything here.
     */
    public int createBill(PurchaseFromVendorBean purchaseFromVendorBean) {
        return namedParameterJdbcTemplate.update(
                "INSERT INTO purchasefromvendor(fromcustomerId,date,CGST,orderamount,SGST,IGST,extra,totalamount,orgId,createdById,note,includeInReport,includeincalc,fromaccountid,billno) " +
                        " VALUES(:fromCustomerId,:date,:CGST,:orderAmount,:SGST,:IGST,:extra,:totalAmount,:orgId,:createdById,:note,:includeInReport,includeInCalc,fromAccountId,billNo); ",
                new BeanPropertySqlParameterSource(purchaseFromVendorBean));
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

}
