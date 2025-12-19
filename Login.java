package Login;

import java.util.HashMap;
import java.util.Map;

public class Login {

    public static class Employee {
        public String id, name, role, password;

        public Employee(String id, String name, String role, String password) {
            this.id = id;
            this.name = name;
            this.role = role;
            this.password = password;
        }
    }

    public static class EmployeeSystem {
        public static Map<String, Employee> employees = new HashMap<>();

        static {
          
            employees.put("C6001", new Employee("C6001", "Tan Guan Han", "Manager", "a2b1c0"));
            employees.put("M0012", new Employee("M0012", "LIN KU YUN", "Manager", "shi789"));
            employees.put("M002", new Employee("M002","SOO MEI LING", "Part-time", "admin456"));
            employees.put("M003", new Employee("M003","YU YONG LING", "Part-time", "admin789"));
            employees.put("M004", new Employee("M004","TAY ZHI YUAN", "Part-time", "yong123"));
            employees.put("M005", new Employee("M005","ZHANG PEI YING", "Part-time", "yong456"));
            employees.put("M006", new Employee("M006","TAN LIANG YUN", "Full-time", "yong789"));
            employees.put("M007", new Employee("M007","SING YU JING", "Full-time", "jing123"));
            employees.put("M008", new Employee("M008","YAN YEE JIA", "Full-time", "jing456"));
            employees.put("M009", new Employee("M009","YII PEI YUN", "Full-time", "jing789"));
            employees.put("M0010", new Employee("M0010","SAN FU JINA", "Full-time", "shi123"));
            employees.put("M0011", new Employee("M0011","MAK JING WEN", "Full-time", "shi456"));
        }

      
        public static Employee authenticate(String id, String password) {
            if (employees.containsKey(id)) {
                Employee emp = employees.get(id);
                if (emp.password.equals(password)) {
                    return emp;
                }
            }
            return null;
        }
    }
}