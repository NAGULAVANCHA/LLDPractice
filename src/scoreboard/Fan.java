package scoreboard;

/** A fan who subscribes to match updates. */
public class Fan implements ScoreObserver {
    private final String name;

    public Fan(String name) { this.name = name; }

    @Override
    public void onScoreUpdate(Match match, String event) {
        System.out.println("    📱 [" + name + "] " + event);
    }

    public String getName() { return name; }
}

