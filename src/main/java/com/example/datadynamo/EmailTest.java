//package com.example.datadynamo;
//
//import java.util.Properties;
//import javax.mail.Message;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//
//public class EmailTest {
//    public static void main(String[] args) {
//        final String username = "vivekan.l.2017.cse@rajalakshmi.edu.in";
//        final String password = "your-email-password";
//
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//
//        Session session = Session.getInstance(props,
//            new javax.mail.Authenticator() {
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication(username, password);
//                }
//            });
//
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(username));
//            message.setRecipients(Message.RecipientType.TO,
//                InternetAddress.parse("recipient@example.com"));
//            message.setSubject("Testing Subject");
//            message.setText("Dear Mail Crawler," + "\n\n No spam to my email!");
//
//            Transport.send(message);
//            System.out.println("Email sent successfully!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
