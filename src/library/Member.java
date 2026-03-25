package library;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private final String memberId;
    private final String name;
    private final List<Loan> activeLoans;
    private static final int MAX_BOOKS = 5;

    public Member(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        this.activeLoans = new ArrayList<>();
    }

    public boolean canBorrow() { return activeLoans.size() < MAX_BOOKS; }

    public void addLoan(Loan loan) { activeLoans.add(loan); }
    public void removeLoan(Loan loan) { activeLoans.remove(loan); }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public List<Loan> getActiveLoans() { return activeLoans; }

    @Override
    public String toString() {
        return name + " (ID:" + memberId + ", loans:" + activeLoans.size() + "/" + MAX_BOOKS + ")";
    }
}

