package com.ANP.service;

import com.ANP.bean.CustomerBean;
import com.ANP.bean.EmployeeBean;
import com.ANP.bean.Organization;
import com.ANP.bean.OrganizationRegistrationBean;
import com.ANP.controller.CustomerController;
import com.ANP.controller.EmployeeController;
import com.ANP.controller.OrganizationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class TestService {
    private static final Logger logger = LoggerFactory.getLogger(TestService.class);

    @Autowired
    OrganizationController organizationController;

    @Autowired
    CustomerController customerController;

    @Autowired
    EmployeeController employeeController;

    @Async
    public void createOrgs(int counter, Long unique) {
        logger.trace("Entering : createOrgs: " + counter);
        OrganizationRegistrationBean organizationRegistrationBean = new OrganizationRegistrationBean();

        Organization org = new Organization();

        org.setOrgName("Test-" + counter);
        org.setState("Maharashtra");
        org.setCity("Pune");
        organizationRegistrationBean.setOrgBean(org);

        EmployeeBean employeeBean = new EmployeeBean();
        employeeBean.setMobile("" + unique);
        employeeBean.setFirst("Test-" + counter);
        organizationRegistrationBean.setEmployeeBean(employeeBean);

        organizationController.createOrganization(organizationRegistrationBean);
        logger.trace("Exiting Org for Iteration=" + counter);
    }

    @Async
    public void createCustomers(int counter, Long unique) {
        logger.trace("Entering : createCustomers: " + counter);

        CustomerBean customerBean = new CustomerBean();
        customerBean.setOrgId(66);
        customerBean.setName("Test User-" + unique);
        customerBean.setFirmname("Test Firm-" + unique );
        customerBean.setCreatedbyId("E199");

        customerController.createCustomer(customerBean);

        logger.trace("Exiting Org for createCustomers=" + counter);
    }

    @Async
    public void createEmployee(int counter, Long unique) {
        logger.trace("Entering : createEmployee: " + counter);
        long start = System.currentTimeMillis();

        EmployeeBean employeeBean = new EmployeeBean();
        employeeBean.setOrgId(66);
        employeeBean.setMobile(""+unique);
        employeeBean.setFirst("Fist-" + unique);
        employeeBean.setLast("Last" );

        employeeBean.setCreatedbyId("E199");

        employeeController.createEmployee(employeeBean);
        long end = System.currentTimeMillis();

        logger.trace("Exiting Org for createEmployee=" + counter);
        logger.trace("Time Taken[" + (end-start)/1000 + "] s");

    }

}
