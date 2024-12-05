package org.example.View;

import org.example.Controller.MemoryController;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class LoadInstructionPanel extends JPanel {
    private JTextField loadAddressField;
    private JTextField loadDataField;
    private JButton generateRandomButton;
    private JButton submitButton;

    public LoadInstructionPanel() {
        setBorder(BorderFactory.createTitledBorder("Load Instruction"));
        setLayout(new GridLayout(3, 2, 5, 5));

        // Row 1: Label and Load Address Field
        add(new JLabel("LOAD (in hex) #:"));
        loadAddressField = new JTextField();
        add(loadAddressField);

        // Row 2: Instruction List Placeholder
        loadDataField = new JTextField("List of next 10 Instructions");
        loadDataField.setEditable(false); // Read-only field for instruction list
        add(loadDataField);
        add(new JLabel("")); // Empty cell to align the buttons

        // Row 3: Buttons
        generateRandomButton = new JButton("Gen. Random");
        submitButton = new JButton("Submit");
        add(generateRandomButton);
        add(submitButton);
    }

    // Generate Random Address and Instructions
    public void generateRandomLoad(MemoryController memoryController) {
        if (memoryController == null) {
            JOptionPane.showMessageDialog(this, "Please initialize the simulation first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int virtualMemorySize = memoryController.getVirtualMemory().getSize();
        Random random = new Random();

        // Generate a single random address within the valid range
        int randomAddress = random.nextInt(virtualMemorySize);
        loadAddressField.setText(Integer.toHexString(randomAddress).toUpperCase());

        // Generate a list of 10 random instructions (hexadecimal values)
        StringBuilder instructionList = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int randomInstruction = random.nextInt(virtualMemorySize); // Ensure it's within the valid range
            instructionList.append(Integer.toHexString(randomInstruction).toUpperCase());
            if (i < 9) {
                instructionList.append(","); // Add comma separator between instructions
            }
        }

        // Update the Load Data field with the generated instructions
        loadDataField.setText(instructionList.toString());
    }

    // Handle Load Submission
    public void handleLoadSubmit(MemoryController memoryController, EventLogPanel eventLogPanel) {
        if (memoryController == null) {
            JOptionPane.showMessageDialog(this, "Please initialize the simulation first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int address = Integer.parseInt(loadAddressField.getText(), 16); // Convert Hex to Decimal
            String data = loadDataField.getText();

            memoryController.loadInstruction(address, data); // Load instruction to memory
            eventLogPanel.appendLog("Loaded address " + loadAddressField.getText() + " with data: " + data + "\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid address format. Please use a valid hexadecimal value.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Set Functionality for Generate Random Button
    public void setGenerateRandomFunction(Runnable function) {
        generateRandomButton.addActionListener(e -> function.run());
    }

    // Set Functionality for Submit Button
    public void setSubmitFunction(Runnable function) {
        submitButton.addActionListener(e -> function.run());
    }
}
