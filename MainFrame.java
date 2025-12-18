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
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // ARCITECT FEATURE: Session Management
    // This variable stores "Who is logged in right now?"
    // Member 1 (Login) will SET this. Member 5 (Sales) will GET this.
    private String currentEmployeeId = null; 

    public MainFrame() {
        setTitle("GoldenHour OMS");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on screen

        // 1. Setup Layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 2. Add Panels
        // NOTE: These classes must exist for this file to compile!
        // Pass 'this' to everyone so they can call mainFrame.showCard("MENU")
        mainPanel.add(new LoginPanel(this), "LOGIN"); 
        mainPanel.add(new MenuPanel(this), "MENU");   
        mainPanel.add(new AttendancePanel(this), "ATTENDANCE"); 

        add(mainPanel);
        setVisible(true);
    }

    // 3. Navigation Method
    public void showCard(String cardName) {
        cardLayout.show(mainPanel, cardName);
    }
    
    // 4. Session Methods (Your team NEEDS these)
    public void setSession(String employeeId) {
        this.currentEmployeeId = employeeId;
    }

    public String getSession() {
        return currentEmployeeId;
    }
    
    public void logout() {
        this.currentEmployeeId = null;
        showCard("LOGIN");
    }

    public static void main(String[] args) {
        // Swing utilities should run on the Event Dispatch Thread (Best Practice)
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}