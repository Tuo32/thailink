package com.liu.thailink.utils;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

// this is used to send authentication mail
public class EmailUtils {
    public static void sendEmail(String code, String receiver) {
        // Sender's email ID needs to be mentioned
        String from = "liutianen22@outlook.com";
        // Assuming you are sending email from localhost
        String host = "smtp-mail.outlook.com";
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.starttls.enable","true");
        properties.setProperty("mail.smtp.auth","true");
        // login session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, "lqweasdzxcJ");
                }
        });
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            // Set Subject: header field
            message.setSubject("Thailink authentication");
            message.setText("Your authentication code is "+code);
            // Send message
            Transport.send(message);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

}
