/*
 * StockCountPanel.java (Updated with Morning/Night Selection)
 */
package stock;

import core.Database;
import core.Product;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;

public class StockCountPanel extends JPanel {
    // UI Components
    private JLabel currentModelLabel;
    private JTextField countInput;
    private JTextArea logArea;
    private JComboBox<String> outletBox;
    private JComboBox<String> sessionTypeBox; // [New Feature] Morning/Night Select
    private JButton submitBtn, finishBtn, startBtn;
    private JProgressBar progressBar;

    // Session State
    private List<Product> productList;
    private int currentIndex = 0;
    private int correctCount = 0;
    private int mismatchCount = 0;
    private boolean isSessionActive = false;

    public StockCountPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- 1. Top Control Panel ---
        JPanel topPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        
        // Outlet & Type Selection
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        selectionPanel.add(new JLabel("Outlet:"));
        String[] outlets = {"C60", "C61", "C62", "C63", "C64", "C65", "C66", "C67", "C68", "C69"};
        outletBox = new JComboBox<>(outlets);
        
        selectionPanel.add(new JLabel("   Session:")); // Spacer
        // [New Feature] Added Morning/Night Selection
        String[] types = {"Morning Stock Count", "Night Stock Count"};
        sessionTypeBox = new JComboBox<>(types);
        
        startBtn = new JButton("Start Session");
        
        selectionPanel.add(outletBox);
        selectionPanel.add(sessionTypeBox);
        selectionPanel.add(startBtn);

        // Current Item Display
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        currentModelLabel = new JLabel("Status: Idle. Select Type & Click 'Start'.");
        currentModelLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        currentModelLabel.setForeground(Color.BLUE);
        itemPanel.add(currentModelLabel);

        // Input Area
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("Physical Count:"));
        countInput = new JTextField(10);
        countInput.setEnabled(false); 
        submitBtn = new JButton("Submit & Next");
        submitBtn.setEnabled(false);
        finishBtn = new JButton("End Session Early");
        finishBtn.setEnabled(false);
        
        inputPanel.add(countInput);
        inputPanel.add(submitBtn);
        inputPanel.add(finishBtn);

        topPanel.add(selectionPanel);
        topPanel.add(itemPanel);
        topPanel.add(inputPanel);

        // --- 2. Center Log Area ---
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(logArea);
        
        // Progress Bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);

        // --- Event Listeners ---
        startBtn.addActionListener(e -> startSession());
        submitBtn.addActionListener(e -> processCurrentItem());
        finishBtn.addActionListener(e -> generateFinalReport());
        
        countInput.addActionListener(e -> {
            if (submitBtn.isEnabled()) processCurrentItem();
        });
    }

    private void startSession() {
        productList = Database.getInstance().getProducts();
        if (productList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No products in database!");
            return;
        }
        
        currentIndex = 0;
        correctCount = 0;
        mismatchCount = 0;
        isSessionActive = true;

        // 获取用户选择的类型 (Morning/Night)
        String selectedType = (String) sessionTypeBox.getSelectedItem();

        // UI Updates
        logArea.setText("");
        logArea.append("=== " + selectedType.toUpperCase() + " STARTED ===\n"); // [New Feature]
        logArea.append("Date: " + LocalDate.now() + "\n");
        logArea.append("Time: " + LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")) + "\n");
        logArea.append("Outlet: " + outletBox.getSelectedItem() + "\n");
        logArea.append("-----------------------------------\n");

        outletBox.setEnabled(false);
        sessionTypeBox.setEnabled(false); // Lock selection during session
        startBtn.setEnabled(false);
        countInput.setEnabled(true);
        submitBtn.setEnabled(true);
        finishBtn.setEnabled(true);
        countInput.requestFocus();

        updateProgress();
        showCurrentModel();
    }

    private void showCurrentModel() {
        if (currentIndex < productList.size()) {
            Product p = productList.get(currentIndex);
            currentModelLabel.setText("Item " + (currentIndex + 1) + "/" + productList.size() + ": " + p.getModelName());
        } else {
            generateFinalReport(); 
        }
    }

    private void processCurrentItem() {
        if (!isSessionActive) return;

        try {
            String inputStr = countInput.getText().trim();
            if (inputStr.isEmpty()) return;

            int physicalQty = Integer.parseInt(inputStr);
            Product p = productList.get(currentIndex);
            String outlet = (String) outletBox.getSelectedItem();
            int systemQty = p.getStock(outlet);

            logArea.append("Model: " + p.getModelName() + "\n");
            logArea.append("Counted: " + physicalQty + "\n");
            logArea.append("Store Record: " + systemQty + "\n");

            if (physicalQty == systemQty) {
                logArea.append(">> Stock tally correct.\n\n");
                correctCount++;
            } else {
                int diff = Math.abs(physicalQty - systemQty);
                logArea.append(">> ! MISMATCH DETECTED (" + diff + " unit difference)\n\n");
                mismatchCount++;
            }

            currentIndex++;
            countInput.setText("");
            updateProgress();
            showCurrentModel(); 

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }
    }

    private void generateFinalReport() {
        isSessionActive = false;
        String selectedType = (String) sessionTypeBox.getSelectedItem();

        logArea.append("-----------------------------------\n");
        logArea.append(selectedType + " Summary:\n"); // [New Feature]
        logArea.append("Total Models Checked: " + currentIndex + "\n");
        logArea.append("Tally Correct: " + correctCount + "\n");
        logArea.append("Mismatches: " + mismatchCount + "\n");
        
        if (mismatchCount > 0) {
            logArea.append("Warning: Please verify stock.\n");
        } else {
            logArea.append("Stock count completed successfully.\n");
        }
        logArea.append("===================================\n");

        currentModelLabel.setText("Session Completed.");
        currentModelLabel.setForeground(new Color(0, 100, 0));
        countInput.setEnabled(false);
        submitBtn.setEnabled(false);
        finishBtn.setEnabled(false);
        outletBox.setEnabled(true);
        sessionTypeBox.setEnabled(true); // Unlock selection
        startBtn.setEnabled(true);
        progressBar.setValue(100);
    }

    private void updateProgress() {
        int total = productList.size();
        int percent = (int) (((double) currentIndex / total) * 100);
        progressBar.setValue(percent);
        progressBar.setString(currentIndex + " / " + total + " Checked");
    }
}