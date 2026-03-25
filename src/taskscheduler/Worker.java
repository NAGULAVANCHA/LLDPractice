package taskscheduler;

/**
 * Worker thread — pulls tasks from the pool's queue and executes them.
 * Runs in a loop until shutdown is signaled.
 */
public class Worker extends Thread {
    private final ThreadPool pool;
    private volatile boolean running = true;

    public Worker(String name, ThreadPool pool) {
        super(name);
        this.pool = pool;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Task task = pool.takeTask(); // blocks until a task is available
                if (task == null) break;     // poison pill or shutdown
                System.out.println("  [" + getName() + "] Executing: " + task.getName());
                long start = System.currentTimeMillis();
                task.execute();
                long elapsed = System.currentTimeMillis() - start;
                System.out.println("  [" + getName() + "] Completed: " + task.getName() +
                        " (" + elapsed + "ms)");
            } catch (InterruptedException e) {
                if (!running) break;
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.out.println("  [" + getName() + "] Task failed: " + e.getMessage());
            }
        }
        System.out.println("  [" + getName() + "] Stopped.");
    }

    public void shutdown() {
        running = false;
        this.interrupt();
    }
}

