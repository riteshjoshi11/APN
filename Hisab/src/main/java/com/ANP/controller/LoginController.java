package com.ANP.controller;

import com.ANP.bean.IntermediateLoginBean;
import com.ANP.bean.OTPBean;
import com.ANP.bean.SuccessLoginBean;
import com.ANP.repository.OTPDAO;
import com.ANP.service.LoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/login")
public class LoginController {

    @Autowired
    LoginHandler loginHandler;

    @Autowired
    OTPDAO otpdao;

    @PostMapping(path = "/sendOTP", produces = "application/json" )
    public ResponseEntity sendOTP(@RequestParam String mobileNumber) {
        ResponseEntity<String> responseEntity = null;
        if(loginHandler.sendOTP(mobileNumber)) {
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.OK);
        }
        return responseEntity;
    }

    @PostMapping(path = "/verifyOTP", produces = "application/json" )
    public ResponseEntity verifyOTP(OTPBean otpBean) {
        ResponseEntity<String> responseEntity = null;
        if(otpdao.validateOTP(otpBean)){
            responseEntity = new ResponseEntity<>("Success", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("Failure", HttpStatus.EXPECTATION_FAILED);
        }
        return responseEntity;
    }

    /*
        Once mobile number is verified using OTP then this method will be invoked by UI.
     */
    @PostMapping(path = "/getUserRegistrationStatusOnVerifiedOTP", produces = "application/json" )
    public IntermediateLoginBean getUserRegistrationStatusOnVerifiedOTP(@RequestParam String mobileNumber) {
        return loginHandler.isMobileRegistered(mobileNumber);
    }

    /*
    Once user has either selected his/her organization from the one or more organization, this will give the details of that organization.
    */
    @PostMapping(path = "/getLoggedInUserDetails", produces = "application/json" )
    public SuccessLoginBean getLoggedInUserDetails(@RequestParam  String mobileNumber, @RequestParam long orgId) {
        return loginHandler.getLoggedInUserDetails(mobileNumber,orgId);
    }

}
