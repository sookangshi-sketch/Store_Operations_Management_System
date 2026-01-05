/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import java.util.HashMap;
import java.util.Map;

public class Product implements CSVSerializable {
    private String modelName;
    private double price;
    private Map<String, Integer> stockPerOutlet; // OutletCode -> Quantity

    public Product() {
        stockPerOutlet = new HashMap<>();
    }

    public Product(String modelName, double price) {
        this.modelName = modelName;
        this.price = price;
        this.stockPerOutlet = new HashMap<>();
    }

    public String getModelName() { return modelName; }
    public double getPrice() { return price; }
    
    public int getStock(String outletCode) {
        return stockPerOutlet.getOrDefault(outletCode, 0);
    }

    public void setStock(String outletCode, int quantity) {
        stockPerOutlet.put(outletCode, quantity);
    }
    
    public void updateStock(String outletCode, int change) {
        int current = getStock(outletCode);
        stockPerOutlet.put(outletCode, Math.max(0, current + change));
    }

    // Returns a copy of the stock map for display
    public Map<String, Integer> getAllStocks() {
        return new HashMap<>(stockPerOutlet);
    }

    @Override
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(modelName).append(",").append(price);
        // Assumes specific order C60-C69 for CSV consistency or uses key-value pairs if needed.
        // For simplicity based on your PDF example, we iterate strictly:
        String[] outlets = {"C60","C61","C62","C63","C64","C65","C66","C67","C68","C69"};
        for (String code : outlets) {
            sb.append(",").append(getStock(code));
        }
        return sb.toString();
    }

    @Override
    public void fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length >= 12) { // Model + Price + 10 Outlets
            this.modelName = parts[0].trim();
            this.price = Double.parseDouble(parts[1].trim());
            
            String[] outlets = {"C60","C61","C62","C63","C64","C65","C66","C67","C68","C69"};
            for (int i = 0; i < outlets.length; i++) {
                if (i + 2 < parts.length) {
                    stockPerOutlet.put(outlets[i], Integer.parseInt(parts[i+2].trim()));
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return modelName + " (RM" + price + ")";
    }
}