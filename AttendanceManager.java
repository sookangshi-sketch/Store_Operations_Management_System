/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FOP_Assignment;

/**
 *
 * @author user
 */
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AttendanceManager {

    // Temporary helper until Member 1 finishes their part
    public int getTotalEmployeeCount() {
        // Return a fake number, or count a fake list
        return 5; // Pretend there are 5 employees in the company
    }   

    // 1. The Database (A List in memory)
    private List<AttendanceRecord> records;
    private final String FILE_NAME = "attendance.dat";

    // 2. Constructor: Loads data immediately when the app starts
    public AttendanceManager() {
        records = new ArrayList<>();
        loadFromFile();
    }

    // --- CORE FEATURES ---

    // Feature A: Check In
    // Returns true if successful, false if already checked in
    // Feature A: Check In
    public boolean checkIn(String employeeId) {
        if (hasActiveSession(employeeId)) {
            return false; // Prevent double check-in
        }

        String date = LocalDate.now().toString();
        // Format time for display (e.g., "09:15 am")
        String timeString = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH));
        
        // --- NEW LOGIC: DETECT LATE ARRIVALS ---
        // 1. Set the "Late" deadline (e.g., 9:00 AM)
        LocalTime lateDeadline = LocalTime.of(9, 0); 
        LocalTime now = LocalTime.now();
        
        String initialStatus = "Active"; 
        
        // 2. Compare current time with deadline
        if (now.isAfter(lateDeadline)) {
            // If they are late, we flag them immediately!
            initialStatus = "Late";
        }
        // ---------------------------------------
        
        // Create the new record with the correct status
        AttendanceRecord newRecord = new AttendanceRecord(date, employeeId, timeString, "---", initialStatus);
        
        records.add(newRecord);
        saveToFile();
        return true;
    }

    // Feature B: Check Out
    public boolean checkOut(String employeeId) {
        // Find the active record for this person
        for (AttendanceRecord record : records) {
            if (record.getEmployeeId().equals(employeeId) && 
                ("Active".equals(record.getStatus()) || "Late".equals(record.getStatus()))) {
                
                // Update the data
                String timeOut = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
                record.setTimeOut(timeOut);
                record.setStatus("Present");
                
                // SAVE updates to file
                saveToFile();
                return true;
            }
        }
        return false; // No active session found
    }

    // Feature C: Get Data (for the UI Table)
    public List<AttendanceRecord> getAllRecords() {
        return records;
    }

    // Helper: Check if user is currently working
    public boolean hasActiveSession(String employeeId) {
        for (AttendanceRecord record : records) {
            if (record.getEmployeeId().equals(employeeId) && "Active".equals(record.getStatus())) {
                return true;
            }
        }
        return false;
    }

    // --- FILE SAVING LOGIC (The "Plumbing") ---
    // Industry Standard: Using ObjectOutputStream to save the whole list at once

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(records);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace(); // detailed error for developer
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return; // First time running? Do nothing.

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            records = (List<AttendanceRecord>) ois.readObject();
            System.out.println("Data loaded: " + records.size() + " records.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}