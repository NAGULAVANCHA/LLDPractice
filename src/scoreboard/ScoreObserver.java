package scoreboard;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/** Observer interface: notified on score changes. */
public interface ScoreObserver {
    void onScoreUpdate(Match match, String event);
}


