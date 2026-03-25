package library;

import java.util.*;
import java.util.stream.Collectors;

public class Library {
    private final Map<String, Book> books;     // isbn -> Book
    private final Map<String, Member> members; // memberId -> Member
    private final List<Loan> allLoans;

    public Library() {
        books = new HashMap<>();
        members = new HashMap<>();
        allLoans = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }

    public void registerMember(Member member) {
        members.put(member.getMemberId(), member);
    }

    public Loan borrowBook(String memberId, String isbn) {
        Member member = members.get(memberId);
        Book book = books.get(isbn);

        if (member == null) { System.out.println("  Member not found: " + memberId); return null; }
        if (book == null) { System.out.println("  Book not found: " + isbn); return null; }
        if (!member.canBorrow()) { System.out.println("  " + member.getName() + " has max loans!"); return null; }
        if (!book.checkout()) { System.out.println("  " + book.getTitle() + " not available!"); return null; }

        Loan loan = new Loan(book, member);
        member.addLoan(loan);
        allLoans.add(loan);
        System.out.println("  ✅ Borrowed: " + loan);
        return loan;
    }

    public double returnBook(int loanId) {
        Loan loan = allLoans.stream().filter(l -> l.getLoanId() == loanId).findFirst().orElse(null);
        if (loan == null) { System.out.println("  Loan not found: " + loanId); return 0; }
        double fine = loan.returnBook();
        System.out.println("  ✅ Returned: " + loan);
        if (fine > 0) System.out.println("  ⚠️ Fine: $" + String.format("%.2f", fine));
        return fine;
    }

    public List<Book> searchByTitle(String keyword) {
        return books.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> searchByAuthor(String author) {
        return books.values().stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void displayBooks() {
        System.out.println("=== Library Catalog ===");
        books.values().forEach(b -> System.out.println("  " + b));
    }

    public static void main(String[] args) {
        System.out.println("=== Library Management System Demo ===\n");

        Library lib = new Library();
        lib.addBook(new Book("978-1", "Design Patterns", "GoF", 3));
        lib.addBook(new Book("978-2", "Clean Code", "Robert Martin", 2));
        lib.addBook(new Book("978-3", "CLRS Algorithms", "Cormen", 1));

        lib.registerMember(new Member("M1", "Alice"));
        lib.registerMember(new Member("M2", "Bob"));

        lib.displayBooks();

        System.out.println("\n--- Borrowing ---");
        Loan l1 = lib.borrowBook("M1", "978-1");
        Loan l2 = lib.borrowBook("M1", "978-2");
        Loan l3 = lib.borrowBook("M2", "978-1");

        lib.displayBooks();

        System.out.println("\n--- Search by author ---");
        lib.searchByAuthor("martin").forEach(b -> System.out.println("  Found: " + b));

        System.out.println("\n--- Returning ---");
        lib.returnBook(l1.getLoanId());
        lib.displayBooks();
    }
}

