package figma;

public class FigmaDemo {
    public static void main(String[] args) {
        System.out.println("=== Collaborative Design Tool (Figma-like) Demo ===\n");

        // Create canvas and session
        Canvas canvas = new Canvas("My Design");
        CollaborationSession session = new CollaborationSession(canvas);

        // Users join
        DesignUser alice = new DesignUser("Alice");
        DesignUser bob = new DesignUser("Bob");
        session.join(alice);
        session.join(bob);

        // Alice adds shapes
        System.out.println("\n--- Alice adds shapes ---");
        Shape rect = new Rectangle(10, 10, 100, 50, "blue");
        Shape circle = new Circle(200, 100, 30, "red");
        Shape text = new TextBox(50, 200, 150, 30, "black", "Hello Figma!");
        canvas.addShape(rect);
        canvas.addShape(circle);
        canvas.addShape(text);
        session.broadcast("Alice added 3 shapes");
        canvas.display();

        // Bob moves a shape
        System.out.println("\n--- Bob moves rectangle ---");
        canvas.moveShape(rect, 50, 20);
        session.broadcast("Bob moved rectangle");
        canvas.display();

        // Undo the move
        System.out.println("\n--- Undo move ---");
        canvas.undo();
        canvas.display();

        // Redo the move
        System.out.println("\n--- Redo move ---");
        canvas.redo();
        canvas.display();

        // Group shapes
        System.out.println("\n--- Group rect + circle ---");
        Group group = new Group(0, 0);
        group.addChild(rect);
        group.addChild(circle);
        System.out.println("Created: " + group);

        // Move entire group
        System.out.println("\n--- Move group by (10, 10) ---");
        canvas.moveShape(group, 10, 10);
        System.out.println("After group move: " + group);

        // Delete a shape
        System.out.println("\n--- Delete text ---");
        canvas.deleteShape(text);
        canvas.display();

        // Undo delete
        System.out.println("\n--- Undo delete ---");
        canvas.undo();
        canvas.display();

        // Bob leaves
        session.leave(bob);
    }
}

