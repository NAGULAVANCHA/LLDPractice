package ridesharing;

/**
 * Represents a single ride from pickup to destination.
 */
public class Ride {
    public enum RideStatus {
        REQUESTED, DRIVER_ASSIGNED, EN_ROUTE, IN_PROGRESS, COMPLETED, CANCELLED
    }

    private static int counter = 0;
    private final int rideId;
    private final Rider rider;
    private Driver driver;
    private final Location pickup;
    private final Location destination;
    private RideStatus status;
    private double fare;
    private static final double RATE_PER_KM = 10.0; // $/km
    private static final double BASE_FARE = 5.0;

    public Ride(Rider rider, Location pickup, Location destination) {
        this.rideId = ++counter;
        this.rider = rider;
        this.pickup = pickup;
        this.destination = destination;
        this.status = RideStatus.REQUESTED;
        this.fare = 0;
    }

    public void assignDriver(Driver driver) {
        this.driver = driver;
        this.status = RideStatus.DRIVER_ASSIGNED;
        driver.setStatus(DriverStatus.EN_ROUTE);
        System.out.println("  ✅ Driver " + driver.getName() + " assigned to Ride#" + rideId);
    }

    public void startTrip() {
        this.status = RideStatus.IN_PROGRESS;
        driver.setStatus(DriverStatus.ON_TRIP);
        driver.setCurrentLocation(pickup); // driver arrives at pickup
        System.out.println("  🚗 Ride#" + rideId + " started: " + pickup + " → " + destination);
    }

    public void completeTrip() {
        double distance = pickup.distanceTo(destination);
        this.fare = BASE_FARE + distance * RATE_PER_KM;
        this.status = RideStatus.COMPLETED;
        driver.setCurrentLocation(destination); // driver is now at destination
        driver.setStatus(DriverStatus.AVAILABLE);
        System.out.println("  🏁 Ride#" + rideId + " completed! Distance: " +
                String.format("%.1f km", distance) + " | Fare: $" + String.format("%.2f", fare));
    }

    public void cancel() {
        this.status = RideStatus.CANCELLED;
        if (driver != null) {
            driver.setStatus(DriverStatus.AVAILABLE);
        }
        System.out.println("  ❌ Ride#" + rideId + " cancelled.");
    }

    public int getRideId() { return rideId; }
    public Rider getRider() { return rider; }
    public Driver getDriver() { return driver; }
    public RideStatus getStatus() { return status; }
    public double getFare() { return fare; }
    public Location getPickup() { return pickup; }
    public Location getDestination() { return destination; }

    @Override
    public String toString() {
        return "Ride#" + rideId + " [" + status + "] " +
                rider.getName() + " → " + (driver != null ? driver.getName() : "unassigned") +
                " | " + pickup + " → " + destination +
                (fare > 0 ? " | $" + String.format("%.2f", fare) : "");
    }
}

