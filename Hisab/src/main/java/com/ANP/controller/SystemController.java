package com.ANP.controller;


import com.ANP.bean.SystemAuditBean;
import com.ANP.bean.SystemBean;
import com.ANP.repository.UISystemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/system")
public class SystemController {
    @Autowired
    UISystemDAO systemDAO;

    @Async
    @PostMapping(path = "/deleteDataForOrganization", produces = "application/json")
    public ResponseEntity deleteOrganizationTranaction(@RequestBody SystemBean systemBean)  {
        System.out.println("deleteOrganizationTranaction=" + systemBean);
        systemDAO.softDeleteOrganizationTransaction(systemBean);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @Async
    @PostMapping(path = "/deleteAuditData", produces = "application/json")
    public ResponseEntity deleteAuditData(@RequestBody SystemAuditBean systemAuditBean)
    {
        systemDAO.softDeleteAuditData(systemAuditBean.getOrgId(),systemAuditBean.getRecordId(),
                systemAuditBean.getIdentifier(),systemAuditBean.isDeleteAll());
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
