package com.ANP.service;

import com.ANP.bean.EmployeeBean;
import com.ANP.bean.OrganizationRegistrationBean;
import com.ANP.repository.CalculationTrackerDAO;
import com.ANP.repository.OrgDAO;
import com.ANP.util.ANPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class OrganizationHandler {

    @Autowired
    OrgDAO orgDAO;

    @Autowired
    EmployeeHandler employeeHandler;

    @Autowired
    CalculationTrackerDAO calculationTrackerDAO;

    /*
     * The method create Organization as well as the first default employee (SUPER_ADMIN)
     * It also initializes (create) entry into Calculation Table
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrganization(OrganizationRegistrationBean organizationRegistrationBean) {
        String clientId = generateClientId();
        organizationRegistrationBean.getOrgBean().setClientId(clientId);
        long orgKey = orgDAO.createOrganization(organizationRegistrationBean.getOrgBean(), organizationRegistrationBean.getEmployeeBean());

        //Create Default Employee
        EmployeeBean employeeBean = organizationRegistrationBean.getEmployeeBean();
//        employeeBean.setTypeInt(ANPConstants.EMPLOYEE_TYPE_SUPER_ADMIN);
        employeeBean.setTypeInt(EmployeeBean.EmployeeTypeEnum.SUPER_ADMIN.getValue());

        employeeBean.setOrgId(orgKey);
        //setting BigDecimal to 0.0 to fix balance not getting updated for Admin / Company Account
        employeeBean.setInitialBalance(new BigDecimal("0.0"));
        employeeHandler.createEmployee(employeeBean);
        //Create/Initialize Organization Details so that we can update later.
        orgDAO.createOrganizationDetails(orgKey);
        //Create a virtual employee for tracking company account.
        EmployeeBean employeeBeanVirtual = new EmployeeBean();
        employeeBeanVirtual.setFirst("COMPANY");
        employeeBeanVirtual.setLast("ACCOUNT");
        employeeBeanVirtual.setMobile("0000000000");
        //employeeBeanVirtual.setTypeInt(ANPConstants.EMPLOYEE_TYPE_VIRTUAL);
        employeeBean.setTypeInt(EmployeeBean.EmployeeTypeEnum.VIRTUAL.getValue());
        //setting BigDecimal to 0.0 to fix balance not getting updated for Admin / Company Account
        employeeBeanVirtual.setInitialBalance(new BigDecimal("0.0"));
        employeeBeanVirtual.setOrgId(orgKey);
        employeeHandler.createEmployee(employeeBeanVirtual);

        //Initialize Calculation Tracker for the Org
        calculationTrackerDAO.createCalculationTracker(orgKey);
        return true;
    }

    public String generateClientId() {


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssS");
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDateTime = dateTime.format(formatter);


        Random rand = new Random();
        int randomNumber = (rand.nextInt(89999) + 10000);
        String randomNumberString = Integer.toString(randomNumber);
        return (formattedDateTime + "-" + randomNumberString);
    }
}