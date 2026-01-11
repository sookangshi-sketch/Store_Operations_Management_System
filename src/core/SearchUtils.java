package core;

import java.util.ArrayList;
import java.util.List;
import sales.SalesRecord;

public class SearchUtils {

    // 1. Optimization: Use contains to implement fuzzy search, and it is case-insensitive
    public static Product searchProduct(String keyword, List<Product> products) {
        if (keyword == null) return null;
        String key = keyword.toLowerCase().trim();

        for (Product p : products) {
            // If the ID or name contains this word, it is considered found
            if (p.getModelName().toLowerCase().contains(key)) {
                return p;
            }
        }
        return null;
    }

    // 2. Sales record search remains unchanged
    public static List<SalesRecord> searchSales(String keyword, List<SalesRecord> records) {
        List<SalesRecord> results = new ArrayList<>();
        String key = keyword.toLowerCase().trim();
        for (SalesRecord r : records) {
            if (r.getCustomerName().toLowerCase().contains(key) ||
                    r.getModelName().toLowerCase().contains(key)) {
                results.add(r);
            }
        }
        return results;
    }
}