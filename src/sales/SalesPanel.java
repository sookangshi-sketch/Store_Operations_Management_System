
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
import java.time.format.DateTimeFormatter;

public class SalesPanel extends JPanel {
    private Employee currentUser;
    private JTextField customerField, modelField, qtyField, totalField;
    private JComboBox<String> methodBox;
    private JTextArea receiptArea;

    public SalesPanel(Employee user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        customerField = new JTextField();
        modelField = new JTextField();
        qtyField = new JTextField();
        totalField = new JTextField();
        methodBox = new JComboBox<>(new String[]{"Cash", "Debit/Credit Card", "E-wallet", "Other"});
        JButton processBtn = new JButton("Process Sale");

        form.add(new JLabel("Customer Name:")); form.add(customerField);
        form.add(new JLabel("Model Name:"));    form.add(modelField);
        form.add(new JLabel("Quantity:"));      form.add(qtyField);
        form.add(new JLabel("Total Price (RM):")); form.add(totalField);
        form.add(new JLabel("Method:"));        form.add(methodBox);
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
        String method = (String) methodBox.getSelectedItem();
        String qtyStr = qtyField.getText();
        String totalStr = totalField.getText();

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
            double total;
            if (!totalStr.trim().isEmpty()) {
                total = Double.parseDouble(totalStr);
            } else {
                total = p.getPrice() * qty;
            }

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
            JOptionPane.showMessageDialog(this, "Invalid Quantity or Price");
        }
    }

    private void generateReceiptFile(SalesRecord sale) {
        String filename = "sales_" + sale.getDate() + ".txt";
        String timeStr = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"))
                            .toLowerCase().replace("am", "a.m.").replace("pm", "p.m.");
        
        try (FileWriter fw = new FileWriter(filename, true)) { // append mode
            fw.write("=== Record New Sale ===\n");
            fw.write("Date: " + sale.getDate() + "\n");
            fw.write("Time: " + timeStr + "\n");
            fw.write("Customer Name: " + sale.getCustomerName() + "\n");
            fw.write("Item(s) Purchased:\n");
            fw.write("Model: " + sale.getModelName() + "\n");
            fw.write("Quantity: " + sale.getQuantity() + "\n");
            fw.write(String.format("Unit Price: RM%.2f\n", (sale.getTotal() / sale.getQuantity())));
            fw.write("Transaction Method: " + sale.getMethod() + "\n");
            fw.write(String.format("Subtotal: RM%.2f\n", sale.getTotal()));
            fw.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}