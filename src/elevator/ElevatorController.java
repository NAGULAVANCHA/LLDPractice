package elevator;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages multiple elevators and dispatches requests
 * to the most suitable elevator.
 *
 * Dispatch Strategy: Assign to the nearest idle elevator,
 * or the elevator already moving in the right direction.
 */
public class ElevatorController {
    private final List<Elevator> elevators;

    public ElevatorController(int numElevators, int minFloor, int maxFloor) {
        elevators = new ArrayList<>();
        for (int i = 1; i <= numElevators; i++) {
            elevators.add(new Elevator(i, minFloor, maxFloor));
        }
    }

    public void handleRequest(Request request) {
        Elevator best = findBestElevator(request);
        System.out.println("Dispatching " + request + " to Elevator-" + best.getId());
        best.addRequest(request);
    }

    private Elevator findBestElevator(Request request) {
        Elevator best = null;
        int bestScore = Integer.MAX_VALUE;

        for (Elevator e : elevators) {
            int score = calculateScore(e, request);
            if (score < bestScore) {
                bestScore = score;
                best = e;
            }
        }
        return best;
    }

    /**
     * Scoring heuristic (lower = better):
     * - Idle elevator: distance to request floor
     * - Moving towards request: distance (preferred)
     * - Moving away: large penalty
     */
    private int calculateScore(Elevator e, Request request) {
        int distance = Math.abs(e.getCurrentFloor() - request.getFromFloor());

        if (e.isIdle()) {
            return distance;
        }

        boolean movingTowards =
                (e.getDirection() == Direction.UP && request.getFromFloor() >= e.getCurrentFloor()) ||
                (e.getDirection() == Direction.DOWN && request.getFromFloor() <= e.getCurrentFloor());

        if (movingTowards) {
            return distance; // good — on the way
        }
        return distance + 1000; // penalty — moving away
    }

    /**
     * Process one step for all elevators.
     */
    public void stepAll() {
        for (Elevator e : elevators) {
            e.step();
        }
    }

    public void displayStatus() {
        System.out.println("=== Elevator System Status ===");
        for (Elevator e : elevators) {
            e.displayStatus();
        }
        System.out.println("==============================");
    }

    public List<Elevator> getElevators() { return elevators; }
}

