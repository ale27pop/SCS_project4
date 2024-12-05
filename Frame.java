package org.example.Model;

public class Frame {
    private int frameNumber; // Unique frame number
    private Page loadedPage; // Page currently loaded in this frame

    // Constructor
    public Frame(int frameNumber) {
        this.frameNumber = frameNumber;
        this.loadedPage = null; // Initialize as empty
    }

    // Getter for frame number
    public int getFrameNumber() {
        return frameNumber;
    }

    // Getter for the page loaded in this frame
    public Page getLoadedPage() {
        return loadedPage;
    }

    // Setter for the page to load into this frame
    public void setLoadedPage(Page loadedPage) {
        this.loadedPage = loadedPage;
    }

    // Check if the frame is empty (no page loaded)
    public boolean isEmpty() {
        return loadedPage == null;
    }
}
