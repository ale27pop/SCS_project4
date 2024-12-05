package org.example.Model;

import java.util.ArrayList;
import java.util.List;

public class VirtualMemory {
    private List<Page> pages;

    public VirtualMemory(int size) {
        pages = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            pages.add(new Page(i));
        }
    }

    public Page getPage(int pageNumber) {
        return pages.get(pageNumber);
    }

    public List<Page> getPages() {
        return pages;
    }

    public int getSize() {// Returns the number of pages in virtual memory
        return pages.size();
    }
}

