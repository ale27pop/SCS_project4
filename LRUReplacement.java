package org.example.Model;

import org.example.Controller.MemoryController;

import java.util.HashMap;
import java.util.Map;

public class LRUReplacement implements PageReplacementAlgorithm {
    private Map<Integer, Long> accessTimeMap; // Tracks last access time for each page

    public LRUReplacement() {
        this.accessTimeMap = new HashMap<>();
    }

    @Override
    public void replacePage(int pageNumber, MemoryController memoryController) {
        if (memoryController.isPageInMemory(pageNumber)) {
            memoryController.getMemoryLogger().log("Page " + pageNumber + " is already in memory.");
            updateAccessTime(pageNumber);
            return;
        }

        if (memoryController.isMemoryFull()) {
            int leastRecentlyUsedPage = findLeastRecentlyUsedPage();
            memoryController.removePageFromMemory(leastRecentlyUsedPage);
            memoryController.getMemoryLogger().log("Page " + leastRecentlyUsedPage + " replaced by page " + pageNumber);
        }

        memoryController.loadPageIntoMemory(memoryController.getPageFromVirtualMemory(pageNumber));
        updateAccessTime(pageNumber);
    }

    @Override
    public int getReplacementCount() {
        return 0;
        //TO BE IMPLEMENTED
    }

    private int findLeastRecentlyUsedPage() {
        return accessTimeMap.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .get().getKey();
    }

    private void updateAccessTime(int pageNumber) {
        accessTimeMap.put(pageNumber, System.currentTimeMillis());
    }
}
