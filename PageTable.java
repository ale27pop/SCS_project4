package org.example.Model;

import java.util.HashMap;
import java.util.Map;

public class PageTable {
    private final Map<Integer, PageEntry> pageTable; // Maps page number to PageEntry (frame number + valid bit)
    private int accessCount; // Tracks the total number of accesses to the page table
    private int pageFaultCount; // Tracks the number of page faults

    // Constructor
    public PageTable() {
        this.pageTable = new HashMap<>();
        this.accessCount = 0;
        this.pageFaultCount = 0;
    }

    // Get a map of valid virtual-to-physical page mappings
    public Map<Integer, Integer> getPageTableMap() {
        Map<Integer, Integer> resultMap = new HashMap<>();
        for (Map.Entry<Integer, PageEntry> entry : pageTable.entrySet()) {
            int virtualPage = entry.getKey();
            PageEntry pageEntry = entry.getValue();

            // Only include valid pages in the result map
            if (pageEntry.valid) {
                resultMap.put(virtualPage, pageEntry.frameNumber);
            }
        }
        return resultMap;
    }

    // Inner class to store page table entries
    private static class PageEntry {
        int frameNumber; // Physical frame number
        boolean valid;   // Indicates if the page is valid (in memory)

        public PageEntry(int frameNumber, boolean valid) {
            this.frameNumber = frameNumber;
            this.valid = valid;
        }
    }

    /**
     * Maps a page number to a frame number and marks it as valid.
     * If the page already exists, it updates the frame and validity status.
     */
    public synchronized void mapPageToFrame(int pageNumber, int frameNumber) {
        pageTable.put(pageNumber, new PageEntry(frameNumber, true));
        System.out.println("Mapped Virtual Page " + pageNumber + " to Physical Frame " + frameNumber);
    }

    /**
     * Retrieves the frame number for a given page.
     * @param pageNumber The page number to lookup.
     * @return The frame number if the page is valid, or -1 if the page is not in memory.
     */
    public synchronized int getFrameForPage(int pageNumber) {
        accessCount++;
        PageEntry entry = pageTable.get(pageNumber);

        if (entry == null || !entry.valid) {
            pageFaultCount++;
            System.out.println("Page Fault: Page " + pageNumber + " is not in memory.");
            return -1; // Indicates a page fault
        }

        System.out.println("Page Hit: Page " + pageNumber + " is mapped to Frame " + entry.frameNumber);
        return entry.frameNumber;
    }

    /**
     * Marks a page as invalid (e.g., when it is evicted from memory).
     * @param pageNumber The page number to mark as invalid.
     */
    public synchronized void invalidatePage(int pageNumber) {
        if (pageTable.containsKey(pageNumber)) {
            pageTable.get(pageNumber).valid = false;
            System.out.println("Invalidated Page " + pageNumber);
        } else {
            System.err.println("Attempted to invalidate non-existent Page " + pageNumber);
        }
    }

    /**
     * Removes a page from the table completely.
     * @param pageNumber The page number to remove.
     */
    public synchronized void removePage(int pageNumber) {
        if (pageTable.remove(pageNumber) != null) {
            System.out.println("Removed Page " + pageNumber + " from the table.");
        } else {
            System.err.println("Attempted to remove non-existent Page " + pageNumber);
        }
    }

    /**
     * Checks if a page is currently mapped to a frame and valid.
     * @param pageNumber The page number to check.
     * @return True if the page is valid in memory, false otherwise.
     */
    public synchronized boolean isPageInMemory(int pageNumber) {
        PageEntry entry = pageTable.get(pageNumber);
        return entry != null && entry.valid;
    }

    /**
     * Clears the entire page table and resets statistics.
     */
    public synchronized void clear() {
        pageTable.clear();
        accessCount = 0;
        pageFaultCount = 0;
        System.out.println("Cleared the page table and reset statistics.");
    }

    /**
     * Retrieves the total number of page faults.
     * @return The number of page faults.
     */
    public synchronized int getPageFaultCount() {
        return pageFaultCount;
    }

    /**
     * Retrieves the total number of accesses to the page table.
     * @return The access count.
     */
    public synchronized int getAccessCount() {
        return accessCount;
    }

    /**
     * Prints the current state of the page table.
     */
    public synchronized void printPageTable() {
        System.out.println("Page Table:");
        for (Map.Entry<Integer, PageEntry> entry : pageTable.entrySet()) {
            int pageNumber = entry.getKey();
            PageEntry pageEntry = entry.getValue();
            System.out.println("Page " + pageNumber + " -> Frame " + pageEntry.frameNumber +
                    " (Valid: " + pageEntry.valid + ")");
        }
    }
}
