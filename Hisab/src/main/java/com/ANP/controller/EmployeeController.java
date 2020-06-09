package com.ANP.controller;

import com.ANP.bean.*;
import com.ANP.repository.EmployeeDAO;
import com.ANP.service.EmployeeHandler;
import com.ANP.util.ANPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {

    @Autowired
    private EmployeeHandler employeeHandler;

    @Autowired
    private EmployeeDAO employeeDAO;


    @PostMapping(path = "/createEmployee", produces = "application/json")
    public ResponseEntity createEmployee(@Valid @RequestBody EmployeeBean employeeBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isEmployeeCreated = employeeHandler.createEmployee(employeeBean);
        if (isEmployeeCreated) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }

    @PostMapping(path = "/createEmpSalary", produces = "application/json")
    public ResponseEntity createEmployeeSalary(@Valid @RequestBody EmployeeSalary employeeSalaryBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isEmployeeSalCreated = employeeHandler.createSalary(employeeSalaryBean);
        if (isEmployeeSalCreated) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }


    @PostMapping(path = "/createEmpSalaryPayment", produces = "application/json")
    public ResponseEntity createEmployeeSalaryPayment(@Valid @RequestBody EmployeeSalaryPayment employeeSalaryPayment) {
        employeeHandler.createSalaryPayment(employeeSalaryPayment);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/updateLoginRequired", produces = "application/json")
    @ResponseBody
    public ResponseEntity updateLoginRequired(@RequestBody EmployeeBean employeeBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isupdateLoginRequired = employeeHandler.updateLoginRequired(employeeBean.getEmployeeId(), employeeBean.getLoginrequired());
        if (isupdateLoginRequired) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }

    @PostMapping(path = "/updateMobile", produces = "application/json")
    @ResponseBody
    public ResponseEntity updateMobile(@RequestBody EmployeeBean employeeBean) {
        ResponseEntity<String> responseEntity = null;
        boolean isupdateMobile = employeeHandler.updateMobile(employeeBean.getEmployeeId(), employeeBean.getMobile());
        if (isupdateMobile) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }

    @PostMapping(path = "/updateSalaryBalance", produces = "application/json")
    public ResponseEntity updateSalaryBalance(@RequestParam String EmployeeId, @RequestParam double balance, @RequestParam String operation) {
        ResponseEntity<String> responseEntity = null;
        boolean isupdateSalaryBalance = employeeHandler.UpdateEmpSalaryBalance(EmployeeId, balance, operation);
        if (isupdateSalaryBalance) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }

    /*
       This method will mainly used by UI fill Employee List in the Employee Module
       orgID: mandatory: if not provided return error
       firstName: can be empty
       lastName: can be empty
       Main Logic:
       -------------
       (Search at-least by orgId) AND (if firstName OR lastName) provided then try to search using those as well. Please do not forget to use %LIKE% for names
       Return: EmployeeBean with only EmployeeID, First, LastName populated (Nothing else, for the optimization we are doing this)
    */
    @PostMapping(path = "/getEmployeeListByName", produces = "application/json")
    public List<EmployeeBean> getEmployeeListByName(@RequestBody EmployeeBean employeeBean) {
        return employeeDAO.searchEmployees(employeeBean);
    }


    /*
     * This method returns the list of employee with almost all employee attributes along with Salary and Current Balance
     * This will be used in list employee screen
     * The filter can be dynamic and can start with "e.<employee table attribute>"
     */
    @PostMapping(path = "/listEmployeesWithBalancePaged", produces = "application/json")
    public List<EmployeeBean> listEmployeesWithBalancePaged(@Valid @RequestBody ListParametersBean listParametersBean) {
        return employeeDAO.listEmployeesWithBalancePaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
    }
    /*
     * This method returns the list of employee  Salaries with almost all employee attributes
     * This will be used in list employee Salary screen
     * The filter can be dynamic and can start with "e.<employee table attribute>" or "empsal.<Employee Salary Table attribute>"
     */
    @PostMapping(path = "/listEmpSalariesPaged", produces = "application/json")
    public List<EmployeeSalary> listEmpSalariesPaged(@Valid @RequestBody ListParametersBean listParametersBean) {
        return employeeDAO.listEmpSalariesPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
    }

    @PostMapping(path = "/listEmpPaidSalariesPaged", produces = "application/json")
    public List<EmployeeSalaryPayment> listEmpPaidSalariesPaged(@Valid @RequestBody ListParametersBean listParametersBean) {
        return employeeDAO.listEmpPaidSalariesPaged(listParametersBean.getOrgID(), listParametersBean.getSearchParam(), listParametersBean.getOrderBy(),
                listParametersBean.getNoOfRecordsToShow(), listParametersBean.getStartIndex());
    }
}
