package sales;

import core.Database;
import core.Employee;
import core.Product;
import core.SearchUtils;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class SalesPanel extends JPanel {
    private Employee currentUser;
    private JTextField customerField, modelField, qtyField, totalField;
    private JComboBox<String> methodBox;
    private JComboBox<String> outletBox; // 1. 新增 Outlet 选择框
    private JTextArea receiptArea;

    public SalesPanel(Employee user) {
        this.currentUser = user;
        setLayout(new BorderLayout());

        // 2. 将行数从 6 改为 7，以容纳新的一行 Outlet
        JPanel form = new JPanel(new GridLayout(7, 2, 5, 5)); 
        
        customerField = new JTextField();
        modelField = new JTextField();
        qtyField = new JTextField();
        totalField = new JTextField();
        // 设置 totalField 为不可编辑，因为我们会自动计算
        totalField.setEditable(false); 
        totalField.setBackground(new Color(230, 230, 230));

        methodBox = new JComboBox<>(new String[]{"Cash", "Debit/Credit Card", "E-wallet", "Other"});
        
        // 3. 初始化 Outlet 选择框 (C60 - C69)
        String[] outlets = {"C60", "C61", "C62", "C63", "C64", "C65", "C66", "C67", "C68", "C69"};
        outletBox = new JComboBox<>(outlets);

        JButton processBtn = new JButton("Process Sale");

        // --- 添加组件到面板 ---
        form.add(new JLabel("Customer Name:")); form.add(customerField);
        form.add(new JLabel("Select Outlet:")); form.add(outletBox); // 新增行
        form.add(new JLabel("Model Name:"));    form.add(modelField);
        form.add(new JLabel("Quantity:"));      form.add(qtyField);
        form.add(new JLabel("Total Price (RM):")); form.add(totalField);
        form.add(new JLabel("Method:"));        form.add(methodBox);
        form.add(new JLabel(""));               form.add(processBtn);

        receiptArea = new JTextArea();
        receiptArea.setEditable(false);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(receiptArea), BorderLayout.CENTER);

        processBtn.addActionListener(e -> processTransaction());

        // 4. 添加自动计算监听器 (Key Listener)
        KeyAdapter autoCalc = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calculateTotal();
            }
        };

        // 当用户在 Model 或 Quantity 输入框打字松开键盘时，触发计算
        modelField.addKeyListener(autoCalc);
        qtyField.addKeyListener(autoCalc);
    }

    // 5. 新增：自动计算总价的方法
    private void calculateTotal() {
        String model = modelField.getText().trim();
        String qtyStr = qtyField.getText().trim();

        // 如果还没输入完，就不计算
        if (model.isEmpty() || qtyStr.isEmpty()) return;

        // 查找产品获取单价
        Product p = SearchUtils.searchProduct(model, Database.getInstance().getProducts());
        
        if (p != null) {
            try {
                int qty = Integer.parseInt(qtyStr);
                double total = p.getPrice() * qty;
                totalField.setText(String.format("%.2f", total)); // 更新总价
            } catch (NumberFormatException e) {
                // 如果输入的不是数字，忽略
            }
        }
    }

    private void processTransaction() {
        String cust = customerField.getText();
        String model = modelField.getText();
        String method = (String) methodBox.getSelectedItem();
        String qtyStr = qtyField.getText();
        String totalStr = totalField.getText();
        
        // 6. 获取用户选择的 Outlet，不再使用硬编码的 "C60"
        String currentOutlet = (String) outletBox.getSelectedItem(); 

        Product p = SearchUtils.searchProduct(model, Database.getInstance().getProducts());

        if (p == null) {
            JOptionPane.showMessageDialog(this, "Model not found!");
            return;
        }

        try {
            int qty = Integer.parseInt(qtyStr);
            
            // 7. 检查指定 Outlet 的库存
            if (p.getStock(currentOutlet) < qty) {
                JOptionPane.showMessageDialog(this, "Insufficient Stock at " + currentOutlet + "!");
                return;
            }

            // 计算总价 (如果自动计算没触发或被清空，这里做最后一次保障)
            double total;
            if (!totalStr.trim().isEmpty()) {
                total = Double.parseDouble(totalStr);
            } else {
                total = p.getPrice() * qty;
            }

            // 8. 更新库存 (Update Stock)
            p.updateStock(currentOutlet, -qty);
            Database.getInstance().saveProducts();

            // Create Record
            String date = LocalDate.now().toString();
            String time = LocalTime.now().toString().substring(0, 5);
            SalesRecord sale = new SalesRecord(date, time, currentUser.getId(), cust, model, qty, total, method);
            Database.getInstance().addSale(sale);

            // Generate Receipt
            generateReceiptFile(sale);
            receiptArea.setText("Transaction Successful at " + currentOutlet + "!\n" + sale.toString());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Quantity");
        }
    }

    private void generateReceiptFile(SalesRecord sale) {
        String filename = "sales_" + sale.getDate() + ".txt";
        String timeStr = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"))
                            .toLowerCase().replace("am", "a.m.").replace("pm", "p.m.");
        
        try (FileWriter fw = new FileWriter(filename, true)) { // append mode
            fw.write("=== Record New Sale ===\n");
            fw.write("Date: " + sale.getDate() + "\n");
            fw.write("Time: " + timeStr + "\n");
            fw.write("Outlet: " + outletBox.getSelectedItem() + "\n"); // 在收据上也记录 Outlet
            fw.write("Customer Name: " + sale.getCustomerName() + "\n");
            fw.write("Item(s) Purchased:\n");
            fw.write("Model: " + sale.getModelName() + "\n");
            fw.write("Quantity: " + sale.getQuantity() + "\n");
            fw.write(String.format("Unit Price: RM%.2f\n", (sale.getTotal() / sale.getQuantity())));
            fw.write("Transaction Method: " + sale.getMethod() + "\n");
            fw.write(String.format("Subtotal: RM%.2f\n", sale.getTotal()));
            fw.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}