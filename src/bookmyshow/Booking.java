package bookmyshow;

import java.util.List;

public class Booking {
    private static int counter = 0;
    private final int bookingId;
    private final String userName;
    private final Show show;
    private final List<Seat> seats;
    private final double totalAmount;
    private BookingStatus status;

    public enum BookingStatus { CONFIRMED, CANCELLED }

    public Booking(String userName, Show show, List<Seat> seats) {
        this.bookingId = ++counter;
        this.userName = userName;
        this.show = show;
        this.seats = seats;
        this.totalAmount = seats.stream().mapToDouble(Seat::getPrice).sum();
        this.status = BookingStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
        show.cancelSeats(seats);
    }

    public int getBookingId() { return bookingId; }
    public BookingStatus getStatus() { return status; }

    @Override
    public String toString() {
        return "Booking#" + bookingId + " | " + userName + " | " + show.getMovie().getTitle() +
                " | Seats: " + seats + " | $" + String.format("%.2f", totalAmount) +
                " | " + status;
    }
}

