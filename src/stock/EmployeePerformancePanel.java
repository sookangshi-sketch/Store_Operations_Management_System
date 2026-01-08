package stock;

import core.Database;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import sales.SalesRecord;

public class EmployeePerformancePanel extends JPanel {
    public EmployeePerformancePanel() {
        setLayout(new BorderLayout());
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JButton loadBtn = new JButton("Load Performance Report (Manager Only)");
        add(loadBtn, BorderLayout.NORTH);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
        
        loadBtn.addActionListener(e -> {
            List<SalesRecord> sales = Database.getInstance().getSalesLog();
            
            // Map 1: ID -> Total Revenue
            Map<String, Double> empSales = new HashMap<>();
            // Map 2: ID -> Transaction Count (新增功能)
            Map<String, Integer> empCount = new HashMap<>();
            // Map 3: ID -> Name (为了显示名字)
            Map<String, String> empNames = new HashMap<>();
            
            // 遍历所有销售记录
            for(SalesRecord s : sales) {
                String id = s.getEmployeeId();
                empSales.put(id, empSales.getOrDefault(id, 0.0) + s.getTotal());
                empCount.put(id, empCount.getOrDefault(id, 0) + 1);
                
                // 尝试获取员工名字（从 SalesRecord 里通常没有名字，只有 ID，需要查 Employee 表）
                // 简单起见，如果 SalesRecord 没有名字字段，我们这里只显示 ID。
                // *更高级做法*：去 Employee 列表里查名字。
                empNames.putIfAbsent(id, findEmpName(id));
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("=== EMPLOYEE PERFORMANCE METRICS ===\n\n");
            sb.append(String.format("%-10s | %-15s | %-12s | %s\n", "ID", "Name", "Trans. Count", "Total Sales"));
            sb.append("-------------------------------------------------------------\n");
            
            // 按销售额降序排序
            empSales.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue())) 
                .forEach(entry -> {
                    String id = entry.getKey();
                    double total = entry.getValue();
                    int count = empCount.get(id);
                    String name = empNames.get(id);
                    
                    sb.append(String.format("%-10s | %-15s | %-12d | RM %.2f\n", 
                            id, name, count, total));
                });
                
            reportArea.setText(sb.toString());
        });
    }

    // 辅助方法：通过 ID 查找名字
    private String findEmpName(String id) {
        for (core.Employee e : Database.getInstance().getEmployees()) {
            if (e.getId().equalsIgnoreCase(id)) {
                return e.getName();
            }
        }
        return "Unknown";
    }
}