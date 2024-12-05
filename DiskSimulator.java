package org.example.Model;

import java.util.HashMap;
import java.util.Map;

public class DiskSimulator {
    // Map to store pages in "disk," using page number as the key and Page object as the value
    private Map<Integer, Page> diskStorage;

    public DiskSimulator() {
        diskStorage = new HashMap<>();
    }

    // Adds a page to disk storage (when it is removed from physical memory)
    public void storePage(Page page) {
        diskStorage.put(page.getPageNumber(), page);
        page.setInMemory(false); // Mark page as not in memory
    }

    // Retrieves a page from disk storage (when it needs to be loaded back into physical memory)
    public Page retrievePage(int pageNumber) {
        Page page = diskStorage.get(pageNumber);
        if (page != null) {
            diskStorage.remove(pageNumber); // Remove from disk once it's loaded back
            page.setInMemory(true); // Mark page as in memory
        }
        return page;
    }

    // Checks if a specific page is stored on disk
    public boolean isPageOnDisk(int pageNumber) {
        return diskStorage.containsKey(pageNumber);
    }

    // Removes a page from disk storage (used when a page is permanently removed)
    public void removePage(int pageNumber) {
        diskStorage.remove(pageNumber);
    }

    // Clears the entire disk storage (useful for resetting the simulation)
    public void clearDisk() {
        diskStorage.clear();
    }

    // Returns the total number of pages currently stored on disk
    public int getDiskPageCount() {
        return diskStorage.size();
    }
}
