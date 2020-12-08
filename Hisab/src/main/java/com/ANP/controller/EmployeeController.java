package com.ANP.controller;

import com.ANP.bean.*;
import com.ANP.repository.EmployeeDAO;
import com.ANP.service.EmployeeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {

    @Autowired
    private EmployeeHandler employeeHandler;

    @Autowired
    private EmployeeDAO employeeDAO;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);


    @PostMapping(path = "/createEmployee", produces = "application/json")
    public ResponseEntity createEmployee(@Valid @RequestBody EmployeeBean employeeBean) {
        employeeHandler.createEmployee(employeeBean);
        return new ResponseEntity<>("Success", HttpStatus.OK);

    }

    @PostMapping(path = "/updateEmployee", produces = "application/json")
    public ResponseEntity updateEmployee(@Valid @RequestBody EmployeeBean employeeBean) {
        logger.trace("Entering updateEmployee(): EmployeeBean:" + employeeBean);
        Instant start = Instant.now();
        employeeHandler.updateEmployee(employeeBean);
        logger.trace("Exiting updateEmployee() : Time Taken[" + Duration.between(start, Instant.now()).toMillis() + "]");

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/createEmpSalary", produces = "application/json")
    public ResponseEntity createEmployeeSalary(@Valid @RequestBody EmployeeSalary employeeSalaryBean) {
        employeeHandler.createSalary(employeeSalaryBean);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }


    @PostMapping(path = "/createEmpSalaryPayment", produces = "application/json")
    public ResponseEntity createEmployeeSalaryPayment(@Valid @RequestBody EmployeeSalaryPayment employeeSalaryPayment) {
        employeeHandler.createSalaryPayment(employeeSalaryPayment);
        return new ResponseEntity<>("Success", HttpStatus.OK);
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

    @PostMapping(path = "/getEmployeeAccountsByName", produces = "application/json")
    public List<AccountBean> getEmployeeAccountsByName(@RequestBody AccountBean accountBean) {
        return employeeDAO.getEmployeeAccountsByNames(accountBean);
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

    @GetMapping(path = "/getEmployeeById", produces = "application/json")
    public EmployeeBean getEmployeeById(@RequestParam Long orgId, @RequestParam String employeeId) {
        return employeeDAO.getEmployeeById(orgId, employeeId);
    }

    @PostMapping(path = "/deleteEmpSalaryDue", produces = "application/json")
    public ResponseEntity deleteEmpSalaryDue(@RequestBody EmployeeSalary employeeSalary) {
        employeeHandler.deleteEmpSalaryDue(employeeSalary);
        return new ResponseEntity<>("Success", HttpStatus.OK);

    }

    @PostMapping(path = "/deleteEmpSalaryPayment", produces = "application/json")
    public ResponseEntity deleteEmpSalaryPayment(@RequestBody EmployeeSalaryPayment employeeSalaryPayment) {
        employeeHandler.deleteEmpSalaryPayment(employeeSalaryPayment);
        return new ResponseEntity<>("Success", HttpStatus.OK);

    }
}
