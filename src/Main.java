import java.util.Scanner;

/**
 * LLD Practice — Master Runner
 * Run any of the 20 LLD implementations from here.
 * Ranked by 2026 interview importance.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║     LLD Interview Practice Suite (2026 Ranked)      ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║  1.  LRU Cache          (HashMap + DLL)          ⭐ ║");
        System.out.println("║  2.  Rate Limiter       (4 algorithms)           ⭐ ║");
        System.out.println("║  3.  Parking Lot        (Singleton, Strategy)    ⭐ ║");
        System.out.println("║  4.  Ride-Sharing Uber  (Strategy, State)       🆕 ║");
        System.out.println("║  5.  Task Scheduler     (Thread Pool, Queues)   🆕 ║");
        System.out.println("║  6.  Pub-Sub Queue      (Producer/Consumer)      ⭐ ║");
        System.out.println("║  7.  Vending Machine    (State Pattern)            ║");
        System.out.println("║  8.  BookMyShow         (Concurrency, Booking)     ║");
        System.out.println("║  9.  Notification System (Observer Pattern)        ║");
        System.out.println("║  10. Splitwise          (Strategy, SRP)            ║");
        System.out.println("║  11. LFU Cache          (3 HashMaps)               ║");
        System.out.println("║  12. Elevator System    (Strategy, Scheduling)     ║");
        System.out.println("║  13. Logging Framework  (Chain of Responsibility)  ║");
        System.out.println("║  14. ATM Machine        (State, CoR)               ║");
        System.out.println("║  15. Hotel Booking      (Date ranges, Concurrency) ║");
        System.out.println("║  16. Shopping Cart      (Strategy, Pricing)        ║");
        System.out.println("║  17. Figma Design Tool  (Command, Composite)       ║");
        System.out.println("║  18. File System        (Composite Pattern)        ║");
        System.out.println("║  19. Tic-Tac-Toe        (O(1) win detection)       ║");
        System.out.println("║  20. Snake & Ladder     (OOP, Game loop)           ║");
        System.out.println("║  0.  Run ALL demos                                 ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.print("Choose (0-20): ");

        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        switch (choice) {
            case 1  -> lrucache.LRUCache.main(args);
            case 2  -> { try { ratelimiter.RateLimiterDemo.main(args); } catch (Exception e) { e.printStackTrace(); } }
            case 3  -> parkinglot.ParkingLotDemo.main(args);
            case 4  -> ridesharing.RideSharingDemo.main(args);
            case 5  -> { try { taskscheduler.TaskSchedulerDemo.main(args); } catch (Exception e) { e.printStackTrace(); } }
            case 6  -> pubsub.PubSubDemo.main(args);
            case 7  -> vendingmachine.VendingMachineDemo.main(args);
            case 8  -> bookmyshow.BookMyShowDemo.main(args);
            case 9  -> notification.NotificationService.main(args);
            case 10 -> splitwise.SplitwiseDemo.main(args);
            case 11 -> lfucache.LFUCache.main(args);
            case 12 -> elevator.ElevatorDemo.main(args);
            case 13 -> logger.Logger.main(args);
            case 14 -> atm.ATM.main(args);
            case 15 -> hotel.Hotel.main(args);
            case 16 -> shoppingcart.ShoppingCartDemo.main(args);
            case 17 -> figma.FigmaDemo.main(args);
            case 18 -> filesystem.Directory.main(args);
            case 19 -> tictactoe.TicTacToeDemo.main(args);
            case 20 -> snakeladder.SnakeLadderGame.main(args);
            case 0  -> {
                String[] demos = {
                    "LRU Cache", "Rate Limiter", "Parking Lot", "Ride-Sharing (Uber)",
                    "Task Scheduler", "Pub-Sub Queue", "Vending Machine", "BookMyShow",
                    "Notification", "Splitwise", "LFU Cache", "Elevator",
                    "Logger", "ATM", "Hotel", "Shopping Cart",
                    "Figma", "File System", "Tic-Tac-Toe", "Snake & Ladder"
                };
                Runnable[] runners = {
                    () -> lrucache.LRUCache.main(args),
                    () -> { try { ratelimiter.RateLimiterDemo.main(args); } catch (Exception e) { e.printStackTrace(); } },
                    () -> parkinglot.ParkingLotDemo.main(args),
                    () -> ridesharing.RideSharingDemo.main(args),
                    () -> { try { taskscheduler.TaskSchedulerDemo.main(args); } catch (Exception e) { e.printStackTrace(); } },
                    () -> pubsub.PubSubDemo.main(args),
                    () -> vendingmachine.VendingMachineDemo.main(args),
                    () -> bookmyshow.BookMyShowDemo.main(args),
                    () -> notification.NotificationService.main(args),
                    () -> splitwise.SplitwiseDemo.main(args),
                    () -> lfucache.LFUCache.main(args),
                    () -> elevator.ElevatorDemo.main(args),
                    () -> logger.Logger.main(args),
                    () -> atm.ATM.main(args),
                    () -> hotel.Hotel.main(args),
                    () -> shoppingcart.ShoppingCartDemo.main(args),
                    () -> figma.FigmaDemo.main(args),
                    () -> filesystem.Directory.main(args),
                    () -> tictactoe.TicTacToeDemo.main(args),
                    () -> snakeladder.SnakeLadderGame.main(args),
                };
                for (int i = 0; i < runners.length; i++) {
                    System.out.println("\n\n" + "=".repeat(60));
                    System.out.println("  DEMO " + (i + 1) + ": " + demos[i]);
                    System.out.println("=".repeat(60));
                    runners[i].run();
                }
            }
            default -> System.out.println("Invalid choice!");
        }
    }
}