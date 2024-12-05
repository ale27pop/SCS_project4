package org.example.View;

import org.example.Controller.MemoryController;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
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

    /**
     * Generate random load instructions and update the fields.
     * @param memoryController The memory controller for simulation.
     */
    public void generateRandomLoad(MemoryController memoryController) {
        if (memoryController == null) {
            JOptionPane.showMessageDialog(this, "Please initialize the simulation first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int virtualMemorySize = memoryController.getVirtualMemory().getSize();
        Random random = new Random();

        // Generate a random load address within the valid range
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

    /**
     * Handle the submission of load instructions.
     * Parses the data, validates the input, and sends it to the memory controller.
     * @param memoryController The memory controller for processing the load instruction.
     * @param eventLogPanel The panel to log events.
     */
    public void handleLoadSubmit(MemoryController memoryController, EventLogPanel eventLogPanel) {
        if (memoryController == null) {
            JOptionPane.showMessageDialog(this, "Please initialize the simulation first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Validate and parse the load address
            String addressText = loadAddressField.getText();
            if (addressText == null || addressText.trim().isEmpty()) {
                throw new IllegalArgumentException("Load address cannot be empty.");
            }
            int address = Integer.parseInt(addressText.trim(), 16); // Convert Hex to Decimal

            // Validate and parse the instruction list
            String data = loadDataField.getText();
            if (data == null || data.trim().isEmpty()) {
                throw new IllegalArgumentException("Instruction list cannot be empty.");
            }

            String[] instructions = data.split(",");
            if (instructions.length != 10) {
                throw new IllegalArgumentException("Instruction list must contain exactly 10 instructions.");
            }

            // Validate each instruction
            for (String instruction : instructions) {
                if (instruction.trim().isEmpty()) {
                    throw new IllegalArgumentException("Instruction list contains an invalid (empty) instruction.");
                }
                Integer.parseInt(instruction.trim(), 16); // Validate as a hexadecimal number
            }

            // Submit the parsed data to the memory controller
            memoryController.loadInstruction(address, Arrays.toString(instructions));

            // Log the successful submission
            eventLogPanel.appendLog("Loaded address " + addressText + " with data: " + data + "\n");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid address or instruction format. Use valid hexadecimal values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Set functionality for the "Generate Random" button.
     * @param function The function to execute when the button is clicked.
     */
    public void setGenerateRandomFunction(Runnable function) {
        generateRandomButton.addActionListener(e -> function.run());
    }

    /**
     * Set functionality for the "Submit" button.
     * @param function The function to execute when the button is clicked.
     */
    public void setSubmitFunction(Runnable function) {
        submitButton.addActionListener(e -> function.run());
    }
}
