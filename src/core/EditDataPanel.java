/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import javax.swing.*;
import java.awt.*;

public class EditDataPanel extends JPanel {
    private JTextField modelField, stockField;
    private JComboBox<String> outletCombo;
    private JTextArea logArea;

    public EditDataPanel() {
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        modelField = new JTextField();
        stockField = new JTextField();
        
        // Hardcoded outlets for simplicity in this dropdown
        String[] outlets = {"C60","C61","C62","C63","C64","C65","C66","C67","C68","C69"};
        outletCombo = new JComboBox<>(outlets);
        
        JButton updateBtn = new JButton("Update Stock");
        
        formPanel.add(new JLabel("Model Name:"));
        formPanel.add(modelField);
        formPanel.add(new JLabel("Outlet:"));
        formPanel.add(outletCombo);
        formPanel.add(new JLabel("New Quantity:"));
        formPanel.add(stockField);
        formPanel.add(new JLabel(""));
        formPanel.add(updateBtn);
        
        logArea = new JTextArea();
        logArea.setEditable(false);
        
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(logArea), BorderLayout.CENTER);
        
        updateBtn.addActionListener(e -> updateStock());
    }

    private void updateStock() {
        String model = modelField.getText().trim();
        String outlet = (String) outletCombo.getSelectedItem();
        String qtyStr = stockField.getText().trim();
        
        Product p = SearchUtils.searchProduct(model, Database.getInstance().getProducts());
        
        if (p == null) {
            logArea.append("Error: Model " + model + " not found.\n");
            return;
        }
        
        try {
            int newQty = Integer.parseInt(qtyStr);
            p.setStock(outlet, newQty);
            Database.getInstance().saveProducts(); // Save to CSV immediately
            logArea.append("Success: " + model + " at " + outlet + " updated to " + newQty + "\n");
        } catch (NumberFormatException ex) {
            logArea.append("Error: Invalid quantity.\n");
        }
    }
}