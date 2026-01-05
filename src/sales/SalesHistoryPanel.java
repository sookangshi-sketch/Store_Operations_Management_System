
package sales;

import core.Database;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class SalesHistoryPanel extends JPanel {
    private JTextArea displayArea;
    private JComboBox<String> sortOptions;

    public SalesHistoryPanel() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        JButton loadBtn = new JButton("Load All");
        sortOptions = new JComboBox<>(new String[]{"Sort by Date", "Sort by Amount (Bubble)", "Sort by Name"});
        JButton sortBtn = new JButton("Apply Sort");

        top.add(loadBtn);
        top.add(sortOptions);
        top.add(sortBtn);

        displayArea = new JTextArea();
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        loadBtn.addActionListener(e -> showList(Database.getInstance().getSalesLog()));
        
        sortBtn.addActionListener(e -> {
            List<SalesRecord> list = new ArrayList<>(Database.getInstance().getSalesLog());
            String choice = (String) sortOptions.getSelectedItem();
            
            if (choice.contains("Amount")) {
                SortingUtils.bubbleSortByAmount(list);
            } else if (choice.contains("Date")) {
                SortingUtils.sortByDate(list);
            } else {
                SortingUtils.sortByName(list);
            }
            showList(list);
        });
    }

    private void showList(List<SalesRecord> list) {
        displayArea.setText("");
        displayArea.append("Date       | Customer       | Model    | Total\n");
        displayArea.append("------------------------------------------------\n");
        for (SalesRecord r : list) {
            displayArea.append(String.format("%s | %-14s | %-8s | RM%.2f\n", 
                r.getDate(), r.getCustomerName(), r.getModelName(), r.getTotal()));
        }
    }
}