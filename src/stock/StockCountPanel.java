/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package stock;

import core.Database;
import core.Product;
import core.SearchUtils;
import javax.swing.*;
import java.awt.*;

public class StockCountPanel extends JPanel {
    private JTextField modelField, countField, outletField;
    private JTextArea resultArea;

    public StockCountPanel() {
        setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        modelField = new JTextField();
        countField = new JTextField();
        outletField = new JTextField("C60"); // Default current store
        JButton checkBtn = new JButton("Verify Stock");

        inputPanel.add(new JLabel("Model Name:")); inputPanel.add(modelField);
        inputPanel.add(new JLabel("Physical Count:")); inputPanel.add(countField);
        inputPanel.add(new JLabel("Outlet Code:")); inputPanel.add(outletField);
        inputPanel.add(new JLabel("")); inputPanel.add(checkBtn);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        checkBtn.addActionListener(e -> {
            String model = modelField.getText();
            String outlet = outletField.getText();
            Product p = SearchUtils.searchProduct(model, Database.getInstance().getProducts());

            if (p != null) {
                try {
                    int physical = Integer.parseInt(countField.getText());
                    int system = p.getStock(outlet);
                    
                    resultArea.append("Model: " + model + "\n");
                    resultArea.append("System Record: " + system + "\n");
                    resultArea.append("Physical Count: " + physical + "\n");
                    
                    if (physical == system) {
                        resultArea.append("Status: Stock Tally Correct.\n\n"); // [cite: 72]
                    } else {
                        resultArea.append("Status: ! MISMATCH DETECTED !\n\n"); // [cite: 73]
                    }
                } catch (NumberFormatException ex) {
                    resultArea.append("Error: Invalid number.\n");
                }
            } else {
                resultArea.append("Error: Model not found.\n");
            }
        });
    }
}