package com.ANP.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;
import javax.mail.Transport;


public class EmailUtil {

    public void sendEmail(String recipient, String sender, String senderPassword, String host, String pathname) {

        Properties properties = System.getProperties();

        // Setting up mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port","587");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.auth", true);


        // creating session object to get properties
        Session session = Session.getDefaultInstance(properties,new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(
                        sender, senderPassword);// Specify the Username and the PassWord
            }
        });
        try
        {
            // MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Sorry for disturbing at this hour");
            message.setText("Did I mistakenly set that as subject? Oh yes I did Well I am sorry about that too");

            MimeBodyPart textBodyPart = new MimeBodyPart();

            Multipart multipart = new MimeMultipart();


            // Set From Field: adding senders email to from field.
            message.setFrom(new InternetAddress(sender));



            Path path = Paths.get(pathname);
            String fileName = path.getFileName().toString();
            DataSource source = new FileDataSource(pathname);
            textBodyPart.setDataHandler(new DataHandler(source));
            textBodyPart.setFileName(fileName);
            multipart.addBodyPart(textBodyPart);

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
