# Problem 11: BookMyShow (Movie Ticket Booking) — Complete Guide

---

## Part 1: Understanding the Problem

An online ticket booking system (like BookMyShow / Fandango):
- Users browse movies and shows
- Select seats from a seat map
- Book tickets with atomic seat locking (no double booking)
- Cancel bookings and release seats

### Requirements
- ✓ Movies have multiple shows (different times/screens)
- ✓ Each show has its OWN seat availability
- ✓ Different seat types with different prices (Silver, Gold, Platinum)
- ✓ **Prevent double booking** — the core interview challenge
- ✓ Cancel booking releases seats

---

## Part 2: The Key Insight — Concurrency & Atomic Booking

### The Double Booking Problem
```
Thread 1 (Alice): Check seat A1 → available ✓
Thread 2 (Bob):   Check seat A1 → available ✓    ← BOTH see it free!
Thread 1 (Alice): Book seat A1 → success
Thread 2 (Bob):   Book seat A1 → success          ← DOUBLE BOOKED! 💥
```

### Solution: Synchronized + Atomic Check-and-Book
```java
public synchronized boolean bookSeats(List<Seat> seats) {
    // STEP 1: Check ALL seats first
    for (Seat s : seats) {
        if (bookedSeats.contains(s)) return false;  // fail ALL if any taken
    }
    // STEP 2: Book ALL (only reached if ALL are available)
    bookedSeats.addAll(seats);
    return true;
}
```

**Why atomic (all-or-nothing)?**
If Alice wants seats A1 + A2, and A2 is taken, she shouldn't get A1 alone — that leaves her with a single seat she didn't want.

**Why `synchronized`?**
Only ONE thread can enter `bookSeats()` at a time. Prevents the check-then-act race condition.

---

## Part 3: The Code — Explained

### SeatType — Enum with Pricing
```java
public enum SeatType {
    SILVER(150.0), GOLD(250.0), PLATINUM(400.0);
    private final double price;
}
```

### Seat — Identity by (row, col)
```java
public class Seat {
    private final int row, col;
    private final SeatType type;

    // IMPORTANT: equals/hashCode based on (row, col)
    public int hashCode() { return 31 * row + col; }
    public boolean equals(Object o) {
        if (!(o instanceof Seat s)) return false;
        return row == s.row && col == s.col;
    }
}
```

**Why override equals/hashCode?** So `bookedSeats.contains(seat)` works correctly. Two Seat objects with the same (row, col) are the SAME seat.

### Show — Seat Map per Show
```java
public class Show {
    private final Movie movie;
    private final String screenName, time;
    private final List<Seat> allSeats;
    private final Set<Seat> bookedSeats;  // HashSet for O(1) lookup

    public synchronized List<Seat> getAvailableSeats() {
        return allSeats.stream()
            .filter(s -> !bookedSeats.contains(s))
            .collect(toList());
    }

    public synchronized boolean bookSeats(List<Seat> seats) { /* atomic */ }

    public synchronized void cancelSeats(List<Seat> seats) {
        bookedSeats.removeAll(seats);
    }
}
```

**Critical design:** Each Show has its OWN `bookedSeats`. Same screen at 7 PM vs 9 PM = different shows with different availability.

### Booking
```java
public class Booking {
    private final String userName;
    private final Show show;
    private final List<Seat> seats;
    private final double totalAmount;  // sum of seat prices
    private BookingStatus status;      // CONFIRMED or CANCELLED

    public void cancel() {
        status = BookingStatus.CANCELLED;
        show.cancelSeats(seats);  // release seats back!
    }
}
```

---

## Part 4: Data Flow — Booking Seats

```
1. Alice selects Show "Inception 7 PM"
2. Alice sees available seats: A1(Silver), A2(Silver), B1(Gold)...
3. Alice picks: [B1(Gold), B2(Gold)]

4. show.bookSeats([B1, B2])
   → synchronized block entered
   → Check B1: not in bookedSeats ✓
   → Check B2: not in bookedSeats ✓
   → bookedSeats.addAll([B1, B2])
   → return true

5. Booking created: Alice | Inception | [B1,B2] | $500 | CONFIRMED

6. Bob tries [B1(Gold)]:
   → Check B1: IN bookedSeats ✗
   → return false → "Seat already booked!"
```

---

## Part 5: Follow-Up Questions

| Question | Answer |
|---|---|
| Optimistic vs pessimistic locking? | Our code uses pessimistic (`synchronized`). Optimistic would use a version number and retry on conflict. |
| Temporary seat lock? | Lock seats for 10 minutes while user pays. Release if payment times out. Use a `ScheduledExecutor`. |
| Multiple theatres? | Add `Theatre` class with `List<Screen>`, each screen has shows. |
| Payment integration? | Separate `PaymentService` — don't mix with booking logic (SRP). |
| Seat selection UI? | Return `List<Seat>` with availability status. Frontend renders the seat map. |

---

## Part 6: Patterns Recap

| Pattern | Where & Why |
|---|---|
| **Concurrency** | `synchronized` prevents double booking |
| **Atomic Operations** | All-or-nothing seat booking |
| **Enum with Data** | SeatType holds price — type-safe pricing |
| **SRP** | Movie, Show, Seat, Booking each have one job |
| **equals/hashCode** | Seat identity for Set operations |

---

📁 **Source code:** `src/bookmyshow/`
