package com.ANP.controller;

import com.ANP.bean.OrganizationRegistrationBean;
import com.ANP.service.OrganizationHandler;

import com.ANP.repository.OrgDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(path = "/organization")
public class OrganizationController {
    @Autowired
    private OrgDAO orgdao;
    @Autowired
    private OrganizationHandler orghandler;

    @PostMapping(path = "/createOrganization", produces = "application/json" )
    public ResponseEntity createOrganization(@RequestBody OrganizationRegistrationBean organizationRegistrationBean) {
        boolean isOrganizationCreated = orghandler.createOrganization(organizationRegistrationBean);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}