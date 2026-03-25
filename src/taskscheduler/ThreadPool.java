package taskscheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Custom Thread Pool — manages a fixed set of worker threads
 * and a task queue. Tasks are submitted and executed asynchronously.
 */
public class ThreadPool {
    private final int poolSize;
    private final List<Worker> workers;
    private final BlockingQueue<Task> taskQueue;
    private volatile boolean isShutdown = false;

    /**
     * FIFO thread pool — tasks executed in submission order.
     */
    public ThreadPool(int poolSize) {
        this(poolSize, false);
    }

    /**
     * @param priorityMode if true, higher-priority tasks execute first
     */
    public ThreadPool(int poolSize, boolean priorityMode) {
        this.poolSize = poolSize;
        if (priorityMode) {
            // PriorityBlockingQueue: higher priority = lower comparator value = executed first
            this.taskQueue = new PriorityBlockingQueue<>(11,
                    (a, b) -> Integer.compare(b.getPriority(), a.getPriority()));
        } else {
            this.taskQueue = new LinkedBlockingQueue<>();
        }
        this.workers = new ArrayList<>();

        // Create and start worker threads
        for (int i = 1; i <= poolSize; i++) {
            Worker worker = new Worker("Worker-" + i, this);
            workers.add(worker);
            worker.start();
        }
        System.out.println("ThreadPool started with " + poolSize + " workers" +
                (priorityMode ? " (priority mode)" : " (FIFO mode)"));
    }

    /**
     * Submit a task to the pool.
     */
    public void submit(Task task) {
        if (isShutdown) {
            System.out.println("  ❌ Pool is shut down, rejecting: " + task.getName());
            return;
        }
        taskQueue.offer(task);
        System.out.println("  Submitted: " + task);
    }

    /**
     * Submit a Runnable as a task.
     */
    public void submit(String name, Runnable work) {
        submit(new SimpleTask(name, work));
    }

    /**
     * Called by workers to get the next task. Blocks if queue is empty.
     */
    Task takeTask() throws InterruptedException {
        return taskQueue.poll(500, TimeUnit.MILLISECONDS);
    }

    /**
     * Graceful shutdown: stop accepting new tasks, wait for existing ones.
     */
    public void shutdown() {
        System.out.println("\n--- Shutting down thread pool ---");
        isShutdown = true;

        // Wait for queue to drain
        while (!taskQueue.isEmpty()) {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
        }

        // Give workers time to finish current task
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        for (Worker w : workers) {
            w.shutdown();
        }

        // Wait for all workers to finish
        for (Worker w : workers) {
            try { w.join(2000); } catch (InterruptedException ignored) {}
        }
        System.out.println("Thread pool shut down. Pending tasks: " + taskQueue.size());
    }

    public int getQueueSize() { return taskQueue.size(); }
    public int getPoolSize() { return poolSize; }
    public boolean isShutdown() { return isShutdown; }
}

