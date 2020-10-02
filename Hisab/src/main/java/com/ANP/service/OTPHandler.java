package com.ANP.service;
import com.ANP.bean.OTPBean;
import com.ANP.repository.OTPDAO;
import com.ANP.util.ANPUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class OTPHandler {
    @Autowired
    OTPDAO otpdao;

    @Autowired
    UserCommunicationHandler userCommunicationHandler;

    public boolean sendOTP(String mobile) {
        String otp = "";
        if(!ANPUtils.isTestEnvironment()) {
            otp = generateNumberOTP(6);
        } else {
            otp = ANPUtils.getTestModeOTP();
        }
        OTPBean otpBean = new OTPBean();
        otpBean.setMobileNumber(mobile);
        otpBean.setOtp(otp);
        int dbCreateStatus = otpdao.createOTP(otpBean);
        //SEND OTP ONLY WHEN PROD ENV (MENAING TESTMODE OTP IS NOT GIVEN
        if (dbCreateStatus > 0 && !ANPUtils.isTestEnvironment()) {
            //SEND SMS

            System.out.println("will send SMS");
            // sendSms.sendSMS(mobile,"Pin for verification"+otp);

            //IF DB CREATE IS SUCCESSFUL THEN SEND SUCCESS
            userCommunicationHandler.sendOTP(mobile, "", otp);
        }
        return true;
    }

    public void verifyOTP(OTPBean otpBean) {
        otpdao.validateOTP(otpBean);
    }

    public static String generateNumberOTP(int len) {
        System.out.println("Generating OTP using random() : ");
        System.out.print("You OTP is: ");
        // Using numeric values
        String numbers = "0123456789";
        // Using random method
        Random rndm_method = new Random();
        char[] otp = new char[len];
        for (int i = 0; i < len; i++) {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return String.valueOf(otp);

    }//

}
