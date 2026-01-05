/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core;

public class Employee implements CSVSerializable {
    private String id;
    private String name;
    private String role; // "Manager", "Full-time", "Part-time"
    private String password;

    public Employee() {}

    public Employee(String id, String name, String role, String password) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.password = password;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getPassword() { return password; }

    // 在 Employee.java 的 Getters 下面添加：
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
    public void setPassword(String password) { this.password = password; }

    public boolean checkPassword(String inputPass) {
        return this.password.equals(inputPass);
    }

    @Override
    public String toCSV() {
        return id + "," + name + "," + role + "," + password;
    }

    @Override
    public void fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length >= 4) {
            this.id = parts[0].trim();
            this.name = parts[1].trim();
            this.role = parts[2].trim();
            this.password = parts[3].trim();
        }
    }
    
    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}