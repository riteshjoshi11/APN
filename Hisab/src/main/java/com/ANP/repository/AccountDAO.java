package com.ANP.repository;

import com.ANP.bean.AccountBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public boolean createAccount(AccountBean accountBean) {
        boolean result = false;
        int accountCreated = namedParameterJdbcTemplate.update(
                "insert into account (ownerid,accountnickname,type,details,currentbalance,lastbalance,orgid,createdbyid)" +
                               " values(:ownerid,:accountnickname,:type,:details,:currentbalance,:lastbalance,:orgId,:userID)",
                new BeanPropertySqlParameterSource(accountBean));
        if (accountCreated > 0) {
            result = true;
        }
        return result;
    }

    //Operations: (ADD,SUBTRACT)
    public boolean updateAccountBalance(long accountId, double balance, String operation) {
        //logic 1. for the accountID update the balance
        //1. Get the  account:currentBalance & update the value over to account:LastBalance
        // 2. New account:currentBalance= currentBalance (operations +/-) Balance

      return false;
    }


}
