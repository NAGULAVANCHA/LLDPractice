package figma;

public class AddShapeCommand implements Command {
    private final Canvas canvas;
    private final Shape shape;

    public AddShapeCommand(Canvas canvas, Shape shape) {
        this.canvas = canvas;
        this.shape = shape;
    }

    @Override
    public void execute() { canvas.addShapeDirect(shape); }

    @Override
    public void undo() { canvas.removeShapeDirect(shape); }

    @Override
    public String describe() { return "Add " + shape; }
}

