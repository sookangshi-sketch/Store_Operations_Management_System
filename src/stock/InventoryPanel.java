/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package stock;

import core.Database;
import core.Product;
import javax.swing.*;
import java.awt.*;

public class InventoryPanel extends JPanel {
    public InventoryPanel() {
        setLayout(new BorderLayout());
        String[] columns = {"Model", "Price", "C60 Stock", "C61 Stock", "C62 Stock"};
        
        java.util.List<Product> list = Database.getInstance().getProducts();
        String[][] data = new String[list.size()][columns.length];
        
        for(int i=0; i<list.size(); i++) {
            Product p = list.get(i);
            data[i][0] = p.getModelName();
            data[i][1] = String.valueOf(p.getPrice());
            data[i][2] = String.valueOf(p.getStock("C60"));
            data[i][3] = String.valueOf(p.getStock("C61"));
            data[i][4] = String.valueOf(p.getStock("C62"));
        }

        JTable table = new JTable(data, columns);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}