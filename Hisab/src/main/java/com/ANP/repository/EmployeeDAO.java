package com.ANP.repository;

import com.ANP.bean.*;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
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

    private static final Logger logger = LoggerFactory.getLogger(EmployeeDAO.class);

    @Transactional(rollbackFor = Exception.class)
    public String createEmployee(EmployeeBean employeeBean) {
        logger.trace("Entering: createEmployee: employeeBean: " + employeeBean);
        String id = "";
        isMobileDuplicate(employeeBean, false);
        try {
            String idSql = "SELECT getEmployeeId() ";
            Map param = new HashMap<String, Object>();
            id = namedParameterJdbcTemplate.queryForObject(idSql, param, String.class);
            employeeBean.setEmployeeId(id);
            namedParameterJdbcTemplate.update(
                    "insert into employee (id,first,last,mobile,loginrequired,orgid,type,mobile2,initialsalarybalance) " +
                            " values(:employeeId,:first,:last,:mobile,:loginrequired" +
                            ",:orgId, :typeInt,:mobile2,:initialSalaryBalance)", new BeanPropertySqlParameterSource(employeeBean));
        } catch (DuplicateKeyException e) {
            throw new CustomAppException("Duplicate Entry", "SERVER.CREATE_EMPLOYEE.DUPLICATE", HttpStatus.EXPECTATION_FAILED);
        }
        logger.trace("Exiting: createEmployee: id" + id);

        return id;
    }

    @Transactional(rollbackFor = Exception.class)
    public void createEmployeeSalary(EmployeeSalary employeeSalaryBean) {
        if (!employeeSalaryBean.isForceCreate()) {
            this.isDuplicateSalaryDueSuspect(employeeSalaryBean);
        }
        namedParameterJdbcTemplate.update("insert into employeesalary(toemployeeid,amount,details,orgid,createdbyid,includeincalc) " +
                        "values(:toEmployeeID,:amount,:details,:orgId,:createdbyId,:includeInCalc)",
                new BeanPropertySqlParameterSource(employeeSalaryBean));
    }

    @Transactional(rollbackFor = Exception.class)
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
        return namedParameterJdbcTemplate.query("select e.*, acc.currentbalance, acc.initialbalance,acc.id, " +
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
            Integer empTypeInteger = rs.getInt("e.type");
            String empType = rs.getString("emptype");


/*
            //if SUPER ADMIN then make it business partner for UI to display
            if(empTypeInteger==1) {
                empTypeInteger = 2;
                empType = "Business Partner" ;
            } else if(empTypeInteger==6) {
                //if VIRTUAL then make it Default for UI to display
                empTypeInteger = 7;
                empType = "Default" ;
            }
  */
            empbean.setTypeInt(empTypeInteger); // Business Partner
            empbean.setType(empType);

            empbean.setCurrentsalarybalance(rs.getBigDecimal("e.currentsalarybalance"));
            empbean.setCurrentAccountBalance(rs.getBigDecimal("acc.currentbalance"));
            empbean.setAccountId(rs.getLong("acc.id"));
            empbean.setInitialSalaryBalance(rs.getBigDecimal("e.initialsalarybalance"));

            empbean.setMobile2(rs.getString("e.mobile2"));

            if (rs.getBigDecimal("acc.initialbalance") != null) {
                empbean.setInitialBalance(rs.getBigDecimal("acc.initialbalance"));
            } else {
                empbean.setInitialBalance(new BigDecimal(0.0));
            }
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
                        " empsal.details, empsal.includeincalc,empsal.createdate,empsal.createdbyid," +
                        "(select concat(`first`,' ',`last`,'[',`mobile`,']') from `employee` where id = empsal.createdbyid) as createdByEmployeeName " +
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
            employeeSalary.setAmount(rs.getBigDecimal("empsal.amount"));
            employeeSalary.setDetails(rs.getString("empsal.details"));
            employeeSalary.setIncludeInCalc(rs.getBoolean("empsal.includeincalc"));
            employeeSalary.setCreateDate(rs.getTimestamp("empsal.createdate"));
            employeeSalary.setCreatedbyId(rs.getString("empsal.createdbyid"));
            employeeSalary.setCreatedByEmpoyeeName(rs.getString("createdByEmployeeName"));

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
                        "(select last from employee emp where emp.id=empsalpay.fromemployeeid and emp.orgid=:orgid) as fromEmpLastName," +
                        "(select concat(`first`,' ',`last`,'[',`mobile`,']') from `employee` where id = empsalpay.createdbyid) as createdByEmployeeName " +
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
            employeeSalaryPayment.setAmount(rs.getBigDecimal("empsalpay.amount"));
            employeeSalaryPayment.setDetails(rs.getString("empsalpay.details"));
            employeeSalaryPayment.setIncludeInCalc(rs.getBoolean("empsalpay.includeincalc"));
            employeeSalaryPayment.getFromEmployeeBean().setEmployeeId(rs.getString("empsalpay.fromemployeeid"));
            employeeSalaryPayment.getFromEmployeeBean().setFirst(rs.getString("fromEmpFirstName"));
            employeeSalaryPayment.getFromEmployeeBean().setLast(rs.getString("fromEmpLastName"));
            employeeSalaryPayment.setTransferDate(rs.getTimestamp("empsalpay.transferdate"));
            employeeSalaryPayment.setCreateDate(rs.getTimestamp("empsalpay.createdate"));
            employeeSalaryPayment.setCreatedbyId(rs.getString("empsalpay.createdbyid"));
            employeeSalaryPayment.setCreatedByEmpoyeeName(rs.getString("createdByEmployeeName"));
            return employeeSalaryPayment;
        }
    }

    private void isDuplicatePaySalarySuspect(EmployeeSalaryPayment employeeSalaryPayment) {
        //Do a count(*) query and if you found count>0 then throw this error else nothing
        Map<String, Object> params = new HashMap<>();
        params.put("orgid", employeeSalaryPayment.getOrgId());
        params.put("toemployeeid", employeeSalaryPayment.getToEmployeeId());

        params.put("amount", employeeSalaryPayment.getAmount().longValue());

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

        params.put("amount", employeeSalary.getAmount().longValue());

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
        logger.trace("Entering : updateEmployee: the employeebean which will be saved[" + employeeBean + "]");
        if (employeeBean.getTypeInt() <= 0) {
            throw new CustomAppException("Employee Type cannot be 0 or blank", "SERVER.UPDATE_EMPLOYEE.NULLVALUE", HttpStatus.EXPECTATION_FAILED);
        }

        String toAppend = ",initialsalarybalance=:initialSalaryBalance";

        namedParameterJdbcTemplate.update("update employee set first = :first," +
                "last=:last, loginrequired = :loginrequired, type = :typeInt, mobile2 =:mobile2 " + toAppend +
                " where orgid = :orgId and id = :employeeId", new BeanPropertySqlParameterSource(employeeBean));
    }


    public EmployeeBean getEmployeeById(Long orgId, String employeeId) {
        logger.trace("Entering : getEmployeeById : orgId[" + orgId + "] employeeId [" + employeeId + "]");

        List<SearchParam> searchParams = new ArrayList<>();
        SearchParam param = new SearchParam();
        param.setCondition("and");
        param.setFieldName("e.id");
        param.setFieldType(ANPConstants.SEARCH_FIELDTYPE_STRING);
        param.setSoperator("=");
        param.setValue(employeeId);
        searchParams.add(param);
        EmployeeBean retEmployeeBean = null;
        List<EmployeeBean> employeeList = listEmployeesWithBalancePaged(orgId, searchParams, "", 1, 1);

        if (employeeList != null && !employeeList.isEmpty()) {
            retEmployeeBean = employeeList.get(0);
        }
        logger.trace("Exiting : getEmployeeById : retEmployeeBean[" + retEmployeeBean + "]");
        return retEmployeeBean;
    }

    public void updateEmployeeSalaryBalance(EmployeeAuditBean employeeAuditBean) {
        StoredProcedure procedure = new GenericStoredProcedure();
        procedure.setDataSource(dataSource);
        procedure.setSql("UpdateEmployeeSalaryBalanceWithAudit_Procedure");
        procedure.setFunction(false);
        SqlParameter[] declareparameters = {
                new SqlParameter("employeeid", Types.VARCHAR),
                new SqlParameter("orgid", Types.BIGINT),
                new SqlParameter("amount", Types.FLOAT),
                new SqlParameter("otherparty", Types.VARCHAR),
                new SqlParameter("txntype", Types.VARCHAR),
                new SqlParameter("operation", Types.VARCHAR),
                new SqlParameter("txndate", Types.DATE)
        };

        procedure.setParameters(declareparameters);
        procedure.compile();
        System.out.println(employeeAuditBean.getEmployeeid() + ","
                + employeeAuditBean.getAmount() + ","
                + employeeAuditBean.getType() + ","
                + employeeAuditBean.getOperation());

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

    public void isMobileDuplicate(EmployeeBean employeeBean, boolean isUpdateScenario) {

        Map<String, Object> params = new HashMap<>();
        params.put("mobile", employeeBean.getMobile());
        params.put("mobile2", employeeBean.getMobile2());
        params.put("orgId", employeeBean.getOrgId());
        String queryPart = "";

        if (!ANPUtils.isNullOrEmpty(employeeBean.getMobile2())) {
            queryPart = queryPart + " (mobile = :mobile2 or mobile2 = :mobile2) ";
        }

        if (!ANPUtils.isNullOrEmpty(employeeBean.getMobile())) {
            if(ANPUtils.isNullOrEmpty(queryPart)) {
                queryPart = " (mobile = :mobile or mobile2 = :mobile) ";
            } else {
                queryPart = queryPart + " or (mobile = :mobile or mobile2 = :mobile) ";
            }
        }

        logger.trace("isMobileDuplicate: SQL [" + queryPart + "]");

        if (!ANPUtils.isNullOrEmpty(queryPart)) {
            Integer count = namedParameterJdbcTemplate.queryForObject("select count(*) from employee where orgid=:orgId AND " +
                    queryPart, params, Integer.class);

            logger.trace("isMobileDuplicate: the query output [" + count + "]");

            if (!isUpdateScenario && count > 0) { //in case of create, the query should return 0
                throw new CustomAppException("The mobile or alternate mobile no. is already registered for your business with some other user", "SERVER.CREATE_EMPLOYEE.DUPLICATE_MOBILE", HttpStatus.EXPECTATION_FAILED);
            }

            if (isUpdateScenario && count > 1) { //in case of updates, the query may return 1 (for self Record)
                throw new CustomAppException("The mobile or alternate mobile no. is already registered for your business with some other user", "SERVER.UPDATE_EMPLOYEE.DUPLICATE_MOBILE", HttpStatus.EXPECTATION_FAILED);
            }
        }
    }

    public int deleteEmployeeSalary(EmployeeSalary employeeSalary) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", employeeSalary.getSalaryID());
        param.put("orgid", employeeSalary.getOrgId());

        return namedParameterJdbcTemplate.update("update employeesalary set isdeleted = true, deletedate = CURDATE() " +
                "where id = :id and orgid = :orgid", param);
    }

    public int deleteEmpSalaryPayment(EmployeeSalaryPayment employeeSalaryPayment) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", employeeSalaryPayment.getSalaryPaymentID());
        param.put("orgid", employeeSalaryPayment.getOrgId());
        return namedParameterJdbcTemplate.update("update employeesalarypayment set isdeleted = true, deletedate = CURDATE() " +
                "where id = :id and orgid = :orgid", param);

    }

    public String isMobilePresent(String mobileNumber) {

        logger.trace("isMobilePresent entr: mobileNumber [" + mobileNumber + "]");


        String retMobileNumber = "";

        if (!ANPUtils.isNullOrEmpty(mobileNumber)) {

            Map<String, Object> params = new HashMap<>();
            params.put("mobileNumber", mobileNumber);

            Integer count = namedParameterJdbcTemplate.queryForObject("select count(*) from employee where " +
                    " mobile=:mobileNumber or  mobile2=:mobileNumber", params, Integer.class);

            if (count > 1) {
                return mobileNumber;
            }
        }

        logger.trace("isMobilePresent exit: mobileNumber [" + mobileNumber + "]");

        return retMobileNumber;
        }
    }

