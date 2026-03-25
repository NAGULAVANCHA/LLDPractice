package scoreboard;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages all live matches. Read-heavy optimized with ConcurrentHashMap.
 * Thousands of readers (fans) vs few writers (score updaters).
 */
public class Scoreboard {
    private final Map<String, Match> matches = new ConcurrentHashMap<>();

    public Match createMatch(String matchId, String teamA, String teamB) {
        Match match = new Match(matchId, teamA, teamB);
        matches.put(matchId, match);
        System.out.println("📋 Match created: " + match);
        return match;
    }

    public Match getMatch(String matchId) { return matches.get(matchId); }

    public List<Match> getLiveMatches() {
        List<Match> live = new ArrayList<>();
        for (Match m : matches.values())
            if (m.getStatus() == MatchStatus.LIVE || m.getStatus() == MatchStatus.HALF_TIME)
                live.add(m);
        return live;
    }

    public void displayAll() {
        System.out.println("\n=== Scoreboard ===");
        for (Match m : matches.values()) System.out.println("  " + m);
    }
}

