package attendance;

import core.Database;
import core.Employee;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AttendanceManager {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    // --- 打卡功能 ---

    public static String clockIn(String empId) {
        String date = LocalDate.now().toString();
        
        // 验证：是否已经打过卡但没签退？
        if (isCurrentlyClockedIn(empId, date)) {
            return "Error: You are already clocked in.";
        }

        String time = LocalTime.now().format(TIME_FMT);
        AttendanceRecord record = new AttendanceRecord(date, empId, "IN", time);
        Database.getInstance().addAttendance(record);
        
        return "Clock In Successful at " + time;
    }

    public static String clockOut(String empId) {
        String date = LocalDate.now().toString();

        // 验证：是否还没打卡就点签退？
        if (!isCurrentlyClockedIn(empId, date)) {
            return "Error: You have not clocked in today.";
        }

        String time = LocalTime.now().format(TIME_FMT);
        AttendanceRecord record = new AttendanceRecord(date, empId, "OUT", time);
        Database.getInstance().addAttendance(record);
        
        double hours = calculateDailyHours(empId, date);
        return String.format("Clock Out Successful at %s. Total Hours Today: %.2f", time, hours);
    }

    // --- 核心权限逻辑：根据角色获取记录 ---
    
    public static List<AttendanceRecord> getFilteredRecords(Employee user) {
        List<AttendanceRecord> allLogs = Database.getInstance().getAttendanceLog();
        List<AttendanceRecord> result = new ArrayList<>();

        // 1. 判断是否是经理 (忽略大小写，防止 "manager" 和 "Manager" 不匹配)
        boolean isManager = "Manager".equalsIgnoreCase(user.getRole());

        if (isManager) {
            // 经理：返回所有人的记录
            return allLogs;
        } else {
            // 普通员工 (Full-time / Part-time)：只筛选自己的 ID
            for (AttendanceRecord r : allLogs) {
                if (r.getEmployeeId().equals(user.getId())) {
                    result.add(r);
                }
            }
            return result;
        }
    }

    // --- 辅助方法 ---

    private static boolean isCurrentlyClockedIn(String empId, String date) {
        List<AttendanceRecord> logs = Database.getInstance().getAttendanceLog();
        String lastType = "OUT"; // 默认为 OUT 状态

        for (AttendanceRecord r : logs) {
            if (r.getEmployeeId().equals(empId) && r.getDate().equals(date)) {
                lastType = r.getType(); // 找到当天最后一条记录的状态
            }
        }
        return "IN".equals(lastType);
    }

    private static double calculateDailyHours(String empId, String date) {
        List<AttendanceRecord> logs = Database.getInstance().getAttendanceLog();
        LocalTime inTime = null;
        double totalHours = 0;

        for (AttendanceRecord r : logs) {
            if (r.getEmployeeId().equals(empId) && r.getDate().equals(date)) {
                if ("IN".equals(r.getType())) {
                    try { inTime = LocalTime.parse(r.getTime(), TIME_FMT); } catch (Exception e) {}
                } else if ("OUT".equals(r.getType()) && inTime != null) {
                    try {
                        LocalTime outTime = LocalTime.parse(r.getTime(), TIME_FMT);
                        long minutes = Duration.between(inTime, outTime).toMinutes();
                        totalHours += (minutes / 60.0);
                        inTime = null; 
                    } catch (Exception e) {}
                }
            }
        }
        return totalHours;
    }
}