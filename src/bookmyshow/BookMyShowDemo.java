package bookmyshow;

import java.util.*;

public class BookMyShowDemo {
    public static void main(String[] args) {
        System.out.println("=== BookMyShow Demo ===\n");

        Movie movie = new Movie("m1", "Inception", 148, "Sci-Fi");

        // Create seat layout: 2 rows Silver, 1 row Gold, 1 row Platinum
        List<Seat> seats = new ArrayList<>();
        for (int col = 0; col < 5; col++) {
            seats.add(new Seat(0, col, SeatType.SILVER));
            seats.add(new Seat(1, col, SeatType.SILVER));
            seats.add(new Seat(2, col, SeatType.GOLD));
            seats.add(new Seat(3, col, SeatType.PLATINUM));
        }

        Show show = new Show("s1", movie, "Screen-1", "7:00 PM", seats);
        System.out.println("Show: " + show);

        // User 1 books Gold seats
        System.out.println("\n--- Alice books 2 Gold seats ---");
        List<Seat> aliceSeats = List.of(new Seat(2, 0, SeatType.GOLD), new Seat(2, 1, SeatType.GOLD));
        if (show.bookSeats(aliceSeats)) {
            Booking b1 = new Booking("Alice", show, aliceSeats);
            System.out.println("  ✅ " + b1);
        }

        // User 2 tries to book same seat (should fail)
        System.out.println("\n--- Bob tries same Gold seat ---");
        List<Seat> bobSeats = List.of(new Seat(2, 0, SeatType.GOLD));
        if (show.bookSeats(bobSeats)) {
            Booking b2 = new Booking("Bob", show, bobSeats);
            System.out.println("  ✅ " + b2);
        } else {
            System.out.println("  Bob's booking FAILED (seat taken)");
        }

        // Bob books different seats
        System.out.println("\n--- Bob books Platinum seats ---");
        List<Seat> bobSeats2 = List.of(new Seat(3, 2, SeatType.PLATINUM), new Seat(3, 3, SeatType.PLATINUM));
        if (show.bookSeats(bobSeats2)) {
            Booking b3 = new Booking("Bob", show, bobSeats2);
            System.out.println("  ✅ " + b3);

            // Bob cancels
            System.out.println("\n--- Bob cancels ---");
            b3.cancel();
            System.out.println("  Cancelled: " + b3);
        }

        // Show available seats
        System.out.println("\n--- Available seats ---");
        System.out.println("  " + show);
        for (Seat s : show.getAvailableSeats()) {
            System.out.println("    " + s);
        }
    }
}

