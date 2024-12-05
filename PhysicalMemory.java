package org.example.Model;

import java.util.ArrayList;
import java.util.List;

public class PhysicalMemory {
    private final List<Frame> frames; // List of frames representing physical memory
    private final int size; // Total size of physical memory (number of frames)
    private int loadedPageCount; // Tracks the number of loaded pages

    // Constructor
    public PhysicalMemory(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Physical memory size must be greater than 0.");
        }
        this.size = size;
        this.frames = new ArrayList<>();
        this.loadedPageCount = 0;

        // Initialize frames with unique frame numbers
        for (int i = 0; i < size; i++) {
            frames.add(new Frame(i));
        }
    }

    /**
     * Retrieve a specific frame by its frame number.
     * @param frameNumber The frame number to retrieve.
     * @return The corresponding frame.
     * @throws IndexOutOfBoundsException if the frame number is invalid.
     */
    public synchronized Frame getFrame(int frameNumber) {
        if (frameNumber < 0 || frameNumber >= size) {
            throw new IndexOutOfBoundsException("Frame number " + frameNumber + " is out of bounds.");
        }
        return frames.get(frameNumber);
    }

    /**
     * Find the first available free frame.
     * @return The first free frame, or null if none are available.
     */
    public synchronized Frame getFreeFrame() {
        for (Frame frame : frames) {
            if (frame.isEmpty()) {
                return frame; // Return the first empty frame
            }
        }
        System.out.println("No free frames available.");
        return null; // No free frames available
    }

    /**
     * Check if physical memory is full (no free frames).
     * @return True if no free frames are available, false otherwise.
     */
    public synchronized boolean isFull() {
        return loadedPageCount == size;
    }

    /**
     * Clear all frames in physical memory.
     * This removes all pages from the frames.
     */
    public synchronized void clear() {
        for (Frame frame : frames) {
            frame.setLoadedPage(null); // Remove the page from each frame
        }
        loadedPageCount = 0; // Reset loaded page count
        System.out.println("Physical memory cleared.");
    }

    /**
     * Get the current memory usage as a percentage.
     * @return The memory usage as a formatted string percentage.
     */
    public synchronized String getMemoryUsagePercentage() {
        return String.format("%.2f", (loadedPageCount * 100.0) / size) + "%";
    }

    /**
     * Get the total number of frames in physical memory.
     * @return The total number of frames.
     */
    public synchronized int getSize() {
        return size;
    }

    /**
     * Calculate the number of currently free frames in physical memory.
     * @return The count of free frames.
     */
    public synchronized int getFreeFrameCount() {
        return size - loadedPageCount;
    }

    /**
     * Get a list of all frames in physical memory.
     * @return A list of all frames.
     */
    public synchronized List<Frame> getFrames() {
        return new ArrayList<>(frames); // Return a copy to prevent direct modification
    }

    /**
     * Load a page into a specific frame.
     * @param frame The frame to load the page into.
     * @param page The page to load.
     */
    public synchronized void loadPageIntoFrame(Frame frame, Page page) {
        if (frame == null || page == null) {
            throw new IllegalArgumentException("Frame and page cannot be null.");
        }
        if (!frame.isEmpty()) {
            System.out.println("Evicting Page " + frame.getLoadedPage().getPageNumber() +
                    " from Frame " + frame.getFrameNumber());
        }
        frame.setLoadedPage(page);
        loadedPageCount++;
        System.out.println("Loaded Page " + page.getPageNumber() + " into Frame " + frame.getFrameNumber());
    }

    /**
     * Evict a page from a specific frame.
     * @param frame The frame to evict the page from.
     */
    public synchronized void evictPageFromFrame(Frame frame) {
        if (frame == null) {
            throw new IllegalArgumentException("Frame cannot be null.");
        }
        if (!frame.isEmpty()) {
            System.out.println("Evicting Page " + frame.getLoadedPage().getPageNumber() +
                    " from Frame " + frame.getFrameNumber());
            frame.setLoadedPage(null);
            loadedPageCount--;
        }
    }

    /**
     * Print the current state of all frames for debugging purposes.
     */
    public synchronized void printMemoryState() {
        System.out.println("Physical Memory State:");
        for (Frame frame : frames) {
            String status = frame.isEmpty() ? "Empty" : "Page " + frame.getLoadedPage().getPageNumber();
            System.out.println("Frame " + frame.getFrameNumber() + ": " + status);
        }
    }
}
