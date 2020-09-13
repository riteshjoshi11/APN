package com.ANP.service;

import com.ANP.util.SMSProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 *
 * This Class will handle the logic of user communication
 */

@Service
public class UserCommunicationHandler {
    /*
      * this method picks up OTP template and send the OTP to the user
      * THis will be interacting with SMS or EMAIL PROVIDERS
      * Currently this method will only interact with SMS Provider
     */
    @Autowired
    SMSProvider smsProvider;

    public void sendOTP(String mobile, String email, String otp) {
        //toto write a methord to get Message based on user preference
        String message = "one time verification code" + otp;
        smsProvider.sendSMS(mobile, message);
    }
}
