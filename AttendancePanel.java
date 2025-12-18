/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FOP_Assignment;

/**
 *
 * @author user
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AttendancePanel extends JPanel {

    private MainFrame mainFrame;
    private AttendanceManager manager;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;

    // -- COLOR PALETTE --
    private final Color PRIMARY_COLOR = new Color(51, 153, 255);   
    private final Color BG_COLOR = new Color(245, 247, 250);       
    private final Color TEXT_DARK = new Color(50, 50, 50);         
    
    // -- STATE CONTROL --
    private JButton checkInBtn;
    private JButton checkOutBtn;
    
    // -- DASHBOARD LABELS --
    private JLabel lblPresentCount;
    private JLabel lblLateCount;
    private JLabel lblAbsentCount;

    public AttendancePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.manager = new AttendanceManager();

        // 1. Layout & Background
        setLayout(new BorderLayout(20, 20)); 
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30)); 

        // 2. TOP SECTION: Header + Stats
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(BG_COLOR);

        JLabel titleLabel = new JLabel("Attendance Overview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_DARK);
        topContainer.add(titleLabel, BorderLayout.NORTH);

        // Initialize the labels first
        lblPresentCount = new JLabel("0");
        lblLateCount = new JLabel("0");
        lblAbsentCount = new JLabel("0");

        // --- INSERT THIS MISSING SECTION ---
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(BG_COLOR); // If you have this variable defined
        statsPanel.setBorder(new EmptyBorder(20, 0, 10, 0)); // If you want the spacing

        // Add them to the panel using the modified helper method (see Step C)
        statsPanel.add(createStatCard("Present Today", lblPresentCount, new Color(46, 204, 113))); 
        statsPanel.add(createStatCard("Late Arrival", lblLateCount, new Color(241, 196, 15)));   
        statsPanel.add(createStatCard("Absent", lblAbsentCount, new Color(231, 76, 60)));
        
        topContainer.add(statsPanel, BorderLayout.CENTER);
        add(topContainer, BorderLayout.NORTH);

        // 3. CENTER SECTION: Data Table
        // Definition: Date | ID | Time In | Time Out | Status
        String[] columns = {"Date", "Employee ID", "Time In", "Time Out", "Status"};
        
        // DATA MODEL: Starts EMPTY (Real world apps don't hardcode data in constructor)
        tableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        attendanceTable = new JTable(tableModel);
        styleTable(attendanceTable);

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
        scrollPane.getViewport().setBackground(Color.WHITE); 
        
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(new LineBorder(new Color(230, 230, 230), 1, true)); 
        tableCard.add(scrollPane, BorderLayout.CENTER);

        add(tableCard, BorderLayout.CENTER);

        // 4. BOTTOM SECTION: Action Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BG_COLOR);

        checkInBtn = createStyledButton("Check In", PRIMARY_COLOR);
        checkOutBtn = createStyledButton("Check Out", new Color(100, 100, 100)); 
        JButton backBtn = createStyledButton("Back", new Color(231, 76, 60)); 

        // --- REAL WORLD LOGIC IMPLEMENTATION ---

        checkInBtn.addActionListener(e -> performCheckIn());
        checkOutBtn.addActionListener(e -> performCheckOut());
        backBtn.addActionListener(e -> mainFrame.showCard("MENU"));

        buttonPanel.add(checkInBtn);
        buttonPanel.add(checkOutBtn);
        buttonPanel.add(backBtn);

        add(buttonPanel, BorderLayout.SOUTH);
        
        // Initial Load (Currently loads nothing, but ready for future)
        loadAttendanceHistory();
        updateButtonStates();
    }

    // --- LOGIC METHODS (The "Brain") ---

    private void performCheckIn() {
        String empId = mainFrame.getSession();
    
        // 1. Tell Manager to save data
        boolean success = manager.checkIn(empId);
    
        if (success) {
            // 2. Refresh the table to show the new data
            loadAttendanceHistory(); 
            JOptionPane.showMessageDialog(this, "Checked In Successfully!");
            updateButtonStates();
        } else {
            JOptionPane.showMessageDialog(this, "You are already checked in!");
        }
    }

    private void performCheckOut() {
        String empId = mainFrame.getSession();
    
        // 1. Tell Manager to update data
        boolean success = manager.checkOut(empId);
    
        if (success) {
            // 2. Refresh table
            loadAttendanceHistory();
            if (success) {
        // 2. Refresh table
        loadAttendanceHistory();
        
        // --- NEW CODE STARTS HERE ---
        // Find the record we just updated to show the hours
        String hoursWorked = "0.0 hours";
        
        // Look at the last row in the table (which is the one we just finished)
        // (This works because your table loads data in order)
        if (manager.getAllRecords().size() > 0) {
             AttendanceRecord lastRecord = manager.getAllRecords().get(manager.getAllRecords().size() - 1);
             hoursWorked = lastRecord.getDuration();
        }

        // Show the requirement-compliant message
        String message = "Checked Out Successfully!\n" +
                         "Total Hours Worked: " + hoursWorked;
                         
        JOptionPane.showMessageDialog(this, message);
        // --- NEW CODE ENDS HERE ---

        updateButtonStates();
    }
            updateButtonStates();
        } else {
            JOptionPane.showMessageDialog(this, "Error: No active session found.");
        }
    }

    // Toggles buttons so you can't Check In twice
    private void updateButtonStates() {
        boolean hasActiveSession = false;
        
        // Check if last row is "Active"
        if (tableModel.getRowCount() > 0) {
             String lastStatus = (String) tableModel.getValueAt(tableModel.getRowCount() - 1, 4);
             if ("Active".equals(lastStatus) || "Late".equals(lastStatus)) {
                 hasActiveSession = true;
             }
        }

        checkInBtn.setEnabled(!hasActiveSession); // If active, disable Check In
        checkOutBtn.setEnabled(hasActiveSession); // If active, enable Check Out
        
        // Visual cue: Change Check Out color if active
        checkOutBtn.setBackground(hasActiveSession ? new Color(241, 196, 15) : new Color(200, 200, 200));
    }
    
    // --- UPDATED METHOD: COUNTS UNIQUE PEOPLE ONLY ---
    private void updateDashboardStats() {
        // 1. Use a Set to track UNIQUE Employee IDs (Sets automatically ignore duplicates)
        java.util.HashSet<String> presentPeople = new java.util.HashSet<>();
        java.util.HashSet<String> latePeople = new java.util.HashSet<>();
        
        int late = 0;
        int absent = 0;
        
        String today = LocalDate.now().toString();

        // 2. Loop through ALL records
        for (AttendanceRecord r : manager.getAllRecords()) {
            // Only count records for TODAY
            if (r.getDate().equals(today)) {
                String status = r.getStatus();
                String empId = r.getEmployeeId();
                
                // Logic: If they are "Present" or "Active", add their ID to the Set
                if ("Present".equalsIgnoreCase(status) || 
                    "Active".equalsIgnoreCase(status) ||
                    "Late".equalsIgnoreCase(status)) {
                    presentPeople.add(empId); 
                    // If "TEST-USER-001" is already in the set, this line does nothing!
                    
                } 
                if ("Late".equalsIgnoreCase(status)) {
                    latePeople.add(empId);
                }
            }
        }

        // --- INSERT THIS NEW LOGIC HERE ---
        // Since we don't have the real employee list yet, we "Mock" (pretend) there are 5 employees.
        int totalEmployees = 5; 
        
        // Absent = Total Staff - Unique People Present
        int calculatedAbsent = totalEmployees - presentPeople.size();
        
        // Safety check: Prevent negative numbers
        if (calculatedAbsent < 0) calculatedAbsent = 0;
        // ----------------------------------

        // 3. Update the screen
        // .size() counts how many unique items are in the box
        lblPresentCount.setText(String.valueOf(presentPeople.size())); 
        lblLateCount.setText(String.valueOf(latePeople.size()));
        
        // CHANGE THIS LINE to use your new calculation:
        lblAbsentCount.setText(String.valueOf(calculatedAbsent)); 
    }

    
    
    // Placeholder for when you finish AttendanceManager
    private void loadAttendanceHistory() {
        // Clear existing rows first (to avoid duplicates)
        tableModel.setRowCount(0);
    
        // Get real data from Manager
        java.util.List<AttendanceRecord> list = manager.getAllRecords();
    
        // Loop and add to table
        for (AttendanceRecord r : list) {
            tableModel.addRow(r.toRowData());
        }
        
        updateDashboardStats();
    }

    // --- DESIGN HELPER METHODS (Unchanged) ---

    // UPDATED HELPER: Now accepts a JLabel object instead of a String
    private JPanel createStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setForeground(Color.GRAY);
        titleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Style the label that was passed in
        valueLabel.setForeground(TEXT_DARK);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel accentLine = new JPanel();
        accentLine.setBackground(accentColor);
        accentLine.setPreferredSize(new Dimension(4, 0));

        card.add(accentLine, BorderLayout.WEST);
        
        JPanel content = new JPanel(new GridLayout(2, 1));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(0, 10, 0, 0)); 
        content.add(titleLbl);
        content.add(valueLabel); // Add the live label
        
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35); 
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(230, 230, 230));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(232, 240, 254)); 
        table.setSelectionForeground(Color.BLACK);
        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(0, 40)); 
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); 
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}