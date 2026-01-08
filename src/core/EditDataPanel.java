/*
 * EditDataPanel.java (Updated with ComboBox for Payment Method)
 */
package core;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import sales.SalesRecord;

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

    // Sales 控件 
    private JTextField salesSearchField; 
    private JTextField editCustField, editModelField, editQtyField, editTotalField;
    // [New Feature] Changed from TextField to ComboBox
    private JComboBox<String> editMethodBox; 
    private SalesRecord currentEditingSale = null;

    public EditDataPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. 顶部：选择要编辑的类型
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Editing Mode:"));
        String[] modes = {"Edit Product Stock", "Edit Employee Info", "Edit Sales Info"};
        modeCombo = new JComboBox<>(modes);
        topPanel.add(modeCombo);
        add(topPanel, BorderLayout.NORTH);

        // 2. 中间：动态表单
        dynamicPanel = new JPanel(new CardLayout());
        dynamicPanel.add(createProductPanel(), "Edit Product Stock");
        dynamicPanel.add(createEmployeePanel(), "Edit Employee Info");
        dynamicPanel.add(createSalesPanel(), "Edit Sales Info"); 

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

        empIdField = new JTextField();
        empNameField = new JTextField();
        empPassField = new JTextField();
        String[] roles = {"Manager", "Full-time", "Part-time"};
        empRoleCombo = new JComboBox<>(roles);
        JButton findBtn = new JButton("Find & Load");
        JButton saveBtn = new JButton("Save Changes");

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

    // --- C. 销售记录编辑面板 ---
    private JPanel createSalesPanel() {
        JPanel p = new JPanel(new GridLayout(7, 2, 5, 5));
        p.setBorder(BorderFactory.createTitledBorder("Sales Record Management"));

        salesSearchField = new JTextField();
        JButton searchBtn = new JButton("Find Last Sale");
        
        editCustField = new JTextField();
        editModelField = new JTextField();
        editQtyField = new JTextField();
        editTotalField = new JTextField();
        
        // [New Feature] Initialize ComboBox instead of TextField
        editMethodBox = new JComboBox<>(new String[]{"Cash", "Debit/Credit Card", "E-wallet", "Other"});
        
        JButton saveSaleBtn = new JButton("Update Sale Record");

        JPanel searchBox = new JPanel(new BorderLayout());
        searchBox.add(salesSearchField, BorderLayout.CENTER);
        searchBox.add(searchBtn, BorderLayout.EAST);

        p.add(new JLabel("Search Customer Name:")); p.add(searchBox);
        p.add(new JLabel("Customer Name:")); p.add(editCustField);
        p.add(new JLabel("Model:")); p.add(editModelField);
        p.add(new JLabel("Quantity:")); p.add(editQtyField);
        p.add(new JLabel("Total Price:")); p.add(editTotalField);
        p.add(new JLabel("Method:")); p.add(editMethodBox); // Add Box
        p.add(new JLabel("")); p.add(saveSaleBtn);

        searchBtn.addActionListener(e -> loadSales());
        saveSaleBtn.addActionListener(e -> saveSales());

        return p;
    }

    // --- Logic Methods ---

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

    private Employee currentEditingEmp = null;
    private void loadEmployee() {
        String id = empIdField.getText().trim();
        currentEditingEmp = null;
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
        }
    }

    private void saveEmployee() {
        if (currentEditingEmp == null) return;
        currentEditingEmp.setName(empNameField.getText().trim());
        currentEditingEmp.setRole((String) empRoleCombo.getSelectedItem());
        currentEditingEmp.setPassword(empPassField.getText().trim());
        Database.getInstance().saveEmployees();
        logArea.append("✅ Employee updated successfully!\n");
    }

    // --- Sales Logic ---
    private void loadSales() {
        String custName = salesSearchField.getText().trim();
        List<SalesRecord> sales = Database.getInstance().getSalesLog();
        currentEditingSale = null;

        for (int i = sales.size() - 1; i >= 0; i--) {
            if (sales.get(i).getCustomerName().equalsIgnoreCase(custName)) {
                currentEditingSale = sales.get(i);
                break;
            }
        }

        if (currentEditingSale != null) {
            editCustField.setText(currentEditingSale.getCustomerName());
            editModelField.setText(currentEditingSale.getModelName());
            editQtyField.setText(String.valueOf(currentEditingSale.getQuantity()));
            editTotalField.setText(String.valueOf(currentEditingSale.getTotal()));
            
            // [New Feature] Set Selected Item in ComboBox
            editMethodBox.setSelectedItem(currentEditingSale.getMethod());
            
            logArea.append("🔎 Loaded Sale for: " + custName + "\n");
        } else {
            logArea.append("❌ No sales found for customer: " + custName + "\n");
        }
    }

    private void saveSales() {
        if (currentEditingSale == null) return;
        
        try {
            // [New Feature] Get value from ComboBox
            String newMethod = (String) editMethodBox.getSelectedItem();

            SalesRecord newRecord = new SalesRecord(
                currentEditingSale.getDate(),
                currentEditingSale.getTime(),
                currentEditingSale.getEmployeeId(),
                editCustField.getText(),
                editModelField.getText(),
                Integer.parseInt(editQtyField.getText()),
                Double.parseDouble(editTotalField.getText()),
                newMethod // Use the new method
            );
            
            List<SalesRecord> list = Database.getInstance().getSalesLog();
            int index = list.indexOf(currentEditingSale);
            if(index != -1) {
                list.set(index, newRecord);
                Database.getInstance().saveSales();
                logArea.append("✅ Sales record updated!\n");
                currentEditingSale = newRecord; 
            }

        } catch (Exception e) {
            logArea.append("❌ Error updating sale: " + e.getMessage() + "\n");
        }
    }
}