package taskscheduler;

public class SimpleTask implements Task {
    private final String name;
    private final Runnable work;
    private final int priority;

    public SimpleTask(String name, Runnable work) {
        this(name, work, 0);
    }

    public SimpleTask(String name, Runnable work, int priority) {
        this.name = name;
        this.work = work;
        this.priority = priority;
    }

    @Override
    public String getName() { return name; }

    @Override
    public void execute() { work.run(); }

    @Override
    public int getPriority() { return priority; }

    @Override
    public String toString() { return "Task[" + name + ", priority=" + priority + "]"; }
}

