package LoginUI;

import Login.Login; // Fixed import
import javax.swing.*;
import java.awt.*;
import FOP_Assignment.MainFrame; // Ensure correct package for MainFrame

public class LoginUI extends JPanel {
    private JTextField txtUserID;
    private JPasswordField txtPassword;
    private JLabel lblMessage;
    private MainFrame mainFrame;

    // FIXED: Constructor name now matches Class name
    public LoginUI(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // Background with Gradient
        GradientPanel background = new GradientPanel();
        background.setLayout(new GridBagLayout());
        add(background);

        // Login Card Setup
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        card.setBackground(Color.WHITE);

        txtUserID = new JTextField(15);
        txtPassword = new JPasswordField(15);
        JButton btnLogin = new JButton("Login");
        lblMessage = new JLabel(" ");
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        // UI Components [cite: 27]
        card.add(new JLabel("User ID:"));
        card.add(txtUserID);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(new JLabel("Password:"));
        card.add(txtPassword);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(btnLogin);
        card.add(lblMessage);

        background.add(card);

        // Login Action [cite: 27, 28]
        btnLogin.addActionListener(e -> {
            String id = txtUserID.getText();
            String pass = new String(txtPassword.getPassword());
            
            // Authenticate using your Logic class
            Login.Employee user = Login.Login.EmployeeSystem.authenticate(id, pass);

            if (user != null) {
                // SUCCESS: Save the ID to the MainFrame session [cite: 103]
                mainFrame.setSession(user.id); 
                mainFrame.showCard("MENU");    
                lblMessage.setText(" ");
                txtPassword.setText("");
            } else {
                // FAILURE: Show unsuccessful login message [cite: 28, 39]
                lblMessage.setText("Invalid ID or Password"); 
                lblMessage.setForeground(Color.RED);
            }
        });
    }

    // Gradient Panel for visual design [cite: 214]
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(0, 0, new Color(20, 40, 80), 0, getHeight(), new Color(70, 130, 200));
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}