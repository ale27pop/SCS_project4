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
        pageTableModel = new DefaultTableModel(new Object[]{"Index", "Valid", "PhysicalPage#"}, 0);
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

    public void updateTLBEntries(String[] virtualPages, String[] physicalPages) {
        tlbTableModel.setRowCount(0); // Clear existing rows
        for (int i = 0; i < virtualPages.length; i++) {
            tlbTableModel.addRow(new Object[]{i, virtualPages[i], physicalPages[i]});
        }
    }

    public void updatePageTable(String[][] data) {
        DefaultTableModel model = (DefaultTableModel) pageTable.getModel();
        model.setRowCount(0); // Clear existing rows
        for (String[] row : data) {
            model.addRow(row);
        }
    }
    public void updatePhysicalMemory(String[][] data) {
        DefaultTableModel model = (DefaultTableModel) physicalMemoryTable.getModel();
        model.setRowCount(0); // Clear existing rows
        for (String[] row : data) {
            model.addRow(row); // Add each row to the table
        }
    }

    public void updateTLBEntries(String[][] data) {
        DefaultTableModel model = (DefaultTableModel) tlbTable.getModel();
        model.setRowCount(0); // Clear existing rows
        for (String[] row : data) {
            model.addRow(row);
        }
    }

}
