package org.example.View;

import org.example.Controller.MemoryController;
import org.example.Model.FIFOReplacement;
import org.example.Model.Frame;
import org.example.Model.LRUReplacement;
import org.example.Model.PageReplacementAlgorithm;
import org.example.Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class SimulatorGUI extends JFrame {
    private SettingsPanel settingsPanel;
    private MemoryPanel memoryPanel;
    private StatusPanel statusPanel;
    private EventLogPanel eventLogPanel;
    private LoadInstructionPanel loadInstructionPanel;

    private MemoryController memoryController;

    public SimulatorGUI() {
        setTitle("Virtual Memory Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen layout

        // Title Panel
        add(createTitlePanel(), BorderLayout.NORTH);

        // Main Panels Layout
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        add(mainPanel, BorderLayout.CENTER);

        // Left Panel (Settings and Load Instruction)
        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        settingsPanel = new SettingsPanel();
        loadInstructionPanel = new LoadInstructionPanel();
        leftPanel.add(settingsPanel);
        leftPanel.add(loadInstructionPanel);
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Center Panel (Memory Visualization) with extended width
        JPanel centerPanel = new JPanel(new BorderLayout());
        memoryPanel = new MemoryPanel();
        centerPanel.add(memoryPanel, BorderLayout.CENTER);
        centerPanel.setPreferredSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.6),
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight())); // Extend memory panel width
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Right Panel (Statistics)
        statusPanel = new StatusPanel();
        mainPanel.add(statusPanel, BorderLayout.EAST);

        // Bottom Panel (Event Log)
        eventLogPanel = new EventLogPanel();
        add(eventLogPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);

        // Attach functionality to buttons
        attachListeners();
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        JLabel title = new JLabel("VIRTUAL MEMORY SIMULATOR");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(title);
        return titlePanel;
    }

    private void attachListeners() {
        // Attach functionality for simulation settings
        settingsPanel.setSubmitButtonFunction(this::handleSimulationSetup);
        settingsPanel.setResetButtonFunction(this::resetSimulation);

        // Attach functionality for load instructions
        loadInstructionPanel.setGenerateRandomFunction(() -> loadInstructionPanel.generateRandomLoad(memoryController));
        loadInstructionPanel.setSubmitFunction(() -> loadInstructionPanel.handleLoadSubmit(memoryController, eventLogPanel));
    }

    private void handleSimulationSetup() {
        try {
            // Get inputs from Settings Panel
            int virtualMemorySize = settingsPanel.getVirtualMemorySize();
            int physicalMemorySize = settingsPanel.getPhysicalPageSize();
            int tlbEntries = settingsPanel.getTlbEntries();
            String selectedAlgorithm = settingsPanel.getSelectedAlgorithm();

            // Select page replacement algorithm
            PageReplacementAlgorithm algorithm = switch (selectedAlgorithm.toUpperCase()) {
                case "FIFO" -> new FIFOReplacement();
                case "LRU" -> new LRUReplacement();
                default -> throw new IllegalArgumentException("Invalid algorithm selected.");
            };

            // Perform calculations
            int offsetBits = 2; // Fixed value since we're dividing by 2^2
            int physicalPageRows = physicalMemorySize / (int) Math.pow(2, offsetBits); // Divide by 2^2
            int pageTableRows = virtualMemorySize / 4; // Virtual Memory Size divided by Page Size (4)

            // Log the calculations in the Event Log Panel
            eventLogPanel.appendLog("Physical Page Rows = " + physicalMemorySize + " / 2^" + offsetBits + " = " + physicalPageRows + " rows\n");
            eventLogPanel.appendLog("Page Table Rows = " + virtualMemorySize + " / 4 = " + pageTableRows + " rows\n");
            eventLogPanel.appendLog("TLB Rows = " + tlbEntries + " rows\n");

            memoryController = new MemoryController(
                    virtualMemorySize,         // Virtual Memory Size
                    physicalMemorySize,        // Physical Memory Size
                    algorithm,                 // Page Replacement Algorithm (FIFO, LRU, etc.)
                    tlbEntries,                // Number of TLB Entries
                    eventLogPanel              // Pass the EventLogPanel instance for logging
            );

            // Update TLB table (initialize with empty data)
            memoryPanel.updateTLBEntries(new String[tlbEntries], new String[tlbEntries]);

            // Populate Page Table
            String[][] pageTableData = new String[pageTableRows][3]; // Columns: Index, Valid, Physical Page#
            for (int i = 0; i < pageTableRows; i++) {
                pageTableData[i][0] = String.valueOf(i); // Index
                pageTableData[i][1] = "0"; // Valid bit (0 = not loaded initially)
                pageTableData[i][2] = "-"; // No physical page assigned initially
            }
            memoryPanel.updatePageTable(pageTableData);

            // Populate Physical Memory
            String[][] physicalMemoryData = new String[physicalPageRows][2]; // Columns: Physical Page#, Content
            for (int i = 0; i < physicalPageRows; i++) {
                physicalMemoryData[i][0] = String.valueOf(i); // Physical Page#
                physicalMemoryData[i][1] = "-"; // Empty content initially
            }
            memoryPanel.updatePhysicalMemory(physicalMemoryData);

            // Update the Statistics Panel
            statusPanel.updateStatistics(memoryController);

            // Log initialization completion
            eventLogPanel.appendLog("Simulation initialized with " + selectedAlgorithm + " algorithm.\n");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for simulation settings.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Algorithm Selection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetSimulation() {
        // Clear memory controller
        memoryController = null;

        // Clear memory visualization
        memoryPanel.updateTLBEntries(new String[0], new String[0]); // Clear TLB table
        memoryPanel.updatePageTable(new String[0][0]); // Clear Page Table
        memoryPanel.updatePhysicalMemory(new String[0][0]); // Clear Physical Memory

        // Reset statistics panel
        statusPanel.updateStatistics(null); // Pass null to reset statistics safely

        // Clear event log
        eventLogPanel.clearLog();
        eventLogPanel.appendLog("Simulation reset.\n");
    }

    // Refresh all memory visualization tables (TLB, Page Table, Physical Memory)
    public void refreshTables() {
        if (memoryController == null) return;

        // Refresh TLB Table
        Map<Integer, Integer> tlbEntries = memoryController.getTLBEntries();
        String[][] tlbData = new String[tlbEntries.size()][2];
        int index = 0;
        for (Map.Entry<Integer, Integer> entry : tlbEntries.entrySet()) {
            tlbData[index][0] = String.valueOf(entry.getKey()); // Virtual Page#
            tlbData[index][1] = String.valueOf(entry.getValue()); // Physical Page#
            index++;
        }
        memoryPanel.updateTLBEntries(tlbData);

        // Refresh Page Table
        Map<Integer, Integer> pageTableEntries = memoryController.getPageTableMap();
        String[][] pageTableData = new String[pageTableEntries.size()][3];
        index = 0;
        for (Map.Entry<Integer, Integer> entry : pageTableEntries.entrySet()) {
            pageTableData[index][0] = String.valueOf(entry.getKey()); // Index
            pageTableData[index][1] = entry.getValue() != -1 ? "1" : "0"; // Valid Bit
            pageTableData[index][2] = entry.getValue() != -1 ? String.valueOf(entry.getValue()) : "-"; // Physical Page#
            index++;
        }
        memoryPanel.updatePageTable(pageTableData);

        // Refresh Physical Memory Table
        List<Frame> frames = memoryController.getPhysicalMemoryFrames(); // Retrieve all frames
        String[][] physicalMemoryData = new String[frames.size()][2]; // Two columns: Physical Page# and Content

        for (int i = 0; i < frames.size(); i++) {
            Frame frame = frames.get(i); // Get the current frame
            physicalMemoryData[i][0] = String.valueOf(frame.getFrameNumber()); // Physical Page#
            physicalMemoryData[i][1] = frame.isEmpty() ? "-" : "Page " + frame.getLoadedPage().getPageNumber(); // Content
        }

// Update the Physical Memory table in the GUI
        memoryPanel.updatePhysicalMemory(physicalMemoryData);


// Update the Physical Memory table in the GUI
        memoryPanel.updatePhysicalMemory(physicalMemoryData);

        memoryPanel.updatePhysicalMemory(physicalMemoryData);

        memoryPanel.updatePhysicalMemory(physicalMemoryData);
    }

    // Handle the submission of instructions and refresh tables
    public void handleInstructionSubmission() {
        if (memoryController == null) {
            JOptionPane.showMessageDialog(this, "Please initialize the simulation first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Process instructions using the LoadInstructionPanel
        loadInstructionPanel.handleLoadSubmit(memoryController, eventLogPanel);

        // Refresh all tables to reflect updated memory state
        refreshTables();

        // Update statistics in the Status Panel
        statusPanel.updateStatistics(memoryController);
    }

}
