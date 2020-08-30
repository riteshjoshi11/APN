package com.ANP.repository;

import com.ANP.bean.*;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.object.GenericStoredProcedure;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Repository;


import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;


@Repository
/*
    handles Employee, EmployeeSalary, EmployeePayment
 */
public class EmployeeDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private DataSource dataSource;


    public int createEmployee(EmployeeBean employeeBean) {
        try {
            String idSql = "SELECT getEmployeeId() ";
            Map param = new HashMap<String, Object>();
            String id = namedParameterJdbcTemplate.queryForObject(idSql, param, String.class);
            employeeBean.setEmployeeId(id);
            return namedParameterJdbcTemplate.update(
                    "insert into employee (id,first,last,mobile,loginrequired,orgid,type,mobile2) " +
                            " values(:employeeId,:first,:last,:mobile,:loginrequired" +
                            ",:orgId, :typeInt,:mobile2)", new BeanPropertySqlParameterSource(employeeBean));
        } catch (DuplicateKeyException e) {
            throw new CustomAppException("Duplicate Entry", "SERVER.CREATE_EMPLOYEE.DUPLICATE", HttpStatus.EXPECTATION_FAILED);
        }
    }


    public void createEmployeeSalary(EmployeeSalary employeeSalaryBean) {
        if (!employeeSalaryBean.isForceCreate()) {
            this.isDuplicateSalaryDueSuspect(employeeSalaryBean);
        }
        namedParameterJdbcTemplate.update("insert into employeesalary(toemployeeid,amount,details,orgid,createdbyid,includeincalc) " +
                        "values(:toEmployeeID,:amount,:details,:orgId,:createdbyId,:includeInCalc)",
                new BeanPropertySqlParameterSource(employeeSalaryBean));
    }

    public boolean createEmployeeSalaryPayment(EmployeeSalaryPayment employeeSalaryPaymentBean) {

        if (!employeeSalaryPaymentBean.isForceCreate()) {
            this.isDuplicatePaySalarySuspect(employeeSalaryPaymentBean);
        }

        if (namedParameterJdbcTemplate.update("insert into employeesalarypayment(fromaccountid," +
                "amount,details,toemployeeid,fromemployeeid,orgid,includeincalc,createdbyid) values(:fromAccountId," +
                ":amount" +
                ",:details,:toEmployeeId,:fromEmployeeId,:orgId,:includeInCalc,:createdbyId)", new BeanPropertySqlParameterSource
                (employeeSalaryPaymentBean)) != 0)
            return true;
        else
            return false;
    }

    /*
        This method will mainly used by UI to search for employee in the Employee module
        orgID: mandatory: if not provided return error
        firstName: can be empty
        lastName: can be empty
        Main Logic:
        -------------
        (Search at-least by orgId) AND (if firstName OR lastName) provided then try to search using those as well. Please do not forget to use %LIKE% for names
        Return: EmployeeBean with only EmployeeID, First, LastName populated (Nothing else, for the optimization we are doing this)
     */

    public List<EmployeeBean> searchEmployees(EmployeeBean employeeBean) {
        if (employeeBean.getOrgId() <= 0) {
            throw new CustomAppException("orgId is mandatory", "SERVER.SEARCH_EMPLOYEE.NOTAVAILABLE", HttpStatus.EXPECTATION_FAILED);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("orgid", employeeBean.getOrgId());
        List<EmployeeBean> employeeBeanList = namedParameterJdbcTemplate.query("select first,last,id from employee " +
                " where orgid = :orgid", params, new EmployeeDAO.EmployeeMapper());
        return employeeBeanList;
    }


    private static final class EmployeeMapper implements RowMapper<EmployeeBean> {
        public EmployeeBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeBean empbean = new EmployeeBean();
            empbean.setFirst(rs.getString("first"));
            empbean.setLast(rs.getString("last"));
            empbean.setEmployeeId(rs.getString("id"));
            return empbean;
        }
    }


    public List<EmployeeBean> listEmployeesWithBalancePaged(long orgID, Collection<SearchParam> searchParams,
                                                            String orderBy, int noOfRecordsToShow, int startIndex) {
        if (startIndex == 0) {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);
        if (ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "e.first,e.last desc";
        }
        return namedParameterJdbcTemplate.query("select e.*, acc.currentbalance, acc.id, " +
                        " (select empt.`name` from employeetype empt where e.type = empt.id) as emptype " +
                        " from employee e,account acc where e.id=acc.ownerid and e.orgid=:orgID " +
                        ANPUtils.getWhereClause(searchParams) + " order by  " + orderBy + "  limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new FullEmployeeMapper());
    }

    private static final class FullEmployeeMapper implements RowMapper<EmployeeBean> {
        public EmployeeBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeBean empbean = new EmployeeBean();
            empbean.setFirst(rs.getString("e.first"));
            empbean.setLast(rs.getString("e.last"));
            empbean.setEmployeeId(rs.getString("e.id"));
            empbean.setMobile(rs.getString("e.mobile"));
            empbean.setLoginrequired(rs.getBoolean("e.loginrequired"));
            empbean.setType(rs.getString("emptype"));
            empbean.setCurrentsalarybalance(rs.getFloat("e.currentsalarybalance"));
            empbean.setCurrentAccountBalance(rs.getFloat("acc.currentbalance"));
            empbean.setAccountId(rs.getLong("acc.id"));
            //empbean.setCreateDate(rs.getTimestamp("e.createdate"));
            empbean.setMobile2(rs.getString("e.mobile2"));
            return empbean;
        }
    }//end FullEmployeeMapper


    public List<EmployeeSalary> listEmpSalariesPaged(long orgID, Collection<SearchParam> searchParams,
                                                     String orderBy, int noOfRecordsToShow, int startIndex) {
        if (startIndex == 0) {
            startIndex = 1;

        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgid", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);

        if (ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "empsal.id desc";
        }

        return namedParameterJdbcTemplate.query("select e.id, e.first, e.last, e.mobile, e.type, empsal.amount," +
                        " empsal.details, empsal.includeincalc,empsal.createdate,empsal.createdbyid " +
                        " from employee e,employeesalary empsal where e.id=empsal.toemployeeid and e.orgid=:orgid " +
                        " and (empsal.isdeleted is null or  empsal.isdeleted <> true) " +
                        ANPUtils.getWhereClause(searchParams) + " order by  " + orderBy + "  limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new FullEmployeeSalaryMapper());
    }


    private static final class FullEmployeeSalaryMapper implements RowMapper<EmployeeSalary> {
        public EmployeeSalary mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeSalary employeeSalary = new EmployeeSalary();
            employeeSalary.getEmployeeBean().setFirst(rs.getString("e.first"));
            employeeSalary.getEmployeeBean().setLast(rs.getString("e.last"));
            employeeSalary.getEmployeeBean().setEmployeeId(rs.getString("e.id"));
            employeeSalary.getEmployeeBean().setMobile(rs.getString("e.mobile"));
            //      employeeSalary.getEmployeeBean().setType(rs.getString("e.type"));
            employeeSalary.setAmount(rs.getFloat("empsal.amount"));
            employeeSalary.setDetails(rs.getString("empsal.details"));
            employeeSalary.setIncludeInCalc(rs.getBoolean("empsal.includeincalc"));
            employeeSalary.setCreateDate(rs.getTimestamp("empsal.createdate"));
            employeeSalary.setCreatedbyId(rs.getString("empsal.createdbyid"));
            return employeeSalary;
        }
    }//end

    /*
     *
     */
    public List<EmployeeSalaryPayment> listEmpPaidSalariesPaged(long orgID, Collection<SearchParam> searchParams,
                                                                String orderBy, int noOfRecordsToShow, int startIndex) {
        if (startIndex == 0) {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("orgid", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);

        if (ANPUtils.isNullOrEmpty(orderBy)) {
            orderBy = "empsalpay.id desc";
        }
        List<EmployeeSalaryPayment> EmployeeSalaryPaymentlist = namedParameterJdbcTemplate.query("select e.id, e.first, e.last, e.mobile, e.type, empsalpay.amount, " +
                        "empsalpay.details,empsalpay.includeincalc,empsalpay.transferdate,empsalpay.createdate,empsalpay.createdbyid,empsalpay.fromemployeeid," +
                        "(select first from employee emp where emp.id=empsalpay.fromemployeeid and emp.orgid=:orgid) as fromEmpFirstName," +
                        "(select last from employee emp where emp.id=empsalpay.fromemployeeid and emp.orgid=:orgid) as fromEmpLastName" +
                        " from employee e, employeesalarypayment empsalpay " +
                        "where e.id=empsalpay.toemployeeid and e.orgid=:orgid and (empsalpay.isdeleted is null or empsalpay.isdeleted <> true) " +
                        ANPUtils.getWhereClause(searchParams) + " order by  " + orderBy + "  limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new FullEmployeeSalaryPayment());
        return EmployeeSalaryPaymentlist;
    }

    private static final class FullEmployeeSalaryPayment implements RowMapper<EmployeeSalaryPayment> {
        public EmployeeSalaryPayment mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeSalaryPayment employeeSalaryPayment = new EmployeeSalaryPayment();
            employeeSalaryPayment.getToEmployeeBean().setFirst(rs.getString("e.first"));
            employeeSalaryPayment.getToEmployeeBean().setLast(rs.getString("e.last"));
            employeeSalaryPayment.getToEmployeeBean().setEmployeeId(rs.getString("e.id"));
            employeeSalaryPayment.getToEmployeeBean().setMobile(rs.getString("e.mobile"));
            employeeSalaryPayment.setAmount(rs.getFloat("empsalpay.amount"));
            employeeSalaryPayment.setDetails(rs.getString("empsalpay.details"));
            employeeSalaryPayment.setIncludeInCalc(rs.getBoolean("empsalpay.includeincalc"));
            employeeSalaryPayment.getFromEmployeeBean().setEmployeeId(rs.getString("empsalpay.fromemployeeid"));
            employeeSalaryPayment.getFromEmployeeBean().setFirst(rs.getString("fromEmpFirstName"));
            employeeSalaryPayment.getFromEmployeeBean().setLast(rs.getString("fromEmpLastName"));
            employeeSalaryPayment.setTransferDate(rs.getTimestamp("empsalpay.transferdate"));
            employeeSalaryPayment.setCreateDate(rs.getTimestamp("empsalpay.createdate"));
            employeeSalaryPayment.setCreatedbyId(rs.getString("empsalpay.createdbyid"));
            return employeeSalaryPayment;
        }
    }

    private void isDuplicatePaySalarySuspect(EmployeeSalaryPayment employeeSalaryPayment) {
        //Do a count(*) query and if you found count>0 then throw this error else nothing
        Map<String, Object> params = new HashMap<>();
        params.put("orgid", employeeSalaryPayment.getOrgId());
        params.put("toemployeeid", employeeSalaryPayment.getToEmployeeId());

        long actualamount = (long) (employeeSalaryPayment.getAmount());
        params.put("amount", actualamount);

        Integer count = namedParameterJdbcTemplate.queryForObject("select count(*) from ( SELECT  floor(amount) as amount ," +
                " id FROM employeesalarypayment where orgid=:orgid and toemployeeid=:toemployeeid and (isdeleted is null or isdeleted<> true) " +
                " and createdate >= date(now()) + interval -5 day   order by id desc limit 1) paysalary where amount = :amount", params, Integer.class);
        System.out.println(count);
        if (count > 0) {
            throw new CustomAppException("The Salary payment like duplicate", "SERVER.CREATE_SALARY_PAYMENT.DUPLICATE_SUSPECT", HttpStatus.CONFLICT);
        }
    }


    private void isDuplicateSalaryDueSuspect(EmployeeSalary employeeSalary) {
        //Do a count(*) query and if you found count>0 then throw this error else nothing
        Map<String, Object> params = new HashMap<>();
        params.put("orgid", employeeSalary.getOrgId());
        params.put("toemployeeid", employeeSalary.getToEmployeeID());

        long actualamount = (long) (employeeSalary.getAmount());
        params.put("amount", actualamount);

        Integer count = namedParameterJdbcTemplate.queryForObject("select count(*) from ( SELECT  floor(amount) as amount , " +
                " id FROM employeesalary where orgid=:orgid and toemployeeid=:toemployeeid and (isdeleted is null or isdeleted<> true) " +
                " and createdate >= date(now()) + interval -5 day   order by id desc limit 1) salarydue where amount = :amount", params, Integer.class);
        System.out.println(count);
        if (count > 0) {
            throw new CustomAppException("The Salary Due looks like duplicate", "SERVER.CREATE_SALARY_DUE.DUPLICATE_SUSPECT", HttpStatus.CONFLICT);
        }
    }

    public List<AccountBean> getEmployeeAccountsByNames(AccountBean accountBean) {
        if (accountBean.getOrgId() <= 0) {
            throw new CustomAppException("orgId is mandatory", "SERVER.GET_EMPLOYEE_ACCOUNT.NOTAVAILABLE", HttpStatus.EXPECTATION_FAILED);
        }
        System.out.println(accountBean.getOrgId());
        String orgId = Long.toString(accountBean.getOrgId());
        String nickname = accountBean.getAccountnickname();
        return namedParameterJdbcTemplate.getJdbcTemplate().query("select accountnickname,ownerid,id from account where type = 'Employee' and orgid = ? and (accountnickname" +
                        " like ?)  ", new String[]{orgId, "%" + nickname + "%"}
                , new EmployeeDAO.AccByNickMapper());

    }

    private static final class AccByNickMapper implements RowMapper<AccountBean> {
        public AccountBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            AccountBean accountBean = new AccountBean();
            accountBean.setOwnerid(rs.getString("ownerid"));
            accountBean.setAccountnickname(rs.getString("accountnickname"));
            accountBean.setAccountId(rs.getLong("id"));
            return accountBean;
        }
    }

    /*
    * This method is single update method to handle all the updates
     */
    public void updateEmployee(EmployeeBean employeeBean) {
        if (employeeBean.getTypeInt() <= 0) {
            throw new CustomAppException("Employee Type cannot be 0 or blank", "SERVER.UPDATE_EMPLOYEE.NULLVALUE", HttpStatus.EXPECTATION_FAILED);
        }
        namedParameterJdbcTemplate.update("update employee set first = :first," +
                "last=:last, loginrequired = :loginrequired, type = :typeInt, mobile2 =:mobile2 " +
                " where orgid = :orgId and id = :employeeId", new BeanPropertySqlParameterSource(employeeBean));
    }


    public  EmployeeBean getEmployeeById(Long orgId, String employeeId) {
        List<SearchParam> searchParams = new ArrayList<>();
        SearchParam param = new SearchParam();
        param.setCondition("and");
        param.setFieldName("e.id");
        param.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        param.setSoperator("=");
        param.setValue(employeeId);
        searchParams.add(param);
        List<EmployeeBean> employeeList = listEmployeesWithBalancePaged(orgId, searchParams,"",1,1);
        if (employeeList!=null && !employeeList.isEmpty()) {
            return employeeList.get(0);
        }
        return null;
    }

    public void updateEmployeeSalaryBalance(EmployeeAuditBean employeeAuditBean) {
        StoredProcedure procedure = new GenericStoredProcedure();
        procedure.setDataSource(dataSource);
        procedure.setSql("UpdateEmployeeSalaryBalanceWithAudit_Procedure");
        procedure.setFunction(false);
        SqlParameter[] declareparameters = {
                new SqlParameter("employeeid", Types.VARCHAR),
                new SqlParameter("orgid",Types.BIGINT),
                new SqlParameter("amount",Types.FLOAT),
                new SqlParameter("otherparty", Types.VARCHAR),
                new SqlParameter("txntype",Types.VARCHAR),
                new SqlParameter("operation",Types.VARCHAR),
                new SqlParameter("txndate",Types.DATE)
        };

        procedure.setParameters(declareparameters);
        procedure.compile();
        System.out.println(employeeAuditBean.getEmployeeid() + ","
                +  employeeAuditBean.getAmount() + ","
                +  employeeAuditBean.getType() + ","
                +  employeeAuditBean.getOperation());

        Map<String, Object> result = procedure.execute(
                employeeAuditBean.getEmployeeid(),
                employeeAuditBean.getOrgId(),
                employeeAuditBean.getAmount(),
                employeeAuditBean.getOtherPartyName(),
                employeeAuditBean.getType(),
                employeeAuditBean.getOperation(),
                employeeAuditBean.getTransactionDate()
        );
        System.out.println("Status " + result);
    }
}