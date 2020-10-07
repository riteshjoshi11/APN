package com.ANP.repository;

import com.ANP.bean.CustomerAuditBean;
import com.ANP.bean.EmployeeAuditBean;
import com.ANP.bean.SearchParam;
import com.ANP.util.ANPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class EmployeeAuditDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<EmployeeAuditBean> listEmployeeAudit(long orgID, List<SearchParam> searchParams, String orderBy, int noOfRecordsToShow, int startIndex) {
        if (startIndex == 0) {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);
        if(ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "empau.id desc";
        }
        return namedParameterJdbcTemplate.query("select empau.* , e.first, e.last " +
                        "from employee e, employeeaudit empau where empau.employeeid = e.id and empau.orgid=:orgID and " +
                        " (empau.isdeleted is null or empau.isdeleted <> true) " +
                        ANPUtils.getWhereClause(searchParams) + " order by "+ orderBy+" limit  :noOfRecordsToShow" + " offset :startIndex",
                param, new EmployeeAuditMapper());

    }

    private static final class EmployeeAuditMapper implements RowMapper<EmployeeAuditBean> {
        public EmployeeAuditBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
            employeeAuditBean.setAuditId(rs.getLong("empau.id"));
            employeeAuditBean.setEmployeeid(rs.getString("empau.employeeid"));
            employeeAuditBean.setAccountid(rs.getLong("empau.accountid"));
            employeeAuditBean.setType(rs.getString("empau.type"));
            employeeAuditBean.setAmount(rs.getBigDecimal("empau.amount"));
            employeeAuditBean.setOperation(rs.getString("empau.operation"));
            employeeAuditBean.setOtherPartyName(rs.getString("empau.otherparty"));
            employeeAuditBean.setNewbalance(rs.getBigDecimal("empau.newbalance"));
            employeeAuditBean.setPreviousbalance(rs.getBigDecimal("empau.previousbalance"));
            employeeAuditBean.setForWhat(rs.getString("empau.forwhat"));
            employeeAuditBean.getEmployeeBean().setFirst(rs.getString("e.first"));
            employeeAuditBean.getEmployeeBean().setLast(rs.getString("e.last"));
            employeeAuditBean.setOrgId(rs.getLong("empau.orgid"));
            employeeAuditBean.setTransactionDate(rs.getTimestamp("empau.txndate"));
            employeeAuditBean.setCreateDate(rs.getTimestamp("empau.date"));
            return employeeAuditBean;
        }
    }

    public List<EmployeeAuditBean> listEmployeeSalaryAudit(long orgID, List<SearchParam> searchParams, String orderBy, int noOfRecordsToShow, int startIndex) {
        if (startIndex == 0) {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);
        if(ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "empau.id desc";
        }
        return namedParameterJdbcTemplate.query("select empau.* , e.first, e.last " +
                        "from employee e, employee_salary_audit empau where empau.employeeid = e.id and empau.orgid=:orgID and " +
                        " (empau.isdeleted is null or empau.isdeleted <> true) " +
                        ANPUtils.getWhereClause(searchParams) + " order by "+ orderBy+" limit  :noOfRecordsToShow" + " offset :startIndex",
                param, new EmployeeSalaryAuditMapper());

    }


    private static final class EmployeeSalaryAuditMapper implements RowMapper<EmployeeAuditBean> {
        public EmployeeAuditBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
            employeeAuditBean.setAuditId(rs.getLong("empau.id"));
            employeeAuditBean.setEmployeeid(rs.getString("empau.employeeid"));
            employeeAuditBean.setType(rs.getString("empau.type"));
            employeeAuditBean.setAmount(rs.getBigDecimal("empau.amount"));
            employeeAuditBean.setOperation(rs.getString("empau.operation"));
            employeeAuditBean.setOtherPartyName(rs.getString("empau.otherparty"));
            employeeAuditBean.setNewbalance(rs.getBigDecimal("empau.newbalance"));
            employeeAuditBean.setPreviousbalance(rs.getBigDecimal("empau.previousbalance"));
            employeeAuditBean.getEmployeeBean().setFirst(rs.getString("e.first"));
            employeeAuditBean.getEmployeeBean().setLast(rs.getString("e.last"));
            employeeAuditBean.setOrgId(rs.getLong("empau.orgid"));
            employeeAuditBean.setTransactionDate(rs.getTimestamp("empau.txndate"));
            return employeeAuditBean;
        }
    }
}