/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package attendance;

import core.CSVSerializable;

public class AttendanceRecord implements CSVSerializable {
    private String date;       // Format: YYYY-MM-DD
    private String employeeId;
    private String type;       // "IN" or "OUT"
    private String time;       // Format: HH:mm

    public AttendanceRecord() {}

    public AttendanceRecord(String date, String employeeId, String type, String time) {
        this.date = date;
        this.employeeId = employeeId;
        this.type = type;
        this.time = time;
    }

    public String getDate() { return date; }
    public String getEmployeeId() { return employeeId; }
    public String getType() { return type; }
    public String getTime() { return time; }

    @Override
    public String toCSV() {
        return date + "," + employeeId + "," + type + "," + time;
    }

    @Override
    public void fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length >= 4) {
            this.date = parts[0].trim();
            this.employeeId = parts[1].trim();
            this.type = parts[2].trim();
            this.time = parts[3].trim();
        }
    }
    
    @Override
    public String toString() {
        return "[" + date + " " + time + "] " + type + " - " + employeeId;
    }
}