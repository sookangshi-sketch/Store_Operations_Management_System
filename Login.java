package asisgnmenty1s1;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Login {

    // ---------------- EMPLOYEE CLASS ----------------
    static class Employee {
        String name;
        String role;
        String password;

        Employee(String name, String role, String password) {
            this.name = name;
            this.role = role;
            this.password = password;
        }
    }

    // ---------------- EMPLOYEE SYSTEM ----------------
    static class EmployeeSystem {

        private static Map<String, Employee> employees = new HashMap<>();
        private static Scanner sc = new Scanner(System.in);

        public static void start() {

            // Create default manager
            employees.put("manager", new Employee("manager", "Manager", "admin123"));
             employees.put("M002", new Employee("SOO MEI LING", "Part-time", "admin456"));
            employees.put("M003", new Employee("YU YONG LING", "Part-time", "admin789"));
            employees.put("M004", new Employee("TAY ZHI YUAN", "Part-time", "yong123"));
            employees.put("M005", new Employee("ZHANG PEI YING", "Part-time", "yong456"));
            employees.put("M006", new Employee("TAN LIANG YUN", "Full-time", "yong789"));
            employees.put("M007", new Employee("SING YU JING", "Full-time", "jing123"));
            employees.put("M008", new Employee("YAN YEE JIA", "Full-time", "jing456"));
            employees.put("M009", new Employee("YII PEI YUN", "Full-time", "jing789"));
            employees.put("M0010", new Employee("SAN FU JINA", "Full-time", "shi123"));
            employees.put("M0011", new Employee("MAK JING WEN", "Full-time", "shi456"));
            employees.put("M0012", new Employee("LIN KU YUN", "Manager", "shi789"));

            while (true) {
                Employee loggedIn = login();

                if (loggedIn != null) {
                    System.out.println("\nWelcome " + loggedIn.name + " (" + loggedIn.role + ")");
                    boolean stayLoggedIn = true;

                    while (stayLoggedIn) {
                        System.out.println("\n--- MENU ---");
                        System.out.println("1. Register New Employee (Manager only)");
                        System.out.println("2. Logout");

                        System.out.print("Choose: ");
                        int choice = sc.nextInt();
                        sc.nextLine();

                        switch (choice) {
                            case 1:
                                if (loggedIn.role.equalsIgnoreCase("Manager")) {
                                    registerEmployee();
                                } else {
                                    System.out.println("❌ Only Manager can register new employees.");
                                }
                                break;

                            case 2:
                                System.out.println("✔ Logged out successfully!");
                                stayLoggedIn = false;
                                break;

                            default:
                                System.out.println("Invalid option. Try again.");
                        }
                    }
                }
            }
        }

        // ---------------- LOGIN -----------------
        private static Employee login() {
            System.out.println("\n=== Employee Login ===");
            System.out.print("Name: ");
            String name = sc.nextLine().toLowerCase();

            System.out.print("Password: ");
            String password = sc.nextLine();

            if (employees.containsKey(name)) {
                if (employees.get(name).password.equals(password)) {
                    System.out.println("✔ Login Successful!");
                    return employees.get(name);
                }
            }

            System.out.println("❌ Login failed! Invalid name or password.");
            return null;
        }

        // ---------------- REGISTER EMPLOYEE (Name-based) -----------------
        private static void registerEmployee() {
            System.out.println("\n=== Register New Employee ===");

            System.out.print("Enter Employee Name: ");
            String newName = sc.nextLine().toLowerCase();

            // Check duplicate name
            if (employees.containsKey(newName)) {
                System.out.println("❌ Employee name already exists!");
                return;
            }

            System.out.print("Enter Role (Part-time / Full-time): ");
            String role = sc.nextLine();

            System.out.print("Enter Password: ");
            String pass = sc.nextLine();

            // Add employee
            employees.put(newName, new Employee(newName, role, pass));
            System.out.println("✔ Employee registered successfully!");
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {
        EmployeeSystem.start();
    }
}
