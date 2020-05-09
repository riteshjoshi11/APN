package com.ANP.repository;

import com.ANP.bean.EmployeeBean;
import com.ANP.bean.EmployeeSalary;
import com.ANP.bean.EmployeeSalaryPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
/*
    handles Employee, EmployeeSalary, EmployeePayment

 */
public class EmployeeDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private JdbcTemplate jdbcTemplate;

    public int createEmployee(EmployeeBean employeeBean) {

        String idSql = "SELECT getEmployeeId() ";
        Map param = new HashMap<String, Object>();
        String id = namedParameterJdbcTemplate.queryForObject(idSql, param, String.class);
        employeeBean.setEmployeeId(id);
        return namedParameterJdbcTemplate.update(
                "insert into employee (id,first,last,mobile,loginrequired,loginusername,currentsalarybalance" +
                        ",lastsalarybalance,orgid) values(:employeeId,:first,:last,:mobile,:loginrequired,:loginusername" +
                        ",:currentsalarybalance,:lastsalarybalance,:orgId)", new BeanPropertySqlParameterSource(employeeBean));
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
        //TODO Joshi: Here you need to update the 'Employee:CurrentSalaryBalance' field based on the 'operation' passed
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("toemployeeid", toEmployeeID);
        in.addValue("balance", balance);
        int n;
        if (operation.equals("ADD"))
            n = namedParameterJdbcTemplate.update("update employeesalary set amount = amount" +
                    "+ :balance where toemployeeid = :toemployeeid", in);
        else
            n = namedParameterJdbcTemplate.update("update employeesalary set amount = amount" +
                    "- :balance where toemployeeid = :toemployeeid", in);
        if (n != 0)
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
                jdbcTemplate.query("select * from employee where orgid = ? and (first" +
                                " like ? or last like ?)  ",
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
 //           empbean.setOrgId(rs.getLong("orgid"));
            return empbean;
        }
    }

}