package scoreboard;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A live match with scores, events, and observer notifications.
 * Uses CopyOnWriteArrayList for thread-safe observer iteration (read-heavy).
 */
public class Match {
    private final String matchId;
    private final String teamA;
    private final String teamB;
    private int scoreA;
    private int scoreB;
    private MatchStatus status;
    private final List<String> eventLog = new ArrayList<>();
    private final List<ScoreObserver> observers = new CopyOnWriteArrayList<>();

    public Match(String matchId, String teamA, String teamB) {
        this.matchId = matchId;
        this.teamA = teamA;
        this.teamB = teamB;
        this.scoreA = 0;
        this.scoreB = 0;
        this.status = MatchStatus.UPCOMING;
    }

    public void subscribe(ScoreObserver observer) { observers.add(observer); }
    public void unsubscribe(ScoreObserver observer) { observers.remove(observer); }

    public synchronized void startMatch() {
        this.status = MatchStatus.LIVE;
        String event = "🏟️ " + teamA + " vs " + teamB + " — KICK OFF!";
        eventLog.add(event);
        notifyObservers(event);
    }

    public synchronized void goal(String team, String scorer, int minute) {
        if (team.equals(teamA)) scoreA++;
        else scoreB++;
        String event = "⚽ GOAL! " + scorer + " (" + team + ") " + minute + "' — " +
                teamA + " " + scoreA + " - " + scoreB + " " + teamB;
        eventLog.add(event);
        notifyObservers(event);
    }

    public synchronized void halfTime() {
        this.status = MatchStatus.HALF_TIME;
        String event = "⏸️ HALF TIME: " + teamA + " " + scoreA + " - " + scoreB + " " + teamB;
        eventLog.add(event);
        notifyObservers(event);
    }

    public synchronized void endMatch() {
        this.status = MatchStatus.COMPLETED;
        String winner = scoreA > scoreB ? teamA : scoreB > scoreA ? teamB : "DRAW";
        String event = "🏁 FULL TIME: " + teamA + " " + scoreA + " - " + scoreB + " " + teamB +
                " | " + (winner.equals("DRAW") ? "It's a draw!" : winner + " wins!");
        eventLog.add(event);
        notifyObservers(event);
    }

    public synchronized void addEvent(String event) {
        eventLog.add(event);
        notifyObservers(event);
    }

    private void notifyObservers(String event) {
        for (ScoreObserver obs : observers) obs.onScoreUpdate(this, event);
    }

    // Read-only snapshot for read-heavy access — no locking needed for reads
    public String getMatchId() { return matchId; }
    public String getTeamA() { return teamA; }
    public String getTeamB() { return teamB; }
    public int getScoreA() { return scoreA; }
    public int getScoreB() { return scoreB; }
    public MatchStatus getStatus() { return status; }
    public List<String> getEventLog() { return Collections.unmodifiableList(eventLog); }

    @Override
    public String toString() {
        return matchId + ": " + teamA + " " + scoreA + " - " + scoreB + " " + teamB + " [" + status + "]";
    }
}

