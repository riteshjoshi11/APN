package com.ANP.service;

import com.ANP.bean.Organization;
import com.ANP.repository.OrgDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationHandler {

    @Autowired
    OrgDAO orgDAO;

    //Transaction not required here
    public boolean createOrganization(Organization organizationBean) {
      orgDAO.createOrganization(organizationBean);
      return true;
    }
}