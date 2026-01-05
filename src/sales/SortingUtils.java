/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sales;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortingUtils {

    // Bubble Sort Implementation (Required by project prerequisite)
    public static void bubbleSortByAmount(List<SalesRecord> records) {
        int n = records.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (records.get(j).getTotal() > records.get(j + 1).getTotal()) {
                    // Swap
                    SalesRecord temp = records.get(j);
                    records.set(j, records.get(j + 1));
                    records.set(j + 1, temp);
                }
            }
        }
    }

    // Standard sorts using Java Collections
    public static void sortByDate(List<SalesRecord> records) {
        Collections.sort(records, Comparator.comparing(SalesRecord::getDate));
    }

    public static void sortByName(List<SalesRecord> records) {
        Collections.sort(records, Comparator.comparing(SalesRecord::getCustomerName));
    }
}