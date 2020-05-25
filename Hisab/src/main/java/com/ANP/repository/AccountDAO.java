package com.ANP.repository;

import com.ANP.bean.*;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public boolean createAccount(AccountBean accountBean) {
        boolean result = false;
        int accountCreated = namedParameterJdbcTemplate.update(
                "insert into account (ownerid,accountnickname,type,details,currentbalance,lastbalance,orgid,createdbyid)" +
                        " values(:ownerid,:accountnickname,:type,:details,:currentbalance,:lastbalance,:orgId,:createdbyId)",
                new BeanPropertySqlParameterSource(accountBean));
        if (accountCreated > 0) {
            result = true;
        }
        return result;
    }

    //Operations: (ADD,SUBTRACT)

    public boolean updateAccountBalance(long accountId, double balance, String operation) {
        int updateSuccessful;
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", accountId);
        mapSqlParameterSource.addValue("balance", balance);
        if (operation.equals("ADD"))
            updateSuccessful = namedParameterJdbcTemplate.update("update account set lastbalance = currentbalance, currentbalance = currentbalance" +
                    "+ :balance  where id = :id ", mapSqlParameterSource);
        else
            updateSuccessful = namedParameterJdbcTemplate.update("update account set lastbalance = currentbalance, currentbalance = currentbalance" +
                    "- :balance  where id = :id ", mapSqlParameterSource);
        if (updateSuccessful != 0)
            return true;
        else
            return false;
        //logic 1. for the accountID update the balance
        //1. Get the  account:currentBalance & update the value over to account:LastBalance
        // 2. New account:currentBalance= currentBalance (operations +/-) Balance
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
                                "accountnickname like ? and orgid = ? and type='" + ANPConstants.LOGIN_TYPE_CUSTOMER +  "'",
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
        TODO: Paras : Please write a single method querying three table Organization, Employee, Account
        and Build Organization, Employee, Account and Set into the respective beans inside the Success Login Bean

     */
    public SuccessLoginBean getUserDetails(String mobileNumber, long orgId) {
        /*
        Make a query here with three tables
         */
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("mobileNumber", mobileNumber);
        in.addValue("orgId", orgId);

        List<UserDetailBean> userDetailBeanList =  namedParameterJdbcTemplate.query(
                "select account.id,account.ownerid,account.accountnickname,account.createdbyid" +
                        ", account.currentbalance, account.lastbalance, employee.id, employee.first" +
                        ", employee.last, employee.mobile, employee.loginusername, employee.currentsalarybalance" +
                        ",employee.lastsalarybalance, organization.id, organization.orgname" +
                        ", organization.state, organization.city from organization,employee,account where " +
                        "employee.id = account.ownerid" +
                        " and employee.orgid = organization.id and employee.mobile = :mobileNumber and " +
                        "employee.orgid = :orgId",
                in, new userDetailMapper());

        if(userDetailBeanList==null || userDetailBeanList.size()==0) {
           throw new RuntimeException("Cannot fetch details with the given parameters.");
        }

        UserDetailBean userDetailBean = userDetailBeanList.get(0);
        EmployeeBean employeeBean = new EmployeeBean();
        SuccessLoginBean successLoginBean = new SuccessLoginBean();

        //  Further Process the  Result and set the respective values into the EmployeeBean

        employeeBean.setEmployeeId(userDetailBean.getEmployeeId());
        employeeBean.setFirst(userDetailBean.getFirst());
        employeeBean.setLast(userDetailBean.getLast());
        employeeBean.setMobile(userDetailBean.getMobile());
        employeeBean.setLoginusername(userDetailBean.getLoginusername());
        employeeBean.setOrgId(userDetailBean.getOrgId());

        //employeeBean.setLoginrequired(userDetailBean.getLoginrequired());

        employeeBean.setCurrentsalarybalance(userDetailBean.getCurrentsalarybalance());
        employeeBean.setLastsalarybalance(userDetailBean.getLastsalarybalance());
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
            //   userDetailBean.setLoginrequired(rs.getBoolean("employee.loginrequired"));
            //   userDetailBean.setType(rs.getString("employee.type"));
            userDetailBean.setLoginusername(rs.getString("employee.loginusername"));
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
            return userDetailBean;
        }
    }

    public List<CustomerInvoiceBean> listSalesPaged(long orgID, List<SearchParam> searchParams,
                                                    String orderBy, int noOfRecordsToShow, int startIndex) {
        if(startIndex == 0)
        {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);
        param.put("orderBy", orderBy);
        return namedParameterJdbcTemplate.query(
                "select cusinv.id,cusinv.tocustomerid,cusinv.date,cusinv.orderamount,cusinv.cgst,cusinv.sgst,cusinv.igst," +
                        "cusinv.totalamount,cusinv.invoiceno,cusinv.toaccountid,cusinv.orgid,cusinv.includeinreport," +
                        "cusinv.includeincalc, c.name,c.firmname,c.city,c.mobile1,c.gstin from customer c," +
                        " customerinvoice cusinv where c.id=cusinv.tocustomerid and cusinv.orgid=:orgID " +
                        ANPUtils.getWhereClause(searchParams) + " order by :orderBy limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new SalesPagedMapper());
    }
    private static final class SalesPagedMapper implements RowMapper<CustomerInvoiceBean> {
        public CustomerInvoiceBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            CustomerInvoiceBean customerInvoiceBean = new CustomerInvoiceBean();
            customerInvoiceBean.getCustomerBean().setName(rs.getString("c.name"));
            customerInvoiceBean.getCustomerBean().setCity(rs.getString("c.city"));
            customerInvoiceBean.getCustomerBean().setFirmname(rs.getString("c.firmname"));
            customerInvoiceBean.getCustomerBean().setGstin(rs.getString("c.gstin"));
            customerInvoiceBean.getCustomerBean().setMobile1(rs.getString("c.mobile1"));
//          customerInvoiceBean.getCustomerBean().setMobile2(rs.getString("c.mobile2"));
            customerInvoiceBean.setOrderAmount(rs.getDouble("orderamount"));
            customerInvoiceBean.setCGST(rs.getDouble("cusinv.cgst"));
            customerInvoiceBean.setIGST(rs.getFloat("cusinv.igst"));
            customerInvoiceBean.setSGST(rs.getFloat("cusinv.sgst"));
            customerInvoiceBean.setTotalAmount(rs.getFloat("cusinv.totalamount"));
            customerInvoiceBean.setInvoiceNo(rs.getString("cusinv.invoiceno"));
            customerInvoiceBean.setOrgId(rs.getLong("cusinv.orgid"));
            customerInvoiceBean.setToAccountId(rs.getLong("cusinv.toaccountid"));
            customerInvoiceBean.setIncludeInCalc(rs.getBoolean("cusinv.includeincalc"));
            customerInvoiceBean.setIncludeInReport(rs.getBoolean("cusinv.includeinreport"));
            customerInvoiceBean.setDate(rs.getDate("cusinv.date"));
            return customerInvoiceBean;
        }
    }

    public List<PayToVendorBean> listPayToVendorPaged(long orgID, List<SearchParam> searchParams,
                                                      String orderBy, int noOfRecordsToShow, int startIndex)
    {
        if(startIndex == 0)
        {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);
        param.put("orderBy", orderBy);
        return namedParameterJdbcTemplate.query(
                "select paytov.fromaccountid, paytov.toaccountid, paytov.date, paytov.amount," +
                        " paytov.details, paytov.fromemployeeid, paytov.tocustomerid, paytov.orgid, paytov.includeincalc," +
                        " c.name,c.firmname,c.city,c.mobile1,c.gstin from customer c," +
                        " paytovendor paytov where c.id=paytov.tocustomerid and paytov.orgid=:orgID " +
                        ANPUtils.getWhereClause(searchParams) + " order by :orderBy limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new PayToVendorMapper());
    }
       private static final class PayToVendorMapper implements RowMapper<PayToVendorBean> {
        public PayToVendorBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            PayToVendorBean payToVendorBean = new PayToVendorBean();
            payToVendorBean.getCustomerBean().setName(rs.getString("c.name"));
            payToVendorBean.getCustomerBean().setCity(rs.getString("c.city"));
            payToVendorBean.getCustomerBean().setFirmname(rs.getString("c.firmname"));
            payToVendorBean.getCustomerBean().setGstin(rs.getString("c.gstin"));
            payToVendorBean.getCustomerBean().setMobile1(rs.getString("c.mobile1"));
            payToVendorBean.setFromAccountID(rs.getLong("paytov.fromaccountid"));
            payToVendorBean.setToAccountID(rs.getLong("paytov.toaccountid"));
            payToVendorBean.setPaymentDate(rs.getDate("paytov.date"));
            payToVendorBean.setAmount(rs.getFloat("paytov.amount"));
            payToVendorBean.setDetails(rs.getString("paytov.details"));
            payToVendorBean.setFromEmployeeID(rs.getString("paytov.fromemployeeid"));
            payToVendorBean.setOrgId(rs.getLong("paytov.orgid"));
            payToVendorBean.setToCustomerID(rs.getString("paytov.tocustomerid"));
            payToVendorBean.setIncludeInCalc(rs.getBoolean("paytov.includeincalc"));
            return payToVendorBean;
        }
    }
    public List<InternalTransferBean> listInternalTransfer(long orgID, List<SearchParam> searchParams, String orderBy, int noOfRecordsToShow, int startIndex) {
        if(startIndex == 0)
        {
            startIndex = 1;
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("orgID", orgID);
        param.put("noOfRecordsToShow", noOfRecordsToShow);
        param.put("startIndex", startIndex - 1);
        param.put("orderBy", orderBy);
        return namedParameterJdbcTemplate.query(
                "select e.mobile,e.first,e.last, internal.details," +
                        " (select emp.first from employee emp where emp.id = internal.fromemployeeid) as fromfirst,"+
                        " (select emp.last from employee emp where emp.id = internal.fromemployeeid) as fromlast, " +
                        " (select emp.mobile from employee emp where emp.id = internal.fromemployeeid) as frommobile " +
                        "from employee e, internaltransfer internal where e.id=internal.toemployeeid and internal.orgid=:orgID " +
                        ANPUtils.getWhereClause(searchParams) + " order by :orderBy limit  :noOfRecordsToShow"
                        + " offset :startIndex",
                param, new InternalTransferMapper());

    }
    private static final class InternalTransferMapper implements RowMapper<InternalTransferBean> {
        public InternalTransferBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            InternalTransferBean internalTransferBean = new InternalTransferBean();
            internalTransferBean.setDetails(rs.getString("internal.details"));
            internalTransferBean.getFromEmployee().setFirst(rs.getString("fromfirst"));
            internalTransferBean.getFromEmployee().setLast(rs.getString("fromlast"));
            internalTransferBean.getFromEmployee().setMobile(rs.getString("frommobile"));
            internalTransferBean.getToEmployee().setFirst(rs.getString("e.first"));
            internalTransferBean.getToEmployee().setLast(rs.getString("e.last"));
            internalTransferBean.getToEmployee().setMobile(rs.getString("e.mobile"));
            return internalTransferBean;
        }
    }
}