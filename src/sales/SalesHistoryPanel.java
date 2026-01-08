package sales;

import core.Database;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class SalesHistoryPanel extends JPanel {
    private JTextArea displayArea;
    private JComboBox<String> sortOptions;
    private JTextField startDateField, endDateField;
    private JLabel totalLabel;

    public SalesHistoryPanel() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new GridLayout(2, 1, 5, 5));
        
        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Start Date (yyyy-mm-dd):"));
        startDateField = new JTextField(8);
        filterPanel.add(startDateField);
        filterPanel.add(new JLabel("End Date:"));
        endDateField = new JTextField(8);
        filterPanel.add(endDateField);
        JButton applyBtn = new JButton("Filter & Sort");
        filterPanel.add(applyBtn);

        // Sort Options
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortOptions = new JComboBox<>(new String[]{
            "Date (Newest First)", 
            "Date (Oldest First)", 
            "Amount (High->Low) [BubbleSort]", // 明确标记
            "Customer Name (A-Z)"
        });
        sortPanel.add(new JLabel("Sort By:"));
        sortPanel.add(sortOptions);
        
        totalLabel = new JLabel("Total: RM0.00");
        sortPanel.add(totalLabel);

        top.add(filterPanel);
        top.add(sortPanel);

        displayArea = new JTextArea();
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        applyBtn.addActionListener(e -> {
            List<SalesRecord> list = new ArrayList<>(Database.getInstance().getSalesLog());
            List<SalesRecord> filtered = new ArrayList<>();
            
            // 1. Filter
            try {
                String startTxt = startDateField.getText().trim();
                String endTxt = endDateField.getText().trim();
                LocalDate start = startTxt.isEmpty() ? null : LocalDate.parse(startTxt);
                LocalDate end = endTxt.isEmpty() ? null : LocalDate.parse(endTxt);

                for (SalesRecord r : list) {
                    LocalDate d = LocalDate.parse(r.getDate());
                    if (start != null && d.isBefore(start)) continue;
                    if (end != null && d.isAfter(end)) continue;
                    filtered.add(r);
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Date Format. Use yyyy-MM-dd");
                return;
            }

            // 2. Sort Logic
            String choice = (String) sortOptions.getSelectedItem();
            
            if (choice.contains("BubbleSort")) {
                // *关键修改*：调用你在 SortingUtils 写的冒泡排序
                SortingUtils.bubbleSortByAmount(filtered);
                // 默认是从小到大，如果需要从大到小，可以再 reverse 一下，或者改冒泡逻辑
                // 这里假设我们想要高->低，可以在这里 Collections.reverse(filtered);
                // 或者修改 bubbleSortByAmount 内部逻辑。
                // 简单起见，我们反转它以符合 "High->Low"
                java.util.Collections.reverse(filtered);
            } 
            else if (choice.contains("Date (Newest")) SortingUtils.sortByDateDesc(filtered);
            else if (choice.contains("Date (Oldest")) SortingUtils.sortByDate(filtered);
            else SortingUtils.sortByName(filtered);
            
            showList(filtered);
        });
    }

    private void showList(List<SalesRecord> list) {
        displayArea.setText("");
        double totalSales = 0;
        displayArea.append(String.format("%-12s | %-15s | %-10s | %s\n", "Date", "Customer", "Method", "Total"));
        displayArea.append("----------------------------------------------------------\n");
        for (SalesRecord r : list) {
            displayArea.append(String.format("%s | %-15s | %-10s | RM%.2f\n", 
                r.getDate(), r.getCustomerName(), r.getMethod(), r.getTotal()));
            totalSales += r.getTotal();
        }
        totalLabel.setText(String.format("Total: RM%.2f", totalSales));
    }
}