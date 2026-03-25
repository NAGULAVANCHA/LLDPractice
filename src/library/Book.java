package library;

/**
 * LIBRARY MANAGEMENT SYSTEM LLD
 * ===============================
 * Key Concepts:
 *  - CRUD operations on Books, Members, and Loans
 *  - Encapsulation:  Book availability managed internally
 *  - SRP:            Book, Member, Loan, Library each have clear responsibilities
 *  - Search:         Search books by title, author, or ISBN
 *  - Business Rules: Max books per member, due dates, fines
 *
 * Interview Points:
 *  - Book vs BookItem (multiple copies of same book)
 *  - Reservation system (queue for unavailable books)
 *  - Fine calculation on overdue returns
 *  - Singleton for Library (optional)
 */
public class Book {
    private final String isbn;
    private final String title;
    private final String author;
    private int totalCopies;
    private int availableCopies;

    public Book(String isbn, String title, String author, int copies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = copies;
        this.availableCopies = copies;
    }

    public boolean isAvailable() { return availableCopies > 0; }

    public boolean checkout() {
        if (availableCopies <= 0) return false;
        availableCopies--;
        return true;
    }

    public void returnBook() {
        if (availableCopies < totalCopies) availableCopies++;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getAvailableCopies() { return availableCopies; }
    public int getTotalCopies() { return totalCopies; }

    @Override
    public String toString() {
        return "\"" + title + "\" by " + author + " [ISBN:" + isbn +
                "] (available: " + availableCopies + "/" + totalCopies + ")";
    }
}

