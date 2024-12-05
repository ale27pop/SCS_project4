package org.example.Model;

public class StatisticsCollector {
    private int pageFaultCount;
    private int pageReplacementCount;
    private int totalPageRequests;
    private int physicalMemorySize; // Number of frames in physical memory
    private int pagesInMemory; // Current number of pages loaded in memory

    public StatisticsCollector(int physicalMemorySize) {
        this.physicalMemorySize = physicalMemorySize;
        reset();
    }

    // Resets all statistics to their initial state
    public void reset() {
        pageFaultCount = 0;
        pageReplacementCount = 0;
        totalPageRequests = 0;
        pagesInMemory = 0;
    }

    // Increments the page fault count
    public void incrementPageFault() {
        pageFaultCount++;
    }

    // Increments the page replacement count
    public void incrementPageReplacement() {
        pageReplacementCount++;
    }

    // Increments the total page request count
    public void incrementPageRequest() {
        totalPageRequests++;
    }

    // Updates the number of pages currently in memory
    public void updatePagesInMemory(int pagesInMemory) {
        this.pagesInMemory = pagesInMemory;
    }

    // Calculates and returns the page fault rate as a percentage
    public double getPageFaultRate() {
        return totalPageRequests == 0 ? 0 : ((double) pageFaultCount / totalPageRequests) * 100;
    }

    // Calculates and returns the memory usage as a percentage
    public double getMemoryUsage() {
        return physicalMemorySize == 0 ? 0 : ((double) pagesInMemory / physicalMemorySize) * 100;
    }

    // Getters for each statistic
    public int getPageFaultCount() {
        return pageFaultCount;
    }

    public int getPageReplacementCount() {
        return pageReplacementCount;
    }

    public int getTotalPageRequests() {
        return totalPageRequests;
    }
}
