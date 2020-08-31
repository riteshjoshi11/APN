package com.ANP.repository;

import com.ANP.bean.*;
import com.ANP.util.ANPConstants;
import com.ANP.util.CustomAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.object.GenericStoredProcedure;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AccountDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    public void createAccount(AccountBean accountBean) {
        KeyHolder holder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                "insert into account (ownerid,accountnickname,type,details,orgid,createdbyid,initialbalance)" +
                        " values(:ownerid,:accountnickname,:type,:details,:orgId,:createdbyId,:initialBalance)",
                new BeanPropertySqlParameterSource(accountBean),holder);

        long generatedAccKey = holder.getKey().longValue();
        System.out.println("createAccount: Generated Key=" + generatedAccKey);
        accountBean.setAccountId(generatedAccKey);
        if( ( (accountBean.getCurrentbalance()==0 || accountBean.getCurrentbalance()==0.0)) ) {
            System.out.println("Current Balance is 0 So not updating the value into DB");
            return;
        }
        updateInitialBalance(accountBean);
/*
        if(ANPConstants.LOGIN_TYPE_CUSTOMER.equalsIgnoreCase(accountBean.getType())) {
            CustomerAuditBean customerAuditBean = new CustomerAuditBean();
            customerAuditBean.setOrgId(accountBean.getOrgId());
            customerAuditBean.setCustomerid(accountBean.getOwnerid());
            customerAuditBean.setAccountid(generatedAccKey);
            customerAuditBean.setAmount(accountBean.getCurrentbalance());
            customerAuditBean.setType(ANPConstants.AUDIT_TYPE_INITIAL_BALANCE);
            customerAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            customerAuditBean.setOtherPartyName("-"); //This will be opposite party
            customerAuditBean.setTransactionDate(new Date());
            this.updateCustomerAccountBalance(customerAuditBean);

        } else if(ANPConstants.LOGIN_TYPE_EMPLOYEE.equalsIgnoreCase(accountBean.getType())) {
            EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
            employeeAuditBean.setOrgId(accountBean.getOrgId());
            employeeAuditBean.setEmployeeid(accountBean.getOwnerid());
            employeeAuditBean.setAccountid(generatedAccKey);
            employeeAuditBean.setAmount(accountBean.getCurrentbalance());
            employeeAuditBean.setType(ANPConstants.AUDIT_TYPE_INITIAL_BALANCE);
            employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            employeeAuditBean.setForWhat(ANPConstants.AUDIT_TYPE_INITIAL_BALANCE);
            employeeAuditBean.setOtherPartyName("-");
            employeeAuditBean.setTransactionDate(new Date());
            this.updateEmployeeAccountBalance(employeeAuditBean);
        } else {
            throw new CustomAppException("Account Creation Login type is invalid","SERVER.ACCOUNT_CREATE.LOGIN_TYPE_INVALID", HttpStatus.EXPECTATION_FAILED);
        }

 */
    }

    public void updateInitialBalance(AccountBean accountBean) {
        if(ANPConstants.LOGIN_TYPE_CUSTOMER.equalsIgnoreCase(accountBean.getType())) {
            CustomerAuditBean customerAuditBean = new CustomerAuditBean();
            customerAuditBean.setOrgId(accountBean.getOrgId());
            customerAuditBean.setCustomerid(accountBean.getOwnerid());
            customerAuditBean.setAccountid(accountBean.getAccountId());
            customerAuditBean.setAmount(accountBean.getInitialBalance());
            customerAuditBean.setType(ANPConstants.AUDIT_TYPE_INITIAL_BALANCE);
            customerAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            customerAuditBean.setOtherPartyName("-"); //This will be opposite party
            customerAuditBean.setTransactionDate(new Date());
            this.updateCustomerAccountBalance(customerAuditBean);

        } else if(ANPConstants.LOGIN_TYPE_EMPLOYEE.equalsIgnoreCase(accountBean.getType())) {
            EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
            employeeAuditBean.setOrgId(accountBean.getOrgId());
            employeeAuditBean.setEmployeeid(accountBean.getOwnerid());
            employeeAuditBean.setAccountid(accountBean.getAccountId());
            employeeAuditBean.setAmount(accountBean.getInitialBalance());
            employeeAuditBean.setType(ANPConstants.AUDIT_TYPE_INITIAL_BALANCE);
            employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
            employeeAuditBean.setForWhat(ANPConstants.AUDIT_TYPE_INITIAL_BALANCE);
            employeeAuditBean.setOtherPartyName("-");
            employeeAuditBean.setTransactionDate(new Date());
            this.updateEmployeeAccountBalance(employeeAuditBean);
        } else {
            throw new CustomAppException("Account Creation Login type is invalid","SERVER.ACCOUNT_CREATE.LOGIN_TYPE_INVALID", HttpStatus.EXPECTATION_FAILED);
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
        This method will mainly used by UI to search for Accounts in almost all forms
        orgID: mandatory: if not provided return error
        nickName: can be empty

        Main Logic:
        -------------
        (Search at-least by orgId) AND (if nickName ) provided then try to search using that as well. Please do not forget to use %LIKE% for nickName
        Return: List:AccountBean with only AccountID, OwnerID, NickName populated (Nothing else, for the optimization we are doing this)
     */
    public List<AccountBean> searchAccounts(AccountBean accountBean) {
        System.out.println(accountBean.getOrgId());
        if (accountBean.getOrgId() == 0) {
            throw new java.lang.RuntimeException("orgId is mandatory");
        }

        String orgId = Long.toString(accountBean.getOrgId());
        String accountnickname = accountBean.getAccountnickname();

        if (accountnickname == null) {
            accountnickname = "";
        }

        List<AccountBean> accountBeanList =
                jdbcTemplate.query("select id,ownerid,accountnickname from account where " +
                                "accountnickname like ? and orgid = ? and type='" + ANPConstants.LOGIN_TYPE_CUSTOMER + "'",
                        new String[]{"%" + accountnickname + "%", orgId}
                        , new AccountMapper());
        return accountBeanList;
    }


    private static final class AccountMapper implements RowMapper<AccountBean> {

        public AccountBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            AccountBean accbean = new AccountBean();
            accbean.setOwnerid(rs.getString("ownerid"));
            accbean.setAccountnickname(rs.getString("accountnickname"));
            //   accbean.setOrgId(rs.getLong("orgid"));
            accbean.setAccountId(rs.getLong("id"));
            return accbean;
        }
    }

    /*
        Paras : Please write a single method querying three table Organization, Employee, Account
        and Build Organization, Employee, Account and Set into the respective beans inside the Success Login Bean
     */
    public SuccessLoginBean getUserDetails(String mobileNumber, long orgId) {
        /*
        Make a query here with three tables
         */
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("mobileNumber", mobileNumber);
        in.addValue("orgId", orgId);

        List<UserDetailBean> userDetailBeanList = namedParameterJdbcTemplate.query(
                "select employee.loginrequired, account.id,account.ownerid,account.accountnickname,account.createdbyid" +
                        ", account.currentbalance, account.lastbalance, employee.id, employee.first" +
                        ", employee.last, employee.mobile, employee.currentsalarybalance" +
                        ",employee.lastsalarybalance, organization.id, organization.orgname" +
                        ", organization.state, organization.city,employee.type,employee.mobile2,organization.clientid from organization,employee,account where " +
                        "employee.id = account.ownerid" +
                        " and employee.orgid = organization.id and (employee.mobile = :mobileNumber or employee.mobile2=:mobileNumber)  and " +
                        "employee.orgid = :orgId",
                in, new userDetailMapper());

        if (userDetailBeanList == null || userDetailBeanList.isEmpty()) {
            throw new CustomAppException("Cannot fetch employee details with the given parameters.",
                    "SERVER.getUserDetails.NO_EMPLOYEE_WITH_GIVEN_PARAM", HttpStatus.BAD_REQUEST);
        }

        UserDetailBean userDetailBean = userDetailBeanList.get(0);

        EmployeeBean employeeBean = new EmployeeBean();
        SuccessLoginBean successLoginBean = new SuccessLoginBean();

        //  Further Process the  Result and set the respective values into the EmployeeBean

        employeeBean.setEmployeeId(userDetailBean.getEmployeeId());
        employeeBean.setFirst(userDetailBean.getFirst());
        employeeBean.setLast(userDetailBean.getLast());
        employeeBean.setMobile(userDetailBean.getMobile());
        employeeBean.setOrgId(userDetailBean.getOrgId());

        employeeBean.setLoginrequired(userDetailBean.getLoginrequired());

        employeeBean.setCurrentsalarybalance(userDetailBean.getCurrentsalarybalance());
        employeeBean.setLastsalarybalance(userDetailBean.getLastsalarybalance());
        employeeBean.setTypeInt(Integer.parseInt(userDetailBean.getType()));
        successLoginBean.setEmployeeBean(employeeBean);

        //  Further Process the  Result and set the respective values into the AccountBean

        AccountBean accountBean = new AccountBean();

        accountBean.setAccountId(userDetailBean.getAccountId());
        accountBean.setOwnerid(userDetailBean.getOwnerid());
        accountBean.setAccountnickname(userDetailBean.getAccountnickname());
        accountBean.setCreatedbyid(userDetailBean.getCreatedbyid());
        accountBean.setCurrentbalance(userDetailBean.getCurrentbalance());
        accountBean.setLastbalance(userDetailBean.getLastbalance());
        accountBean.setOrgId(userDetailBean.getOrgId());
        successLoginBean.setAccountBean(accountBean);

        /*
            Further Process the  Result and set the respective values into the OrganizationBean
        */

        Organization organizationBean = new Organization();

        organizationBean.setOrgId(userDetailBean.getOrgId());
        organizationBean.setOrgName(userDetailBean.getOrgName());
        organizationBean.setCity(userDetailBean.getCity());
        organizationBean.setState(userDetailBean.getState());
        organizationBean.setClientId(userDetailBean.getClientId());

        successLoginBean.setOrganization(organizationBean);
        return successLoginBean;
    }

    private static final class userDetailMapper implements RowMapper<UserDetailBean> {

        public UserDetailBean mapRow(ResultSet rs, int rowNum) throws SQLException {

            UserDetailBean userDetailBean = new UserDetailBean();


            userDetailBean.setEmployeeId(rs.getString("employee.id"));
            userDetailBean.setFirst(rs.getString("employee.first"));
            userDetailBean.setLast(rs.getString("employee.last"));
            userDetailBean.setMobile(rs.getString("employee.mobile"));
            userDetailBean.setLoginrequired(rs.getBoolean("employee.loginrequired"));
            userDetailBean.setType(rs.getString("employee.type"));
            userDetailBean.setCurrentsalarybalance((rs.getDouble("employee.currentsalarybalance")));
            userDetailBean.setLastsalarybalance((rs.getDouble("employee.lastsalarybalance")));


            //organizationbean
            userDetailBean.setOrgName(rs.getString("organization.orgname"));
            userDetailBean.setCity(rs.getString("organization.city"));
            userDetailBean.setState(rs.getString("organization.state"));
            userDetailBean.setOrgId(rs.getLong("organization.id"));


            //accountbean
            userDetailBean.setAccountId(rs.getLong("account.id"));
            userDetailBean.setOwnerid(rs.getString("account.ownerid"));
            userDetailBean.setAccountnickname(rs.getString("account.accountnickname"));
//          userDetailBean.setType(rs.getString("account.type"));
//          userDetailBean.setDetails(rs.getString("account.details"));
//          userDetailBean.setOrgId(rs.getLong("account.orgid"));
            userDetailBean.setCreatedbyid(rs.getString("account.createdbyid"));
            userDetailBean.setCurrentbalance(rs.getDouble("account.currentbalance"));
            userDetailBean.setLastbalance(rs.getDouble("account.lastbalance"));
            userDetailBean.setMobile2(rs.getString("employee.mobile2"));
            userDetailBean.setClientId(rs.getString("organization.clientid"));
            return userDetailBean;
        }
    }

    public double getCashWithYou(String employeeID, long orgId) {
        System.out.println("getCashWithYou : employeeID[" + employeeID +"] orgid[" + orgId+ "]");
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("employeeID", employeeID);
        in.addValue("orgId", orgId);
        double retValue = 0.0 ;
        List<Double> cashwithyouList =  namedParameterJdbcTemplate.query("select sum(currentbalance) as cashwithyou from account where ownerid=:employeeID " +
                " and orgId=:orgId group by ownerid",in,new CashWithYouMapper());
        if(cashwithyouList!=null && !cashwithyouList.isEmpty()) {
            retValue = cashwithyouList.get(0);
        }
        return retValue;
    }

    private static final class CashWithYouMapper implements RowMapper<Double> {
        public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
          return rs.getDouble("cashwithyou");
        }
    }

    public boolean updateCustomerAccountBalance(CustomerAuditBean customerAuditBean) {
        StoredProcedure procedure = new GenericStoredProcedure();
        procedure.setDataSource(dataSource);
        procedure.setSql("UpdateCustomerBalanceWithAudit_Procedure");
        procedure.setFunction(false);
        SqlParameter[] declareparameters = {
                new SqlParameter("customerid", Types.VARCHAR),
                new SqlParameter("accountid", Types.BIGINT),
                new SqlParameter("amount",Types.FLOAT),
                new SqlParameter("otherparty", Types.VARCHAR),
                new SqlParameter("txntype",Types.VARCHAR),
                new SqlParameter("operation",Types.VARCHAR),
                new SqlParameter("orgid",Types.BIGINT),
                new SqlParameter("txndate",Types.DATE),
        };

        procedure.setParameters(declareparameters);
        procedure.compile();
        System.out.println(customerAuditBean.getCustomerid() + "," +  customerAuditBean.getAccountid() + "," +
                customerAuditBean.getAmount() + "," +    customerAuditBean.getOtherPartyName() + "," +  customerAuditBean.getOperation());
        Map<String, Object> result = procedure.execute(
                customerAuditBean.getCustomerid(),
                customerAuditBean.getAccountid(),
                customerAuditBean.getAmount(),
                customerAuditBean.getOtherPartyName(),
                customerAuditBean.getType(),
                customerAuditBean.getOperation(),
                customerAuditBean.getOrgId(),
                customerAuditBean.getTransactionDate()
        );
        System.out.println("Status " + result);
        return true;
    }

    public boolean updateEmployeeAccountBalance(EmployeeAuditBean employeeAuditBean) {
        StoredProcedure procedure = new GenericStoredProcedure();
        procedure.setDataSource(dataSource);
        procedure.setSql("UpdateEmployeeBalanceWithAudit_Procedure");
        procedure.setFunction(false);
        SqlParameter[] declareparameters = {
                new SqlParameter("employeeid", Types.VARCHAR),
                new SqlParameter("accountid", Types.BIGINT),
                new SqlParameter("amount",Types.FLOAT),
                new SqlParameter("otherparty", Types.VARCHAR),
                new SqlParameter("txntype",Types.VARCHAR),
                new SqlParameter("operation",Types.VARCHAR),
                new SqlParameter("forwhat",Types.VARCHAR),
                new SqlParameter("orgid",Types.BIGINT),
                new SqlParameter("txndate",Types.DATE),
        };

        procedure.setParameters(declareparameters);
        procedure.compile();
        System.out.println(employeeAuditBean.getEmployeeid() + "," +  employeeAuditBean.getAccountid() + "," +
        employeeAuditBean.getAmount() + "," +    employeeAuditBean.getOtherPartyName() + "," +  employeeAuditBean.getOperation());
        Map<String, Object> result = procedure.execute(
                employeeAuditBean.getEmployeeid(),
                employeeAuditBean.getAccountid(),
                employeeAuditBean.getAmount(),
                employeeAuditBean.getOtherPartyName(),
                employeeAuditBean.getType(),
                employeeAuditBean.getOperation(),
                employeeAuditBean.getForWhat(),
                employeeAuditBean.getOrgId(),
                employeeAuditBean.getTransactionDate()
        );
        System.out.println("Status " + result);
        return true;
    }

    public void updateAccountNickName(String ownerId, long orgId, String nickname)
    {
        Map<String,Object> param = new HashMap<>();
        param.put("ownerId",ownerId);
        param.put("orgId",orgId);
        param.put("nickname", nickname);
        namedParameterJdbcTemplate.update("update account set accountnickname = :nickname " +
                "where orgid = :orgId and ownerid = :ownerId",param);
    }
    public void updateInitialBalanceField(String ownerId, long orgId, float initialBalance)
    {
        Map<String,Object> param = new HashMap<>();
        param.put("ownerId",ownerId);
        param.put("orgId",orgId);
        param.put("initialBalance", initialBalance);
        namedParameterJdbcTemplate.update("update account set initialbalance = :initialBalance " +
                "where orgid = :orgId and ownerid = :ownerId",param);
    }

}