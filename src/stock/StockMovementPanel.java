/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package stock;

import core.Database;
import core.Employee;
import core.Product;
import core.SearchUtils;
import javax.swing.*;
import java.awt.*;

public class StockMovementPanel extends JPanel {
    private JComboBox<String> typeCombo;
    private JTextField modelField, qtyField, fromField, toField;
    private JTextArea logArea;

    public StockMovementPanel(Employee user) {
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
        typeCombo = new JComboBox<>(new String[]{"Stock In", "Stock Out"});
        modelField = new JTextField();
        qtyField = new JTextField();
        fromField = new JTextField("HQ"); // Default
        toField = new JTextField("C60");  // Default
        JButton actionBtn = new JButton("Confirm Movement");

        form.add(new JLabel("Type:"));    form.add(typeCombo);
        form.add(new JLabel("Model:"));   form.add(modelField);
        form.add(new JLabel("Quantity:"));form.add(qtyField);
        form.add(new JLabel("From (Code):")); form.add(fromField);
        form.add(new JLabel("To (Code):"));   form.add(toField);
        form.add(new JLabel(""));         form.add(actionBtn);

        logArea = new JTextArea();
        logArea.setEditable(false);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        actionBtn.addActionListener(e -> processMovement());
    }

    private void processMovement() {
        String type = (String) typeCombo.getSelectedItem();
        String model = modelField.getText().trim();
        String toOutlet = toField.getText().trim();
        String fromOutlet = fromField.getText().trim();
        
        Product p = SearchUtils.searchProduct(model, Database.getInstance().getProducts());
        
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Model not found.");
            return;
        }

        try {
            int qty = Integer.parseInt(qtyField.getText().trim());
            
            // Logic: Stock In adds to 'To' outlet. Stock Out subtracts from 'From' outlet.
            // Simplified for assignment: Just update the specific outlet mentioned in 'To' or 'From'
            // based on context.
            
            if (type.equals("Stock In")) {
                p.updateStock(toOutlet, qty);
                logArea.append("Added " + qty + " units to " + toOutlet + "\n");
            } else {
                // Stock Out
                int current = p.getStock(fromOutlet);
                if (current < qty) {
                    JOptionPane.showMessageDialog(this, "Insufficient stock at " + fromOutlet);
                    return;
                }
                p.updateStock(fromOutlet, -qty);
                logArea.append("Removed " + qty + " units from " + fromOutlet + "\n");
            }
            
            Database.getInstance().saveProducts();
            ReceiptGenerator.saveStockReceipt(type, fromOutlet, toOutlet, model, qty);
            logArea.append("Receipt generated.\n");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Quantity");
        }
    }
}