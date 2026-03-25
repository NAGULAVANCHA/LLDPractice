package ridesharing;

/**
 * Strategy Pattern: Different ways to match a rider with a driver.
 */
public interface MatchingStrategy {
    Driver findDriver(RideService service, Rider rider);
    String getName();
}

