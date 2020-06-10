package com.ANP.repository;

import com.ANP.bean.*;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;


@Repository
/*
    handles Employee, EmployeeSalary, EmployeePayment

 */
public class EmployeeDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public int createEmployee(EmployeeBean employeeBean) {
        try {
            String idSql = "SELECT getEmployeeId() ";
            Map param = new HashMap<String, Object>();
            String id = namedParameterJdbcTemplate.queryForObject(idSql, param, String.class);
            employeeBean.setEmployeeId(id);
            return  namedParameterJdbcTemplate.update(
                    "insert into employee (id,first,last,mobile,loginrequired,loginusername,currentsalarybalance" +
                            ",lastsalarybalance,orgid,type) values(:employeeId,:first,:last,:mobile,:loginrequired,:loginusername" +
                            ",:currentsalarybalance,:lastsalarybalance,:orgId, :type)", new BeanPropertySqlParameterSource(employeeBean));
        }
        catch (DuplicateKeyException e) {
            throw new CustomAppException("Duplicate Entry","SERVER.CREATE_EMPLOYEE.DUPLICATE", HttpStatus.EXPECTATION_FAILED);
        }
        }

    public boolean updateLoginRequired(String employeeId, boolean loginRequired) {
        //TODO Joshi: Update loginRequired attribute for employeeID passed
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("id", employeeId);
        in.addValue("loginrequired", loginRequired);
        if (namedParameterJdbcTemplate.update("update employee set loginrequired = :loginrequired where id = :id", in) != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateMobile(String employeeID, String mobile) {
        //TODO Joshi: Update mobile attribute for employeeID passed
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("id", employeeID);
        in.addValue("mobile", mobile);
        if (namedParameterJdbcTemplate.update("update employee set mobile = :mobile where id = :id", in) != 0)
            return true;
        else
            return false;

    }

    //operation values(ADD,SUBTRACT)
    public boolean UpdateEmpSalaryBalance(String toEmployeeID, double balance, String operation) {
        int updateSuccess;
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("toemployeeid", toEmployeeID);
        mapSqlParameterSource.addValue("balance", balance);
        if (operation.equals("ADD"))
            updateSuccess = namedParameterJdbcTemplate.update("update employee set lastsalarybalance = currentsalarybalance, " +
                    "currentsalarybalance = currentsalarybalance + :balance where id = :toemployeeid", mapSqlParameterSource);
        else
            updateSuccess = namedParameterJdbcTemplate.update("update employee set lastsalarybalance = currentsalarybalance, " +
                    "currentsalarybalance = currentsalarybalance - :balance where id = :toemployeeid", mapSqlParameterSource);
        if (updateSuccess != 0)
            return true;
        else
            return false;
        //Overall Effect: Employee:CurrentSalaryBalance = Employee:CurrentSalaryBalance 'operation(ADD/SUBTRACT)' balance
        //Please note there is a mysql trigger running which is copying old value of Employee:CurrentSalaryBalance to Employee:LastSalaryBalance for audit purpose

    }

    public boolean createEmployeeSalary(EmployeeSalary employeeSalaryBean) {
        //TODO Joshi: Create a employee here
        if (namedParameterJdbcTemplate.update(
                "insert into employeesalary(toemployeeid,amount,details,orgid,createdbyid,includeincalc) " +
                        "values(:toEmployeeID,:amount,:details,:orgId,:createdbyId,:includeInCalc)",
                new BeanPropertySqlParameterSource(employeeSalaryBean)) != 0)
            return true;
        else
            return false;
    }

    public boolean createEmployeeSalaryPayment(EmployeeSalaryPayment employeeSalaryPaymentBean) {
        //TODO Joshi: Create a employee here
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

        if(employeeBean.getOrgId()==0)
            throw new java.lang.RuntimeException("orgId is mandatory"); //we are throwing an error when orgId is not submitted
        System.out.println(employeeBean.getOrgId());
        String orgId = Long.toString(employeeBean.getOrgId());
        String firstName = employeeBean.getFirst();
        String lastName = employeeBean.getLast();
        if(firstName==null)
            firstName = "";
        if(lastName==null)
            lastName = "";
        List<EmployeeBean> employeeBeanList=
                jdbcTemplate.query("select employee.first,employee.last,employee.id,account.id from employee, account " +
                                " where  employee.id = account.ownerid and employee.orgid = ? and (employee.first" +
                                " like ? or employee.last like ?)  ",
                        new String[]{orgId,"%"+firstName+"%","%"+lastName+"%"}
                        ,new EmployeeDAO.EmployeeMapper());
        return employeeBeanList;
    }



    private static final class EmployeeMapper implements RowMapper<EmployeeBean> {
        public EmployeeBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeBean empbean = new EmployeeBean();
            empbean.setFirst(rs.getString("first"));
            empbean.setLast(rs.getString("last"));
            empbean.setEmployeeId(rs.getString("id"));
            empbean.setAccountId(rs.getLong("account.id"));
 //           empbean.setOrgId(rs.getLong("orgid"));
            return empbean;
        }
    }


    public List<EmployeeBean> listEmployeesWithBalancePaged(long orgID, Collection<SearchParam> searchParams,
                                                            String orderBy, int noOfRecordsToShow, int startIndex) {
        if(startIndex == 0)
        {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow",noOfRecordsToShow);
        param.put("startIndex",startIndex-1);

        if(ANPUtils.isNullOrEmpty(orderBy)) {
            param.put("orderBy", "id desc");
        } else {
            param.put("orderBy", orderBy);
        }


        return namedParameterJdbcTemplate.query("select e.*, acc.currentbalance " +
                        " from employee e,account acc where e.id=acc.ownerid and e.orgid=:orgID " +
                        ANPUtils.getWhereClause(searchParams) + " order by :orderBy limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new FullEmployeeMapper()) ;
    }


    private static final class FullEmployeeMapper implements RowMapper<EmployeeBean> {
        public EmployeeBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeBean empbean = new EmployeeBean();
            empbean.setFirst(rs.getString("e.first"));
            empbean.setLast(rs.getString("e.last"));
            empbean.setEmployeeId(rs.getString("e.id"));
            empbean.setMobile(rs.getString("e.mobile"));
            empbean.setLoginrequired(rs.getBoolean("e.loginrequired"));
            empbean.setLoginusername(rs.getString("e.loginusername"));
            empbean.setType(rs.getString("e.type"));
            empbean.setCurrentsalarybalance(rs.getFloat("e.currentsalarybalance"));
            empbean.setCurrentAccountBalance(rs.getFloat("acc.currentbalance"));
            return empbean;
        }
    }//end FullEmployeeMapper


    public List<EmployeeSalary> listEmpSalariesPaged(long orgID, Collection<SearchParam> searchParams,
                                                            String orderBy, int noOfRecordsToShow, int startIndex) {
        if(startIndex == 0)
        {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgid", orgID);
        param.put("noOfRecordsToShow",noOfRecordsToShow);
        param.put("startIndex",startIndex-1);

        if(ANPUtils.isNullOrEmpty(orderBy)) {
            param.put("orderBy", "id desc");
        } else {
            param.put("orderBy", orderBy);
        }


        return namedParameterJdbcTemplate.query("select e.id, e.first, e.last, e.mobile, e.type, empsal.amount," +
                        " empsal.details, empsal.includeincalc,empsal.createdate " +
                        " from employee e,employeesalary empsal where e.id=empsal.toemployeeid and e.orgid=:orgid " +
                          ANPUtils.getWhereClause(searchParams) + " order by :orderBy limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new FullEmployeeSalaryMapper()) ;
    }


    private static final class FullEmployeeSalaryMapper implements RowMapper<EmployeeSalary> {
        public EmployeeSalary mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeSalary employeeSalary = new EmployeeSalary();
            employeeSalary.getEmployeeBean().setFirst(rs.getString("e.first"));
            employeeSalary.getEmployeeBean().setLast(rs.getString("e.last"));
            employeeSalary.getEmployeeBean().setEmployeeId(rs.getString("e.id"));
            employeeSalary.getEmployeeBean().setMobile(rs.getString("e.mobile"));
            employeeSalary.getEmployeeBean().setType(rs.getString("e.type"));
            employeeSalary.setAmount(rs.getFloat("empsal.amount"));
            employeeSalary.setDetails(rs.getString("empsal.details"));
            employeeSalary.setIncludeInCalc(rs.getBoolean("empsal.includeincalc"));
            employeeSalary.setCreateDate(rs.getDate("empsal.createdate"));
            return employeeSalary;
        }
    }//end

    /*
    *
     */
    public List<EmployeeSalaryPayment> listEmpPaidSalariesPaged(long orgID, Collection<SearchParam> searchParams,
                                                                String orderBy, int noOfRecordsToShow, int startIndex) {
        if(startIndex == 0)
        {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("orgid", orgID);
        param.put("noOfRecordsToShow",noOfRecordsToShow);
        param.put("startIndex",startIndex-1);
        if(ANPUtils.isNullOrEmpty(orderBy)) {
            param.put("orderBy", "id desc");
        } else {
            param.put("orderBy", orderBy);
        }
        List<EmployeeSalaryPayment> EmployeeSalaryPaymentlist = namedParameterJdbcTemplate.query("select e.id, e.first, e.last, e.mobile, e.type, empsalpay.amount, " +
                        "empsalpay.details,empsalpay.includeincalc,empsalpay.transferdate,empsalpay.fromemployeeid," +
                        "(select first from employee emp where emp.id=empsalpay.fromemployeeid and emp.orgid=:orgid) as fromEmpFirstName," +
                        "(select last from employee emp where emp.id=empsalpay.fromemployeeid and emp.orgid=:orgid) as fromEmpLastName" +
                        " from employee e, employeesalarypayment empsalpay " +
                        "where e.id=empsalpay.toemployeeid and e.orgid=:orgid " +
                        ANPUtils.getWhereClause(searchParams) + " order by :orderBy limit  :noOfRecordsToShow"
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
            employeeSalaryPayment.getToEmployeeBean().setType(rs.getString("e.type"));
            employeeSalaryPayment.setAmount(rs.getFloat("empsalpay.amount"));
            employeeSalaryPayment.setDetails(rs.getString("empsalpay.details"));
            employeeSalaryPayment.setIncludeInCalc(rs.getBoolean("empsalpay.includeincalc"));
            employeeSalaryPayment.getFromEmployeeBean().setEmployeeId(rs.getString("empsalpay.fromemployeeid"));
            employeeSalaryPayment.getFromEmployeeBean().setFirst(rs.getString("fromEmpFirstName"));
            employeeSalaryPayment.getFromEmployeeBean().setLast(rs.getString("fromEmpLastName"));
            employeeSalaryPayment.setTransferDate(rs.getDate("empsalpay.transferdate"));
            return employeeSalaryPayment;
        }
    }
}