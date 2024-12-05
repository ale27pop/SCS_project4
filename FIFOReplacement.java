package org.example.Model;

import org.example.Controller.MemoryController;

import java.util.LinkedList;
import java.util.Queue;

public class FIFOReplacement implements PageReplacementAlgorithm {
    private Queue<Integer> pageQueue; // Tracks the order of pages loaded into memory
    private int replacementCount; // Tracks the number of page replacements

    public FIFOReplacement() {
        this.pageQueue = new LinkedList<>();
        this.replacementCount = 0;
    }

    @Override
    public void replacePage(int pageNumber, MemoryController memoryController) {
        // Check if the page is already in memory
        if (memoryController.isPageInMemory(pageNumber)) {
            memoryController.getMemoryLogger().log("Page " + pageNumber + " is already in memory.");
            return;
        }

        // Check if memory is full and replacement is needed
        if (memoryController.isMemoryFull()) {
            int oldestPage = pageQueue.poll(); // Get the oldest page in memory
            memoryController.removePageFromMemory(oldestPage); // Remove the oldest page
            memoryController.getMemoryLogger().log("Page " + oldestPage + " replaced by page " + pageNumber);
            replacementCount++; // Increment the replacement counter
        }

        // Load the new page into memory
        memoryController.loadPageIntoMemory(memoryController.getPageFromVirtualMemory(pageNumber));
        pageQueue.add(pageNumber); // Add the new page to the queue
        memoryController.getMemoryLogger().log("Page " + pageNumber + " loaded into memory.");
    }

    @Override
    public int getReplacementCount() {
        return replacementCount; // Return the total number of page replacements
    }
}
