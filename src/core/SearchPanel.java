/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import sales.SalesRecord;

public class SearchPanel extends JPanel {
    private JTextField searchField;
    private JTextArea resultArea;
    private JComboBox<String> typeCombo;

    public SearchPanel() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel topPanel = new JPanel();
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        typeCombo = new JComboBox<>(new String[]{"Stock Info", "Sales Info"});
        
        topPanel.add(new JLabel("Search By Keyword:"));
        topPanel.add(searchField);
        topPanel.add(typeCombo);
        topPanel.add(searchBtn);
        
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        searchBtn.addActionListener(e -> performSearch());
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        String type = (String) typeCombo.getSelectedItem();
        resultArea.setText(""); // clear

        if (keyword.isEmpty()) {
            resultArea.setText("Please enter a keyword.");
            return;
        }

        if (type.equals("Stock Info")) {
            Product p = SearchUtils.searchProduct(keyword, Database.getInstance().getProducts());
            if (p != null) {
                resultArea.append("Model: " + p.getModelName() + "\n");
                resultArea.append("Price: RM" + p.getPrice() + "\n");
                resultArea.append("Stock by Outlet:\n");
                p.getAllStocks().forEach((k, v) -> 
                    resultArea.append("  " + k + ": " + v + "\n")
                );
            } else {
                resultArea.append("Model not found.");
            }
        } else {
            List<SalesRecord> results = SearchUtils.searchSales(keyword, Database.getInstance().getSalesLog());
            if (results.isEmpty()) {
                resultArea.append("No sales records found.");
            } else {
                for (SalesRecord r : results) {
                    resultArea.append(r.toString() + "\n----------------\n");
                }
            }
        }
    }
}