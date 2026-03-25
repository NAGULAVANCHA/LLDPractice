# Problem 11: BookMyShow

| Pattern | Why |
|---|---|
| **Concurrency** | `synchronized` booking prevents double-booking of seats |
| **Atomic Operations** | Either ALL requested seats are booked, or NONE |
| **Enum** | SeatType (SILVER, GOLD, PLATINUM) and BookingStatus |

## Key Design Decisions

### Atomic Booking
```java
public synchronized boolean bookSeats(List<Seat> seats) {
    // Check ALL seats first
    for (Seat s : seats) {
        if (bookedSeats.contains(s)) return false;  // fail ALL
    }
    // Book ALL only if all available
    bookedSeats.addAll(seats);
    return true;
}
```
If you check-and-book one at a time, another thread could grab a seat between your check and book!

### Each Show Has Its Own Seats
- Same screen, different shows → different seat availability
- Show at 7 PM sells out ≠ Show at 9 PM sells out

### Seat Equality
Seats are identified by (row, col, type). Two `Seat` objects with the same row/col should be `.equals()`.

## Entities
```
Movie → has title, duration, genre
Show  → a screening: movie + screen + time + seats
Seat  → row, col, type (SILVER/GOLD/PLATINUM)
Booking → user + show + seats + status
```

📁 `src/bookmyshow/`

