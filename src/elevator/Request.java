package elevator;

public class Request {
    private final int fromFloor;
    private final int toFloor;
    private final Direction direction;
    private final long timestamp;

    public Request(int fromFloor, int toFloor) {
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.direction = toFloor > fromFloor ? Direction.UP : Direction.DOWN;
        this.timestamp = System.currentTimeMillis();
    }

    // External request (hallway button — only direction, no destination yet)
    public Request(int floor, Direction direction) {
        this.fromFloor = floor;
        this.toFloor = -1; // unknown until passenger enters
        this.direction = direction;
        this.timestamp = System.currentTimeMillis();
    }

    public int getFromFloor() { return fromFloor; }
    public int getToFloor() { return toFloor; }
    public Direction getDirection() { return direction; }
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        if (toFloor == -1) return "ExternalReq(floor=" + fromFloor + ", dir=" + direction + ")";
        return "Req(" + fromFloor + " -> " + toFloor + ")";
    }
}

