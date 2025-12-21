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
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPanel extends JPanel {

    private MainFrame mainFrame;
    private JLabel welcomeLabel;

    // -- COLORS --
    private final Color PRIMARY_COLOR = new Color(51, 153, 255);
    private final Color BG_COLOR = new Color(240, 242, 245); // Slightly darker for contrast
    private final Color CARD_BG = Color.WHITE;
    private final Color TEXT_DARK = new Color(50, 50, 50);
    private final Color TEXT_LIGHT = new Color(120, 120, 120);

    public MenuPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // 1. HEADER SECTION
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(25, 40, 25, 40));

        // Title & Welcome Text
        JPanel textContainer = new JPanel(new GridLayout(2, 1, 0, 5));
        textContainer.setOpaque(false);
        
        JLabel titleLabel = new JLabel("GoldenHour Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        
        welcomeLabel = new JLabel("Welcome, User");
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        welcomeLabel.setForeground(new Color(230, 230, 230));

        textContainer.add(titleLabel);
        textContainer.add(welcomeLabel);

        // Logout Button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(255, 255, 255, 40));
        logoutBtn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(255, 255, 255, 100), 1, true), // Rounded-ish border
                new EmptyBorder(8, 20, 8, 20)
        ));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> mainFrame.logout());

        // Hover effect for Logout
        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { logoutBtn.setOpaque(true); }
            public void mouseExited(MouseEvent e) { logoutBtn.setOpaque(false); }
        });

        headerPanel.add(textContainer, BorderLayout.WEST);
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);


        // 2. DASHBOARD GRID SECTION
        // We use a GridBagLayout wrapper to keep the buttons centered and not stretched
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(BG_COLOR);

        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        gridPanel.setBackground(BG_COLOR);
        // Set a preferred size so it doesn't get too small or too big
        gridPanel.setPreferredSize(new Dimension(900, 350)); 

        // --- ADDING CARDS WITH ICONS ---
        // 1. Attendance (Lead: You)
        gridPanel.add(createMenuCard("Attendance", "Clock In/Out", "🕒", "ATTENDANCE", new Color(46, 204, 113)));
        
        // 2. Sales (Member 5)
        gridPanel.add(createMenuCard("Point of Sales", "New Transaction", "🛒", "SALES", new Color(52, 152, 219)));
        
        // 3. Inventory (Member 4)
        gridPanel.add(createMenuCard("Inventory", "Stock Check", "📦", "STOCK", new Color(155, 89, 182)));
        
        // 4. Search (Member 6)
        gridPanel.add(createMenuCard("Search Data", "Records Lookup", "🔍", "SEARCH", new Color(241, 196, 15)));
        
        // 5. Employees (Member 1)
        gridPanel.add(createMenuCard("Employees", "Manage Staff", "👥", "ADMIN", new Color(231, 76, 60)));
        
        // 6. Invisible Placeholder (To fill the empty slot and balance the grid)
        JPanel placeholder = new JPanel();
        placeholder.setOpaque(false);
        gridPanel.add(placeholder);

        centerWrapper.add(gridPanel);
        add(centerWrapper, BorderLayout.CENTER);


        // 3. AUTO-REFRESH LOGIC
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                String user = mainFrame.getSession();
                welcomeLabel.setText(user != null ? "Welcome back, " + user : "Welcome, Guest");
            }
        });
    }

    // --- HELPER METHOD TO CREATE BEAUTIFUL CARDS ---
    private JButton createMenuCard(String title, String subtitle, String iconSymbol, String targetCard, Color accentColor) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(15, 0)); // Gap between icon and text
        btn.setBackground(CARD_BG);
        
        // Compound border: Line on outside + Padding on inside
        btn.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1), 
            new EmptyBorder(20, 25, 20, 20)
        ));
        
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);

        // 1. Left Color Bar (Thicker for better visibility)
        JPanel colorBar = new JPanel();
        colorBar.setBackground(accentColor);
        colorBar.setPreferredSize(new Dimension(8, 0)); 
        
        // 2. Icon (Using Unicode Text)
        JLabel iconLbl = new JLabel(iconSymbol);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40)); // Large Icon
        iconLbl.setForeground(accentColor);
        iconLbl.setBorder(new EmptyBorder(0, 10, 0, 0));

        // 3. Text Section
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        textPanel.setBackground(CARD_BG);
        textPanel.setOpaque(false);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLbl.setForeground(TEXT_DARK);

        JLabel subLbl = new JLabel(subtitle);
        subLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subLbl.setForeground(TEXT_LIGHT);

        textPanel.add(titleLbl);
        textPanel.add(subLbl);

        // Assemble the button
        btn.add(colorBar, BorderLayout.WEST);
        btn.add(iconLbl, BorderLayout.EAST); // Icon on the right looks modern
        btn.add(textPanel, BorderLayout.CENTER);

        // --- HOVER EFFECT ---
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(248, 249, 250)); // Light Gray
                btn.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(accentColor, 1), // Highlight border with color
                    new EmptyBorder(20, 25, 20, 20)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(CARD_BG); // Reset
                btn.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(230, 230, 230), 1),
                    new EmptyBorder(20, 25, 20, 20)
                ));
            }
        });

        // Click Action
        btn.addActionListener(e -> {
            if (targetCard.equals("ATTENDANCE")) {
                mainFrame.showCard(targetCard);
            } else if (targetCard.equals("STOCK")) {
                mainFrame.showCard(targetCard);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Feature '" + title + "' is being built by another teammate!", 
                    "Under Construction", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return btn;
    }
}