package com.ANP.util;

import org.springframework.stereotype.Component;

@Component
public interface SendOtp {
    public void sendSMS(String mobileno, String message);
}
