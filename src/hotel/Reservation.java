package hotel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {
    public enum Status { CONFIRMED, CANCELLED, CHECKED_IN, CHECKED_OUT }

    private static int counter = 0;
    private final int reservationId;
    private final String guestName;
    private final Room room;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final double totalAmount;
    private Status status;

    public Reservation(String guestName, Room room, LocalDate checkIn, LocalDate checkOut) {
        this.reservationId = ++counter;
        this.guestName = guestName;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        this.totalAmount = nights * room.getPricePerNight();
        this.status = Status.CONFIRMED;
    }

    public void cancel() {
        this.status = Status.CANCELLED;
    }

    public int getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public Room getRoom() { return room; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public double getTotalAmount() { return totalAmount; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return "Reservation#" + reservationId + " | " + guestName + " | " + room +
                " | " + checkIn + " to " + checkOut +
                " | $" + String.format("%.2f", totalAmount) + " | " + status;
    }
}

