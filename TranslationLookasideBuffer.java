package org.example.Model;

import java.util.LinkedHashMap;
import java.util.Map;

public class TranslationLookasideBuffer {
    private final int maxEntries; // Maximum number of TLB entries
    private final LinkedHashMap<Integer, Integer> tlb; // Maps virtual page numbers to physical frame numbers
    private int accessCount; // Tracks total number of accesses to the TLB
    private int hitCount; // Tracks number of TLB hits
    private int missCount; // Tracks number of TLB misses

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

        this.accessCount = 0;
        this.hitCount = 0;
        this.missCount = 0;
    }

    /**
     * Add a mapping from virtual page number to physical frame number.
     * If the TLB is full, it evicts the least recently used entry.
     */
    public synchronized void addEntry(int virtualPageNumber, int physicalFrameNumber) {
        if (virtualPageNumber < 0 || physicalFrameNumber < 0) {
            throw new IllegalArgumentException("Page and frame numbers must be non-negative.");
        }
        tlb.put(virtualPageNumber, physicalFrameNumber); // Add or update the mapping
        System.out.println("TLB Update: Added Virtual Page " + virtualPageNumber + " -> Physical Frame " + physicalFrameNumber);
    }

    /**
     * Retrieve the physical frame number for a given virtual page number.
     * @param virtualPageNumber The virtual page number to lookup.
     * @return The physical frame number if found, or -1 if not present (miss).
     */
    public synchronized int getFrameNumber(int virtualPageNumber) {
        accessCount++; // Increment access count
        if (tlb.containsKey(virtualPageNumber)) {
            hitCount++; // Increment hit count
            System.out.println("TLB Hit: Virtual Page " + virtualPageNumber + " -> Physical Frame " + tlb.get(virtualPageNumber));
            return tlb.get(virtualPageNumber);
        } else {
            missCount++; // Increment miss count
            System.out.println("TLB Miss: Virtual Page " + virtualPageNumber + " not found in TLB.");
            return -1; // Indicate a TLB miss
        }
    }

    /**
     * Check if a virtual page number is in the TLB.
     * @param virtualPageNumber The virtual page number to check.
     * @return True if the page is in the TLB, false otherwise.
     */
    public synchronized boolean containsPage(int virtualPageNumber) {
        return tlb.containsKey(virtualPageNumber);
    }

    /**
     * Get all entries in the TLB (for visualization or debugging).
     * @return A copy of the TLB entries.
     */
    public synchronized Map<Integer, Integer> getEntries() {
        return new LinkedHashMap<>(tlb); // Return a copy of the TLB entries
    }

    /**
     * Clear all entries in the TLB.
     */
    public synchronized void clear() {
        tlb.clear();
        accessCount = 0;
        hitCount = 0;
        missCount = 0;
        System.out.println("TLB Cleared: All entries and statistics reset.");
    }

    /**
     * Get the total number of accesses to the TLB.
     * @return The total access count.
     */
    public synchronized int getAccessCount() {
        return accessCount;
    }

    /**
     * Get the number of TLB hits.
     * @return The TLB hit count.
     */
    public synchronized int getHitCount() {
        return hitCount;
    }

    /**
     * Get the number of TLB misses.
     * @return The TLB miss count.
     */
    public synchronized int getMissCount() {
        return missCount;
    }

    /**
     * Get the TLB hit ratio.
     * @return The hit ratio as a percentage (0 to 100).
     */
    public synchronized double getHitRatio() {
        return accessCount == 0 ? 0 : (hitCount / (double) accessCount) * 100;
    }

    /**
     * Print the current state of the TLB.
     */
    public synchronized void printTLBState() {
        System.out.println("Current TLB State:");
        tlb.forEach((virtualPage, physicalFrame) ->
                System.out.println("Virtual Page " + virtualPage + " -> Physical Frame " + physicalFrame));
    }

    /**
     * Print TLB statistics (hits, misses, and hit ratio).
     */
    public synchronized void printStatistics() {
        System.out.println("TLB Statistics:");
        System.out.println("Total Accesses: " + accessCount);
        System.out.println("Hits: " + hitCount);
        System.out.println("Misses: " + missCount);
        System.out.println("Hit Ratio: " + String.format("%.2f", getHitRatio()) + "%");
    }
}
