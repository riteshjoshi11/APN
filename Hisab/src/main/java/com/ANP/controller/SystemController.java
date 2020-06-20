package com.ANP.controller;


import com.ANP.bean.SystemBean;
import com.ANP.repository.SystemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/system")
public class SystemController {
    @Autowired
    SystemDAO systemDAO;

    @PostMapping(path = "/deleteOrganizationTransaction", produces = "application/json")
    public ResponseEntity deleteOrganizationTranaction(@RequestBody SystemBean systemBean)
    {
        systemDAO.deleteOrganizationTransaction(systemBean.getOrgID(), systemBean.isDeleteCompanyData(), systemBean.isDeleteSalaryData(), systemBean.isDeleteAuditData());
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
