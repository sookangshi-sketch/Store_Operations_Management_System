
package assignment1002part2;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.io.File;

public class Emailautomation {

    public static void sendSalesReport(String toEmail, String filePath, String date, double totalSales) {

        final String senderEmail = "************@gmail.com";  // your email
        final String senderPassword = "****************";       // your app password

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
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            msg.setSubject("Daily Sales Report - " + date);

            // Email message content
            MimeBodyPart messageBody = new MimeBodyPart();
            messageBody.setText("Daily Sales Report\nDate: " + date +
                    "\nTotal Sales: RM" + totalSales + 
                    "\nReport attached.\n\nSent automatically by system.");

            // Attachment
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.attachFile(new File(filePath));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBody);
            multipart.addBodyPart(attachment);

            msg.setContent(multipart);

            Transport.send(msg);
            System.out.println("Email sent successfully!");

        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
     public static void main(String[] args) {

        String toEmail = "junjingwong89@gmail.com";  
        String filePath = "sales_2025-10-13.txt";  
        String date = "2025-10-13";
        double totalSales = 850.60;

        sendSalesReport(toEmail, filePath, date, totalSales);
}


    }
