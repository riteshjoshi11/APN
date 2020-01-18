package com.ANP.repository;

import com.ANP.bean.CustomerBilling;
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
public class customerBillingDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;



    public List<Expense> getExpenses(){

        List <Expense> list=new ArrayList<Expense>();
        Expense obj1=new Expense();

        obj1.setAmount(101);
        obj1.setOrgId(1);
        obj1.setDate(new Date()   );

        list.add(obj1);
        return list;

    }



    public int createBill(CustomerBilling bill) {
        return namedParameterJdbcTemplate.update(
                "INSERT INTO customer_billing(customerId,date,CGST,amount,SGST,IGST,extra,total,orgId,createdById,note,includeInReport) "+
                        " VALUES(:customerId,:date,:CGST,:amount,:SGST,:IGST,:extra,:total,:orgId,:createdById,:note,:includeInReport); ",
                new BeanPropertySqlParameterSource(bill));
}


    public List<Expense> findExpense(Expense expense) {
        String whereCondition = "";
        if (null != expense.getCategory()) {
            whereCondition = "category = :category";
        } else if (null != expense.getToPartyName()) {
            if(!"".equals(whereCondition)){
                whereCondition=whereCondition+" And ";
            }
            whereCondition = whereCondition +"toPartyName = :toPartyName";
        } else if (0 != expense.getOrgId()) {
            if(!"".equals(whereCondition)){
                whereCondition=whereCondition+" And ";
            }
            whereCondition =whereCondition+ "orgId =:orgId";
        }


        return namedParameterJdbcTemplate.query(
                "select * from expense where " + whereCondition,
                new BeanPropertySqlParameterSource(expense), new ExpenseMapper());



    }

    private static final class ExpenseMapper implements RowMapper<Expense> {
        public Expense mapRow(ResultSet rs, int rowNum) throws SQLException {
            Expense obj = new Expense();
            obj.setId(rs.getInt("id"));
            obj.setCategory(rs.getString("category"));
            obj.setAmount(rs.getFloat("amount"));
            return obj;
        }
    }

    }
