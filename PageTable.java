package org.example.Model;

import java.util.HashMap;
import java.util.Map;

public class PageTable {
    private Map<Integer, Integer> pageTable; // Maps page number to frame number
    private int accessCount; // Tracks the total number of accesses to the page table

    public PageTable() {
        this.pageTable = new HashMap<>();
        this.accessCount = 0;
    }

    // Map a page number to a frame number
    public void mapPageToFrame(int pageNumber, int frameNumber) {
        pageTable.put(pageNumber, frameNumber);
    }

    // Retrieve the frame number for a given page, or -1 if not in memory
    public int getFrameForPage(int pageNumber) {
        accessCount++; // Increment access count on each lookup
        return pageTable.getOrDefault(pageNumber, -1);
    }

    // Remove a page from the table
    public void removePage(int pageNumber) {
        pageTable.remove(pageNumber);
    }

    // Check if a page is currently mapped to a frame in memory
    public boolean isPageInMemory(int pageNumber) {
        return pageTable.containsKey(pageNumber);
    }

    // Clear the entire page table
    public void clear() {
        pageTable.clear();
        accessCount = 0; // Reset access count
    }

    // Get the number of times the page table has been accessed
    public int getAccessCount() {
        return accessCount;
    }

    public Map<Integer, Integer> getPageTableMap() {
        return pageTable;
    }

}
