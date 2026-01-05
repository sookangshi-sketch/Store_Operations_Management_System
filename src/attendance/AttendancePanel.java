/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package attendance;

import core.Employee;
import javax.swing.*;
import java.awt.*;

public class AttendancePanel extends JPanel {
    private Employee currentEmployee; // Needs to be set after login
    private JTextArea logArea;

    public AttendancePanel(Employee user) {
        this.currentEmployee = user;
        setLayout(new BorderLayout());

        JPanel btnPanel = new JPanel();
        JButton inBtn = new JButton("Clock In");
        JButton outBtn = new JButton("Clock Out");
        
        btnPanel.add(new JLabel("Actions: "));
        btnPanel.add(inBtn);
        btnPanel.add(outBtn);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setText("Welcome, " + (user != null ? user.getName() : "Guest"));

        add(btnPanel, BorderLayout.NORTH);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        inBtn.addActionListener(e -> {
            if(currentEmployee == null) return;
            String msg = AttendanceManager.clockIn(currentEmployee.getId());
            logArea.append("\n" + msg);
        });

        outBtn.addActionListener(e -> {
            if(currentEmployee == null) return;
            String msg = AttendanceManager.clockOut(currentEmployee.getId());
            logArea.append("\n" + msg);
        });
    }
}
