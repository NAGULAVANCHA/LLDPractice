package taskscheduler;

public class TaskSchedulerDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Task Scheduler / Thread Pool Demo ===\n");

        // --- Demo 1: Basic FIFO Thread Pool ---
        System.out.println("--- Demo 1: FIFO Thread Pool (3 workers, 6 tasks) ---\n");
        ThreadPool fifoPool = new ThreadPool(3);

        for (int i = 1; i <= 6; i++) {
            final int taskNum = i;
            fifoPool.submit("Task-" + taskNum, () -> {
                try {
                    Thread.sleep(500 + (int)(Math.random() * 500));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        Thread.sleep(4000);
        fifoPool.shutdown();

        // --- Demo 2: Priority Thread Pool ---
        System.out.println("\n--- Demo 2: Priority Thread Pool ---\n");
        ThreadPool priorityPool = new ThreadPool(2, true);

        // Submit low-priority tasks first, then high-priority
        priorityPool.submit(new SimpleTask("LowPri-A", () -> {
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }, 1));
        priorityPool.submit(new SimpleTask("LowPri-B", () -> {
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }, 1));
        priorityPool.submit(new SimpleTask("HighPri-X", () -> {
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }, 10));
        priorityPool.submit(new SimpleTask("MedPri-Y", () -> {
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
        }, 5));

        Thread.sleep(3000);
        priorityPool.shutdown();

        // --- Demo 3: Scheduled Tasks ---
        System.out.println("\n--- Demo 3: Scheduled / Delayed Tasks ---\n");
        ScheduledThreadPool scheduledPool = new ScheduledThreadPool(2);

        scheduledPool.submit(new SimpleTask("Immediate-1", () -> {
            System.out.println("    → Immediate task done!");
        }));

        scheduledPool.schedule(new SimpleTask("Delayed-2sec", () -> {
            System.out.println("    → I ran after 2 seconds!");
        }), 2000);

        scheduledPool.scheduleAtFixedRate("Heartbeat", () -> {
            System.out.println("    → ♥ heartbeat");
        }, 1000, 1500);

        Thread.sleep(6000);
        scheduledPool.shutdown();

        System.out.println("\n--- Rejected task after shutdown ---");
        fifoPool.submit("TooLate", () -> System.out.println("Should not run"));
    }
}

