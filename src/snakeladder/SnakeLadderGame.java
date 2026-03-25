package snakeladder;

import java.util.*;

public class SnakeLadderGame {
    private final SnakeLadderBoard board;
    private final Dice dice;
    private final Queue<SnakeLadderPlayer> players;
    private SnakeLadderPlayer winner;

    public SnakeLadderGame(SnakeLadderBoard board, Dice dice, List<SnakeLadderPlayer> playerList) {
        this.board = board;
        this.dice = dice;
        this.players = new LinkedList<>(playerList);
        this.winner = null;
    }

    public boolean playTurn() {
        if (winner != null) return true;

        SnakeLadderPlayer player = players.poll();
        int roll = dice.roll();
        int newPos = player.getPosition() + roll;

        System.out.print("  " + player.getName() + " rolled " + roll + ": " + player.getPosition());

        if (newPos > board.getWinPosition()) {
            System.out.println(" -> stays (would exceed " + board.getWinPosition() + ")");
            players.offer(player);
            return false;
        }

        System.out.println(" -> " + newPos);
        newPos = board.getFinalPosition(newPos);
        player.setPosition(newPos);

        if (newPos == board.getWinPosition()) {
            winner = player;
            System.out.println("  🏆 " + player.getName() + " WINS!");
            return true;
        }

        players.offer(player);
        return false;
    }

    public void playFullGame(int maxTurns) {
        System.out.println("\n--- Game Start ---");
        int turn = 0;
        while (turn < maxTurns) {
            if (playTurn()) break;
            turn++;
        }
        if (winner == null) System.out.println("  Game ended after " + maxTurns + " turns (no winner).");
    }

    public SnakeLadderPlayer getWinner() { return winner; }

    public static void main(String[] args) {
        System.out.println("=== Snake & Ladder Demo ===\n");

        SnakeLadderBoard board = new SnakeLadderBoard(30);
        board.addSnake(27, 5);
        board.addSnake(21, 9);
        board.addSnake(17, 7);
        board.addLadder(3, 22);
        board.addLadder(5, 8);
        board.addLadder(11, 26);
        board.addLadder(20, 29);

        Dice dice = new Dice(1, 6);

        List<SnakeLadderPlayer> playerList = List.of(
                new SnakeLadderPlayer("Alice"),
                new SnakeLadderPlayer("Bob")
        );

        SnakeLadderGame game = new SnakeLadderGame(board, dice, playerList);
        game.playFullGame(100);
    }
}

