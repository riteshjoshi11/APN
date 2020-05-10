package com.ANP.repository;

import com.ANP.bean.AccountBean;
import com.ANP.bean.EmployeeBean;
import com.ANP.bean.Organization;
import com.ANP.bean.SuccessLoginBean;
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
import java.util.List;

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
        int n;
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("id", accountId);
        in.addValue("balance", balance);
        if (operation.equals("add"))
            n = namedParameterJdbcTemplate.update("update account set lastbalance = currentbalance, currentbalance = currentbalance" +
                    "+ :balance  where id = :id, ", in);
        else
            n = namedParameterJdbcTemplate.update("update account set lastbalance = currentbalance, currentbalance = currentbalance" +
                    "- :balance  where id = :id, ", in);

        if (n != 0)
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
                jdbcTemplate.query("select id,ownerid,accountnickname from account where accountnickname like ? and orgid = ?",
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
        SuccessLoginBean successLoginBean = new SuccessLoginBean();
        EmployeeBean employeeBean = new EmployeeBean() ;
        /*
            Process Result and set the respective values into the employeeBean
         */
        successLoginBean.setEmployeeBean(employeeBean);


        AccountBean accountBean = new AccountBean();
         /*
           Further Process the Result and set the respective values into the AccountBean
         */
        successLoginBean.setAccountBean(accountBean);


        Organization organizationBean = new Organization();
          /*
            Further Process the  Result and set the respective values into the OrganizationBean
         */
        successLoginBean.setOrganization(organizationBean);
        return successLoginBean;
    }

}