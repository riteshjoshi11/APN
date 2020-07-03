package com.ANP.service;
import java.util.*;
import com.ANP.bean.*;
import com.ANP.repository.AccountDAO;
import com.ANP.repository.EmployeeDAO;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
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
    public void createEmployee(EmployeeBean employeeBean) {

        System.out.println("Start CreateEmployee");

/*       if(ANPUtils.isNullOrEmpty(employeeBean.getType())) {
            employeeBean.setType(employeeBean.getType());
       }
 */
       if(employeeBean.getTypeInt()==0)
       {
           employeeBean.setTypeInt(ANPConstants.EMPLOYEE_TYPE_DEFAULT);
       }
        employeeDAO.createEmployee(employeeBean);

        //Now Create an associated account
        AccountBean accountBean = new AccountBean();
        accountBean.setAccountnickname(employeeBean.getFirst() + " " +  employeeBean.getLast());
        accountBean.setCurrentbalance(employeeBean.getCurrentAccountBalance());
        accountBean.setCreatedbyid(employeeBean.getCreatedbyId());
        accountBean.setType(ANPConstants.LOGIN_TYPE_EMPLOYEE);
        accountBean.setOwnerid(employeeBean.getEmployeeId());
        accountBean.setOrgId(employeeBean.getOrgId());
        accountDAO.createAccount(accountBean);
        System.out.println("End CreateEmployee");
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateLoginRequired(String employeeId, boolean loginRequired) {
        if(employeeDAO.updateLoginRequired(employeeId,loginRequired)) {
            return true;
        }
        else {
        return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateMobile(String employeeID, String mobile) {
        if(employeeDAO.updateMobile(employeeID,mobile)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean UpdateEmpSalaryBalance(String employeeID, double balance, String operation) {
        if(employeeDAO.UpdateEmpSalaryBalance(employeeID,balance,operation)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    //Create Salary and Update Employee:LastSalaryBalance
    public boolean createSalary(EmployeeSalary employeeSalaryBean) {
        //TODO Joshi: add additional code here
        boolean isSalaryCreated = false;
        employeeDAO.createEmployeeSalary(employeeSalaryBean);
        isSalaryCreated = employeeDAO.UpdateEmpSalaryBalance(employeeSalaryBean.getToEmployeeID(),employeeSalaryBean.getAmount(), "ADD" );
        return isSalaryCreated;
    }

    @Transactional(rollbackFor = Exception.class)
    //Create Salary and Update Employee:LastSalaryBalance
    //SUBTRACT PAYING PARTY BALANCE
    public void createSalaryPayment(EmployeeSalaryPayment employeeSalaryPaymentBean) {
        //create an Employee Salary Entry
        employeeDAO.createEmployeeSalaryPayment(employeeSalaryPaymentBean);

        //Update Employee Salary Balance
        employeeDAO.UpdateEmpSalaryBalance(employeeSalaryPaymentBean.getToEmployeeId(),employeeSalaryPaymentBean.getAmount(), "SUBTRACT" );

        //Update/SUBTRACT From Employee Balance
        EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
        employeeAuditBean.setOrgId(employeeSalaryPaymentBean.getOrgId());
        employeeAuditBean.setEmployeeid(employeeSalaryPaymentBean.getFromEmployeeId());
        employeeAuditBean.setAccountid(employeeSalaryPaymentBean.getFromAccountId());
        employeeAuditBean.setAmount(employeeSalaryPaymentBean.getAmount());
        employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_PAY);
        employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
        employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_SALARYPAY);
        employeeAuditBean.setOtherPartyName(employeeSalaryPaymentBean.getToEmployeeName()); //This will be opposite party
        employeeAuditBean.setTransactionDate(employeeSalaryPaymentBean.getTransferDate());
        accountDAO.updateEmployeeAccountBalance(employeeAuditBean);
    }

}
