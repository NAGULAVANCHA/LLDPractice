package bookmyshow;

/**
 * BOOKMYSHOW / MOVIE TICKET BOOKING LLD
 * =======================================
 * Key Concepts:
 *  - Concurrency:    Synchronized seat locking to prevent double booking
 *  - SRP:            Movie, Theatre, Screen, Show, Seat, Booking all separate
 *  - OCP:            Different seat types (SILVER, GOLD, PLATINUM) with pricing
 *  - Builder:        Booking with multiple optional fields
 *
 * Key Interview Points:
 *  - How to prevent double booking? (synchronized + seat locking)
 *  - How to handle concurrent users selecting same seat? (optimistic locking or synchronized)
 *  - Seat map per show (not per screen — same screen, different shows have different availability)
 *  - Payment is a separate concern (mention but don't deep-dive)
 */
public enum SeatType {
    SILVER(150.0),
    GOLD(250.0),
    PLATINUM(400.0);

    private final double price;

    SeatType(double price) { this.price = price; }

    public double getPrice() { return price; }
}

