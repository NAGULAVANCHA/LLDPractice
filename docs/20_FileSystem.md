# Problem 20: File System

| Pattern | Why |
|---|---|
| **Composite** | Directory IS-A FileSystemEntry, HAS FileSystemEntries |
| **Recursion** | getSize(), display(), find() all recurse into subdirectories |

## Composite Pattern — Classic Example
```
FileSystemEntry (abstract)
├── File      → has name, size, content
└── Directory → has name, children (Map<name, FileSystemEntry>)
                 ↳ children can be Files OR Directories (recursive!)
```

## Recursive Size
```java
// File:
long getSize() { return content.length(); }

// Directory:
long getSize() {
    return children.values().stream()
        .mapToLong(FileSystemEntry::getSize)
        .sum();  // sum of ALL descendant files
}
```

## Recursive Display (tree view)
```
📁 root/ (1500 bytes)
  📁 src/ (1200 bytes)
    📄 Main.java (800 bytes)
    📄 Utils.java (400 bytes)
  📁 docs/ (300 bytes)
    📄 README.md (300 bytes)
```

## Operations
- `add(entry)` — add file/dir to directory
- `remove(name)` — remove by name
- `get(name)` — lookup child
- `find(pattern)` — recursive search by name
- `getSize()` — recursive total size

📁 `src/filesystem/`

