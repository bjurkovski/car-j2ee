package org.banque.utils;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Makes possible sending an email, doesn't need to be instantiated
 * @author wasser
 */
public class Email {

    private static final String SMTP_HOST_NAME = "mail.gmx.com";
    private static final String SMTP_PROTOCOL = "smtps";
    private static final String SMTP_FROM = "banqueejb@gmx.com";
    private static final String SMTP_AUTH_USER = "banqueejb@gmx.com";
    private static final String SMTP_AUTH_PWD = "projetoejb";

    /**
     * Sends an email in plain text. Note that this method is synchronous
     * @param to Email address of the receiver
     * @param subject Subject of the email to be sent
     * @param content The content to be embedded in the email (Plain Text)
     * @throws MessagingException In case there is a timeout
     */
    public static void sendEmail(String to, String subject, String content) throws MessagingException {
        //Sets the properties of the email provider
        Properties props = new Properties();
        props.put("mail.transport.protocol", SMTP_PROTOCOL);
        props.put("mail.smtps.host", SMTP_HOST_NAME);
        props.put("mail.smtps.auth", "true");

        SMTPAuthenticator auth = new SMTPAuthenticator();
        Session mailSession = Session.getDefaultInstance(props, auth);
//        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();


        //Setups the message
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(SMTP_FROM));
        message.setSubject(subject);
        message.setContent(content, "text/plain");
        message.addRecipient(Message.RecipientType.TO,
                new InternetAddress(to));

        //Sends the message
        transport.connect();
        transport.sendMessage(message,
                message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

    private static class SMTPAuthenticator extends Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
        }
    }
}
