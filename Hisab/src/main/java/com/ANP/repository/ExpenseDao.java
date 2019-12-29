package com.ANP.repository;

import com.ANP.bean.Customer;
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
public class ExpenseDao {

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



    public int createExpense(Expense expense) {
            System.out.println("Expense obj "+expense.getToPartyName()+" "+expense.getDate());
        return namedParameterJdbcTemplate.update(
                "INSERT INTO expense(date,Category,Description,amount,toPartyName,orgId,createdById,FromAccountID,ToAccountID,IncludeInCalc)" +
                        "VALUES(:date,:Category,:Description,:amount,:toPartyName,:orgId,:createdById,:FromAccountID,:ToAccountID,:IncludeInCalc);",
                new BeanPropertySqlParameterSource(expense));
}


    public List<Expense> findByNameAndCity(Expense expense) {
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
