package com.ANP.controller;

import com.ANP.bean.OrganizationRegistrationBean;
import com.ANP.service.OrganizationHandler;

import com.ANP.repository.OrgDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/organization")
public class OrganizationController {
    @Autowired
    private OrgDAO orgdao;
    @Autowired
    private OrganizationHandler orghandler;

    @PostMapping(path = "/createOrganization", produces = "application/json" )
    public ResponseEntity createOrganization(@Valid @RequestBody OrganizationRegistrationBean organizationRegistrationBean) {
        System.out.println("Controller:[" + organizationRegistrationBean.getOrgBean().getOrgName() + "]");
        orghandler.createOrganization(organizationRegistrationBean);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}