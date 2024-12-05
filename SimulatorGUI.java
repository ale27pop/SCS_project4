package org.example.View;

import org.example.Controller.MemoryController;
import org.example.Model.*;
import org.example.Model.Frame;

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
        centerPanel.setPreferredSize(new Dimension(
                (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() * 0.6),
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
        loadInstructionPanel.setSubmitFunction(this::handleInstructionSubmission);
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

            // Calculate offsets and table sizes
            int offsetBits = 2; // Fixed value since we're dividing by 2^2
            int physicalPageRows = physicalMemorySize / (int) Math.pow(2, offsetBits); // Divide by 2^2
            int pageTableRows = virtualMemorySize / 4; // Virtual Memory Size divided by Page Size (4)

            // Log the calculations in the Event Log Panel
            eventLogPanel.appendLog("Physical Page Rows = " + physicalMemorySize + " / 2^" + offsetBits + " = " + physicalPageRows + " rows\n");
            eventLogPanel.appendLog("Page Table Rows = " + virtualMemorySize + " / 4 = " + pageTableRows + " rows\n");
            eventLogPanel.appendLog("TLB Rows = " + tlbEntries + " rows\n");

            // Initialize Memory Controller
            memoryController = new MemoryController(
                    virtualMemorySize,         // Virtual Memory Size
                    physicalMemorySize,        // Physical Memory Size
                    algorithm,                 // Page Replacement Algorithm (FIFO, LRU, etc.)
                    tlbEntries,                // Number of TLB Entries
                    eventLogPanel              // Pass the EventLogPanel instance for logging
            );

            // Initialize GUI tables
            initializeMemoryPanel(physicalPageRows, pageTableRows, tlbEntries);

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

    private void initializeMemoryPanel(int physicalPageRows, int pageTableRows, int tlbEntries) {
        // Initialize TLB
        Object[][] tlbData = new Object[tlbEntries][3];
        for (int i = 0; i < tlbEntries; i++) {
            tlbData[i][0] = i; // Entry #
            tlbData[i][1] = null; // Virtual Page#
            tlbData[i][2] = null; // Physical Page#
        }
        memoryPanel.updateTLBEntries(tlbData);

        // Initialize Page Table
        Object[][] pageTableData = new Object[pageTableRows][3];
        for (int i = 0; i < pageTableRows; i++) {
            pageTableData[i][0] = i; // Index
            pageTableData[i][1] = "0"; // Valid bit
            pageTableData[i][2] = "-"; // No physical page
        }
        memoryPanel.updatePageTable(pageTableData);

        // Initialize Physical Memory
        Object[][] physicalMemoryData = new Object[physicalPageRows][2];
        for (int i = 0; i < physicalPageRows; i++) {
            physicalMemoryData[i][0] = i; // Physical Page#
            physicalMemoryData[i][1] = "Empty"; // No data
        }
        memoryPanel.updatePhysicalMemory(physicalMemoryData);
    }

    private void resetSimulation() {
        // Clear memory controller
        memoryController = null;

        // Clear memory visualization
        memoryPanel.updateTLBEntries(new Object[0][3]); // Clear TLB
        memoryPanel.updatePageTable(new Object[0][3]); // Clear Page Table
        memoryPanel.updatePhysicalMemory(new Object[0][2]); // Clear Physical Memory

        // Reset statistics panel
        statusPanel.updateStatistics(null); // Pass null to reset statistics safely

        // Clear event log
        eventLogPanel.clearLog();
        eventLogPanel.appendLog("Simulation reset.\n");
    }

    private void refreshTables() {
        if (memoryController == null) return;

        // Refresh TLB Table
        Map<Integer, Integer> tlbEntries = memoryController.getTLBEntries();
        Object[][] tlbData = new Object[tlbEntries.size()][3];
        int index = 0;
        for (Map.Entry<Integer, Integer> entry : tlbEntries.entrySet()) {
            tlbData[index][0] = index; // Entry #
            tlbData[index][1] = entry.getKey(); // Virtual Page#
            tlbData[index][2] = entry.getValue(); // Physical Page#
            index++;
        }
        memoryPanel.updateTLBEntries(tlbData);

        // Refresh Page Table
        Map<Integer, Integer> pageTableEntries = memoryController.getPageTableMap();
        Object[][] pageTableData = new Object[pageTableEntries.size()][3];
        index = 0;
        for (Map.Entry<Integer, Integer> entry : pageTableEntries.entrySet()) {
            pageTableData[index][0] = entry.getKey(); // Index
            pageTableData[index][1] = entry.getValue() != -1 ? "1" : "0"; // Valid Bit
            pageTableData[index][2] = entry.getValue() != -1 ? entry.getValue().toString() : "-"; // Physical Page#
            index++;
        }
        memoryPanel.updatePageTable(pageTableData);

        // Refresh Physical Memory
        List<Frame> frames = memoryController.getPhysicalMemoryFrames();
        Object[][] physicalMemoryData = new Object[frames.size()][2];
        for (int i = 0; i < frames.size(); i++) {
            Frame frame = frames.get(i);
            physicalMemoryData[i][0] = frame.getFrameNumber();
            physicalMemoryData[i][1] = frame.isEmpty() ? "Empty" : "Page " + frame.getLoadedPage().getPageNumber();
        }
        memoryPanel.updatePhysicalMemory(physicalMemoryData);
    }

    private void handleInstructionSubmission() {
        if (memoryController == null) {
            JOptionPane.showMessageDialog(this, "Please initialize the simulation first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Process instructions
        loadInstructionPanel.handleLoadSubmit(memoryController, eventLogPanel);

        // Refresh tables
        refreshTables();

        // Update statistics
        statusPanel.updateStatistics(memoryController);
    }
}
