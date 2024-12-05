package org.example.Model;

import java.util.LinkedHashMap;
import java.util.Map;

public class TranslationLookasideBuffer {
    private final int maxEntries; // Maximum number of TLB entries
    private final LinkedHashMap<Integer, Integer> tlb; // Maps virtual page numbers to physical frame numbers

    // Constructor
    public TranslationLookasideBuffer(int maxEntries) {
        this.maxEntries = maxEntries;

        // Initialize as an access-order LinkedHashMap to emulate LRU behavior
        this.tlb = new LinkedHashMap<>(maxEntries, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                return size() > maxEntries; // Automatically evict the oldest entry if capacity is exceeded
            }
        };
    }

    // Add a mapping from virtual page number to physical frame number
    public void addEntry(int virtualPageNumber, int physicalFrameNumber) {
        if (virtualPageNumber < 0 || physicalFrameNumber < 0) {
            throw new IllegalArgumentException("Page and frame numbers must be non-negative.");
        }
        tlb.put(virtualPageNumber, physicalFrameNumber); // Add or update the mapping
    }

    // Get the physical frame number for a given virtual page number (returns -1 if not in TLB)
    public int getFrameNumber(int virtualPageNumber) {
        return tlb.getOrDefault(virtualPageNumber, -1); // Fetch the frame number or return -1 if not present
    }

    // Check if a virtual page number is in the TLB
    public boolean containsPage(int virtualPageNumber) {
        return tlb.containsKey(virtualPageNumber);
    }

    // Get all entries in the TLB (for visualization or debugging)
    public Map<Integer, Integer> getEntries() {
        return new LinkedHashMap<>(tlb); // Return a copy of the TLB entries
    }

    // Clear all entries in the TLB
    public void clear() {
        tlb.clear();
    }

    // Method for debugging: Print current TLB state
    public void printTLBState() {
        System.out.println("Current TLB State:");
        tlb.forEach((virtualPage, physicalFrame) ->
                System.out.println("Virtual Page " + virtualPage + " -> Physical Frame " + physicalFrame));
    }


}
