package org.example.Controller;

import org.example.Model.*;
import org.example.View.*;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class MemoryController {
    private VirtualMemory virtualMemory;
    private PhysicalMemory physicalMemory;
    private PageTable pageTable;
    private TranslationLookasideBuffer tlb;
    private int pageFaultCounter;
    private PageReplacementAlgorithm pageReplacementAlgorithm;
    private int simulationSpeed;
    private MemoryLogger memoryLogger;
    private EventLogPanel eventLogPanel;

    public MemoryController(int virtualMemorySize, int physicalMemorySize, PageReplacementAlgorithm replacementAlgorithm, int tlbEntries, EventLogPanel eventLogPanel) {
        this.virtualMemory = new VirtualMemory(virtualMemorySize);
        this.physicalMemory = new PhysicalMemory(physicalMemorySize);
        this.pageTable = new PageTable();
        this.tlb = new TranslationLookasideBuffer(tlbEntries);
        this.pageFaultCounter = 0;
        this.pageReplacementAlgorithm = replacementAlgorithm;
        this.simulationSpeed = 500; // Default simulation speed (500ms delay)
        this.memoryLogger = new MemoryLogger("MemoryLog.txt");
        this.eventLogPanel = eventLogPanel;
    }

    // Helper Methods to Retrieve Data for Tables

    /**
     * Retrieves all TLB entries as a map.
     * @return Map of Virtual Page Number to Physical Frame Number.
     */
    public Map<Integer, Integer> getTLBEntries() {
        return tlb.getEntries(); // Assumes tlb.getEntries() returns a Map<Integer, Integer>
    }

    /**
     * Retrieves the entire Page Table as a map.
     * @return Map of Page Number to Frame Number.
     */
    public Map<Integer, Integer> getPageTableMap() {
        return pageTable.getPageTableMap(); // Use PageTable's method to get the map
    }

    /**
     * Retrieves all frames in physical memory.
     * @return List of Frame objects.
     */
    public List<Frame> getPhysicalMemoryFrames() {
        return physicalMemory.getFrames(); // Use PhysicalMemory's method to get frames
    }

    // Existing Methods

    public void requestPage(int pageNumber) {
        if (pageNumber < 0 || pageNumber >= virtualMemory.getSize()) {
            eventLogPanel.log("Error: Requested page number " + pageNumber + " is out of bounds.");
            return;
        }

        Page page = virtualMemory.getPage(pageNumber);

        // Step 1: Check the TLB for the page
        int frameNumber = tlb.getFrameNumber(pageNumber);
        if (frameNumber != -1) {
            // TLB hit
            eventLogPanel.log("TLB Hit: Page " + pageNumber + " found in TLB (Frame " + frameNumber + ")");
            applySimulationSpeed();
            return;
        } else {
            eventLogPanel.log("TLB Miss: Page " + pageNumber + " not found in TLB.");
        }

        // Step 2: Check the Page Table
        frameNumber = pageTable.getFrameForPage(pageNumber);
        if (frameNumber != -1) {
            // Page table hit
            eventLogPanel.log("Page Table Hit: Page " + pageNumber + " found in Frame " + frameNumber);
            tlb.addEntry(pageNumber, frameNumber); // Update TLB
            eventLogPanel.log("TLB Updated: Page " + pageNumber + " added to TLB (Frame " + frameNumber + ")");
        } else {
            // Step 3: Page fault
            pageFaultCounter++;
            eventLogPanel.log("Page Fault: Page " + pageNumber + " not found in memory. Loading from secondary memory...");
            loadPageIntoMemory(page);
        }

        applySimulationSpeed();
    }

    public void loadPageIntoMemory(Page page) {
        Frame freeFrame = physicalMemory.getFreeFrame();

        if (freeFrame != null) {
            // Use free frame to load the page
            freeFrame.setLoadedPage(page);
            page.setInMemory(true);
            page.setFrameNumber(freeFrame.getFrameNumber());

            // Update the Page Table and TLB
            pageTable.mapPageToFrame(page.getPageNumber(), freeFrame.getFrameNumber());
            tlb.addEntry(page.getPageNumber(), freeFrame.getFrameNumber());

            eventLogPanel.log("Page Loaded: Page " + page.getPageNumber() + " loaded into Frame " + freeFrame.getFrameNumber());
        } else {
            // No free frames; use page replacement
            eventLogPanel.log("Memory Full: No free frames available. Using page replacement algorithm.");
            try {
                pageReplacementAlgorithm.replacePage(page.getPageNumber(), this);
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "Frames are full. Unable to load new pages.",
                        "Memory Full", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void applySimulationSpeed() {
        try {
            Thread.sleep(simulationSpeed);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Simulation interrupted.");
        }
    }

    public int getPageFaultCount() {
        return pageFaultCounter;
    }

    public String getMemoryUsagePercentage() {
        int totalFrames = physicalMemory.getSize();
        int usedFrames = totalFrames - physicalMemory.getFreeFrameCount(); // Frames in use
        return String.format("%.2f", (usedFrames * 100.0) / totalFrames) + "%";
    }

    public void removePageFromMemory(int pageNumber) {
        int frameNumber = pageTable.getFrameForPage(pageNumber);
        if (frameNumber != -1) {
            Frame frame = physicalMemory.getFrame(frameNumber);
            frame.setLoadedPage(null); // Clear the frame
            pageTable.removePage(pageNumber); // Remove mapping from the Page Table
            tlb.clear(); // Clear TLB as the mapping is no longer valid

            eventLogPanel.log("Page Removed: Page " + pageNumber + " removed from Frame " + frameNumber);
        }
    }

    public MemoryLogger getMemoryLogger() {
        return memoryLogger;
    }

    public boolean isMemoryFull() {
        return physicalMemory.isFull();
    }

    public boolean isPageInMemory(int pageNumber) {
        return pageTable.isPageInMemory(pageNumber);
    }

    public Page getPageFromVirtualMemory(int pageNumber) {
        if (pageNumber < 0 || pageNumber >= virtualMemory.getSize()) {
            throw new IllegalArgumentException("Page number " + pageNumber + " is out of bounds. Valid range: 0 to " + (virtualMemory.getSize() - 1));
        }
        return virtualMemory.getPage(pageNumber);
    }

    public String getPageFaultRate() {
        int totalRequests = pageFaultCounter + pageTable.getAccessCount(); // Total page requests
        if (totalRequests == 0) {
            return "0.00%"; // No requests, fault rate is 0%
        }
        return String.format("%.2f", (pageFaultCounter * 100.0) / totalRequests) + "%";
    }

    public String getPageReplacementCount() {
        if (pageReplacementAlgorithm instanceof FIFOReplacement) {
            return String.valueOf(((FIFOReplacement) pageReplacementAlgorithm).getReplacementCount());
        }
        // Add similar handling for other algorithms (e.g., LRUReplacement)
        return "0";
    }

    public ListModel<Object> getVirtualMemory() {
        DefaultListModel<Object> virtualMemoryModel = new DefaultListModel<>();
        for (int i = 0; i < virtualMemory.getSize(); i++) {
            Page page = virtualMemory.getPage(i);
            String pageDetails = "Page " + i + " - ";
            if (page.isInMemory()) {
                pageDetails += "In Memory (Frame " + page.getFrameNumber() + ")";
            } else {
                pageDetails += "Not in Memory";
            }
            virtualMemoryModel.addElement(pageDetails);
        }
        return virtualMemoryModel;
    }

    public void loadInstruction(int address, String data) {
        String[] dataItems = data.split(",");
        eventLogPanel.log("Loading instructions at address " + address + ": " + data);
        for (String item : dataItems) {
            try {
                int pageNumber = Integer.parseInt(item.trim(), 16);
                requestPage(pageNumber);
            } catch (NumberFormatException e) {
                eventLogPanel.log("Error: Invalid data format \"" + item + "\". Skipping...");
            }
        }
    }
}
