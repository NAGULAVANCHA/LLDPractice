package ridesharing;

public class RideSharingDemo {
    public static void main(String[] args) {
        System.out.println("=== Ride-Sharing Service (Uber-like) Demo ===\n");

        RideService service = new RideService(new NearestDriverStrategy());

        // Register drivers at different locations
        Driver alice = new Driver("d1", "Alice", "Toyota Camry", new Location(10, 10));
        Driver bob   = new Driver("d2", "Bob", "Honda Civic", new Location(20, 20));
        Driver charlie = new Driver("d3", "Charlie", "Tesla Model 3", new Location(5, 5));
        service.addDriver(alice);
        service.addDriver(bob);
        service.addDriver(charlie);

        // Register riders
        Rider john = new Rider("r1", "John", new Location(8, 8));
        Rider mary = new Rider("r2", "Mary", new Location(18, 18));
        service.addRider(john);
        service.addRider(mary);

        service.displayDrivers();

        // --- Ride 1: John requests ride → nearest is Charlie (5,5) ---
        Ride ride1 = service.requestRide(john,
                new Location(8, 8), new Location(25, 25));
        ride1.startTrip();
        ride1.completeTrip();
        // Charlie rates John, John rates Charlie
        charlie.addRating(4.5);
        john.addRating(5.0);

        // --- Ride 2: Mary requests ride → nearest available ---
        // Charlie is now at (25,25) and available, Alice at (10,10), Bob at (20,20)
        Ride ride2 = service.requestRide(mary,
                new Location(18, 18), new Location(30, 30));
        ride2.startTrip();
        ride2.completeTrip();

        // --- Ride 3: Switch to highest-rated strategy ---
        System.out.println("\n--- Switching to Highest Rated strategy ---");
        service.setMatchingStrategy(new HighestRatedDriverStrategy(50));
        bob.addRating(4.9);  // Bob has highest rating now

        Ride ride3 = service.requestRide(john,
                new Location(10, 10), new Location(15, 15));
        ride3.startTrip();
        ride3.completeTrip();

        // --- Ride 4: Cancel a ride ---
        System.out.println("\n--- Cancellation ---");
        Ride ride4 = service.requestRide(mary,
                new Location(20, 20), new Location(5, 5));
        ride4.cancel();

        // --- Final status ---
        service.displayDrivers();
        service.displayRideHistory();
    }
}

