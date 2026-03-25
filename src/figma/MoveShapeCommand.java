package figma;

public class MoveShapeCommand implements Command {
    private final Shape shape;
    private final double dx, dy;

    public MoveShapeCommand(Shape shape, double dx, double dy) {
        this.shape = shape;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void execute() { shape.move(dx, dy); }

    @Override
    public void undo() { shape.move(-dx, -dy); }

    @Override
    public String describe() { return "Move " + shape.getType() + "#" + shape.getId() + " by (" + dx + "," + dy + ")"; }
}

