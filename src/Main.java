import java.util.Scanner;

/**
 * LLD Practice — Master Runner
 * Run any of the 20 LLD implementations from here.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║          LLD Interview Practice Suite               ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║  1.  Parking Lot        (Singleton, Strategy)       ║");
        System.out.println("║  2.  Vending Machine    (State Pattern)             ║");
        System.out.println("║  3.  LRU Cache          (HashMap + DLL)             ║");
        System.out.println("║  4.  LFU Cache          (3 HashMaps)                ║");
        System.out.println("║  5.  Notification System (Observer Pattern)         ║");
        System.out.println("║  6.  Splitwise          (Strategy, SRP)             ║");
        System.out.println("║  7.  Figma Design Tool  (Command, Composite)        ║");
        System.out.println("║  8.  Rate Limiter       (4 algorithms)              ║");
        System.out.println("║  9.  Elevator System    (Strategy, Scheduling)      ║");
        System.out.println("║  10. Tic-Tac-Toe        (O(1) win detection)        ║");
        System.out.println("║  11. BookMyShow         (Concurrency, Booking)      ║");
        System.out.println("║  12. Snake & Ladder     (OOP, Game loop)            ║");
        System.out.println("║  13. Library Management (CRUD, Search, Fines)       ║");
        System.out.println("║  14. Shopping Cart      (Strategy, Pricing)         ║");
        System.out.println("║  15. Chess              (Polymorphism, Pieces)      ║");
        System.out.println("║  16. Logging Framework  (Chain of Responsibility)   ║");
        System.out.println("║  17. Pub-Sub Queue      (Producer/Consumer)         ║");
        System.out.println("║  18. ATM Machine        (State, CoR)               ║");
        System.out.println("║  19. Hotel Booking      (Date ranges, Concurrency)  ║");
        System.out.println("║  20. File System        (Composite Pattern)         ║");
        System.out.println("║  0.  Run ALL demos                                  ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.print("Choose (0-20): ");

        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        switch (choice) {
            case 1  -> parkinglot.ParkingLotDemo.main(args);
            case 2  -> vendingmachine.VendingMachineDemo.main(args);
            case 3  -> lrucache.LRUCache.main(args);
            case 4  -> lfucache.LFUCache.main(args);
            case 5  -> notification.NotificationService.main(args);
            case 6  -> splitwise.SplitwiseDemo.main(args);
            case 7  -> figma.FigmaDemo.main(args);
            case 8  -> ratelimiter.RateLimiterDemo.main(args);
            case 9  -> elevator.ElevatorDemo.main(args);
            case 10 -> tictactoe.TicTacToeDemo.main(args);
            case 11 -> bookmyshow.BookMyShowDemo.main(args);
            case 12 -> snakeladder.SnakeLadderGame.main(args);
            case 13 -> library.Library.main(args);
            case 14 -> shoppingcart.ShoppingCartDemo.main(args);
            case 15 -> chess.ChessDemo.main(args);
            case 16 -> logger.Logger.main(args);
            case 17 -> pubsub.PubSubDemo.main(args);
            case 18 -> atm.ATM.main(args);
            case 19 -> hotel.Hotel.main(args);
            case 20 -> filesystem.Directory.main(args);
            case 0  -> {
                String[] demos = { "Parking Lot", "Vending Machine", "LRU Cache", "LFU Cache",
                        "Notification", "Splitwise", "Figma", "Rate Limiter",
                        "Elevator", "Tic-Tac-Toe", "BookMyShow", "Snake & Ladder",
                        "Library", "Shopping Cart", "Chess", "Logger",
                        "Pub-Sub", "ATM", "Hotel", "File System" };
                Runnable[] runners = {
                        () -> parkinglot.ParkingLotDemo.main(args),
                        () -> vendingmachine.VendingMachineDemo.main(args),
                        () -> lrucache.LRUCache.main(args),
                        () -> lfucache.LFUCache.main(args),
                        () -> notification.NotificationService.main(args),
                        () -> splitwise.SplitwiseDemo.main(args),
                        () -> figma.FigmaDemo.main(args),
                        () -> { try { ratelimiter.RateLimiterDemo.main(args); } catch (Exception e) { e.printStackTrace(); } },
                        () -> elevator.ElevatorDemo.main(args),
                        () -> tictactoe.TicTacToeDemo.main(args),
                        () -> bookmyshow.BookMyShowDemo.main(args),
                        () -> snakeladder.SnakeLadderGame.main(args),
                        () -> library.Library.main(args),
                        () -> shoppingcart.ShoppingCartDemo.main(args),
                        () -> chess.ChessDemo.main(args),
                        () -> logger.Logger.main(args),
                        () -> pubsub.PubSubDemo.main(args),
                        () -> atm.ATM.main(args),
                        () -> hotel.Hotel.main(args),
                        () -> filesystem.Directory.main(args),
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