/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package stock;

import core.Database;
import sales.SalesRecord;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsPanel extends JPanel {
    public AnalyticsPanel() {
        setLayout(new BorderLayout());
        JTextArea statsArea = new JTextArea();
        statsArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        statsArea.setEditable(false);
        
        JButton refreshBtn = new JButton("Refresh Analytics");
        add(refreshBtn, BorderLayout.NORTH);
        add(new JScrollPane(statsArea), BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> {
            List<SalesRecord> sales = Database.getInstance().getSalesLog();
            if (sales.isEmpty()) {
                statsArea.setText("No sales data available.");
                return;
            }

            double totalRevenue = 0;
            Map<String, Integer> modelCounts = new HashMap<>();
            
            for (SalesRecord s : sales) {
                totalRevenue += s.getTotal();
                modelCounts.put(s.getModelName(), modelCounts.getOrDefault(s.getModelName(), 0) + 1); // [cite: 229]
            }

            // Find most sold
            String bestModel = "";
            int maxSold = 0;
            for (Map.Entry<String, Integer> entry : modelCounts.entrySet()) {
                if (entry.getValue() > maxSold) {
                    maxSold = entry.getValue();
                    bestModel = entry.getKey();
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append("=== STORE ANALYTICS ===\n\n");
            sb.append(String.format("Total Revenue:     RM %.2f\n", totalRevenue));
            sb.append(String.format("Total Transactions: %d\n", sales.size()));
            sb.append(String.format("Best Selling Model: %s (%d sold)\n", bestModel, maxSold)); // [cite: 229]
            
            statsArea.setText(sb.toString());
        });
    }
}