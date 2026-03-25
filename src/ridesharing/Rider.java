package ridesharing;

public class Rider {
    private final String riderId;
    private final String name;
    private Location currentLocation;
    private double rating;
    private int totalRides;

    public Rider(String riderId, String name, Location location) {
        this.riderId = riderId;
        this.name = name;
        this.currentLocation = location;
        this.rating = 5.0;
        this.totalRides = 0;
    }

    public void addRating(double r) {
        rating = (rating * totalRides + r) / (totalRides + 1);
        totalRides++;
    }

    public String getRiderId() { return riderId; }
    public String getName() { return name; }
    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location loc) { this.currentLocation = loc; }
    public double getRating() { return rating; }

    @Override
    public String toString() { return name + " ★" + String.format("%.1f", rating); }
}

