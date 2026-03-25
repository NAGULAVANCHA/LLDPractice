package elevator;

public class ElevatorDemo {
    public static void main(String[] args) {
        System.out.println("=== Elevator System Demo ===\n");

        ElevatorController controller = new ElevatorController(2, 0, 10);

        // Requests
        controller.handleRequest(new Request(0, 5));   // Ground to 5th
        controller.handleRequest(new Request(3, 7));   // 3rd to 7th
        controller.handleRequest(new Request(8, 1));   // 8th to 1st
        controller.handleRequest(new Request(6, 2));   // 6th to 2nd

        controller.displayStatus();

        // Simulate steps
        System.out.println("\n--- Processing Steps ---");
        for (int i = 0; i < 8; i++) {
            System.out.println("Step " + (i + 1) + ":");
            controller.stepAll();
        }

        controller.displayStatus();

        // New request while elevators are running
        System.out.println("\n--- New request while moving ---");
        controller.handleRequest(new Request(4, 9));
        controller.displayStatus();

        for (int i = 0; i < 5; i++) {
            System.out.println("Step " + (i + 9) + ":");
            controller.stepAll();
        }
        controller.displayStatus();
    }
}

