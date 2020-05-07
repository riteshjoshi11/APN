package com.ANP.controller;

import com.ANP.bean.IntermediateLoginBean;
import com.ANP.service.LoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/login")
public class LoginController {

    @Autowired
    LoginHandler loginHandler;

    @PostMapping(path = "/getUserRegistrationStatusOnVerifiedOTP", produces = "application/json" )
    public boolean sendOTP(String mobileNumber) {
        return loginHandler.sendOTP(mobileNumber);
    }

    @PostMapping(path = "/getUserRegistrationStatusOnVerifiedOTP", produces = "application/json" )
    public IntermediateLoginBean getUserRegistrationStatusOnVerifiedOTP(String mobileNumber) {
        return loginHandler.isMobileRegistered(mobileNumber);
    }

  }
