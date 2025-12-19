package Emailautomation;

import java.time.LocalTime;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;

public class Emailautomation {
    /**
     * Requirement: Send daily sales report summary[cite: 221, 223].
     * Constraint: Must be sent before 10:00 p.m. daily[cite: 224, 225].
     */
    public static void sendSalesReport(String toEmail, String filePath, String date, double totalSales) {
        LocalTime now = LocalTime.now();
        LocalTime deadline = LocalTime.of(22, 0); // 10:00 PM cutoff

        if (now.isAfter(deadline)) {
            System.out.println("Notification: Time limit exceeded. Report not sent."); // [cite: 224]
            return;
        }

        // Email setup for proof of function [cite: 226]
        final String senderEmail = "your_email@gmail.com"; 
        final String senderPassword = "your_app_password"; 

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(senderEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail)); // [cite: 226]
            msg.setSubject("GoldenHour Daily Sales Report - " + date);

            // Body text with required summary [cite: 223]
            MimeBodyPart messageBody = new MimeBodyPart();
            messageBody.setText("Total Sales: RM" + totalSales + "\nDate: " + date);

            // Attachment of generated receipt file [cite: 222]
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.attachFile(new File(filePath));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBody);
            multipart.addBodyPart(attachment);
            msg.setContent(multipart);

            Transport.send(msg);
            System.out.println("Email successfully sent to headquarters.");
        } catch (Exception e) {
            System.out.println("Error: Connection failure."); 
        }
    }
}