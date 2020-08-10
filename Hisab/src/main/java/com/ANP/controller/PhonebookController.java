package com.ANP.controller;

import com.ANP.bean.PhoneBookListingBean;
import com.ANP.bean.PhonebookBean;
import com.ANP.repository.PhonebookDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/phonebook")
public class PhonebookController {

    @Autowired
    PhonebookDAO phonebookDAO;

    @PostMapping(path = "/syncPhonebook", produces = "application/json")

    /*
     * Each RawPhonebookContact contains contactName, Key (EMAIL|WEBSITE|PHONENO), Value
     */
    public ResponseEntity syncPhonebook(@RequestBody PhoneBookListingBean phoneBookListingBean) {
        phonebookDAO.syncPhonebook(phoneBookListingBean.getOrgId(), phoneBookListingBean.getEmployeeId(), phoneBookListingBean.getRawPhonebookContacts());
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    /*
    UI has to simply do -  PhonebookBean.getProcessedContactList to get the list of Contact to be shown
     */
    @PostMapping(path = "/listPhonebookPaged", produces = "application/json")
    public PhonebookBean listPhonebook(@RequestBody PhoneBookListingBean phoneBookListingBean) {
        return phonebookDAO.listProcessedContactsForUI(phoneBookListingBean.getOrgId(), phoneBookListingBean.getEmployeeId());
    }

}
