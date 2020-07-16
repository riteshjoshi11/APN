package com.ANP.repository;

import com.ANP.bean.*;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
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
import java.util.ArrayList;
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
        } catch (DuplicateKeyException e) {
            throw new CustomAppException("Duplicate Entry", "SERVER.CREATE_CITY.DUPLICATE", HttpStatus.EXPECTATION_FAILED);
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

    public List<UIItem> getEmployeeType() {
        //return namedParameterJdbcTemplate.query("select * from employeetype", new EmployeeTypeMapper());
        List<SearchParam> searchParamList = new ArrayList<>();
        SearchParam searchParam = new SearchParam();
        searchParam.setCondition("");
        searchParam.setFieldName("name");
        searchParam.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        searchParam.setSoperator("<>");
        searchParam.setValue("SUPER_ADMIN");


        SearchParam searchParam1 = new SearchParam();
        searchParam1.setCondition("and");
        searchParam1.setFieldName("name");
        searchParam1.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        searchParam1.setSoperator("<>");
        searchParam1.setValue("VIRTUAL");

        SearchParam searchParam2 = new SearchParam();
        searchParam2.setCondition("and");
        searchParam2.setFieldName("name");
        searchParam2.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        searchParam2.setSoperator("<>");
        searchParam2.setValue("default_employee");

        searchParamList.add(searchParam);
        searchParamList.add(searchParam1);
        searchParamList.add(searchParam2);

        return getUIItemListForATable(ANPConstants.DB_TBL_UI_OBJ_EMPLOYEE_TYPE,searchParamList);
    }
/*
    private static final class EmployeeTypeMapper implements RowMapper<EmployeeType> {
        public EmployeeType mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeType employeeType = new EmployeeType();
            employeeType.setId(rs.getLong("id"));
            employeeType.setName((rs.getString("name")));
            return employeeType;
        }
    }
*/
    //OrgDetailsUIBean with three lists
    public OrgDetailsUIBean getOrgDetailsUILists() {
        OrgDetailsUIBean orgDetailsUIBean = new OrgDetailsUIBean();
        //Get the list one by one and set on the orgDetailsBean

        orgDetailsUIBean.setCompanyTypeList(getUIItemListForATable(ANPConstants.DB_TBL_UI_OBJ_COMAPANYTYPE,null));
        orgDetailsUIBean.setBusinessNatureList(getUIItemListForATable(ANPConstants.DB_TBL_UI_OBJ_BUSINESS_NATURE,null));
        orgDetailsUIBean.setNoOfEmployeeList(getUIItemListForATable(ANPConstants.DB_TBL_UI_OBJ_NOOFEMPLOYEES,null));

        return orgDetailsUIBean;
    }

    private List<UIItem> getUIItemListForATable(String tableName, List<SearchParam> searchParams) {
        String sql = "select id,name from " + tableName ;
        if(searchParams != null) {
            sql = "select id, name from " + tableName +" where " + ANPUtils.getWhereClause(searchParams);
        }
            return namedParameterJdbcTemplate.query( sql , new OrgSystemUIMapper());
    }

    private static final class OrgSystemUIMapper implements RowMapper<UIItem> {
        public UIItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            UIItem uiItem = new UIItem();
            uiItem.setUiItemCode(rs.getString("id"));
            uiItem.setUiItemName(rs.getString("name"));
            return uiItem;
        }
    }
}
