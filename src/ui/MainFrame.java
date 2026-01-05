/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import auth.EmailService;
import auth.LoginPanel;
import attendance.AttendancePanel;
import core.Database;
import core.Employee;
import core.SearchPanel;
import core.EditDataPanel;
import sales.SalesPanel;
import sales.SalesHistoryPanel;
import stock.AnalyticsPanel;
import stock.EmployeePerformancePanel;
import stock.InventoryPanel;
import stock.StockCountPanel;
import stock.StockMovementPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private JPanel contentPanel; // Holds the feature panels
    private Employee currentUser;

    public MainFrame() {
        setTitle("GoldenHour Store Operations System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load Data on Startup [cite: 209]
        Database.getInstance().loadAllData();

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // 1. Add Login Panel
        LoginPanel loginPanel = new LoginPanel(this::onLoginSuccess);
        mainContainer.add(loginPanel, "LOGIN");

        // 2. Add Dashboard (Empty for now, built after login)
        // We don't build it yet because we need the 'currentUser' object
        
        add(mainContainer);
        cardLayout.show(mainContainer, "LOGIN");
    }

    private void onLoginSuccess(Employee user) {
        this.currentUser = user;
        initDashboard();
        cardLayout.show(mainContainer, "DASHBOARD");
    }

    private void initDashboard() {
        JPanel dashboard = new JPanel(new BorderLayout());
        
        // Header
        JLabel header = new JLabel("Logged in as: " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        dashboard.add(header, BorderLayout.NORTH);

        // Content Area (CardLayout for features)
        contentPanel = new JPanel(new CardLayout());
        
        // Add Feature Panels
        contentPanel.add(new AttendancePanel(currentUser), "Attendance");
        contentPanel.add(new StockCountPanel(), "Stock Count");
        contentPanel.add(new InventoryPanel(), "Inventory View");
        contentPanel.add(new StockMovementPanel(currentUser), "Stock Movement");
        contentPanel.add(new SalesPanel(currentUser), "New Sale");
        contentPanel.add(new SalesHistoryPanel(), "Sales History");
        contentPanel.add(new SearchPanel(), "Search Info");
        contentPanel.add(new AnalyticsPanel(), "Analytics");
        contentPanel.add(new EditDataPanel(), "Edit");
        contentPanel.add(new EmployeePerformancePanel(), "Performance"); // 
        
        dashboard.add(contentPanel, BorderLayout.CENTER);

        // Sidebar Navigation
        MenuPanel menu = new MenuPanel(e -> navigate(e.getActionCommand()));
        dashboard.add(menu, BorderLayout.WEST);

        // Add dashboard to main container
        mainContainer.add(dashboard, "DASHBOARD");
    }

    private void navigate(String command) {
        if (command.equals("Logout")) {
            performLogout();
        } else if (command.equals("Performance")) {
            // Manager Check [cite: 245]
            if (currentUser.getRole().equalsIgnoreCase("Manager")) {
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, "Performance");
            } else {
                JOptionPane.showMessageDialog(this, "Access Denied: Manager Only.");
            }
        } else {
            // General Navigation
            CardLayout cl = (CardLayout) contentPanel.getLayout();
            cl.show(contentPanel, command);
        }
    }

    private void performLogout() {
        // Trigger Auto Email if it's the end of the day 
        int choice = JOptionPane.showConfirmDialog(this, 
            "Is this the end of the business day? (Triggers Auto-Email)", 
            "Logout", JOptionPane.YES_NO_CANCEL_OPTION);
            
        if (choice == JOptionPane.CANCEL_OPTION) return;
        
        if (choice == JOptionPane.YES_OPTION) {
            // Send email to student's email as per assignment req
            EmailService.sendDailyReport("23001812@siswa.um.edu.my"); 
        }

        currentUser = null;
        cardLayout.show(mainContainer, "LOGIN");
    }
}