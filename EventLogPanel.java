package org.example.View;

import javax.swing.*;
import java.awt.*;

public class EventLogPanel extends JPanel {
    private JTextArea eventLogArea;

    public EventLogPanel() {
        setBorder(BorderFactory.createTitledBorder("Event Log"));
        setLayout(new BorderLayout());

        eventLogArea = new JTextArea(10, 50);
        eventLogArea.setEditable(false); // Log is read-only
        JScrollPane scrollPane = new JScrollPane(eventLogArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Append a message to the event log
    public void log(String message) {
        eventLogArea.append(message + "\n"); // Add new message to the text area
        eventLogArea.setCaretPosition(eventLogArea.getDocument().getLength()); // Auto-scroll to the bottom
    }

    // Clear all log messages
    public void clearLog() {
        eventLogArea.setText("");
    }

    public void appendLog(String s) {

    }
}
