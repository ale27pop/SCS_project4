package org.example;

import javax.swing.SwingUtilities;
import org.example.View.SimulatorGUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimulatorGUI gui = new SimulatorGUI();
            gui.setVisible(true);
        });
    }
}
