package bookmyshow;

import java.util.*;

/**
 * Show = a specific screening of a movie at a time.
 * Each show has its OWN seat availability (not shared with other shows on same screen).
 * Synchronized methods prevent double booking.
 */
public class Show {
    private final String showId;
    private final Movie movie;
    private final String screenName;
    private final String time;
    private final List<Seat> allSeats;
    private final Set<Seat> bookedSeats;

    public Show(String showId, Movie movie, String screenName, String time, List<Seat> seats) {
        this.showId = showId;
        this.movie = movie;
        this.screenName = screenName;
        this.time = time;
        this.allSeats = new ArrayList<>(seats);
        this.bookedSeats = new HashSet<>();
    }

    public synchronized List<Seat> getAvailableSeats() {
        List<Seat> available = new ArrayList<>();
        for (Seat s : allSeats) {
            if (!bookedSeats.contains(s)) available.add(s);
        }
        return available;
    }

    /**
     * Try to book specific seats. Returns true if all seats were available.
     * ATOMIC: either all seats are booked or none.
     */
    public synchronized boolean bookSeats(List<Seat> seats) {
        // Check all seats are available
        for (Seat s : seats) {
            if (bookedSeats.contains(s)) {
                System.out.println("  ❌ Seat " + s.getSeatId() + " already booked!");
                return false;
            }
        }
        // Book all
        bookedSeats.addAll(seats);
        return true;
    }

    public synchronized void cancelSeats(List<Seat> seats) {
        bookedSeats.removeAll(seats);
    }

    public String getShowId() { return showId; }
    public Movie getMovie() { return movie; }
    public String getScreenName() { return screenName; }
    public String getTime() { return time; }

    @Override
    public String toString() {
        return movie.getTitle() + " | " + screenName + " | " + time +
                " | Available: " + getAvailableSeats().size() + "/" + allSeats.size();
    }
}

