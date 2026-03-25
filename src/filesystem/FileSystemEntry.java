package filesystem;

import java.time.LocalDateTime;

/**
 * FILE SYSTEM LLD (Linux-like)
 * ==============================
 * Key Concepts:
 *  - Composite Pattern:   Directory IS-A FileSystemEntry, HAS FileSystemEntries
 *  - This is the CLASSIC composite pattern example
 *  - SRP:                 File stores data, Directory manages children
 *  - Recursion:           getSize() on a directory recursively sums children
 *
 * Interview Points:
 *  - Composite: treat files and directories uniformly
 *  - ls, cd, mkdir, touch, rm, find operations
 *  - Size calculation (recursive for directories)
 *  - Path resolution ("/home/user/docs")
 */
public abstract class FileSystemEntry {
    private final String name;
    private final LocalDateTime created;
    private LocalDateTime modified;

    public FileSystemEntry(String name) {
        this.name = name;
        this.created = LocalDateTime.now();
        this.modified = this.created;
    }

    public String getName() { return name; }
    public LocalDateTime getCreated() { return created; }
    public LocalDateTime getModified() { return modified; }

    protected void touch() { this.modified = LocalDateTime.now(); }

    public abstract long getSize();
    public abstract void display(String indent);
    public abstract boolean isDirectory();
}

