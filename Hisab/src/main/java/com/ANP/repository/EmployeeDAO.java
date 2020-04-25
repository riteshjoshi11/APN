package com.ANP.repository;

import com.ANP.bean.EmployeeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EmployeeDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public boolean createEmployee(EmployeeBean employeeBean) {
        //TODO Joshi: Create a employee here
        return false;
    }
}
