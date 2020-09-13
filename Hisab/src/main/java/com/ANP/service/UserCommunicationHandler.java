package com.ANP.service;

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
    public void sendOTP(String mobile, String email, String otp) {

    }
}
