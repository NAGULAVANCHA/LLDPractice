package tictactoe;

public class TicTacToeDemo {
    public static void main(String[] args) {
        System.out.println("=== Tic-Tac-Toe Demo (O(1) win detection) ===\n");

        Game game = new Game(3, "Alice", "Bob");

        // Simulate a game where X wins via diagonal
        game.play(0, 0); // X
        game.play(0, 1); // O
        game.play(1, 1); // X
        game.play(0, 2); // O
        game.play(2, 2); // X wins diagonal!

        // Demo 2: Draw
        System.out.println("\n--- New Game (Draw) ---\n");
        Game game2 = new Game(3, "Charlie", "Dave");
        game2.play(0, 0); // X
        game2.play(0, 1); // O
        game2.play(0, 2); // X
        game2.play(1, 0); // O
        game2.play(1, 1); // X
        game2.play(2, 0); // O
        game2.play(1, 2); // X
        game2.play(2, 2); // O
        game2.play(2, 1); // X — Draw!
    }
}

