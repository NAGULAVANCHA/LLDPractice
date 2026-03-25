package filesystem;

import java.util.*;

/**
 * Composite: Directory contains other FileSystemEntries
 * (both Files and Directories).
 */
public class Directory extends FileSystemEntry {
    private final Map<String, FileSystemEntry> children;

    public Directory(String name) {
        super(name);
        this.children = new LinkedHashMap<>();
    }

    public void add(FileSystemEntry entry) {
        children.put(entry.getName(), entry);
        touch();
    }

    public void remove(String name) {
        children.remove(name);
        touch();
    }

    public FileSystemEntry get(String name) {
        return children.get(name);
    }

    public Collection<FileSystemEntry> list() {
        return children.values();
    }

    @Override
    public long getSize() {
        // Recursive: sum all children
        return children.values().stream().mapToLong(FileSystemEntry::getSize).sum();
    }

    @Override
    public boolean isDirectory() { return true; }

    @Override
    public void display(String indent) {
        System.out.println(indent + "📁 " + getName() + "/ (" + getSize() + " bytes)");
        for (FileSystemEntry child : children.values()) {
            child.display(indent + "  ");
        }
    }

    /**
     * Find files by name pattern (recursive).
     */
    public List<FileSystemEntry> find(String nameContains) {
        List<FileSystemEntry> results = new ArrayList<>();
        findHelper(this, nameContains.toLowerCase(), results);
        return results;
    }

    private void findHelper(Directory dir, String pattern, List<FileSystemEntry> results) {
        for (FileSystemEntry entry : dir.list()) {
            if (entry.getName().toLowerCase().contains(pattern)) {
                results.add(entry);
            }
            if (entry.isDirectory()) {
                findHelper((Directory) entry, pattern, results);
            }
        }
    }

    /**
     * Resolve a path like "home/user/docs" from this directory.
     */
    public FileSystemEntry resolvePath(String path) {
        String[] parts = path.split("/");
        FileSystemEntry current = this;
        for (String part : parts) {
            if (part.isEmpty()) continue;
            if (!(current instanceof Directory)) return null;
            current = ((Directory) current).get(part);
            if (current == null) return null;
        }
        return current;
    }

    // --- Demo ---
    public static void main(String[] args) {
        System.out.println("=== File System Demo (Composite Pattern) ===\n");

        Directory root = new Directory("root");

        // Build tree
        Directory home = new Directory("home");
        Directory docs = new Directory("docs");
        Directory src = new Directory("src");

        File readme = new File("README.md", "# My Project\nHello world");
        File main = new File("Main.java", "public class Main { }");
        File config = new File("config.yaml", "port: 8080\nhost: localhost");
        File notes = new File("notes.txt", "Remember to study LLD patterns!");

        root.add(home);
        root.add(config);
        home.add(docs);
        home.add(src);
        docs.add(readme);
        docs.add(notes);
        src.add(main);

        // Display tree
        root.display("");

        // Size
        System.out.println("\nTotal size of root: " + root.getSize() + " bytes");
        System.out.println("Size of home: " + home.getSize() + " bytes");

        // Find
        System.out.println("\n--- Find files containing '.java' ---");
        root.find(".java").forEach(f -> System.out.println("  Found: " + f.getName()));

        System.out.println("\n--- Find files containing 'note' ---");
        root.find("note").forEach(f -> System.out.println("  Found: " + f.getName()));

        // Resolve path
        System.out.println("\n--- Resolve path 'home/docs/README.md' ---");
        FileSystemEntry resolved = root.resolvePath("home/docs/README.md");
        if (resolved != null) {
            System.out.println("  Found: " + resolved.getName() + " (" + resolved.getSize() + " bytes)");
            if (resolved instanceof File) {
                System.out.println("  Content: " + ((File) resolved).getContent());
            }
        }

        // Remove
        System.out.println("\n--- Remove notes.txt ---");
        docs.remove("notes.txt");
        root.display("");
    }
}

