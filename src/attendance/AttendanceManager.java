/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package attendance;

import core.Database;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AttendanceManager {

    public static String clockIn(String empId) {
        String date = LocalDate.now().toString();
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        
        AttendanceRecord record = new AttendanceRecord(date, empId, "IN", time);
        Database.getInstance().addAttendance(record);
        
        return "Clock In Successful at " + time;
    }

    public static String clockOut(String empId) {
        String date = LocalDate.now().toString();
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        
        AttendanceRecord record = new AttendanceRecord(date, empId, "OUT", time);
        Database.getInstance().addAttendance(record);
        
        // Calculate hours worked today
        double hours = calculateDailyHours(empId, date);
        return String.format("Clock Out Successful at %s. Total Hours Worked: %.1f", time, hours);
    }

    private static double calculateDailyHours(String empId, String date) {
        List<AttendanceRecord> logs = Database.getInstance().getAttendanceLog();
        LocalTime inTime = null;
        double totalHours = 0;

        for (AttendanceRecord r : logs) {
            if (r.getEmployeeId().equals(empId) && r.getDate().equals(date)) {
                if (r.getType().equals("IN")) {
                    inTime = LocalTime.parse(r.getTime());
                } else if (r.getType().equals("OUT") && inTime != null) {
                    LocalTime outTime = LocalTime.parse(r.getTime());
                    // Calculate difference in minutes then convert to hours
                    long minutes = java.time.Duration.between(inTime, outTime).toMinutes();
                    totalHours += (minutes / 60.0);
                    inTime = null; // Reset for next pair
                }
            }
        }
        return totalHours;
    }
}