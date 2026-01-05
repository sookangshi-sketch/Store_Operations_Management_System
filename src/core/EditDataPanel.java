package core;

import javax.swing.*;
import java.awt.*;

public class EditDataPanel extends JPanel {
    // 模式选择
    private JComboBox<String> modeCombo;
    private JPanel dynamicPanel;
    private JTextArea logArea;

    // Product 控件
    private JTextField prodModelField, stockField;
    private JComboBox<String> outletCombo;

    // Employee 控件
    private JTextField empIdField, empNameField, empPassField;
    private JComboBox<String> empRoleCombo;

    public EditDataPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. 顶部：选择要编辑的类型
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Editing Mode:"));
        String[] modes = {"Edit Product Stock", "Edit Employee Info"};
        modeCombo = new JComboBox<>(modes);
        topPanel.add(modeCombo);
        add(topPanel, BorderLayout.NORTH);

        // 2. 中间：动态表单
        dynamicPanel = new JPanel(new CardLayout());

        // 创建两个面板
        dynamicPanel.add(createProductPanel(), "Edit Product Stock");
        dynamicPanel.add(createEmployeePanel(), "Edit Employee Info");

        add(dynamicPanel, BorderLayout.CENTER);

        // 3. 底部：日志
        logArea = new JTextArea(5, 40);
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.SOUTH);

        // 监听模式切换
        modeCombo.addActionListener(e -> {
            CardLayout cl = (CardLayout) dynamicPanel.getLayout();
            cl.show(dynamicPanel, (String) modeCombo.getSelectedItem());
        });
    }

    // --- A. 产品编辑面板 ---
    private JPanel createProductPanel() {
        JPanel p = new JPanel(new GridLayout(5, 2, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder("Product Stock Management"));

        prodModelField = new JTextField();
        stockField = new JTextField();
        String[] outlets = {"C60","C61","C62","C63","C64","C65","C66","C67","C68","C69"};
        outletCombo = new JComboBox<>(outlets);
        JButton updateBtn = new JButton("Update Stock");

        p.add(new JLabel("Search Model Name:")); p.add(prodModelField);
        p.add(new JLabel("Select Outlet:")); p.add(outletCombo);
        p.add(new JLabel("New Quantity:")); p.add(stockField);
        p.add(new JLabel("")); p.add(updateBtn);

        updateBtn.addActionListener(e -> updateProductStock());
        return p;
    }

    // --- B. 员工编辑面板 ---
    private JPanel createEmployeePanel() {
        JPanel p = new JPanel(new GridLayout(5, 2, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder("Employee Info Management"));

        empIdField = new JTextField(); // 用来搜索
        empNameField = new JTextField();
        empPassField = new JTextField();
        String[] roles = {"Manager", "Full-time", "Part-time"};
        empRoleCombo = new JComboBox<>(roles);

        JButton findBtn = new JButton("Find & Load");
        JButton saveBtn = new JButton("Save Changes");

        // 布局有点紧凑，第一行放搜索
        JPanel searchRow = new JPanel(new BorderLayout());
        searchRow.add(empIdField, BorderLayout.CENTER);
        searchRow.add(findBtn, BorderLayout.EAST);

        p.add(new JLabel("Enter Employee ID:")); p.add(searchRow);
        p.add(new JLabel("Name:")); p.add(empNameField);
        p.add(new JLabel("Role:")); p.add(empRoleCombo);
        p.add(new JLabel("Password:")); p.add(empPassField);
        p.add(new JLabel("")); p.add(saveBtn);

        findBtn.addActionListener(e -> loadEmployee());
        saveBtn.addActionListener(e -> saveEmployee());
        return p;
    }

    // Logic: Update Product
    private void updateProductStock() {
        String model = prodModelField.getText().trim();
        String outlet = (String) outletCombo.getSelectedItem();

        Product p = SearchUtils.searchProduct(model, Database.getInstance().getProducts());

        if (p == null) {
            logArea.append("❌ Product '" + model + "' not found.\n");
            return;
        }

        try {
            int newQty = Integer.parseInt(stockField.getText().trim());
            p.setStock(outlet, newQty);
            Database.getInstance().saveProducts();
            logArea.append("✅ Updated: " + p.getModelName() + " [" + outlet + "] -> " + newQty + "\n");
        } catch (NumberFormatException e) {
            logArea.append("❌ Error: Invalid quantity.\n");
        }
    }

    // Logic: Load Employee
    private Employee currentEditingEmp = null; // 临时存一下正在编辑谁

    private void loadEmployee() {
        String id = empIdField.getText().trim();
        currentEditingEmp = null; // reset

        for (Employee e : Database.getInstance().getEmployees()) {
            if (e.getId().equalsIgnoreCase(id)) {
                currentEditingEmp = e;
                break;
            }
        }

        if (currentEditingEmp != null) {
            empNameField.setText(currentEditingEmp.getName());
            empPassField.setText(currentEditingEmp.getPassword());
            empRoleCombo.setSelectedItem(currentEditingEmp.getRole());
            logArea.append("🔎 Loaded Employee: " + currentEditingEmp.getName() + "\n");
        } else {
            logArea.append("❌ Employee ID '" + id + "' not found.\n");
            empNameField.setText("");
            empPassField.setText("");
        }
    }

    // Logic: Save Employee
    private void saveEmployee() {
        if (currentEditingEmp == null) {
            logArea.append("⚠️ Please find an employee first.\n");
            return;
        }

        // 使用我们在步骤1里加的 Setter
        currentEditingEmp.setName(empNameField.getText().trim());
        currentEditingEmp.setRole((String) empRoleCombo.getSelectedItem());
        currentEditingEmp.setPassword(empPassField.getText().trim());

        Database.getInstance().saveEmployees();
        logArea.append("✅ Employee " + currentEditingEmp.getId() + " updated successfully!\n");
    }
}