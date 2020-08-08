package com.ANP.controller;

import com.ANP.bean.ListParametersBean;
import com.ANP.bean.OrganizationRegistrationBean;
import com.ANP.bean.PhonebookBean;
import com.ANP.repository.PhonebookDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/phonebook")
public class PhonebookController {

    @Autowired
    PhonebookDAO phonebookDAO;

    @PostMapping(path = "/syncPhonebook", produces = "application/json" )
    public ResponseEntity syncPhonebook(@RequestBody PhonebookBean phonebookBean) {
        phonebookDAO.syncPhonebook(phonebookBean);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping(path = "/listPhonebookPaged", produces = "application/json" )
    public PhonebookBean listPhonebookPaged(@RequestBody ListParametersBean parametersBean) {
        return phonebookDAO.listPhonebookPaged(parametersBean);
    }

}
