/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sales;

import core.CSVSerializable;

public class SalesRecord implements CSVSerializable {
    private String date;
    private String time;
    private String employeeId;
    private String customerName;
    private String modelName;
    private int quantity;
    private double total;
    private String method; // Cash, Card, etc.

    public SalesRecord() {}

    public SalesRecord(String date, String time, String employeeId, String customerName, 
                       String modelName, int quantity, double total, String method) {
        this.date = date;
        this.time = time;
        this.employeeId = employeeId;
        this.customerName = customerName;
        this.modelName = modelName;
        this.quantity = quantity;
        this.total = total;
        this.method = method;
    }

    // Getters
    public String getDate() { return date; }
    public String getCustomerName() { return customerName; }
    public String getModelName() { return modelName; }
    public double getTotal() { return total; }
    public String getEmployeeId() { return employeeId; }

    @Override
    public String toCSV() {
        return date + "," + time + "," + employeeId + "," + customerName + "," + 
               modelName + "," + quantity + "," + total + "," + method;
    }

    @Override
    public void fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length >= 8) {
            date = parts[0].trim();
            time = parts[1].trim();
            employeeId = parts[2].trim();
            customerName = parts[3].trim();
            modelName = parts[4].trim();
            quantity = Integer.parseInt(parts[5].trim());
            total = Double.parseDouble(parts[6].trim());
            method = parts[7].trim();
        }
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | Qty: %d | RM%.2f", date, customerName, modelName, quantity, total);
    }
}