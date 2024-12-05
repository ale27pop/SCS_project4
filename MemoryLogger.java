package org.example.Model;

import java.util.ArrayList;
import java.util.List;

public class MemoryLogger {
    private String logName;
    private List<String> logEntries;

    // Constructor with a name for the logger
    public MemoryLogger(String logName) {
        this.logName = logName;
        this.logEntries = new ArrayList<>();
    }

    // Logs a message
    public void log(String message) {
        String entry = logName + ": " + message;
        logEntries.add(entry);
        System.out.println(entry); // Print to console for real-time visibility
    }

    // Returns all log entries
    public List<String> getLogEntries() {
        return logEntries;
    }

    // Clears all logs
    public void clearLog() {
        logEntries.clear();
    }
}
