package ridesharing;

import java.util.*;

/**
 * Main service — manages riders, drivers, and rides.
 */
public class RideService {
    private final List<Rider> riders = new ArrayList<>();
    private final List<Driver> drivers = new ArrayList<>();
    private final List<Ride> rides = new ArrayList<>();
    private MatchingStrategy matchingStrategy;

    public RideService(MatchingStrategy strategy) {
        this.matchingStrategy = strategy;
    }

    public void setMatchingStrategy(MatchingStrategy strategy) {
        this.matchingStrategy = strategy;
        System.out.println("  Matching strategy: " + strategy.getName());
    }

    public void addRider(Rider rider) { riders.add(rider); }
    public void addDriver(Driver driver) { drivers.add(driver); }

    /**
     * Request a ride: find a driver using the matching strategy.
     */
    public synchronized Ride requestRide(Rider rider, Location pickup, Location destination) {
        System.out.println("\n📱 " + rider.getName() + " requests ride: " + pickup + " → " + destination);

        Driver driver = matchingStrategy.findDriver(this, rider);
        if (driver == null) {
            System.out.println("  ❌ No drivers available!");
            return null;
        }

        Ride ride = new Ride(rider, pickup, destination);
        ride.assignDriver(driver);
        rides.add(ride);
        return ride;
    }

    public void displayDrivers() {
        System.out.println("\n=== All Drivers ===");
        for (Driver d : drivers) System.out.println("  " + d);
    }

    public void displayRideHistory() {
        System.out.println("\n=== Ride History ===");
        for (Ride r : rides) System.out.println("  " + r);
    }

    public List<Driver> getDrivers() { return drivers; }
    public List<Rider> getRiders() { return riders; }
}

