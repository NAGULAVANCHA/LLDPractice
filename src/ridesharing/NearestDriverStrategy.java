package ridesharing;

/**
 * Match rider with the NEAREST available driver.
 */
public class NearestDriverStrategy implements MatchingStrategy {
    @Override
    public Driver findDriver(RideService service, Rider rider) {
        Driver best = null;
        double bestDist = Double.MAX_VALUE;

        for (Driver d : service.getDrivers()) {
            if (!d.isAvailable()) continue;
            double dist = d.getCurrentLocation().distanceTo(rider.getCurrentLocation());
            if (dist < bestDist) {
                bestDist = dist;
                best = d;
            }
        }
        return best;
    }

    @Override
    public String getName() { return "Nearest Driver"; }
}

