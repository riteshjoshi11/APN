package com.ANP.service;

import com.ANP.bean.EmployeeBean;
import com.ANP.bean.EmployeeSalary;
import com.ANP.bean.EmployeeSalaryPayment;
import com.ANP.repository.EmployeeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeHandler {

    @Autowired
    EmployeeDAO employeeDAO;

    @Transactional(rollbackFor = Exception.class)
    //Create Employee and Account
    public boolean createEmployee(EmployeeBean employeeBean) {
        //TODO Joshi: Call EmployeeDAO:create
        // Create Account: generate AccountNickName as (First Name<Space>Last Name)  here as per the logic given
        // In a Transaction
        return false;
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
