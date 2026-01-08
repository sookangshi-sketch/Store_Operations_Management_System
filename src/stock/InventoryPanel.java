package stock;

import core.Database;
import core.Product;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class InventoryPanel extends JPanel {
    
    // 这里定义所有的店铺 ID
    // 如果你的 Database 类里有方法能获取所有店铺ID (例如 Database.getAllStoreIds())，最好用那个
    // 这里暂时用数组列出所有可能的店铺
    private static final String[] STORE_IDS = {"C60", "C61", "C62", "C63", "C64", "C65", "C66", "C67", "C68", "C69"};

    public InventoryPanel() {
        setLayout(new BorderLayout());

        // 1. 动态构建表头
        // 列数 = Model + Price + (店铺数量) + Total
        String[] columns = new String[3 + STORE_IDS.length];
        columns[0] = "Model";
        columns[1] = "Price";
        
        // 循环填入店铺列名
        for (int i = 0; i < STORE_IDS.length; i++) {
            columns[2 + i] = STORE_IDS[i]; 
        }
        
        // 最后一列放总库存
        columns[columns.length - 1] = "Total";

        // 2. 获取数据并填充
        List<Product> list = Database.getInstance().getProducts();
        String[][] data = new String[list.size()][columns.length];
        
        for (int i = 0; i < list.size(); i++) {
            Product p = list.get(i);
            
            // 基础信息
            data[i][0] = p.getModelName();
            data[i][1] = String.format("%.2f", p.getPrice()); // 保留两位小数
            
            int totalStock = 0;

            // 循环获取每个店铺的库存
            for (int j = 0; j < STORE_IDS.length; j++) {
                int stock = p.getStock(STORE_IDS[j]);
                data[i][2 + j] = String.valueOf(stock);
                totalStock += stock;
            }
            
            // 最后一列填总数
            data[i][columns.length - 1] = String.valueOf(totalStock);
        }

        // 3. 创建表格 (设置不可编辑)
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        
        // 4. 美化一点：增加顶部标题或刷新按钮（可选）
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Full Inventory Overview");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        topPanel.add(titleLabel);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}