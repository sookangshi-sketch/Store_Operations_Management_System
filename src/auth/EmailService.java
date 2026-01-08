package auth;

import core.Database;
import sales.SalesRecord;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*; // 如果IDE报错找不到这个，说明你需要导入 activation.jar

public class EmailService {

    // ================= 配置区域 =================
    // 你的 Gmail 邮箱地址
    private static final String SENDER_EMAIL = "chenliangquan2@gmail.com"; 
    // 你的 16位 Gmail 应用专用密码 (不是登录密码!)
    private static final String SENDER_PASSWORD = "tubngpyrzckudjew"; 
    // ===========================================

    public static void sendDailyReport(String recipientEmail) {
        String today = LocalDate.now().toString();
        System.out.println("\n[EmailService] Generating report and connecting to SMTP server...");

        // 1. 获取数据并计算
        List<SalesRecord> allSales = Database.getInstance().getSalesLog();
        List<SalesRecord> todaySales = allSales.stream()
                .filter(s -> s.getDate().equals(today))
                .collect(Collectors.toList()); //

        double totalRevenue = todaySales.stream().mapToDouble(SalesRecord::getTotal).sum();
        int transactionCount = todaySales.size();
        
        // 2. 生成物理文件作为附件
        String filename = "DailyReport_" + today + ".txt";
        File attachmentFile = generateAttachmentFile(filename, todaySales, totalRevenue);

        // 3. 配置 SMTP 服务器属性 (以 Gmail 为例)
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // 强制使用 TLS 1.2 防止连接被重置

        // 4. 创建 Session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            // 5. 构建邮件内容
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Daily Sales Report - " + today);

            // 创建多部分内容 (正文 + 附件)
            Multipart multipart = new MimeMultipart();

            // Part 1: 文字正文
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlContent = "<h3>Daily Business Summary</h3>"
                    + "<p>Date: " + today + "</p>"
                    + "<p>Total Transactions: <b>" + transactionCount + "</b></p>"
                    + "<p>Total Revenue: <b>RM " + String.format("%.2f", totalRevenue) + "</b></p>"
                    + "<p><i>Please find the detailed sales log attached.</i></p>";
            messageBodyPart.setContent(htmlContent, "text/html");
            multipart.addBodyPart(messageBodyPart);

            // Part 2: 附件
            if (attachmentFile != null && attachmentFile.exists()) {
                MimeBodyPart attachPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachmentFile);
                attachPart.setDataHandler(new DataHandler(source));
                attachPart.setFileName(filename);
                multipart.addBodyPart(attachPart);
            }

            // 合并并发送
            message.setContent(multipart);
            System.out.println("[EmailService] Sending email to " + recipientEmail + "...");
            
            Transport.send(message);

            System.out.println("[EmailService] ✅ Email Sent Successfully!");

        } catch (AuthenticationFailedException e) {
            System.err.println("[Error] Authentication Failed. Check your Email and App Password.");
        } catch (MessagingException e) {
            System.err.println("[Error] Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 辅助方法：生成文本文件
    private static File generateAttachmentFile(String filename, List<SalesRecord> sales, double total) {
        File file = new File(filename);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write("=== GOLDENHOUR DAILY SALES DETAIL ===\n");
            fw.write("Date: " + LocalDate.now() + "\n");
            fw.write("----------------------------------------------------------------------\n");
            fw.write(String.format("%-10s | %-15s | %-20s | %s\n", "Time", "Customer", "Model", "Amount"));
            fw.write("----------------------------------------------------------------------\n");
            
            for (SalesRecord s : sales) {
                fw.write(String.format("%-10s | %-15s | %-20s | RM %.2f\n", 
                        (s.getTime() == null ? "N/A" : s.getTime()), 
                        s.getCustomerName(), 
                        s.getModelName(), 
                        s.getTotal()));
            }
            
            fw.write("----------------------------------------------------------------------\n");
            fw.write(String.format("GRAND TOTAL: RM %.2f\n", total));
            fw.write("End of Report.");
            return file;
        } catch (IOException e) {
            System.err.println("Could not create attachment file.");
            return null;
        }
    }
}