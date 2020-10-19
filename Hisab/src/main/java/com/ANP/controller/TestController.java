package com.ANP.controller;

import com.ANP.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping(path = "/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    TestService testService;

    @Async
    @PostMapping(path = "/createBulkOrganization", produces = "application/json")
    public ResponseEntity createBulkOrganization() {
        Random rand = new Random();

        for (int i = 1; i <= 10000; i++) {
            Long unique = (long) Math.floor(Math.random() * 9000000000L);
            logger.trace("Creating Org for Iteration=" + unique);
            testService.createOrgs(i, unique);
        }

        return new ResponseEntity<>("Success", HttpStatus.OK);

    }


}
