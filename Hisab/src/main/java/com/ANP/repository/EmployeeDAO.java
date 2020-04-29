package com.ANP.repository;

import com.ANP.bean.EmployeeBean;
import com.ANP.bean.EmployeeSalary;
import com.ANP.bean.EmployeeSalaryPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
/*
    handles Employee, EmployeeSalary, EmployeePayment

 */
public class EmployeeDAO {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public boolean createEmployee(EmployeeBean employeeBean) {
        //TODO Joshi: Create a employee here
        return false;
    }

    public boolean updateLoginRequired(long employeeID, boolean loginRequired) {
        //TODO Joshi: Update loginRequired attribute for employeeID passed
        return false;
    }

    public boolean updateMobile(long employeeID, String mobile) {
        //TODO Joshi: Update mobile attribute for employeeID passed
        return false;
    }

    //operation values(ADD,SUBTRACT)
    public boolean UpdateEmpSalaryBalance(String employeeID, double balance, String operation) {
        //TODO Joshi: Here you need to update the 'Employee:CurrentSalaryBalance' field based on the 'operation' passed
        //Overall Effect: Employee:CurrentSalaryBalance = Employee:CurrentSalaryBalance 'operation(ADD/SUBTRACT)' balance
        //Please note there is a mysql trigger running which is copying old value of Employee:CurrentSalaryBalance to Employee:LastSalaryBalance for audit purpose
        return false;
    }

    public boolean createEmployeeSalary(EmployeeSalary employeeSalaryBean) {
        //TODO Joshi: Create a employee here
        return false;
    }

    public boolean createEmployeeSalaryPayment(EmployeeSalaryPayment employeeSalaryPaymentBean) {
        //TODO Joshi: Create a employee here
        return false;
    }
}
