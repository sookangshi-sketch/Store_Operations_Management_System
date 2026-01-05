/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OutletManager {
    private static final Map<String, String> outlets = new HashMap<>();

    public static void loadOutlets(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; } // Skip header
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    outlets.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading outlets: " + e.getMessage());
        }
    }

    public static String getOutletName(String code) {
        return outlets.getOrDefault(code, "Unknown Outlet");
    }
}