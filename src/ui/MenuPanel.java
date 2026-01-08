package ui;

import core.Employee;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MenuPanel extends JPanel {

    // 定义统一配色
    private static final Color BG_COLOR = new Color(245, 245, 245);    // 浅灰背景
    private static final Color MGR_BTN_COLOR = new Color(255, 240, 245); // 经理按钮淡粉色
    private static final Color LOGOUT_BG_COLOR = new Color(255, 200, 200); // 退出按钮淡红色
    private static final Color LOGOUT_TEXT_COLOR = Color.BLACK; // 退出按钮字体黑色（修复看不见的问题）

    public MenuPanel(ActionListener onNavigate, Employee user) {
        // 1. 使用 BorderLayout 将菜单分为“上部功能区”和“底部退出区”
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY)); // 右侧边框
        setPreferredSize(new Dimension(220, 0)); // 固定宽度

        // --- A. 功能按钮区域 (放在顶部) ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10)); // 增加顶部间距

        // Group 1: 销售与前台
        addSectionHeader(contentPanel, "Sales & Front");
        addButton(contentPanel, "New Sale", onNavigate);
        addButton(contentPanel, "Sales History", onNavigate);
        addButton(contentPanel, "Search Info", onNavigate);
        
        addSeparator(contentPanel);

        // Group 2: 库存管理
        addSectionHeader(contentPanel, "Inventory");
        addButton(contentPanel, "Inventory View", onNavigate);
        addButton(contentPanel, "Stock Count", onNavigate);
        addButton(contentPanel, "Stock Movement", onNavigate);

        addSeparator(contentPanel);

        // Group 3: 人员与分析
        addSectionHeader(contentPanel, "Management");
        addButton(contentPanel, "Attendance", onNavigate);
        addButton(contentPanel, "Analytics", onNavigate);

        // Group 4: 经理专用
        if (user != null && "Manager".equalsIgnoreCase(user.getRole())) {
            addSeparator(contentPanel);
            addSectionHeader(contentPanel, "Manager Access");
            
            JButton perfBtn = createStyledButton("Performance");
            perfBtn.setBackground(MGR_BTN_COLOR);
            perfBtn.addActionListener(onNavigate);
            contentPanel.add(perfBtn);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));

            JButton editBtn = createStyledButton("Edit Data");
            editBtn.setActionCommand("Edit");
            editBtn.setBackground(MGR_BTN_COLOR);
            editBtn.addActionListener(onNavigate);
            contentPanel.add(editBtn);
        }

        // 将功能区包装在 NORTH 中，防止按钮被垂直拉伸
        JPanel northWrapper = new JPanel(new BorderLayout());
        northWrapper.add(contentPanel, BorderLayout.NORTH);
        northWrapper.setBackground(BG_COLOR);
        add(northWrapper, BorderLayout.CENTER);


        // --- B. 底部退出区域 (放在 SOUTH) ---
        JPanel bottomPanel = new JPanel(new GridLayout(1, 1));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // 底部边距
        bottomPanel.setBackground(BG_COLOR);

        JButton logoutBtn = new JButton("Logout / End Day");
        logoutBtn.setActionCommand("Logout");
        logoutBtn.addActionListener(onNavigate);
        
        // --- 修复颜色显示问题的关键代码 ---
        logoutBtn.setBackground(LOGOUT_BG_COLOR); // 淡红色背景
        logoutBtn.setForeground(LOGOUT_TEXT_COLOR); // 黑色字体 (确保可见)
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        logoutBtn.setFocusPainted(false); // 去掉点击虚线
        
        // 强制不透明，解决 Mac/某些 Windows 主题不显示背景色的问题
        logoutBtn.setOpaque(true);
        logoutBtn.setBorderPainted(false); 
        
        bottomPanel.add(logoutBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // --- 辅助方法 ---

    private void addButton(JPanel panel, String text, ActionListener listener) {
        JButton btn = createStyledButton(text);
        btn.addActionListener(listener);
        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setActionCommand(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); // 宽度填满，高度35
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.DARK_GRAY); // 普通按钮用深灰色字体
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        return btn;
    }

    private void addSectionHeader(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 10));
        label.setForeground(Color.GRAY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panel.add(label);
    }

    private void addSeparator(JPanel panel) {
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(sep);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }
}