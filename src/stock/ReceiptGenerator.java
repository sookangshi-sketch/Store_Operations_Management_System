/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package stock;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class ReceiptGenerator {
    
    // Appends stock movement details to a daily file [cite: 104, 105]
    public static void saveStockReceipt(String type, String from, String to, String model, int qty) {
        String date = LocalDate.now().toString();
        String filename = "stock_receipts_" + date + ".txt";
        
        try (FileWriter fw = new FileWriter(filename, true)) {
            fw.write("=== " + type + " ===\n");
            fw.write("Date: " + date + "\n");
            fw.write("From: " + from + "\n");
            fw.write("To: " + to + "\n");
            fw.write("Model: " + model + " (Qty: " + qty + ")\n");
            fw.write("------------------------\n");
        } catch (IOException e) {
            System.err.println("Error writing receipt: " + e.getMessage());
        }
    }
}