package org.example.Model;

import java.util.ArrayList;
import java.util.List;

public class PhysicalMemory {
    private List<Frame> frames; // List of frames representing physical memory
    private int size; // Total size of physical memory (number of frames)

    public PhysicalMemory(int size) {
        this.size = size;
        this.frames = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            frames.add(new Frame(i)); // Initialize each frame with a unique frame number
        }
    }

    // Retrieve a specific frame by its frame number
    public Frame getFrame(int frameNumber) {
        if (frameNumber < 0 || frameNumber >= size) {
            throw new IndexOutOfBoundsException("Frame number " + frameNumber + " is out of bounds.");
        }
        return frames.get(frameNumber);
    }

    // Find the first available free frame
    public Frame getFreeFrame() {
        for (Frame frame : frames) {
            if (frame.isEmpty()) {
                return frame; // Return the first empty frame
            }
        }
        return null; // No free frames available
    }

    // Get the total number of frames in physical memory
    public int getSize() {
        return size;
    }

    // Get a list of all frames in physical memory
    public List<Frame> getFrames() {
        return frames; // Return the list of all frames
    }


    // Calculate the number of currently free frames in physical memory
    public int getFreeFrameCount() {
        int freeCount = 0;
        for (Frame frame : frames) {
            if (frame.isEmpty()) {
                freeCount++;
            }
        }
        return freeCount;
    }

    // Check if the physical memory is full (no free frames)
    public boolean isFull() {
        return getFreeFrameCount() == 0;
    }

    // Clear all frames in physical memory
    public void clear() {
        for (Frame frame : frames) {
            frame.setLoadedPage(null); // Remove the page from each frame
        }
    }

    // Get the current memory usage as a percentage
    public String getMemoryUsagePercentage() {
        int usedFrames = size - getFreeFrameCount();
        return String.format("%.2f", (usedFrames * 100.0) / size) + "%";
    }

    // Print the current state of all frames for debugging purposes
    public void printMemoryState() {
        System.out.println("Physical Memory State:");
        for (Frame frame : frames) {
            String status = frame.isEmpty() ? "Empty" : "Page " + frame.getLoadedPage().getPageNumber();
            System.out.println("Frame " + frame.getFrameNumber() + ": " + status);
        }
    }

}
