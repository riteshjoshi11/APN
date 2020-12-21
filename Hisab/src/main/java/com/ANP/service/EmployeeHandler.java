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
        logger.trace("updateEmployee: Input employeeBean:" + employeeBean);
        EmployeeBean employeeBeanFetched = employeeDAO.getEmployeeById(employeeBean.getOrgId(), employeeBean.getEmployeeId());
        logger.trace("updateEmployee: Fetched from DB employeeBean:" + employeeBeanFetched);


        boolean nameChanged = false ; //track variable to determine whether first or last name changed

        //if input and fetched firstName is not null and there is change in the value
        if ( (!ANPUtils.isNullOrEmpty(employeeBean.getFirst())
                && !ANPUtils.isNullOrEmpty(employeeBeanFetched.getFirst()) )
                && !(employeeBean.getFirst().trim().equalsIgnoreCase(employeeBeanFetched.getFirst().trim()))) {
            nameChanged = true;
        }

        //if firstName (above) not changed then check for update in the last name
        //AND if input and fetched LastName is not null and there is change in the value

        if(!nameChanged && (!ANPUtils.isNullOrEmpty(employeeBean.getLast())
                && !ANPUtils.isNullOrEmpty(employeeBeanFetched.getLast()) )
                && !(employeeBean.getLast().trim().equalsIgnoreCase(employeeBeanFetched.getLast().trim()))) {

        }

        if(nameChanged) {
            logger.trace("updateEmployee: Detected Name change for employee with ID [" + employeeBeanFetched.getEmployeeId() + "]");
            if(employeeBeanFetched.getTypeInt()==EmployeeBean.EmployeeTypeEnum.VIRTUAL.getValue()) {
                logger.trace("employee with ID [" + employeeBeanFetched.getEmployeeId() + "] is an virtual account and we do not allow name change on that");
                throw new CustomAppException("Name updated not allowed on this account", "SERVER.UPDATE_EMPLOYEE_VIRTUAL_CHANGE_NOTALLOWED", HttpStatus.EXPECTATION_FAILED );
            }
            accountDAO.updateAccountNickName(employeeBean.getEmployeeId(), employeeBean.getOrgId(), generateAccountNickName(employeeBean));
        }

        if ( !(employeeBean.getInitialBalance() ==null && employeeBeanFetched.getInitialBalance()==null)
                &&  (employeeBean.getInitialBalance().longValue() != employeeBeanFetched.getInitialBalance().longValue())) {
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
        }

        // if there is change in the employeeInitialSalary balance field
        if (employeeBean.getInitialSalaryBalance() != null) {
            if (employeeBean.getInitialSalaryBalance().compareTo(new BigDecimal("0.0")) > 0) {
                createSalaryDueBasedOnInitialSalaryBalance(employeeBean);
            }
        }

        if((employeeBeanFetched.getTypeInt()==EmployeeBean.EmployeeTypeEnum.SUPER_ADMIN.getValue() ||
                 employeeBeanFetched.getTypeInt()==EmployeeBean.EmployeeTypeEnum.VIRTUAL.getValue())) {
            logger.trace("updateEmployee: The account is either restricted account. Applying different checks");
            //If fetched employee is of type SUPER_ADMIN then we need to check that EmployeeType is not other than Business partner (as we have made SUPER ADMIN as business partner )
            // and we do not allow any updates on SUPER_ADMIN
            if (employeeBeanFetched.getTypeInt()!=employeeBean.getTypeInt() &&
                       ((employeeBeanFetched.getTypeInt()==EmployeeBean.EmployeeTypeEnum.VIRTUAL.getValue()
                           && employeeBean.getTypeInt()!=EmployeeBean.EmployeeTypeEnum.Default.getValue())
                       || (employeeBeanFetched.getTypeInt()==EmployeeBean.EmployeeTypeEnum.SUPER_ADMIN.getValue()
                           && employeeBean.getTypeInt()!=EmployeeBean.EmployeeTypeEnum.BusinessPartner.getValue())) ) {
                throw new CustomAppException("[Type] updates not allowed on this user/account", "SERVER.RESTRICTED_ACCOUNT.CHANGE_NOTALLOWED", HttpStatus.EXPECTATION_FAILED);
            }

            //SUPER ADMIN & VIRTUAL: LOGIN cannot be CHANGED
            if (employeeBean.isLoginrequired() != employeeBeanFetched.isLoginrequired()) {
                throw new CustomAppException("The [LoginRequired] cannot be changed for this user/account", "SERVER.RESTRICTED_ACCOUNT.LOGIN_CHANGE_NOTALLOWED", HttpStatus.EXPECTATION_FAILED);
            }

            if(!ANPUtils.isNullOrEmpty(employeeBean.getMobile2())
                    && !(employeeBean.getMobile2().trim().equalsIgnoreCase(employeeBeanFetched.getMobile2()))
                    && employeeBeanFetched.getTypeInt() ==EmployeeBean.EmployeeTypeEnum.VIRTUAL.getValue()) {
                   //Virtual : Company Account
                    throw new CustomAppException("[Mobile2] updates not allowed on this account", "SERVER.RESTRICTED_ACCOUNT.UPDATE_EMPLOYEE_MOBILE_ONVIRTUAL_NOTALLOWED", HttpStatus.EXPECTATION_FAILED);
           }

            //----- Update handling
            if(employeeBeanFetched.getTypeInt()==EmployeeBean.EmployeeTypeEnum.VIRTUAL.getValue()) {
                employeeBean.setTypeInt(EmployeeBean.EmployeeTypeEnum.VIRTUAL.getValue());
                logger.trace("Updated typeInt for virtual ");
            } else if (employeeBeanFetched.getTypeInt()== EmployeeBean.EmployeeTypeEnum.SUPER_ADMIN.getValue()) {
                employeeBean.setTypeInt(EmployeeBean.EmployeeTypeEnum.SUPER_ADMIN.getValue());
                logger.trace("Updated typeInt for super admin ");

            }
       }
       employeeDAO.updateEmployee(employeeBean);
    }

    public EmployeeBean getEmployeeById(Long orgId, String employeeId) {
        logger.trace("Entering : getEmployeeById : orgId[" + orgId + "] employeeId [" + employeeId + "]");
        EmployeeBean retEmployeeBean = employeeDAO.getEmployeeById(orgId,employeeId);
        if(retEmployeeBean!=null) {
            Integer empTypeInteger = retEmployeeBean.getTypeInt() ;
            String empType = retEmployeeBean.getType();
            //if SUPER ADMIN then make it business partner for UI to display
            if(empTypeInteger==EmployeeBean.EmployeeTypeEnum.SUPER_ADMIN.getValue()) {
                empTypeInteger = EmployeeBean.EmployeeTypeEnum.BusinessPartner.getValue();
                logger.trace("Changed SUPER ADMIN to Business Partner");
                empType = "Business Partner" ;
            } else if(empTypeInteger==EmployeeBean.EmployeeTypeEnum.VIRTUAL.getValue()) {
                //if VIRTUAL then make it Default for UI to display
                empTypeInteger = EmployeeBean.EmployeeTypeEnum.VIRTUAL.getValue();
                logger.trace("Changed VIRTUAL to Default");
                empType = "Default" ;
            }
            //retEmployeeBean.setType(empType);
            retEmployeeBean.setTypeInt(empTypeInteger);
            retEmployeeBean.setType(empType);
        }
        logger.trace("Exiting : getEmployeeById : retEmployeeBean[" + retEmployeeBean + "]");
        return retEmployeeBean;
    }

    }//end class
