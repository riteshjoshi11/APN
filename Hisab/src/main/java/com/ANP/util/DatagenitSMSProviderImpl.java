package com.ANP.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Qualifier("datagenitSMSProvider")
public class DatagenitSMSProviderImpl implements SMSProvider {


    @Autowired
    public RestTemplate restTemplate = new RestTemplate();


    public void getCount(String mobileno, String message) {
        final String uri = "http://nimbusit.co.in/api/checkbalance.asp?username=t1t2ritesh&password=55499174";
        System.out.println("RestTemplate " + restTemplate);
        String result = restTemplate.getForObject(uri, String.class);
        System.out.println(result);
    }

    //TODo working map the response json
    @Override
    /* success response
   {"status":"success","totalnumbers_sbmited":1,"campg_id":"213403","logid":"5f67a4546daa3","code":"100","ts":"2020-09-21 00:19:56"}

  Response 200 OK
  {"status":"failure","code":413,"desc":"Sender Id Not Approved","ts":"2020-09-21 00:16:20"}

  {"status":"success","totalnumbers_sbmited":1,"campg_id":50715,"logid":"5ddbd08dbb053","code":"100","ts":"2019-11-25 18:31:01"}
{"status":"failure","code":401,"desc":"No Auth","ts":"2018-11-20 08:34:04"}
{"status":"failure","code":402,"desc":"Invalid Auth","ts":"2018-11-20 13:03:48"}
{"status":"failure","code":406,"desc":"Message Not Passed","ts":"2018-11-20 12:56:45"}
{"status":"failure","code":407,"desc":"You Don't have HTTP API permission","ts":"2018-11-20 13:04:39"}
{"status":"failure","code":408,"desc":"Invalid Sender ID","ts":"2018-11-20 13:04:39"}
{"status":"failure","code":410,"desc":"Msisdn Not Passed","ts":"2018-11-20 12:56:45"}
{"status":"failure","code":411,"desc":"MSISDN Limit Exceed","ts":"2018-11-20 12:56:45"}
{"status":"failure","code":412,"desc":"Insufficient Balance!","ts":"2018-11-20 12:56:45"}
{"status":"failure","code":413,"desc":"Sender Id Not Approved","ts":"2018-11-20 12:56:45"}
{"status":"failure","code":414,"desc":"Given country not active for your account please contact admin","ts":"2018-11-20 12:56:45"}
 */
    public void sendSMS(String mobileno, String message) {
        System.out.println("sendSMS: DatagenitSMSProviderImpl invoked...");
        final String uri = "https://global.datagenit.com/API/sms-api.php?auth=D!~4623TENNtWwKo1&msisdn=" + mobileno + "&senderid=OTPSMS&message=" + message;
        System.out.println("URL for sending sms" + uri);
        System.out.println("RestTemplate " + restTemplate);
        String result = restTemplate.getForObject(uri, String.class);
        System.out.println("sendSMS: DatagenitSMSProviderImpl OTP Successful...");

        if(!ANPUtils.isNullOrEmpty(result) && (result.contains("failure"))) {
            throw new CustomAppException("SERVER.SEND_SMS_OTP.ERROR", "The status of SMS sending could not be determined", HttpStatus.EXPECTATION_FAILED);
        }
        System.out.println(result);
    }
}
