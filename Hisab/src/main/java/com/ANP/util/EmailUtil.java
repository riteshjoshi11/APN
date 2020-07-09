package com.ANP.util;

import com.ANP.repository.SystemConfigurationReaderDAO;
import org.springframework.http.HttpStatus;

import java.util.*;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;
import javax.mail.Transport;


public class EmailUtil {


    public void sendEmail(String body, String[] toAddresses, String[] ccAdresses,
                          String subject, List<String> attachmentList) throws Exception {

        if(attachmentList.isEmpty()) {
            throw new CustomAppException("No Attachments were found","SERVER.SEND_EMAIL.DOESNOTEXIST", HttpStatus.EXPECTATION_FAILED);
        }

        if(ANPUtils.isNullOrEmpty(toAddresses[0]) && ANPUtils.isNullOrEmpty(ccAdresses[0])){
            throw new CustomAppException("Addresses not found","SERVER.SEND_EMAIL.DOESNOTEXIST", HttpStatus.EXPECTATION_FAILED);
        }

        InternetAddress[] toAddressFormatted = new InternetAddress[toAddresses.length];
        for( int i=0; i < toAddressFormatted.length; i++ ) {
            toAddressFormatted[i] = new InternetAddress(toAddresses[i]);
        }

        InternetAddress[] ccAddressFormatted = new InternetAddress[ccAdresses.length];
        for(int i = 0 ; i< ccAddressFormatted.length;i++){
            ccAddressFormatted[i] = new InternetAddress(ccAdresses[i]);
        }
        Properties properties = System.getProperties();

        //Getting the SystemConfigurations settings into a map
        Map<String,String> systemConfigMap ;
        SystemConfigurationReaderDAO systemConfigurationReaderDAO = new SystemConfigurationReaderDAO();
        systemConfigMap = systemConfigurationReaderDAO.getSystemConfigurationMap();

        String host = systemConfigMap.get("EMAIL.UTILITY.HOST");
        String port = systemConfigMap.get("EMAIL.UTILITY.PORT");
        String fromEmailPassword = systemConfigMap.get("EMAIL.UTILITY.PASSWORD");
        String fromEmailAddress = systemConfigMap.get("EMAIL.UTILITY.FROMEMAILADDRESS");
        String trust = systemConfigMap.get("EMAIL.UTILITY.TRUST");

        if(ANPUtils.isNullOrEmpty(host)) {
            throw new CustomAppException("Host cannot be null","SERVER.SEND_EMAIL.DOESNOTEXIST", HttpStatus.EXPECTATION_FAILED);
        }
        else if(ANPUtils.isNullOrEmpty(port)){
            throw new CustomAppException("Port cannot be null","SERVER.SEND_EMAIL.DOESNOTEXIST", HttpStatus.EXPECTATION_FAILED);
        }

        // Setting up mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port",port);
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.ssl.trust", trust);
        properties.put("mail.smtp.auth", true);



        // creating session object to get properties
        Session session = Session.getDefaultInstance(properties,new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        fromEmailAddress, fromEmailPassword);// Specify the Username and the PassWord
            }
        });
        try
        {
            // MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, toAddressFormatted);
            message.addRecipients(Message.RecipientType.CC,ccAddressFormatted);
            message.setSubject(subject);



            // Set From Field: adding senders email to from field.
            message.setFrom(new InternetAddress(fromEmailAddress));

            Multipart multipart = new MimeMultipart();
            for(String str : attachmentList) {
                MimeBodyPart messageBodyPartForAttachments = new MimeBodyPart();
                DataSource source = new FileDataSource(str);
                messageBodyPartForAttachments.setDataHandler(new DataHandler(source));
                messageBodyPartForAttachments.setFileName(source.getName());
                multipart.addBodyPart(messageBodyPartForAttachments);
            }

            MimeBodyPart messageBodyPartForText = new MimeBodyPart();
            messageBodyPartForText.setText(body);
            multipart.addBodyPart(messageBodyPartForText);

            message.setContent(multipart);
            Transport.send(message);
            System.out.println("Mail successfully sent");
        }
        catch (MessagingException mex)
        {
            mex.printStackTrace();
        }
    }
}
