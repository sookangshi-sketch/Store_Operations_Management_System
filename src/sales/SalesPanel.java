/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sales;

import core.Database;
import core.Employee;
import core.Product;
import core.SearchUtils;
import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class SalesPanel extends JPanel {
    private Employee currentUser;
    private JTextField customerField, modelField, qtyField, methodField;
    private JTextArea receiptArea;

    public SalesPanel(Employee user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        customerField = new JTextField();
        modelField = new JTextField();
        qtyField = new JTextField();
        methodField = new JTextField(); // e.g., Cash, Card
        JButton processBtn = new JButton("Process Sale");

        form.add(new JLabel("Customer Name:")); form.add(customerField);
        form.add(new JLabel("Model Name:"));    form.add(modelField);
        form.add(new JLabel("Quantity:"));      form.add(qtyField);
        form.add(new JLabel("Method:"));        form.add(methodField);
        form.add(new JLabel(""));               form.add(processBtn);

        receiptArea = new JTextArea();
        receiptArea.setEditable(false);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(receiptArea), BorderLayout.CENTER);

        processBtn.addActionListener(e -> processTransaction());
    }

    private void processTransaction() {
        String cust = customerField.getText();
        String model = modelField.getText();
        String method = methodField.getText();
        String qtyStr = qtyField.getText();

        Product p = SearchUtils.searchProduct(model, Database.getInstance().getProducts());

        if (p == null) {
            JOptionPane.showMessageDialog(this, "Model not found!");
            return;
        }

        try {
            int qty = Integer.parseInt(qtyStr);
            // Defaulting to "C60" (KLCC) as the current store for simplicity
            String currentOutlet = "C60"; 
            
            if (p.getStock(currentOutlet) < qty) {
                JOptionPane.showMessageDialog(this, "Insufficient Stock!");
                return;
            }

            // 1. Calculate Total
            double total = p.getPrice() * qty;

            // 2. Update Stock
            p.updateStock(currentOutlet, -qty);
            Database.getInstance().saveProducts();

            // 3. Create Record
            String date = LocalDate.now().toString();
            String time = LocalTime.now().toString().substring(0, 5);
            SalesRecord sale = new SalesRecord(date, time, currentUser.getId(), cust, model, qty, total, method);
            Database.getInstance().addSale(sale);

            // 4. Generate Receipt
            generateReceiptFile(sale);
            receiptArea.setText("Transaction Successful!\n" + sale.toString());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Quantity");
        }
    }

    private void generateReceiptFile(SalesRecord sale) {
        String filename = "receipts_" + sale.getDate() + ".txt";
        try (FileWriter fw = new FileWriter(filename, true)) { // append mode
            fw.write("=== RECEIPT ===\n");
            fw.write("Date: " + sale.getDate() + "\n");
            fw.write("Customer: " + sale.getCustomerName() + "\n");
            fw.write("Item: " + sale.getModelName() + " x" + 1 + "\n"); // Simplified qty logic in display
            fw.write("Total: RM" + sale.getTotal() + "\n");
            fw.write("----------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}