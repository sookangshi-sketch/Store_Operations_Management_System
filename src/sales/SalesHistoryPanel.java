
package sales;

import core.Database;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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

        // Sort & Status Panel
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortOptions = new JComboBox<>(new String[]{
            "Date (Newest First)", "Date (Oldest First)", 
            "Amount (Highest First)", "Amount (Lowest First)", 
            "Customer Name (A-Z)"
        });
        sortPanel.add(new JLabel("Sort By:"));
        sortPanel.add(sortOptions);
        
        totalLabel = new JLabel("Total Sales: RM0.00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        totalLabel.setForeground(new Color(0, 100, 0));
        sortPanel.add(Box.createHorizontalStrut(20));
        sortPanel.add(totalLabel);

        top.add(filterPanel);
        top.add(sortPanel);

        displayArea = new JTextArea();
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        applyBtn.addActionListener(e -> {
            List<SalesRecord> list = new ArrayList<>(Database.getInstance().getSalesLog());
            List<SalesRecord> filtered = new ArrayList<>();
            
            // 1. Filter by Date
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

            // 2. Sort
            String choice = (String) sortOptions.getSelectedItem();
            if (choice.contains("Date (Newest")) SortingUtils.sortByDateDesc(filtered);
            else if (choice.contains("Date (Oldest")) SortingUtils.sortByDate(filtered);
            else if (choice.contains("Amount (High")) SortingUtils.sortByAmountDesc(filtered);
            else if (choice.contains("Amount (Low")) SortingUtils.sortByAmountAsc(filtered);
            else SortingUtils.sortByName(filtered);
            
            showList(filtered);
        });
    }

    private void showList(List<SalesRecord> list) {
        displayArea.setText("");
        double totalSales = 0;
        displayArea.append("Date       | Customer       | Model    | Method   | Total\n");
        displayArea.append("-----------------------------------------------------------\n");
        for (SalesRecord r : list) {
            displayArea.append(String.format("%s | %-14s | %-8s | %-8s | RM%.2f\n", 
                r.getDate(), r.getCustomerName(), r.getModelName(), r.getMethod(), r.getTotal()));
            totalSales += r.getTotal();
        }
        totalLabel.setText(String.format("Total Sales: RM%.2f", totalSales));
    }
}