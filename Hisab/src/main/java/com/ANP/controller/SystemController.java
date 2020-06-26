package com.ANP.controller;


import com.ANP.bean.SystemBean;
import com.ANP.repository.UISystemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/system")
public class SystemController {
    @Autowired
    UISystemDAO systemDAO;

    @PostMapping(path = "/deleteDataForOrganization", produces = "application/json")
    public ResponseEntity deleteOrganizationTranaction(@RequestBody SystemBean systemBean)
    {
        systemDAO.softDeleteOrganizationTransaction(systemBean.getOrgID(), systemBean.isDeleteCompanyData(), systemBean.isDeleteSalaryData(), systemBean.isDeleteAuditData(),
                systemBean.isDeleteEmployeeSalaryBalance(), systemBean.isDeleteEmployeeCompanyBalance(), systemBean.isDeleteCustomerBalance());
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
