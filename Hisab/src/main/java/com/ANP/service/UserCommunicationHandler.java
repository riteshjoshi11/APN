package com.ANP.service;

import com.ANP.util.SMSProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("datagenitSMSProvider")
    SMSProvider smsProvider;

    public void sendOTP(String mobile, String email, String otp) {
        //toto write a methord to get Message based on user preference
        String message = "OTP for accessing Business Setu is:[" + otp + "] Please do not disclose it to anyone else";
        smsProvider.sendSMS(mobile, message);
    }
}
