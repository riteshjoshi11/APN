package com.ANP.controller;

import com.ANP.bean.PhonebookBean;
import com.ANP.bean.RawPhonebookContact;
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
    public ResponseEntity syncPhonebook(@RequestParam long orgId, @RequestParam String employeeId, List<RawPhonebookContact> rawPhonebookContacts) {
        phonebookDAO.syncPhonebook(orgId, employeeId, rawPhonebookContacts);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    /*
    UI has to simply do -  PhonebookBean.getProcessedContactList to get the list of Contact to be shown
     */
    @PostMapping(path = "/listPhonebookPaged", produces = "application/json")
    public PhonebookBean listPhonebook(@RequestParam long orgId, @RequestParam String employeeId) {
        return phonebookDAO.listProcessedContactsForUI(orgId,employeeId);
    }

}
