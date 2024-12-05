package org.example.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MemoryPanel extends JPanel {
    private JTable tlbTable;
    private JTable pageTable;
    private JTable physicalMemoryTable;

    private DefaultTableModel tlbTableModel;
    private DefaultTableModel pageTableModel;
    private DefaultTableModel physicalMemoryTableModel;

    public MemoryPanel() {
        setBorder(BorderFactory.createTitledBorder("Memory Visualization"));
        setLayout(new GridLayout(1, 3, 10, 10)); // Divide into 3 sections

        // TLB Table
        tlbTableModel = new DefaultTableModel(new Object[]{"Entry #", "Virtual Page#", "Physical Page#"}, 0);
        tlbTable = new JTable(tlbTableModel);
        JScrollPane tlbScrollPane = new JScrollPane(tlbTable);
        tlbScrollPane.setBorder(BorderFactory.createTitledBorder("Translation Lookaside Buffer"));

        // Page Table
        pageTableModel = new DefaultTableModel(new Object[]{"Index", "Valid", "Physical Page#"}, 0);
        pageTable = new JTable(pageTableModel);
        JScrollPane pageTableScrollPane = new JScrollPane(pageTable);
        pageTableScrollPane.setBorder(BorderFactory.createTitledBorder("Page Table"));

        // Physical Memory Table
        physicalMemoryTableModel = new DefaultTableModel(new Object[]{"Physical Page#", "Content"}, 0);
        physicalMemoryTable = new JTable(physicalMemoryTableModel);
        JScrollPane physicalMemoryScrollPane = new JScrollPane(physicalMemoryTable);
        physicalMemoryScrollPane.setBorder(BorderFactory.createTitledBorder("Physical Memory"));

        // Add scroll panes to the panel
        add(tlbScrollPane);
        add(pageTableScrollPane);
        add(physicalMemoryScrollPane);
    }

    /**
     * Update TLB table entries.
     * @param data 2D array of TLB data (Entry #, Virtual Page#, Physical Page#).
     */
    public void updateTLBEntries(Object[][] data) {
        tlbTableModel.setRowCount(0); // Clear existing rows
        for (Object[] row : data) {
            tlbTableModel.addRow(row); // Add each row
        }
    }

    /**
     * Update Page Table entries.
     * @param data 2D array of Page Table data (Index, Valid, Physical Page#).
     */
    public void updatePageTable(Object[][] data) {
        pageTableModel.setRowCount(0); // Clear existing rows
        for (Object[] row : data) {
            pageTableModel.addRow(row);
        }
    }

    /**
     * Update Physical Memory table entries.
     * @param data 2D array of Physical Memory data (Physical Page#, Content).
     */
    public void updatePhysicalMemory(Object[][] data) {
        physicalMemoryTableModel.setRowCount(0); // Clear existing rows
        for (Object[] row : data) {
            physicalMemoryTableModel.addRow(row); // Add each row to the table
        }
    }

    /**
     * Refresh all tables at once.
     * @param tlbData TLB data.
     * @param pageTableData Page Table data.
     * @param physicalMemoryData Physical Memory data.
     */
    public void refreshTables(Object[][] tlbData, Object[][] pageTableData, Object[][] physicalMemoryData) {
        updateTLBEntries(tlbData);
        updatePageTable(pageTableData);
        updatePhysicalMemory(physicalMemoryData);
    }

    /**
     * Formats the content for physical memory (e.g., for displaying pages and data).
     * @param physicalPageNumber The physical page number.
     * @param content The content stored in that page.
     * @return A formatted string representation of the memory content.
     */
    public static String formatPhysicalMemoryContent(int physicalPageNumber, Object content) {
        if (content == null) {
            return "Empty";
        }
        return "Page " + physicalPageNumber + ": " + content.toString();
    }
}
