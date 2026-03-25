package library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Loan {
    private static int counter = 0;
    private final int loanId;
    private final Book book;
    private final Member member;
    private final LocalDate issueDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private static final double FINE_PER_DAY = 1.0;
    private static final int LOAN_DAYS = 14;

    public Loan(Book book, Member member) {
        this.loanId = ++counter;
        this.book = book;
        this.member = member;
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusDays(LOAN_DAYS);
    }

    public double returnBook() {
        this.returnDate = LocalDate.now();
        book.returnBook();
        member.removeLoan(this);

        long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
        double fine = daysOverdue > 0 ? daysOverdue * FINE_PER_DAY : 0;
        return fine;
    }

    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }

    public int getLoanId() { return loanId; }
    public Book getBook() { return book; }
    public Member getMember() { return member; }

    @Override
    public String toString() {
        return "Loan#" + loanId + " | " + book.getTitle() + " | " + member.getName() +
                " | Due: " + dueDate + (returnDate != null ? " | Returned: " + returnDate : "");
    }
}

