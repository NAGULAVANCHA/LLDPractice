package ridesharing;

/**
 * RIDE-SHARING SERVICE (Uber/Ola) LLD
 * ======================================
 * Key Concepts:
 *  - State Pattern:    Driver states (AVAILABLE → EN_ROUTE → ON_TRIP → AVAILABLE)
 *  - Strategy Pattern: Matching algorithm (nearest driver, rating-based, etc.)
 *  - Observer Pattern: Notify rider/driver of status changes
 *  - SRP:              Rider, Driver, Ride, Location, RideService separate
 *
 * Interview Points:
 *  - Geospatial matching (simplified as distance calculation)
 *  - Fare calculation (distance × rate per km)
 *  - Driver state management
 *  - Concurrent ride requests
 */
public class Location {
    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Simplified distance: Euclidean distance (in real world, use Haversine formula).
     */
    public double distanceTo(Location other) {
        double dx = this.latitude - other.latitude;
        double dy = this.longitude - other.longitude;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    @Override
    public String toString() {
        return String.format("(%.1f, %.1f)", latitude, longitude);
    }
}

