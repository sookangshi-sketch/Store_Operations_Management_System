package core;

import java.util.ArrayList;
import java.util.List;
import sales.SalesRecord;

public class SearchUtils {

    // 1. 优化：使用 contains 实现模糊搜索，且不区分大小写
    public static Product searchProduct(String keyword, List<Product> products) {
        if (keyword == null) return null;
        String key = keyword.toLowerCase().trim();

        for (Product p : products) {
            // 只要 ID 或 名字 里包含这个词，就算找到
            if (p.getModelName().toLowerCase().contains(key)) {
                return p;
            }
        }
        return null;
    }

    // 2. 销售记录搜索保持不变 (已经是 contains 了，很好)
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