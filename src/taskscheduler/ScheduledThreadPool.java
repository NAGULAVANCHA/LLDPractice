package taskscheduler;

import java.util.concurrent.*;

/**
 * Scheduled Thread Pool — supports delayed and recurring tasks.
 * Like a simplified ScheduledExecutorService / cron scheduler.
 */
public class ScheduledThreadPool {
    private final ThreadPool pool;
    private final ScheduledExecutorService scheduler;

    public ScheduledThreadPool(int poolSize) {
        this.pool = new ThreadPool(poolSize);
        this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "Scheduler");
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * Submit a task immediately.
     */
    public void submit(Task task) {
        pool.submit(task);
    }

    /**
     * Schedule a task to run after a delay.
     */
    public void schedule(Task task, long delayMs) {
        System.out.println("  Scheduled: " + task.getName() + " (delay=" + delayMs + "ms)");
        scheduler.schedule(() -> pool.submit(task), delayMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Schedule a recurring task at a fixed rate.
     */
    public void scheduleAtFixedRate(String name, Runnable work, long initialDelayMs, long periodMs) {
        System.out.println("  Recurring: " + name + " (period=" + periodMs + "ms)");
        scheduler.scheduleAtFixedRate(
                () -> pool.submit(new SimpleTask(name, work)),
                initialDelayMs, periodMs, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
        pool.shutdown();
    }
}

