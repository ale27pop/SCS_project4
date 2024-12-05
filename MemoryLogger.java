package org.example.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MemoryLogger {
    private final String logName;
    private final List<String> logEntries;
    private final int maxLogSize; // Maximum number of log entries (optional limit)

    // Constructor with a name for the logger and optional size limit
    public MemoryLogger(String logName, int maxLogSize) {
        if (maxLogSize <= 0) {
            throw new IllegalArgumentException("Maximum log size must be greater than 0.");
        }
        this.logName = logName;
        this.logEntries = new ArrayList<>();
        this.maxLogSize = maxLogSize;
    }

    // Default constructor without a size limit
    public MemoryLogger(String logName) {
        this(logName, Integer.MAX_VALUE); // Default to unlimited log size
    }

    /**
     * Logs a message with a timestamp.
     * @param message The message to log.
     */
    public synchronized void log(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String entry = String.format("[%s] %s: %s", timestamp, logName, message);

        // Enforce log size limit
        if (logEntries.size() >= maxLogSize) {
            logEntries.remove(0); // Remove the oldest entry to make room
        }

        logEntries.add(entry);
        System.out.println(entry); // Print to console for real-time visibility
    }

    /**
     * Retrieves all log entries.
     * @return A list of all log entries.
     */
    public synchronized List<String> getLogEntries() {
        return new ArrayList<>(logEntries); // Return a copy to prevent modification
    }

    /**
     * Clears all logs.
     */
    public synchronized void clearLog() {
        logEntries.clear();
        System.out.println(logName + ": Log cleared.");
    }

    /**
     * Exports all log entries as a single string.
     * @return A concatenated string of all log entries.
     */
    public synchronized String exportLog() {
        StringBuilder export = new StringBuilder();
        for (String entry : logEntries) {
            export.append(entry).append("\n");
        }
        return export.toString();
    }

    /**
     * Filters log entries containing a specific keyword.
     * @param keyword The keyword to filter logs by.
     * @return A list of log entries containing the keyword.
     */
    public synchronized List<String> filterLogs(String keyword) {
        List<String> filteredLogs = new ArrayList<>();
        for (String entry : logEntries) {
            if (entry.contains(keyword)) {
                filteredLogs.add(entry);
            }
        }
        return filteredLogs;
    }

    /**
     * Prints all log entries for debugging purposes.
     */
    public synchronized void printLog() {
        System.out.println("Log Entries for " + logName + ":");
        for (String entry : logEntries) {
            System.out.println(entry);
        }
    }

    /**
     * Gets the number of log entries.
     * @return The current number of log entries.
     */
    public synchronized int getLogSize() {
        return logEntries.size();
    }
}
