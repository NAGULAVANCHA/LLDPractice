# Problem 20: File System (Linux-like) — Complete Guide

---

## Part 1: Understanding the Problem

A simplified Linux file system with files, directories, and recursive operations.

### Requirements
- ✓ Files with content and size
- ✓ Directories containing files AND other directories (recursive)
- ✓ Operations: add, remove, list, find, getSize, resolvePath
- ✓ Recursive size calculation
- ✓ Tree display

---

## Part 2: The Key Insight — Composite Pattern (Classic Example)

### Real World Analogy
A folder can contain files AND other folders. You can ask "What's the size?" of both a file (direct answer) and a folder (recursive sum). The Composite Pattern lets you treat them **uniformly**.

### The Structure
```
FileSystemEntry (abstract)
├── File      → leaf node (has content, knows its size)
└── Directory → composite node (has children, size = sum of children)
                 ↳ children can be Files OR Directories (recursive!)
```

### Why This Pattern?
Without it, you'd need `instanceof` checks everywhere:
```java
// BAD — without Composite
long getSize(Object entry) {
    if (entry instanceof File) return ((File) entry).getContent().length();
    else if (entry instanceof Directory) {
        long total = 0;
        for (Object child : ((Directory) entry).getChildren())
            total += getSize(child);  // recursive
        return total;
    }
}
```

With Composite, just call `entry.getSize()` — polymorphism handles it:
```java
// GOOD — with Composite
FileSystemEntry entry = ...;
long size = entry.getSize();  // works for BOTH File and Directory!
```

---

## Part 3: The Code — Explained

### FileSystemEntry — Abstract Base
```java
public abstract class FileSystemEntry {
    private final String name;
    private final LocalDateTime created;
    private LocalDateTime modified;

    public abstract long getSize();
    public abstract boolean isDirectory();
    public abstract void display(String indent);

    public void touch() { this.modified = LocalDateTime.now(); }
}
```

### File — Leaf Node
```java
public class File extends FileSystemEntry {
    private String content;

    public long getSize() { return content.length(); }  // direct answer!

    public void write(String content) {
        this.content = content;
        touch();  // update modified time
    }

    public void display(String indent) {
        System.out.println(indent + "📄 " + getName() + " (" + getSize() + " bytes)");
    }
}
```

### Directory — Composite Node
```java
public class Directory extends FileSystemEntry {
    private final Map<String, FileSystemEntry> children;  // LinkedHashMap for order

    public void add(FileSystemEntry entry) {
        children.put(entry.getName(), entry);
        touch();
    }

    public void remove(String name) { children.remove(name); touch(); }

    public long getSize() {
        // RECURSIVE: sum of ALL descendant files
        return children.values().stream()
            .mapToLong(FileSystemEntry::getSize)
            .sum();
    }

    public void display(String indent) {
        System.out.println(indent + "📁 " + getName() + "/ (" + getSize() + " bytes)");
        for (FileSystemEntry child : children.values()) {
            child.display(indent + "  ");  // RECURSIVE display
        }
    }

    // Recursive find by name pattern
    public List<FileSystemEntry> find(String pattern) {
        List<FileSystemEntry> results = new ArrayList<>();
        findHelper(this, pattern.toLowerCase(), results);
        return results;
    }

    private void findHelper(Directory dir, String pattern, List<FileSystemEntry> results) {
        for (FileSystemEntry entry : dir.list()) {
            if (entry.getName().toLowerCase().contains(pattern))
                results.add(entry);
            if (entry.isDirectory())
                findHelper((Directory) entry, pattern, results);  // recurse into subdirs
        }
    }

    // Path resolution: "home/user/docs" → entry
    public FileSystemEntry resolvePath(String path) {
        String[] parts = path.split("/");
        FileSystemEntry current = this;
        for (String part : parts) {
            if (!(current instanceof Directory)) return null;
            current = ((Directory) current).get(part);
            if (current == null) return null;
        }
        return current;
    }
}
```

---

## Part 4: Data Flow — Tree Operations

```
Build:
  root/
  ├── config.yaml (24 bytes)
  └── home/
      ├── docs/
      │   ├── README.md (24 bytes)
      │   └── notes.txt (32 bytes)
      └── src/
          └── Main.java (22 bytes)

root.getSize()
  → config.yaml.getSize() = 24
  → home.getSize()
    → docs.getSize()
      → README.md.getSize() = 24
      → notes.txt.getSize() = 32
      → total = 56
    → src.getSize()
      → Main.java.getSize() = 22
      → total = 22
    → total = 78
  → total = 102 bytes

root.find(".java")
  → check config.yaml → no
  → recurse into home/
    → recurse into docs/
      → check README.md → no
      → check notes.txt → no
    → recurse into src/
      → check Main.java → YES! ✓
  → Result: [Main.java]

root.resolvePath("home/docs/README.md")
  → root.get("home") → home (Directory)
  → home.get("docs") → docs (Directory)
  → docs.get("README.md") → README.md (File) ✓
```

---

## Part 5: Follow-Up Questions

| Question | Answer |
|---|---|
| Permissions? | Add `String owner` and `int permissions` (like Unix 755). Check before read/write. |
| Symlinks? | Create `SymbolicLink extends FileSystemEntry` that stores a target path. `getSize()` delegates to target. |
| Disk space limit? | Track total used space. `add()` checks if (usedSpace + newEntry.getSize()) > limit. |
| mv / cp commands? | `mv`: remove from source dir, add to target dir. `cp`: clone the entry (Prototype) and add to target. |
| How does `ls -la` work? | `display()` already shows names and sizes. Add modified time and permissions to the output. |

---

## Part 6: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **Composite** | File and Directory share `FileSystemEntry` interface — treated uniformly |
| **Recursion** | getSize(), display(), find() all recurse into subdirectories |
| **SRP** | File manages content, Directory manages children |
| **LinkedHashMap** | Preserves insertion order for consistent listing |
| **Path Resolution** | Split path by `/`, traverse tree step by step |

---

📁 **Source code:** `src/filesystem/`
