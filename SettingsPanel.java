package org.example.View;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private JTextField physicalPageSizeField;
    private JTextField virtualMemorySizeField;
    private JTextField tlbEntriesField;
    private JComboBox<String> algorithmComboBox;
    private JButton resetButton;
    private JButton submitButton;

    private Runnable resetButtonFunction; // Functional interface for reset action
    private Runnable submitButtonFunction; // Functional interface for submit action

    public SettingsPanel() {
        setBorder(BorderFactory.createTitledBorder("Simulation Settings"));
        setLayout(new GridLayout(5, 2, 5, 5));

        // Physical Page Size
        add(new JLabel("Physical Page Size (power of 2):"));
        physicalPageSizeField = new JTextField();
        add(physicalPageSizeField);

        // Virtual Memory Size
        add(new JLabel("Virtual Memory Size (power of 2):"));
        virtualMemorySizeField = new JTextField();
        add(virtualMemorySizeField);

        // TLB Entries
        add(new JLabel("TLB Entries:"));
        tlbEntriesField = new JTextField();
        add(tlbEntriesField);

        // Algorithm Selection
        add(new JLabel("Page Replacement Algorithm:"));
        algorithmComboBox = new JComboBox<>(new String[]{"FIFO", "LRU"});
        add(algorithmComboBox);

        // Buttons
        resetButton = new JButton("Reset");
        submitButton = new JButton("Submit");
        add(resetButton);
        add(submitButton);

        // Add action listeners directly invoking the provided functional interfaces
        resetButton.addActionListener(e -> {
            if (resetButtonFunction != null) resetButtonFunction.run();
        });

        submitButton.addActionListener(e -> {
            if (submitButtonFunction != null) submitButtonFunction.run();
        });
    }

    // Getters for the input fields with enhanced validation

    /**
     * Gets the physical page size and checks if it's a power of 2.
     * @return The physical page size.
     */
    public int getPhysicalPageSize() {
        try {
            int size = Integer.parseInt(physicalPageSizeField.getText());
            if (!isPowerOfTwo(size)) {
                throw new IllegalArgumentException("Physical Page Size must be a power of 2.");
            }
            return size;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Physical Page Size must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
            throw e;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    /**
     * Gets the virtual memory size and checks if it's a power of 2.
     * @return The virtual memory size.
     */
    public int getVirtualMemorySize() {
        try {
            int size = Integer.parseInt(virtualMemorySizeField.getText());
            if (!isPowerOfTwo(size)) {
                throw new IllegalArgumentException("Virtual Memory Size must be a power of 2.");
            }
            return size;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Virtual Memory Size must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
            throw e;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    /**
     * Gets the TLB entries and ensures it's a valid integer.
     * @return The number of TLB entries.
     */
    public int getTlbEntries() {
        try {
            return Integer.parseInt(tlbEntriesField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "TLB Entries must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
    }

    /**
     * Gets the selected page replacement algorithm.
     * @return The selected algorithm.
     */
    public String getSelectedAlgorithm() {
        return (String) algorithmComboBox.getSelectedItem();
    }

    // Methods to set functional behavior for buttons
    public void setResetButtonFunction(Runnable resetButtonFunction) {
        this.resetButtonFunction = resetButtonFunction;
    }

    public void setSubmitButtonFunction(Runnable submitButtonFunction) {
        this.submitButtonFunction = submitButtonFunction;
    }

    /**
     * Helper method to check if a number is a power of 2.
     * @param number The number to check.
     * @return True if the number is a power of 2, false otherwise.
     */
    private boolean isPowerOfTwo(int number) {
        return (number > 0) && (number & (number - 1)) == 0;
    }
}
