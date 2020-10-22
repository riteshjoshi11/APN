package com.ANP.controller;

import com.ANP.bean.PhoneBookListingBean;
import com.ANP.bean.PhonebookBean;
import com.ANP.repository.PhonebookDAO;
import com.ANP.service.offline.OfflineArchivePurgeProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/phonebook")
public class PhonebookController {
    @Autowired
    PhonebookDAO phonebookDAO;

    private static final Logger logger = LoggerFactory.getLogger(PhonebookController.class);



    /*
     * Each RawPhonebookContact contains contactName, Key (EMAIL|WEBSITE|PHONENO), Value
     */
    @Async
    @PostMapping(path = "/syncPhonebook", produces = "application/json")
    public ResponseEntity syncPhonebook(@Valid @RequestBody PhoneBookListingBean phoneBookListingBean) {
        phonebookDAO.syncPhonebook(phoneBookListingBean);
        logger.trace("input data=" + phoneBookListingBean.getRawPhonebookContacts());
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }


    /*
     * UI has to simply do -  PhonebookBean.getProcessedContactList to get the list of Contact to be shown
     */
    @PostMapping(path = "/listPhonebook", produces = "application/json")
    public PhonebookBean listPhonebook(@RequestParam  Long orgId, @RequestParam String employeeId) {
        return phonebookDAO.listProcessedContactsForUI(orgId, employeeId);
    }

}
