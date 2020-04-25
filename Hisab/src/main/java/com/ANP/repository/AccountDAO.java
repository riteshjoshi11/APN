package com.ANP.repository;

import com.ANP.bean.AccountBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class AccountDAO {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public boolean createAccount(AccountBean accountBean) {
        //TODO Joshi: create an account in the db
        return false ;
    }
}
