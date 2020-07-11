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
    /*
     * This method is common util method for sending email
     * It reads the host / port / fromAddress information from the systemconfiguration
     * host and from address is mandatory
     * Optional:
     * * default port 25 (if you don't specify as part of SystemConf
     * If your SMTP gateway supports SSL then - You can enable SSL/TLS using: EMAIL.UTILITY.SSLEnabled=true
     * If your SMTP gateway password protected: EMAIL.UTILITY.PASSWORD=<Actual Password>
     *
     */
    public void sendEmail(String body, String[] toAddresses, String[] ccAdresses,
                          String subject, List<String> attachmentList) throws Exception {

        //atleast one field from toAddresses or ccAddresses must be present
        if (toAddresses == null || (ANPUtils.isNullOrEmpty(toAddresses[0]) && (ccAdresses == null || ANPUtils.isNullOrEmpty(ccAdresses[0])))) {
            throw new CustomAppException("No ToAddress or CCAddress present for sending Email", "SERVER.SEND_EMAIL.TOORCC_DOESNOTEXIST", HttpStatus.EXPECTATION_FAILED);
        }

        InternetAddress[] toAddressFormatted = new InternetAddress[toAddresses.length];
        for (int i = 0; i < toAddressFormatted.length; i++) {
            toAddressFormatted[i] = new InternetAddress(toAddresses[i]);
        }

        InternetAddress[] ccAddressFormatted = new InternetAddress[ccAdresses.length];
        for (int i = 0; i < ccAddressFormatted.length; i++) {
            ccAddressFormatted[i] = new InternetAddress(ccAdresses[i]);
        }

        Properties properties = System.getProperties();


        //Getting the SystemConfigurations settings into a map
        Map<String, String> systemConfigMap;
        SystemConfigurationReaderDAO systemConfigurationReaderDAO = new SystemConfigurationReaderDAO();
        systemConfigMap = systemConfigurationReaderDAO.getSystemConfigurationMap();

        String host = systemConfigMap.get("EMAIL.UTILITY.HOST");
        String port = systemConfigMap.get("EMAIL.UTILITY.PORT");

        if (ANPUtils.isNullOrEmpty(host)) {
            throw new CustomAppException("Host cannot be null", "SERVER.SEND_EMAIL.DOESNOTEXIST", HttpStatus.EXPECTATION_FAILED);
        }

        // Setting up mail server host
        properties.put("mail.smtp.host", host);

        if (!ANPUtils.isNullOrEmpty(port) && (Integer.parseInt(port)) > 0) {
            properties.put("mail.smtp.port", port);
        } else {
            properties.put("mail.smtp.port", 25);
        }

        String sslEnabled = systemConfigMap.get("EMAIL.UTILITY.SSLEnabled");

        String trust = null;
        if (!ANPUtils.isNullOrEmpty(sslEnabled) && sslEnabled.equalsIgnoreCase("true")) {
            trust = systemConfigMap.get("EMAIL.UTILITY.TRUST");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.ssl.trust", trust);

        }

        String fromEmailPassword = systemConfigMap.get("EMAIL.UTILITY.PASSWORD");
        String fromEmailAddress = systemConfigMap.get("EMAIL.UTILITY.FROMEMAILADDRESS");

        Session session = null;

        if (!ANPUtils.isNullOrEmpty(fromEmailPassword)) {
            properties.put("mail.smtp.auth", true);
            session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            fromEmailAddress, fromEmailPassword);// Specify the Username and the PassWord
                }
            });
        } else {
            session = Session.getDefaultInstance(properties);
        }

        // creating session object to get properties

        try {
            // MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, toAddressFormatted);
            message.addRecipients(Message.RecipientType.CC, ccAddressFormatted);
            message.setSubject(subject);

            // Set From Field: adding senders email to from field.
            message.setFrom(new InternetAddress(fromEmailAddress));

            Multipart multipart = null;
            if (attachmentList != null && attachmentList.size() > 0) {
                multipart = new MimeMultipart();
                for (String str : attachmentList) {
                    MimeBodyPart messageBodyPartForAttachments = new MimeBodyPart();
                    DataSource source = new FileDataSource(str);
                    messageBodyPartForAttachments.setDataHandler(new DataHandler(source));
                    messageBodyPartForAttachments.setFileName(source.getName());
                    multipart.addBodyPart(messageBodyPartForAttachments);
                }
            }

            MimeBodyPart messageBodyPartForText = new MimeBodyPart();
            messageBodyPartForText.setText(body);

            if (multipart != null) {
                multipart.addBodyPart(messageBodyPartForText);
                message.setContent(multipart);
            }

            Transport.send(message);
            System.out.println("Mail successfully sent");
        } catch (MessagingException mex) {
            throw new CustomAppException("Could not send Email", "SERVER.EAMIL_UTILITY.COULD_NOT_SEND_EMAIL", HttpStatus.EXPECTATION_FAILED);
        }
    }
}
