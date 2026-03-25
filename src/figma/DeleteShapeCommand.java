package figma;

public class DeleteShapeCommand implements Command {
    private final Canvas canvas;
    private final Shape shape;

    public DeleteShapeCommand(Canvas canvas, Shape shape) {
        this.canvas = canvas;
        this.shape = shape;
    }

    @Override
    public void execute() { canvas.removeShapeDirect(shape); }

    @Override
    public void undo() { canvas.addShapeDirect(shape); }

    @Override
    public String describe() { return "Delete " + shape; }
}

