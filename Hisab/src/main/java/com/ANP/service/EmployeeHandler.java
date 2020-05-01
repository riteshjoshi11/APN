package com.ANP.service;

import com.ANP.bean.*;
import com.ANP.repository.AccountDAO;
import com.ANP.repository.EmployeeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeHandler {
    @Autowired
    EmployeeDAO employeeDAO;
    @Autowired
    AccountDAO accountDAO;

    @Transactional(rollbackFor = Exception.class)
    //Create Employee and Account
    public boolean createEmployee(EmployeeBean employeeBean) {
        //TODO Joshi: Call EmployeeDAO:create
        // Create Account: generate AccountNickName as (First Name<Space>Last Name)  here as per the logic given
        // In a Transaction
        boolean isemployeecreated = false;
        employeeDAO.createEmployee(employeeBean);
        AccountBean accountBean = new AccountBean();
        accountBean.setAccountnickname(employeeBean.getFirst() + " " +  employeeBean.getLast());
        accountBean.setCurrentbalance(employeeBean.getCurrentAccountBalance());
        accountBean.setCreatedbyid(employeeBean.getEmployeeId());
        accountBean.setType("E");

        isemployeecreated = accountDAO.createAccount(accountBean);
        return isemployeecreated;
    }

    @Transactional(rollbackFor = Exception.class)
    //Create Salary and Update Employee:LastSalaryBalance
    public boolean createSalary(EmployeeSalary employeeSalaryBean) {
        //TODO Joshi: add additional code here
        employeeDAO.createEmployeeSalary(employeeSalaryBean);
        employeeDAO.UpdateEmpSalaryBalance(employeeSalaryBean.getToEmployeeId(),employeeSalaryBean.getAmount(), "ADD" );
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    //Create Salary and Update Employee:LastSalaryBalance
    public boolean createSalaryPayment(EmployeeSalaryPayment employeeSalaryPaymentBean) {
        //TODO Joshi: Call EmployeeDAO:create
        // Create Account: generate AccountNickName as (First Name<Space>Last Name)  here as per the logic given
        // In a Transaction
        employeeDAO.createEmployeeSalaryPayment(employeeSalaryPaymentBean);
        employeeDAO.UpdateEmpSalaryBalance(employeeSalaryPaymentBean.getToEmployeeId(),employeeSalaryPaymentBean.getAmount(), "SUBTRACT" );
        return false;
    }


}
