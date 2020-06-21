package com.ANP.service;

import com.ANP.bean.CalculationTrackerBean;
import com.ANP.bean.EmployeeBean;
import com.ANP.bean.Organization;
import com.ANP.bean.OrganizationRegistrationBean;
import com.ANP.repository.CalculationTrackerDAO;
import com.ANP.repository.CustomerDao;
import com.ANP.repository.EmployeeDAO;
import com.ANP.repository.OrgDAO;
import com.ANP.util.ANPConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
      long orgKey = orgDAO.createOrganization(organizationRegistrationBean.getOrgBean(),organizationRegistrationBean.getEmployeeBean());

      //Create Default Employee
      EmployeeBean employeeBean = organizationRegistrationBean.getEmployeeBean();
      employeeBean.setType(ANPConstants.EMPLOYEE_TYPE_SUPER_ADMIN);
      employeeBean.setOrgId(orgKey);
      employeeHandler.createEmployee(employeeBean);

      //Create a virtual employee for tracking company account.
      EmployeeBean employeeBeanVirtual = new EmployeeBean();
      employeeBeanVirtual.setFirst("COMPANY");
      employeeBeanVirtual.setLast("ACCOUNT");
      employeeBeanVirtual.setMobile("0000000000");
      employeeBeanVirtual.setType(ANPConstants.EMPLOYEE_TYPE_VIRTUAL);
      employeeBeanVirtual.setOrgId(orgKey);
      employeeHandler.createEmployee(employeeBeanVirtual);

      //Initialize Calculation Tracker for the Org
      calculationTrackerDAO.createCalculationTracker(orgKey);
      return true;
    }
}