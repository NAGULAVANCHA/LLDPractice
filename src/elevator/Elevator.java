package elevator;

import java.util.TreeSet;

/**
 * Single Elevator using the SCAN algorithm.
 * - Maintains two sorted sets: upStops and downStops
 * - Moves in current direction, servicing stops
 * - Reverses when no more stops in current direction
 */
public class Elevator {
    private final int id;
    private int currentFloor;
    private Direction direction;
    private final int minFloor;
    private final int maxFloor;

    // Stops to service in each direction (sorted)
    private final TreeSet<Integer> upStops;
    private final TreeSet<Integer> downStops;

    public Elevator(int id, int minFloor, int maxFloor) {
        this.id = id;
        this.currentFloor = minFloor;
        this.direction = Direction.IDLE;
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        this.upStops = new TreeSet<>();
        this.downStops = new TreeSet<>();
    }

    public synchronized void addRequest(Request request) {
        int destination = request.getToFloor() != -1 ? request.getToFloor() : request.getFromFloor();

        if (destination > currentFloor) {
            upStops.add(destination);
        } else if (destination < currentFloor) {
            downStops.add(destination);
        }
        // Also add pickup floor if it's an external request
        if (request.getFromFloor() != currentFloor) {
            if (request.getFromFloor() > currentFloor) {
                upStops.add(request.getFromFloor());
            } else {
                downStops.add(request.getFromFloor());
            }
        }

        if (direction == Direction.IDLE) {
            direction = destination >= currentFloor ? Direction.UP : Direction.DOWN;
        }
    }

    /**
     * Process one step: move to the next stop.
     */
    public synchronized void step() {
        if (direction == Direction.UP) {
            if (!upStops.isEmpty()) {
                Integer nextStop = upStops.first();
                moveTo(nextStop);
                upStops.remove(nextStop);
            } else if (!downStops.isEmpty()) {
                direction = Direction.DOWN;
                step(); // reverse and process
            } else {
                direction = Direction.IDLE;
            }
        } else if (direction == Direction.DOWN) {
            if (!downStops.isEmpty()) {
                Integer nextStop = downStops.last();
                moveTo(nextStop);
                downStops.remove(nextStop);
            } else if (!upStops.isEmpty()) {
                direction = Direction.UP;
                step();
            } else {
                direction = Direction.IDLE;
            }
        }
    }

    private void moveTo(int floor) {
        System.out.println("  Elevator-" + id + ": Floor " + currentFloor +
                " -> Floor " + floor + " [" + direction + "]");
        currentFloor = floor;
    }

    public boolean isIdle() { return direction == Direction.IDLE; }
    public int getCurrentFloor() { return currentFloor; }
    public int getId() { return id; }
    public Direction getDirection() { return direction; }

    public int pendingStops() {
        return upStops.size() + downStops.size();
    }

    public void displayStatus() {
        System.out.println("  Elevator-" + id + " | Floor: " + currentFloor +
                " | Dir: " + direction +
                " | Up stops: " + upStops +
                " | Down stops: " + downStops);
    }
}

