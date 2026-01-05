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

public class EmployeePerformancePanel extends JPanel {
    public EmployeePerformancePanel() {
        setLayout(new BorderLayout());
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        
        JButton loadBtn = new JButton("Load Performance Report (Manager Only)");
        add(loadBtn, BorderLayout.NORTH);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
        
        loadBtn.addActionListener(e -> {
            List<SalesRecord> sales = Database.getInstance().getSalesLog();
            Map<String, Double> empSales = new HashMap<>();
            
            for(SalesRecord s : sales) {
                empSales.put(s.getEmployeeId(), empSales.getOrDefault(s.getEmployeeId(), 0.0) + s.getTotal());
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("=== EMPLOYEE PERFORMANCE ===\n"); // 
            sb.append("ID       | Total Sales Generated\n");
            sb.append("-----------------------------\n");
            
            empSales.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue())) // Descending sort [cite: 242]
                .forEach(entry -> {
                    sb.append(String.format("%-8s | RM %.2f\n", entry.getKey(), entry.getValue()));
                });
                
            reportArea.setText(sb.toString());
        });
    }
}