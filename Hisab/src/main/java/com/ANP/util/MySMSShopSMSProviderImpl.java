package com.ANP.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Qualifier("MySMSShopSMSProvider")
public class MySMSShopSMSProviderImpl implements SMSProvider {

    @Autowired
    public RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendSMS(String mobileno, String message) {
        System.out.println("sendSMS: NimbusSMSProviderImpl invoked...");

        final String uri = "http://mysmsshop.in/http-api.php?username=businesssetu&password=Echoing$$999&senderid=BSSetu&route=1&number=" + mobileno + "&message=" + message;

        System.out.println("URL for sending sms" + uri);
        System.out.println("RestTemplate " + restTemplate);
        String result = restTemplate.getForObject(uri, String.class);
        System.out.println(result);
    }
}
