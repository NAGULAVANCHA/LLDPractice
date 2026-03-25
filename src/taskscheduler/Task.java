package taskscheduler;

/**
 * TASK SCHEDULER / THREAD POOL LLD
 * ==================================
 * Key Concepts:
 *  - Producer-Consumer:  Tasks submitted by caller, executed by worker threads
 *  - Thread Pool:        Fixed number of reusable threads
 *  - BlockingQueue:      Thread-safe task queue
 *  - Future/Callable:    Return results from async tasks
 *  - Graceful Shutdown:  Stop accepting tasks, finish existing ones
 *
 * Patterns:
 *  - Strategy: Different scheduling policies (FIFO, Priority, Delayed)
 *  - Observer: Notify on task completion
 *  - Command: Task wraps a unit of work
 */
public interface Task {
    String getName();
    void execute();
    int getPriority(); // higher = more urgent
}

