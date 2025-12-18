/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FOP_Assignment;

/**
 *
 * @author user
 */

import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.io.Serializable;

// implementing Serializable is a "Real World" trick. 
// It allows this object to be easily saved to a file later.
public class AttendanceRecord implements Serializable {

    // 1. PRIVATE FIELDS (The Data)
    // No one outside can touch these directly.
    private String date;
    private String employeeId;
    private String timeIn;
    private String timeOut;
    private String status;

    // 2. CONSTRUCTOR (The Setup)
    // This is how you create a new record.
    public AttendanceRecord(String date, String employeeId, String timeIn, String timeOut, String status) {
        this.date = date;
        this.employeeId = employeeId;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.status = status;
    }

    // 3. GETTERS (Read Only Access)
    // Allows the UI to read the data to display in the table
    public String getDate() { return date; }
    public String getEmployeeId() { return employeeId; }
    public String getTimeIn() { return timeIn; }
    public String getTimeOut() { return timeOut; }
    public String getStatus() { return status; }

    // 4. SETTERS (Write Access)
    // Allows the Manager to update data (like adding the Time Out later)
    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // 5. HELPER METHOD (For debugging)
    // If you ever System.out.println(record), this prints a readable text instead of nonsense code.
    @Override
    public String toString() {
        return date + " | " + employeeId + " | " + timeIn + " | " + timeOut + " | " + status;
    }
    
    // 6. HELPER METHOD (For Table Display)
    // Converts this object into an array so the JTable can easily "eat" it.
    public Object[] toRowData() {
        return new Object[]{date, employeeId, timeIn, timeOut, status};
    }
    
    // --- NEW FEATURE: Calculate Working Hours ---
    public String getDuration() {
        // 1. Safety Check: If they haven't checked out, we can't calculate yet
        if (timeOut == null || timeOut.equals("---") || status.equals("Active")) {
            return "Active";
        }

        try {
            // 2. Define the format of your time string (e.g., "12:12 pm")
            // We use Locale.ENGLISH to ensure AM/PM is understood correctly
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

            // 3. Convert Text -> Time Objects
            // .toUpperCase() fixes issues if your computer saves "pm" as "PM" or "pm"
            LocalTime start = LocalTime.parse(timeIn.toUpperCase(), formatter);
            LocalTime end = LocalTime.parse(timeOut.toUpperCase(), formatter);

            // 4. Calculate the difference
            long minutes = Duration.between(start, end).toMinutes();

            // 5. Convert to hours (e.g., 90 mins -> 1.5 hours)
            double hours = minutes / 60.0;
            
            // Format to 1 decimal place (e.g., "8.1 hours")
            return String.format("%.1f hours", hours);

        } catch (Exception e) {
            // If something goes wrong (like weird time format), don't crash.
            System.out.println("Error calculating time: " + e.getMessage());
            return "Error";
        }
    }
}