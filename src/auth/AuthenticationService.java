package auth;

import core.Database;
import core.Employee;
import java.util.List;

public class AuthenticationService {
    
    /**
     * 验证用户登录
     * @param id 用户输入的 ID
     * @param password 用户输入的密码
     * @return 成功返回 Employee 对象，失败返回 null
     */
    public static Employee login(String id, String password) {
        // 1. 获取所有员工数据
        List<Employee> employees = Database.getInstance().getEmployees();
        
        // 2. 遍历查找匹配项
        for (Employee e : employees) {
            // 忽略大小写比较ID，但密码必须完全匹配
            if (e.getId().equalsIgnoreCase(id) && e.checkPassword(password)) {
                System.out.println("[Auth] Login Successful: " + e.getName() + " (" + e.getRole() + ")");
                return e;
            }
        }
        
        // 3. 登录失败
        System.out.println("[Auth] Login Failed: Invalid credentials for ID " + id);
        return null; 
    }
}