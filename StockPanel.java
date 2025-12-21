import java.awt.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class StockPanel extends JPanel {

    private DefaultTableModel tableModel;
    private JTable table;
    private MainFrame mainFrame;
    public StockPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // --- 顶部：操作栏 ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnBack = new JButton("<< Back to Menu");
        JButton btnRefresh = new JButton("Refresh Data");
        
        // 简单的进货/出货模拟
        JButton btnUpdate = new JButton("Update Stock (In/Out)");

        topPanel.add(btnBack);
        topPanel.add(btnRefresh);
        topPanel.add(btnUpdate);
        add(topPanel, BorderLayout.NORTH);

        // --- 中间：库存表格 ---
        // 表头：Model Name, Price, 以及各个分店的库存
        // 这里为了简单，我们只显示 Model, Price, 和一个总库存/详情
        String[] columns = {"Model Name", "Price (RM)", "Model Stock Details"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        // 设置列宽
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(400); // 库存详情列宽一点

        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- 初始化数据 ---
        loadData();

        // ============================
        // 事件逻辑
        // ============================
        
        // 1. 返回主菜单
        btnBack.addActionListener(e -> mainFrame.showCard("MENU"));

        // 2. 刷新数据
        btnRefresh.addActionListener(e -> loadData());

        // 3. 修改库存 (简单弹窗版，替代原来的 Stock In/Out)
        btnUpdate.addActionListener(e -> {
            DataManager.loadAll(); 
            // 弹出对话框输入信息
            JTextField txtOutlet = new JTextField();
            JTextField txtModel = new JTextField();
            JTextField txtQty = new JTextField(); // 正数是进货，负数是出货
            
            Object[] message = {
                "Model Code (e.g., C99):", txtOutlet,
                "Model Name:", txtModel,
                "Quantity (+ for In, - for Out):", txtQty
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Stock In/Out", JOptionPane.OK_CANCEL_OPTION);
            
            if (option == JOptionPane.OK_OPTION) {
                try {
                    String outlet = txtOutlet.getText().trim();
                    String modelName = txtModel.getText().trim();
                    int qty = Integer.parseInt(txtQty.getText().trim());

                    // 查找并更新
                    boolean found = false;
                    for (Model m : DataManager.models) {
                        if (m.getName().equalsIgnoreCase(modelName)) {
                            // 调用 Model 里的方法更新库存
                            m.changeStockForOutlet(outlet, qty);
                            found = true;
                            break;
                        }
                    }
                    
                    if (found) {
                        DataManager.saveModels(); // 记得保存！
                        loadData(); // 刷新表格
                        JOptionPane.showMessageDialog(this, "Stock updated successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Model not found!");
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input!");
                }
            }
            DataManager.saveAll(); 
        });
    }

    // 加载数据到表格
    private void loadData() {
        DataManager.loadAll(); 
        tableModel.setRowCount(0); // 清空旧数据
        for (Model m : DataManager.models) {
            StringBuilder stockStr = new StringBuilder();
            for (Map.Entry<String, Integer> entry : m.getStock().entrySet()) {
                stockStr.append(entry.getKey()).append(":").append(entry.getValue()).append("  ");
            }

            tableModel.addRow(new Object[]{
                m.getName(),
                String.format("%.2f", m.getPrice()),
                stockStr.toString()
            });
        }
        DataManager.saveAll(); 
    }
}
