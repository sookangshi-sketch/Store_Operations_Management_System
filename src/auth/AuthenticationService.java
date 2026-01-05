
package auth;

import core.Database;
import core.Employee;
import java.util.List;

public class AuthenticationService {
    
    public static Employee login(String id, String password) {
        List<Employee> employees = Database.getInstance().getEmployees();
        for (Employee e : employees) {
            if (e.getId().equalsIgnoreCase(id) && e.checkPassword(password)) {
                return e; // Login success [cite: 34]
            }
        }
        return null; // Login failed [cite: 39]
    }
}