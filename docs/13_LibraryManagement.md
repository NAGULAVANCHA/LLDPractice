# Problem 13: Library Management System — Complete Guide

---

## Part 1: Understanding the Problem

A system to manage a library's catalog, members, and book loans.

### Requirements
- ✓ Add books (with multiple copies per ISBN)
- ✓ Register members
- ✓ Borrow and return books
- ✓ Search by title or author
- ✓ Max loan limit per member
- ✓ Fine calculation for overdue returns

---

## Part 2: Key Design — Book vs Copy

### Interview Point: Book vs BookItem
In the real world, a library has 3 copies of "Clean Code". The **Book** entity stores metadata (title, author, ISBN), and `availableCopies` tracks how many are on the shelf.

```java
public class Book {
    private final String isbn, title, author;
    private int totalCopies;
    private int availableCopies;

    public boolean checkout() {
        if (availableCopies <= 0) return false;
        availableCopies--;
        return true;
    }

    public void returnBook() {
        availableCopies++;
    }
}
```

---

## Part 3: The Code — Explained

### Member — Loan Limit
```java
public class Member {
    private final String memberId, name;
    private final List<Loan> activeLoans;
    private static final int MAX_BOOKS = 5;

    public boolean canBorrow() { return activeLoans.size() < MAX_BOOKS; }
}
```

### Loan — Due Date + Fines
```java
public class Loan {
    private final Book book;
    private final Member member;
    private final LocalDate issueDate;
    private final LocalDate dueDate;       // issueDate + 14 days
    private LocalDate returnDate;
    private static final double FINE_PER_DAY = 1.0;

    public double returnBook() {
        this.returnDate = LocalDate.now();
        book.returnBook();                  // increment available copies
        member.removeLoan(this);

        long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
        return daysOverdue > 0 ? daysOverdue * FINE_PER_DAY : 0;
    }
}
```

### Library — Main Service
```java
public class Library {
    private final Map<String, Book> books;      // isbn → Book
    private final Map<String, Member> members;   // memberId → Member

    public Loan borrowBook(String memberId, String isbn) {
        Member member = members.get(memberId);
        Book book = books.get(isbn);

        if (!member.canBorrow()) { "Max loans reached!"; return null; }
        if (!book.checkout())    { "Not available!"; return null; }

        Loan loan = new Loan(book, member);
        member.addLoan(loan);
        return loan;
    }

    public List<Book> searchByTitle(String keyword) {
        return books.values().stream()
            .filter(b -> b.getTitle().toLowerCase().contains(keyword.toLowerCase()))
            .collect(toList());
    }
}
```

---

## Part 4: Data Flow — Borrow and Return

```
1. Alice borrows "Clean Code" (ISBN 978-2, 2 copies available)
   → member.canBorrow()? activeLoans=0 < MAX=5 → YES
   → book.checkout()? availableCopies=2 > 0 → YES, now 1
   → Create Loan(book, alice, today, today+14)
   → alice.addLoan(loan)

2. Alice returns after 18 days (4 days overdue)
   → loan.returnBook()
   → book.returnBook() → availableCopies 1 → 2
   → alice.removeLoan(loan)
   → daysOverdue = 18 - 14 = 4
   → fine = 4 × $1.00 = $4.00
```

---

## Part 5: Follow-Up Questions

| Question | Answer |
|---|---|
| Reservation queue? | If all copies are out, add member to a `Queue<Member>` on the Book. Notify first in queue when returned. |
| Different loan periods? | Add `loanDays` field to Book or create `LoanPolicy` strategy. |
| Concurrent borrows? | Add `synchronized` to `borrowBook()` to prevent lending the last copy twice. |
| E-books vs physical? | Create `BookItem` interface with `PhysicalBook` and `EBook` subclasses. |

---

## Part 6: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **SRP** | Book stores metadata, Library manages catalog, Loan tracks borrowing |
| **Encapsulation** | `availableCopies` managed internally via checkout/returnBook |
| **HashMap** | O(1) lookup by ISBN and memberId |
| **Business Rules** | Max loans per member, fine calculation — all encapsulated |

---

📁 **Source code:** `src/library/`
