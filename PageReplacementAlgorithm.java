package org.example.Model;

import org.example.Controller.*;

public interface PageReplacementAlgorithm {
    /**
     * Replaces a page in memory based on the algorithm's specific rules.
     *
     * @param pageNumber The page number to load into memory.
     * @param memoryController The controller that manages memory operations.
     */
    void replacePage(int pageNumber, MemoryController memoryController);

    int getReplacementCount( );
}
