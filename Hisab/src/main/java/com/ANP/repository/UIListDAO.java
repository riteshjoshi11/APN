package com.ANP.repository;

import com.ANP.bean.City;
import com.ANP.bean.EmployeeType;
import com.ANP.bean.ExpenseCategory;
import com.ANP.bean.OrgDetailsUIBean;
import com.ANP.util.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UIListDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<City> getCity() {
        return namedParameterJdbcTemplate.query(
                "select * from city ", new CityMapper());
    }

    public int createCity(City city) {
        try {
        return namedParameterJdbcTemplate.update(
                "insert into city (name) values(:name)",
                new BeanPropertySqlParameterSource(city));
        }
        catch (DuplicateKeyException e) {
            throw new CustomAppException("Duplicate Entry","SERVER.CREATE_CITY.DUPLICATE", HttpStatus.EXPECTATION_FAILED);
        }
    }

    private static final class CityMapper implements RowMapper<City> {
        public City mapRow(ResultSet rs, int rowNum) throws SQLException {
            City obj = new City();
            obj.setId(rs.getLong("id"));
            obj.setName(rs.getString("name"));
            return obj;
        }
    }

    public List<ExpenseCategory> getExpenseCategory() {
        return namedParameterJdbcTemplate.query(
                "select * from expense_cat ", new ExpenseCatMapper());
    }

    public int createExpenseCat(ExpenseCategory expenseCategory) {
        return namedParameterJdbcTemplate.update(
                "insert into expense_cat (name) values(:name)",
                new BeanPropertySqlParameterSource(expenseCategory));
    }

    private static final class ExpenseCatMapper implements RowMapper<ExpenseCategory> {
        public ExpenseCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
            ExpenseCategory obj = new ExpenseCategory();
            obj.setName(rs.getString("name"));
            return obj;
        }
    }

    public List<EmployeeType> getEmployeeType() {
        return namedParameterJdbcTemplate.query("select * from employeetype", new EmployeeTypeMapper());
    }

    private static final class EmployeeTypeMapper implements RowMapper<EmployeeType> {
        public EmployeeType mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeType employeeType = new EmployeeType();
            employeeType.setId(rs.getLong("id"));
            employeeType.setName((rs.getString("name")));
            return employeeType;
        }
    }

    //please fill OrgDetailsUIBean with three lists
    public OrgDetailsUIBean getOrgDetailsUILists() {
        return null ;
    }
}
