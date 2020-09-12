package com.ANP.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class sendSmsNimbus implements SendOtp {

    private String userName;
    private String password;
    private String senderId;

    @Autowired
    public RestTemplate restTemplate = new RestTemplate();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public void getCount(String mobileno, String message) {
        final String uri = "http://nimbusit.co.in/api/checkbalance.asp?username=t1t2ritesh&password=55499174";
        System.out.println("RestTemplate " + restTemplate);
        String result = restTemplate.getForObject(uri, String.class);
        System.out.println(result);
    }


    @Override
    public void sendSMS(String mobileno, String message) {

        final String uri = "http://nimbusit.co.in/api/swsendSingle.asp?username=t1t2ritesh&password=55499174&sender=SMSOTP&sendto=" + mobileno + "&message=" + message;

        System.out.println("URL for sending sms" + uri);
        System.out.println("RestTemplate " + restTemplate);
        String result = restTemplate.getForObject(uri, String.class);
        System.out.println(result);


    }


}
