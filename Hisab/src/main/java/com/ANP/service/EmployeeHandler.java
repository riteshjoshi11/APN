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

import static com.ANP.util.ANPConstants.LOGIN_TYPE_EMPLOYEE;

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
//        employeeDAO.isMobileDuplicate(employeeBean);

        if(employeeBean.getTypeInt()==0) {
           employeeBean.setTypeInt(ANPConstants.EMPLOYEE_TYPE_DEFAULT);
        }

        employeeDAO.createEmployee(employeeBean);

        //Now Create an associated account
        AccountBean accountBean = new AccountBean();
        accountBean.setAccountnickname(generateAccountNickName(employeeBean));
        accountBean.setCurrentbalance(employeeBean.getInitialBalance());
        accountBean.setInitialBalance(employeeBean.getInitialBalance());
        accountBean.setCreatedbyid(employeeBean.getCreatedbyId());
        accountBean.setType(LOGIN_TYPE_EMPLOYEE);
        accountBean.setOwnerid(employeeBean.getEmployeeId());
        accountBean.setOrgId(employeeBean.getOrgId());
        accountDAO.createAccount(accountBean);
        System.out.println("End CreateEmployee");
    }

    public String generateAccountNickName(EmployeeBean employeeBean){
        String accountNickName = null;
        if(ANPUtils.isNullOrEmpty(employeeBean.getFirst()) || ANPUtils.isNullOrEmpty(employeeBean.getLast())) {
            if(ANPUtils.isNullOrEmpty(employeeBean.getFirst()))
                accountNickName = employeeBean.getLast() + " [" + employeeBean.getMobile() + "]";

            if(ANPUtils.isNullOrEmpty(employeeBean.getLast()))
                accountNickName =  employeeBean.getFirst() + " [" + employeeBean.getMobile() + "]";
        } else {
            accountNickName = employeeBean.getFirst() + " " + employeeBean.getLast() + " [" + employeeBean.getMobile() + "]";
        }
        System.out.println(accountNickName);
        return accountNickName;
    }


    /*
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLoginRequired(String employeeId, boolean loginRequired) {
        if(employeeDAO.updateLoginRequired(employeeId,loginRequired)) {
            return true;
        }
        else {
        return false;
        }
    }
 */

    @Transactional(rollbackFor = Exception.class)
    //Create Salary and Update Employee:LastSalaryBalance
    public void createSalary(EmployeeSalary employeeSalaryBean) {
        //create entry into the salary table
        employeeDAO.createEmployeeSalary(employeeSalaryBean);

        //Employee Salary Audit
        EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
        employeeAuditBean.setOrgId(employeeSalaryBean.getOrgId());
        employeeAuditBean.setEmployeeid(employeeSalaryBean.getToEmployeeID());
        employeeAuditBean.setAmount(employeeSalaryBean.getAmount());
        employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
        employeeAuditBean.setType(ANPConstants.EMPLOYEE_SALARY_AUDIT_TYPE_DUE);
        employeeAuditBean.setTransactionDate(new Date());
        employeeDAO.updateEmployeeSalaryBalance(employeeAuditBean);
    }

    @Transactional(rollbackFor = Exception.class)
    //Create Salary and Update Employee:LastSalaryBalance
    //SUBTRACT PAYING PARTY BALANCE
    public void createSalaryPayment(EmployeeSalaryPayment employeeSalaryPaymentBean) {
        //Lets check if a Salary due to be created as part of the Salary Payment

        System.out.println("To Employee Name=" + employeeSalaryPaymentBean.getToEmployeeName());
        System.out.println("From Employee Name=" + employeeSalaryPaymentBean.getFromEmployeeBean());

        if(employeeSalaryPaymentBean.isCreateSalaryDueAlso()) {
            EmployeeSalary employeeSalary = new EmployeeSalary();
            employeeSalary.setCreateDate(new Date());
            employeeSalary.setIncludeInCalc(true);
            employeeSalary.setDetails(employeeSalaryPaymentBean.getDetails());
            employeeSalary.setAmount(employeeSalaryPaymentBean.getAmount());
            employeeSalary.setToEmployeeID(employeeSalaryPaymentBean.getToEmployeeId());
            employeeSalary.setCreatedbyId(employeeSalaryPaymentBean.getCreatedbyId());
            employeeSalary.setForceCreate(employeeSalaryPaymentBean.isForceCreate());
            employeeSalary.setOrgId(employeeSalaryPaymentBean.getOrgId());
            this.createSalary(employeeSalary);
        }

        //create an Employee Salary Entry
        employeeDAO.createEmployeeSalaryPayment(employeeSalaryPaymentBean);

         //Update/SUBTRACT From Employee Balance
        EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
        employeeAuditBean.setOrgId(employeeSalaryPaymentBean.getOrgId());
        employeeAuditBean.setEmployeeid(employeeSalaryPaymentBean.getFromEmployeeId());
        employeeAuditBean.setAccountid(employeeSalaryPaymentBean.getFromAccountId());
        employeeAuditBean.setAmount(employeeSalaryPaymentBean.getAmount());
        employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_PAY);
        employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
        employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_SALARYPAY);
        employeeAuditBean.setOtherPartyName(employeeSalaryPaymentBean.getFromEmployeeName()); //This will be opposite party
        employeeAuditBean.setTransactionDate(employeeSalaryPaymentBean.getTransferDate());
        accountDAO.updateEmployeeAccountBalance(employeeAuditBean);

        //Audit Employee Salary Balance
        employeeAuditBean.setEmployeeid(employeeSalaryPaymentBean.getToEmployeeId());
        employeeAuditBean.setType(ANPConstants.EMPLOYEE_SALARY_AUDIT_TYPE_PAY);
        employeeDAO.updateEmployeeSalaryBalance(employeeAuditBean);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateEmployee(EmployeeBean employeeBean){
        EmployeeBean employeeBeanFetched = employeeDAO.getEmployeeById(employeeBean.getOrgId(),employeeBean.getEmployeeId());
        System.out.println("first " + employeeBeanFetched.getFirst());
        System.out.println("first input " + employeeBean.getFirst());
        if(!employeeBeanFetched.getFirst().equalsIgnoreCase(employeeBean.getFirst()) ||
                !employeeBeanFetched.getLast().equalsIgnoreCase(employeeBean.getLast())) {
            if(ANPUtils.isNullOrEmpty(employeeBean.getLast()))
                employeeBean.setLast("");
            else if(ANPUtils.isNullOrEmpty(employeeBean.getFirst()))
                employeeBean.setFirst("");
            accountDAO.updateAccountNickName(employeeBean.getEmployeeId(),employeeBean.getOrgId(),generateAccountNickName(employeeBean));
        }

        if(employeeBean.getInitialBalance()!=employeeBeanFetched.getInitialBalance()){

            //This is to update initial balance in the backend.
            accountDAO.updateInitialBalanceField(employeeBean.getEmployeeId(),employeeBean.getOrgId(),employeeBean.getInitialBalance());
            AccountBean accountBean = new AccountBean();

            accountBean.setOrgId(employeeBean.getOrgId());
            accountBean.setOwnerid(employeeBean.getEmployeeId());
            System.out.println("AccountId = " + employeeBeanFetched.getAccountId());
            accountBean.setAccountId(employeeBeanFetched.getAccountId());
            accountBean.setInitialBalance(employeeBean.getInitialBalance());
            accountBean.setType(LOGIN_TYPE_EMPLOYEE);
            accountDAO.updateInitialBalance(accountBean);
            //process in accounDao from line 62-85

        }

        employeeDAO.updateEmployee(employeeBean);

    }


}
