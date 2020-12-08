package com.ANP.service;

import com.ANP.bean.*;
import com.ANP.repository.AccountDAO;
import com.ANP.repository.EmployeeDAO;
import com.ANP.util.ANPConstants;
import com.ANP.util.ANPUtils;
import com.ANP.util.CustomAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static com.ANP.util.ANPConstants.LOGIN_TYPE_EMPLOYEE;

@Service
public class EmployeeHandler {
    @Autowired
    EmployeeDAO employeeDAO;
    @Autowired
    AccountDAO accountDAO;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeHandler.class);

    @Transactional(rollbackFor = Exception.class)
    //Create Employee and Account
    public void createEmployee(EmployeeBean employeeBean) {

        logger.debug("Start CreateEmployee");

        if (employeeBean.getTypeInt() == 0) {
            employeeBean.setTypeInt(ANPConstants.EMPLOYEE_TYPE_DEFAULT);
        }

        String createdEmployeeID = employeeDAO.createEmployee(employeeBean);
        //set the generated ID on the input bean
        employeeBean.setEmployeeId(createdEmployeeID);

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


        //@TODO Paras: Please add a check here if (should not be 0 or 0.0) then call below method
        if (employeeBean.getInitialSalaryBalance() != null) {
            if (employeeBean.getInitialSalaryBalance().compareTo(new BigDecimal("0.0")) > 0) {
                createSalaryDueBasedOnInitialSalaryBalance(employeeBean);
            }
        }
        logger.debug("End CreateEmployee");
    }

    /*
    @TODO Paras : Please complete this method
    Call createSalary method from this method
         */
    private void createSalaryDueBasedOnInitialSalaryBalance(EmployeeBean employeeBean) {

        EmployeeSalary employeeSalary = new EmployeeSalary();
        employeeSalary.setOrgId(employeeBean.getOrgId());
        employeeSalary.setToEmployeeID(employeeBean.getEmployeeId());
        employeeSalary.setAmount(employeeBean.getInitialSalaryBalance());
        employeeSalary.setCreatedbyId(employeeBean.getCreatedbyId());
        createSalary(employeeSalary);

    }

    public String generateAccountNickName(EmployeeBean employeeBean) {
         String accountNickName = null;

        if (ANPUtils.isNullOrEmpty(employeeBean.getFirst()) || ANPUtils.isNullOrEmpty(employeeBean.getLast())) {
            if (ANPUtils.isNullOrEmpty(employeeBean.getFirst()))
                accountNickName = employeeBean.getLast() + " [" + employeeBean.getMobile() + "]";

            if (ANPUtils.isNullOrEmpty(employeeBean.getLast()))
                accountNickName = employeeBean.getFirst() + " [" + employeeBean.getMobile() + "]";
        } else {
            accountNickName = employeeBean.getFirst() + " " + employeeBean.getLast() + " [" + employeeBean.getMobile() + "]";
        }
        //Nitesh: Fixing space issue during search
        if(!ANPUtils.isNullOrEmpty(accountNickName)) {
            accountNickName = accountNickName.trim();
        }

        logger.debug("generated accountNickName[" + accountNickName + "]");
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
    public void deleteEmpSalaryDue(EmployeeSalary employeeSalaryBean) {
        //create entry into the salary table
        int noOfRecordsDeleted;
        noOfRecordsDeleted = employeeDAO.deleteEmployeeSalary(employeeSalaryBean);
        if (noOfRecordsDeleted != 1) {
            throw new CustomAppException("Wrong Employee Salary Due Deletion Entry", "SERVER.EMPLOYEE_SALARY_DUE.NOTEXIST", HttpStatus.EXPECTATION_FAILED);
        }

        //Employee Salary Audit
        EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
        employeeAuditBean.setOrgId(employeeSalaryBean.getOrgId());
        employeeAuditBean.setEmployeeid(employeeSalaryBean.getToEmployeeID());
        employeeAuditBean.setAmount(employeeSalaryBean.getAmount());
        employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
        employeeAuditBean.setType(ANPConstants.EMPLOYEE_SALARY_AUDIT_TYPE_DUE);
        employeeAuditBean.setTransactionDate(new Date());
        employeeDAO.updateEmployeeSalaryBalance(employeeAuditBean);

    }

    @Transactional(rollbackFor = Exception.class)
    //Create Salary and Update Employee:LastSalaryBalance
    //SUBTRACT PAYING PARTY BALANCE
    public void createSalaryPayment(EmployeeSalaryPayment employeeSalaryPaymentBean) {
        //Lets check if a Salary due to be created as part of the Salary Payment

        logger.debug("To Employee Name=" + employeeSalaryPaymentBean.getToEmployeeName());
        logger.debug("From Employee Name=" + employeeSalaryPaymentBean.getFromEmployeeBean());

        if (employeeSalaryPaymentBean.isCreateSalaryDueAlso()) {
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
        //employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_PAY);
        employeeAuditBean.setType("" + EmployeeAuditBean.TRANSACTION_TYPE_ENUM.Salary);
        employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_SUBTRACT);
        // employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_SALARYPAY);
        employeeAuditBean.setOtherPartyName(employeeSalaryPaymentBean.getFromEmployeeName()); //This will be opposite party
        employeeAuditBean.setTransactionDate(employeeSalaryPaymentBean.getTransferDate());
        accountDAO.updateEmployeeAccountBalance(employeeAuditBean);

        //Audit Employee Salary Balance
        employeeAuditBean.setEmployeeid(employeeSalaryPaymentBean.getToEmployeeId());
        employeeAuditBean.setType(ANPConstants.EMPLOYEE_SALARY_AUDIT_TYPE_PAY);
        employeeDAO.updateEmployeeSalaryBalance(employeeAuditBean);
    }

    @Transactional(rollbackFor = Exception.class)
    //Create Salary and Update Employee:LastSalaryBalance
    //SUBTRACT PAYING PARTY BALANCE
    public void deleteEmpSalaryPayment(EmployeeSalaryPayment employeeSalaryPaymentBean) {

        logger.debug("To Employee Name=" + employeeSalaryPaymentBean.getToEmployeeName());
        logger.debug("From Employee Name=" + employeeSalaryPaymentBean.getFromEmployeeBean());
        int noOfRecordsDeleted;
        noOfRecordsDeleted = employeeDAO.deleteEmpSalaryPayment(employeeSalaryPaymentBean);
        if (noOfRecordsDeleted != 1) {
            throw new CustomAppException("Wrong Employee Salary Payment Deletion Entry", "SERVER.EMPLOYEE_SALARY_PAYMENT.NOTEXIST", HttpStatus.EXPECTATION_FAILED);
        }
        //Update/SUBTRACT From Employee Balance
        EmployeeAuditBean employeeAuditBean = new EmployeeAuditBean();
        employeeAuditBean.setOrgId(employeeSalaryPaymentBean.getOrgId());
        employeeAuditBean.setEmployeeid(employeeSalaryPaymentBean.getFromEmployeeId());
        employeeAuditBean.setAccountid(employeeSalaryPaymentBean.getFromAccountId());
        employeeAuditBean.setAmount(employeeSalaryPaymentBean.getAmount());
        //employeeAuditBean.setType(ANPConstants.EMPLOYEE_AUDIT_TYPE_PAY);
        employeeAuditBean.setType("" + EmployeeAuditBean.TRANSACTION_TYPE_ENUM.D_Salary);
        employeeAuditBean.setOperation(ANPConstants.OPERATION_TYPE_ADD);
        //employeeAuditBean.setForWhat(ANPConstants.EMPLOYEE_AUDIT_FORWHAT_SALARYPAY);
        employeeAuditBean.setOtherPartyName(employeeSalaryPaymentBean.getFromEmployeeName()); //This will be opposite party
        employeeAuditBean.setTransactionDate(employeeSalaryPaymentBean.getTransferDate());
        accountDAO.updateEmployeeAccountBalance(employeeAuditBean);

        //Audit Employee Salary Balance
        employeeAuditBean.setEmployeeid(employeeSalaryPaymentBean.getToEmployeeId());
        employeeAuditBean.setType(ANPConstants.EMPLOYEE_SALARY_AUDIT_TYPE_PAY);
        employeeDAO.updateEmployeeSalaryBalance(employeeAuditBean);


    }

    @Transactional(rollbackFor = Exception.class)
    public void updateEmployee(EmployeeBean employeeBean) {
        EmployeeBean employeeBeanFetched = employeeDAO.getEmployeeById(employeeBean.getOrgId(), employeeBean.getEmployeeId());
       //NickName is only updated when first or last name updated
        if (!employeeBeanFetched.getFirst().equalsIgnoreCase(employeeBean.getFirst()) ||
                !employeeBeanFetched.getLast().equalsIgnoreCase(employeeBean.getLast())) {

            if (ANPUtils.isNullOrEmpty(employeeBean.getLast()))
                employeeBean.setLast("");
            else if (ANPUtils.isNullOrEmpty(employeeBean.getFirst()))
                employeeBean.setFirst("");
            accountDAO.updateAccountNickName(employeeBean.getEmployeeId(), employeeBean.getOrgId(), generateAccountNickName(employeeBean));
        }

        if ( !(employeeBean.getInitialBalance() ==null && employeeBeanFetched.getInitialBalance()==null) &&  (employeeBean.getInitialBalance().longValue() != employeeBeanFetched.getInitialBalance().longValue())) {
            //This is to update initial balance in the backend.
            accountDAO.updateInitialBalanceField(employeeBean.getEmployeeId(), employeeBean.getOrgId(), employeeBean.getInitialBalance());
            AccountBean accountBean = new AccountBean();

            accountBean.setOrgId(employeeBean.getOrgId());
            accountBean.setOwnerid(employeeBean.getEmployeeId());
            logger.debug("AccountId = " + employeeBeanFetched.getAccountId());
            accountBean.setAccountId(employeeBeanFetched.getAccountId());
            accountBean.setInitialBalance(employeeBean.getInitialBalance());
            accountBean.setType(LOGIN_TYPE_EMPLOYEE);
            accountDAO.updateInitialBalance(accountBean);
            //process in accounDao from line 62-85
        }

        //@TODO paras : if there is change in the employeeInitialSalary balance field
        if (employeeBean.getInitialSalaryBalance() != null) {
            if (employeeBean.getInitialSalaryBalance().compareTo(new BigDecimal("0.0")) > 0) {
                createSalaryDueBasedOnInitialSalaryBalance(employeeBean);
            }
        }

        employeeDAO.updateEmployee(employeeBean);

    }
}//end class
