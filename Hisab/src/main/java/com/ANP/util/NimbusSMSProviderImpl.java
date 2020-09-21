package com.ANP.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Qualifier("nimbusSMSProvider")
public class NimbusSMSProviderImpl implements SMSProvider {

    @Autowired
    public RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendSMS(String mobileno, String message) {
        System.out.println("sendSMS: NimbusSMSProviderImpl invoked...");

        final String uri = "http://nimbusit.co.in/api/swsendSingle.asp?username=t1niteshindore&password=43138673&sender=ATZSMS&sendto=" + mobileno + "&message=" + message;

        System.out.println("URL for sending sms" + uri);
        System.out.println("RestTemplate " + restTemplate);
        String result = restTemplate.getForObject(uri, String.class);
        System.out.println(result);
   }
}
