package figma;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer Pattern: Users in a session see each other's changes.
 */
public class CollaborationSession {
    private final Canvas canvas;
    private final List<DesignUser> activeUsers;

    public CollaborationSession(Canvas canvas) {
        this.canvas = canvas;
        this.activeUsers = new ArrayList<>();
    }

    public void join(DesignUser user) {
        activeUsers.add(user);
        broadcast(user.getName() + " joined the session");
    }

    public void leave(DesignUser user) {
        activeUsers.remove(user);
        broadcast(user.getName() + " left the session");
    }

    public void broadcast(String message) {
        System.out.println("  [Session] " + message);
        for (DesignUser user : activeUsers) {
            System.out.println("    -> " + user.getName() + " received update");
        }
    }

    public Canvas getCanvas() { return canvas; }
    public List<DesignUser> getActiveUsers() { return activeUsers; }
}

