/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package core;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import attendance.AttendanceRecord; // Will be created in next step
import sales.SalesRecord;           // Will be created in next step

public class Database {
    private static Database instance;
    
    private List<Employee> employees;
    private List<Product> products;
    private List<AttendanceRecord> attendanceLog;
    private List<SalesRecord> salesLog;

    private final String EMP_FILE = "Employee.csv";
    private final String PROD_FILE = "Product.csv";
    private final String ATT_FILE = "Attendance.csv"; // You create this
    private final String SALES_FILE = "Sales.csv";    // You create this

    private Database() {
        employees = new ArrayList<>();
        products = new ArrayList<>();
        attendanceLog = new ArrayList<>();
        salesLog = new ArrayList<>();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // --- LOAD DATA ---
    public void loadAllData() {
        loadEmployees();
        loadProducts();
        loadAttendance();
        loadSales();
        OutletManager.loadOutlets("Outlet.csv");
    }

    private void loadEmployees() {
        employees.clear();
        readCSV(EMP_FILE, line -> {
            Employee e = new Employee();
            e.fromCSV(line);
            employees.add(e);
        });
    }

    private void loadProducts() {
        products.clear();
        readCSV(PROD_FILE, line -> {
            Product p = new Product();
            p.fromCSV(line);
            products.add(p);
        });
    }

    private void loadAttendance() {
        attendanceLog.clear();
        File f = new File(ATT_FILE);
        if(!f.exists()) return;
        readCSV(ATT_FILE, line -> {
            AttendanceRecord a = new AttendanceRecord();
            a.fromCSV(line);
            attendanceLog.add(a);
        });
    }
    
    private void loadSales() {
        salesLog.clear();
        File f = new File(SALES_FILE);
        if(!f.exists()) return;
        readCSV(SALES_FILE, line -> {
            SalesRecord s = new SalesRecord();
            s.fromCSV(line);
            salesLog.add(s);
        });
    }

    // Generic CSV Reader
    private void readCSV(String filename, java.util.function.Consumer<String> parser) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; }
                if (line.trim().isEmpty()) continue;
                parser.accept(line);
            }
        } catch (IOException e) {
            System.out.println("Could not load " + filename + ": " + e.getMessage());
        }
    }

    // --- SAVE DATA ---
    public void saveEmployees() {
        writeCSV(EMP_FILE, "EmployeeID,EmployeeName,Role,Password", employees);
    }

    public void saveProducts() {
        // Build dynamic header for products
        String header = "Model,Price,C60,C61,C62,C63,C64,C65,C66,C67,C68,C69";
        writeCSV(PROD_FILE, header, products);
    }

    public void saveAttendance() {
        writeCSV(ATT_FILE, "Date,EmployeeID,Type,Time", attendanceLog);
    }
    
    public void saveSales() {
        writeCSV(SALES_FILE, "Date,Time,EmployeeID,CustomerName,Model,Qty,Total,Method", salesLog);
    }

    private <T extends CSVSerializable> void writeCSV(String filename, String header, List<T> data) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println(header);
            for (T item : data) {
                pw.println(item.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Error saving " + filename);
        }
    }

    // --- ACCESSORS ---
    public List<Employee> getEmployees() { return employees; }
    public List<Product> getProducts() { return products; }
    public List<AttendanceRecord> getAttendanceLog() { return attendanceLog; }
    public List<SalesRecord> getSalesLog() { return salesLog; }
    
    public void addEmployee(Employee e) { employees.add(e); saveEmployees(); }
    public void addAttendance(AttendanceRecord a) { attendanceLog.add(a); saveAttendance(); }
    public void addSale(SalesRecord s) { salesLog.add(s); saveSales(); }
}