package com.ANP.service;

import com.ANP.bean.EmployeeBean;
import com.ANP.bean.Organization;
import com.ANP.bean.OrganizationRegistrationBean;
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
    EmployeeDAO employeeDAO;

    /*
       The method create Organization as well as the first default employee (SUPER_ADMIN)
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrganization(OrganizationRegistrationBean organizationRegistrationBean) {
      orgDAO.createOrganization(organizationRegistrationBean.getOrgBean());
      EmployeeBean employeeBean = organizationRegistrationBean.getEmployeeBean();
      employeeBean.setType(ANPConstants.EMPLOYEE_TYPE_SUPER_ADMIN);
      employeeDAO.createEmployee(employeeBean);
      return true;
    }
}