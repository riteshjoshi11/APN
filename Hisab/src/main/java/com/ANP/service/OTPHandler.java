package com.ANP.service;
import com.ANP.bean.OTPBean;
import com.ANP.repository.OTPDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class OTPHandler {
    @Autowired
    OTPDAO otpdao;

    public boolean sendOTP(String mobile) {
        String otp=generateNumberOTP(6);
        if("True".equalsIgnoreCase(System.getProperty("TestMode"))){
            System.out.println("Test Mode is enabled to setting OTP to 250250");
            otp="250250";
        }
        OTPBean otpBean = new OTPBean();
        otpBean.setMobileNumber(mobile);
        otpBean.setOtp(otp);
        int dbCreateStatus = otpdao.createOTP(otpBean);
        if(dbCreateStatus>1) {
            //SEND SMS
            //IF SEND SMS IS SUCCESSFUL THEN SEND SUCCESS
        }
        return true;
    }

    public static String generateNumberOTP(int len)
    {
        System.out.println("Generating OTP using random() : ");
        System.out.print("You OTP is: ");
        // Using numeric values
        String numbers = "0123456789";
        // Using random method
        Random rndm_method = new Random();
        char[] otp = new char[len];
        for (int i = 0; i < len; i++)
        {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return String.valueOf(otp);

    }//

    public static void main(String[] args)
    {
        int length = 6;
        System.out.println(generateNumberOTP(length));
    }
}
