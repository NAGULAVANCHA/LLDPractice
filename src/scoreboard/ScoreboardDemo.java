package scoreboard;

public class ScoreboardDemo {
    public static void main(String[] args) {
        System.out.println("=== Live Sports Scoreboard Demo ===\n");

        Scoreboard board = new Scoreboard();
        Match match = board.createMatch("M1", "India", "Australia");

        Fan alice = new Fan("Alice");
        Fan bob = new Fan("Bob");
        match.subscribe(alice);
        match.subscribe(bob);

        match.startMatch();
        match.goal("India", "Kohli", 12);
        match.goal("Australia", "Smith", 25);

        // Bob leaves
        System.out.println("\n  [Bob unsubscribes]\n");
        match.unsubscribe(bob);

        match.halfTime();
        match.goal("India", "Sharma", 67);
        match.goal("India", "Pandya", 82);
        match.endMatch();

        board.displayAll();

        System.out.println("\n--- Event Log ---");
        for (String event : match.getEventLog()) System.out.println("  " + event);
    }
}

