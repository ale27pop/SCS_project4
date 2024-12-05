package org.example.View;

import org.example.Controller.MemoryController;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {
    private JLabel pageFaultLabel;
    private JLabel replacementCountLabel;
    private JLabel memoryUsageLabel;
    private JLabel faultRateLabel;

    public StatusPanel() {
        setBorder(BorderFactory.createTitledBorder("Statistics"));
        setLayout(new GridLayout(4, 1, 5, 5));

        pageFaultLabel = new JLabel("Page Fault Count: [ 0 ]");
        replacementCountLabel = new JLabel("Page Replacement Count: [ 0 ]");
        memoryUsageLabel = new JLabel("Memory Usage: [ 0% ]");
        faultRateLabel = new JLabel("Page Fault Rate: [ 0% ]");

        add(pageFaultLabel);
        add(replacementCountLabel);
        add(memoryUsageLabel);
        add(faultRateLabel);
    }

    public void updateStatistics(MemoryController memoryController) {
        if (memoryController == null) {
            pageFaultLabel.setText("Page Fault Count: [ 0 ]");
            replacementCountLabel.setText("Page Replacement Count: [ 0 ]");
            memoryUsageLabel.setText("Memory Usage: [ 0% ]");
            faultRateLabel.setText("Page Fault Rate: [ 0% ]");
        } else {
            pageFaultLabel.setText("Page Fault Count: [ " + memoryController.getPageFaultCount() + " ]");
            replacementCountLabel.setText("Page Replacement Count: [ " + memoryController.getPageReplacementCount() + " ]");
            memoryUsageLabel.setText("Memory Usage: [ " + memoryController.getMemoryUsagePercentage() + "% ]");
            faultRateLabel.setText("Page Fault Rate: [ " + memoryController.getPageFaultRate() + "% ]");
        }
    }

}
