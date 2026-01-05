/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auth;

import java.time.LocalDate;

public class EmailService {
    // This simulates sending an email since real SMTP requires javax.mail jar.
    // Implementing this prints the email content to console to prove logic.
    public static void sendDailyReport(String managerEmail) {
        String date = LocalDate.now().toString();
        System.out.println("\n[SYSTEM] Sending Auto Email to " + managerEmail + "..."); 
        System.out.println("Subject: Daily Sales Report - " + date);
        System.out.println("Attachment: receipts_" + date + ".txt"); // [cite: 222]
        System.out.println("Body: Please find the attached sales summary for today.");
        System.out.println("[SYSTEM] Email Sent Successfully.\n");
    }
}