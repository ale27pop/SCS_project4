package org.example.View;

import javax.swing.*;
import java.awt.*;

public class AlgorithmSelectionPanel extends JPanel {
    public JComboBox<String> algorithmComboBox;

    public AlgorithmSelectionPanel() {
        setBorder(BorderFactory.createTitledBorder("Algorithm Selection"));
        setLayout(new FlowLayout(FlowLayout.LEFT));

        algorithmComboBox = new JComboBox<>(new String[]{"FIFO", "LRU"});
        add(new JLabel("Algorithm:"));
        add(algorithmComboBox);
    }
}
