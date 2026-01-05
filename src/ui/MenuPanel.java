/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    
    public MenuPanel(ActionListener onNavigate) {
        setLayout(new GridLayout(8, 1, 5, 5));
        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Helper to create buttons
        addButton("Attendance", onNavigate);
        addButton("Stock Count", onNavigate);
        addButton("Inventory View", onNavigate);
        addButton("Stock Movement", onNavigate);
        addButton("New Sale", onNavigate);
        addButton("Sales History", onNavigate);
        addButton("Search Info", onNavigate);
        addButton("Analytics", onNavigate);
        
        // Separator
        add(new JSeparator());
        
        // Manager Only Section (Visually distinct)
        JButton perfBtn = new JButton("Performance (Mgr)");
        perfBtn.setActionCommand("Performance");
        perfBtn.addActionListener(onNavigate);
        perfBtn.setBackground(new Color(255, 200, 200)); // Light red
        add(perfBtn);
        
        JButton editBtn = new JButton("Edit Data");
        editBtn.setActionCommand("Edit");
        editBtn.addActionListener(onNavigate);
        add(editBtn);

        JButton logoutBtn = new JButton("Logout / End Day");
        logoutBtn.setActionCommand("Logout");
        logoutBtn.addActionListener(onNavigate);
        logoutBtn.setBackground(Color.ORANGE);
        add(logoutBtn);
    }

    private void addButton(String name, ActionListener listener) {
        JButton btn = new JButton(name);
        btn.setActionCommand(name);
        btn.addActionListener(listener);
        add(btn);
    }
}