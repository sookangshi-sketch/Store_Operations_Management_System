package uiforlogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UIforLogin extends JFrame {

    private JTextField txtUserID;
    private JPasswordField txtPassword;
    private JLabel lblMessage;

    private final String VALID_ID = "C6001";
    private final String VALID_PASSWORD = "a2b1c0";

    public UIforLogin() {
        setTitle("GoldenHour Store System");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ===== 渐变背景 Panel =====
        JPanel background = new GradientPanel();
        background.setLayout(new BorderLayout());
        add(background);

        // ===== 标题 =====
        JLabel lblTitle = new JLabel("GoldenHour", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Employee Login", JLabel.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(230, 230, 230));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);

        background.add(titlePanel, BorderLayout.NORTH);

        // ===== 表单卡片 =====
        JPanel card = new JPanel(new GridLayout(3, 2, 10, 10));
        card.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        card.setBackground(Color.WHITE);

        card.add(new JLabel("User ID"));
        txtUserID = new JTextField();
        card.add(txtUserID);

        card.add(new JLabel("Password"));
        txtPassword = new JPasswordField();
        card.add(txtPassword);

        JButton btnLogin = new JButton("Login");
        styleButton(btnLogin);
        card.add(new JLabel());
        card.add(btnLogin);

        JPanel centerWrapper = new JPanel();
        centerWrapper.setOpaque(false);
        centerWrapper.add(card);

        background.add(centerWrapper, BorderLayout.CENTER);

        // ===== 提示信息 =====
        lblMessage = new JLabel("", JLabel.CENTER);
        lblMessage.setForeground(Color.YELLOW);
        background.add(lblMessage, BorderLayout.SOUTH);

        // ===== 事件 =====
        btnLogin.addActionListener(e -> login());
        txtPassword.addActionListener(e -> login()); // Enter 登录

        setVisible(true);
    }

    private void login() {
        String userID = txtUserID.getText();
        String password = new String(txtPassword.getPassword());

        if (userID.equals(VALID_ID) && password.equals(VALID_PASSWORD)) {
            lblMessage.setForeground(Color.GREEN);
            lblMessage.setText("Login Successful! Welcome back 👋");
        } else {
            lblMessage.setForeground(Color.ORANGE);
            lblMessage.setText("Unsuccessful login. Try again.");
            txtPassword.setText("");
        }
    }

    // ===== 按钮样式 =====
    private void styleButton(JButton button) {
        button.setBackground(new Color(40, 90, 160));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(30, 70, 130));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(40, 90, 160));
            }
        });
    }

    // ===== 渐变背景 Panel =====
    class GradientPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(
                    0, 0, new Color(20, 40, 80),
                    0, getHeight(), new Color(70, 130, 200)
            );
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        new UIforLogin();
    }
}

