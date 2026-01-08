package ui;

import attendance.AttendancePanel;
import auth.EmailService;
import auth.LoginPanel;
import core.Database;
import core.EditDataPanel;
import core.Employee;
import core.SearchPanel;
import java.awt.*;
import javax.swing.*;
import sales.SalesHistoryPanel;
import sales.SalesPanel;
import stock.AnalyticsPanel;
import stock.EmployeePerformancePanel;
import stock.InventoryPanel;
import stock.StockCountPanel;
import stock.StockMovementPanel;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private JPanel contentPanel; 
    private Employee currentUser;

    public MainFrame() {
        setTitle("GoldenHour Store Operations System");
        // 稍微加宽一点，适应左侧固定的 MenuPanel
        setSize(1024, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load Data on Startup
        Database.getInstance().loadAllData();

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // 1. Add Login Panel
        LoginPanel loginPanel = new LoginPanel(this::onLoginSuccess);
        mainContainer.add(loginPanel, "LOGIN");

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
        
        // --- 顶部 Header ---
        JLabel header = new JLabel("  Logged in as: " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        header.setPreferredSize(new Dimension(0, 40));
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setOpaque(true);
        header.setBackground(new Color(230, 230, 250)); // 淡紫色背景
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY)); // 底部线条
        dashboard.add(header, BorderLayout.NORTH);

        // --- 中间内容区域 ---
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 内容和边框留白
        
        // 添加所有功能面板
        contentPanel.add(new AttendancePanel(currentUser), "Attendance");
        contentPanel.add(new StockCountPanel(), "Stock Count");
        contentPanel.add(new InventoryPanel(), "Inventory View");
        contentPanel.add(new StockMovementPanel(currentUser), "Stock Movement");
        contentPanel.add(new SalesPanel(currentUser), "New Sale");
        contentPanel.add(new SalesHistoryPanel(), "Sales History");
        contentPanel.add(new SearchPanel(), "Search Info");
        contentPanel.add(new AnalyticsPanel(), "Analytics");
        
        // 经理面板 (即使隐藏了按钮，这里加上也没坏处)
        contentPanel.add(new EditDataPanel(), "Edit");
        contentPanel.add(new EmployeePerformancePanel(), "Performance");
        
        dashboard.add(contentPanel, BorderLayout.CENTER);

        // --- 左侧导航栏 ---
        MenuPanel menu = new MenuPanel(e -> navigate(e.getActionCommand()), currentUser);
        dashboard.add(menu, BorderLayout.WEST);

        // Add dashboard to main container
        mainContainer.add(dashboard, "DASHBOARD");
        
        // 刷新一下布局，确保界面渲染正确
        mainContainer.revalidate();
        mainContainer.repaint();
    }

    private void navigate(String command) {
        if (command.equals("Logout")) {
            performLogout();
            return;
        }

        // 安全检查 (防止绕过 UI 直接调用)
        boolean isManager = "Manager".equalsIgnoreCase(currentUser.getRole());
        if ((command.equals("Performance") || command.equals("Edit")) && !isManager) {
            JOptionPane.showMessageDialog(this, "Access Denied: Manager Only.");
            return;
        }

        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, command);
    }

    private void performLogout() {
        int choice = JOptionPane.showConfirmDialog(this, 
            "Is this the end of the business day? (Triggers Auto-Email)", 
            "Logout", JOptionPane.YES_NO_CANCEL_OPTION);
            
        if (choice == JOptionPane.CANCEL_OPTION) return;
        
        if (choice == JOptionPane.YES_OPTION) {
            // 这里填写实际需要的邮箱
            EmailService.sendDailyReport("24231773@siswa.um.edu.my"); 
        }

        currentUser = null;
        cardLayout.show(mainContainer, "LOGIN");
    }
}