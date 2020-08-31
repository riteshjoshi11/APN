package com.ANP.service;
import com.ANP.bean.OTPBean;
import com.ANP.bean.Token;
import com.ANP.repository.OTPDAO;
import com.ANP.util.ANPUtils;
import com.ANP.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Random;
@Service
public class OTPHandler {
    @Autowired
    OTPDAO otpdao;

    public boolean sendOTP(String mobile) {
        String otp = generateNumberOTP(6);
        String testModeOTP = System.getProperty("TestModeOTP");
        if (!ANPUtils.isNullOrEmpty(testModeOTP)) {
            System.out.println("Test Mode is enabled to setting OTP to [" + testModeOTP + "]");
            otp = testModeOTP;
        }
        OTPBean otpBean = new OTPBean();
        otpBean.setMobileNumber(mobile);
        otpBean.setOtp(otp);
        int dbCreateStatus = otpdao.createOTP(otpBean);
        if (dbCreateStatus > 1) {
            //SEND SMS
            //IF DB CREATE IS SUCCESSFUL THEN SEND SUCCESS
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
