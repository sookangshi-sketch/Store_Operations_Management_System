/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package auth;

import core.Database;
import core.Employee;
import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class LoginPanel extends JPanel {
    private JTextField idField;
    private JPasswordField passField;
    private Consumer<Employee> onLoginSuccess; // Callback to MainFrame

    public LoginPanel(Consumer<Employee> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
        setLayout(new GridBagLayout());
        
        JPanel box = new JPanel(new GridLayout(4, 2, 10, 10));
        box.setBorder(BorderFactory.createTitledBorder("GoldenHour System Login"));
        
        idField = new JTextField();
        passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton regBtn = new JButton("Register (Manager Only)"); // [cite: 30]

        box.add(new JLabel("Employee ID:")); box.add(idField);
        box.add(new JLabel("Password:"));    box.add(passField);
        box.add(regBtn);                     box.add(loginBtn);
        
        add(box);

        loginBtn.addActionListener(e -> attemptLogin());
        regBtn.addActionListener(e -> showRegistrationDialog());
    }

    private void attemptLogin() {
        String id = idField.getText();
        String pass = new String(passField.getPassword());
        
        Employee user = AuthenticationService.login(id, pass);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Welcome, " + user.getName());
            onLoginSuccess.accept(user); // Switch to main app
        } else {
            JOptionPane.showMessageDialog(this, "Login Failed: Invalid ID or Password", "Error", JOptionPane.ERROR_MESSAGE); // [cite: 39]
        }
    }

    private void showRegistrationDialog() {
        // Simple security check: Only allow if a Manager is already logged in OR assume this is accessed
        // via a special admin override. For this assignment, we'll ask for manager password first.
        String mgrPass = JOptionPane.showInputDialog("Enter Manager Password to authorize:");
        // Hardcoded "a2b1c0" for demo (based on PDF example) or check against DB managers
        boolean isManager = Database.getInstance().getEmployees().stream()
                .anyMatch(e -> e.getRole().equalsIgnoreCase("Manager") && e.checkPassword(mgrPass));
        
        if (!isManager) {
            JOptionPane.showMessageDialog(this, "Authorization Failed.");
            return;
        }

        // Registration Form
        JTextField newId = new JTextField();
        JTextField newName = new JTextField();
        JTextField newPass = new JTextField();
        String[] roles = {"Full-time", "Part-time"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        
        Object[] message = {
            "New ID:", newId, "Name:", newName, "Password:", newPass, "Role:", roleBox
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Register Employee", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Employee newEmp = new Employee(newId.getText(), newName.getText(), (String)roleBox.getSelectedItem(), newPass.getText());
            Database.getInstance().addEmployee(newEmp);
            JOptionPane.showMessageDialog(this, "Employee Registered Successfully!"); // [cite: 46]
        }
    }
}