package org.example.Model;

public class Page {
    private int pageNumber;
    private boolean isInMemory;
    private int frameNumber; // -1 if not loaded into a frame

    public Page(int pageNumber) {
        this.pageNumber = pageNumber;
        this.isInMemory = false;
        this.frameNumber = -1;
    }

    // getters and setters
    public int getPageNumber() {
        return pageNumber;
    }

    public boolean isInMemory() {
        return isInMemory;
    }

    public void setInMemory(boolean inMemory) {
        isInMemory = inMemory;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }
}
