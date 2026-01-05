/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import java.util.ArrayList;
import java.util.List;
import sales.SalesRecord;

public class SearchUtils {

    // Linear Search for Product by Model Name
    public static Product searchProduct(String modelName, List<Product> products) {
        for (Product p : products) {
            if (p.getModelName().equalsIgnoreCase(modelName)) {
                return p;
            }
        }
        return null;
    }

    // Linear Search for Sales by keyword (Customer name or Model)
    public static List<SalesRecord> searchSales(String keyword, List<SalesRecord> records) {
        List<SalesRecord> results = new ArrayList<>();
        String key = keyword.toLowerCase();
        for (SalesRecord r : records) {
            if (r.getCustomerName().toLowerCase().contains(key) || 
                r.getModelName().toLowerCase().contains(key)) {
                results.add(r);
            }
        }
        return results;
    }
}