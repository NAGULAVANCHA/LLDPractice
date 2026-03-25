# Problem 13: Library Management

| Pattern | Why |
|---|---|
| **SRP** | Book stores metadata, Library manages catalog, Loan tracks borrowing |
| **CRUD** | Add/remove books, register members, search catalog |

## Entities
```
Book   → ISBN, title, author, available copies
Member → name, memberId, active loans
Loan   → book + member + dueDate + returned?
Library → catalog of books, members, loans
```

## Key Operations
- **Search**: by title, author, or ISBN
- **Borrow**: check availability, create Loan, decrement copies
- **Return**: mark Loan as returned, increment copies
- **Fines**: calculate based on (returnDate - dueDate)

## Interview Points
- Max books per member (configurable limit)
- Overdue tracking with date arithmetic
- Concurrent borrow: `synchronized` to prevent lending last copy twice

📁 `src/library/`

