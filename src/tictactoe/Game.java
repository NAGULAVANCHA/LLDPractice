package tictactoe;

/**
 * Game controller: manages turns and determines winner.
 */
public class Game {
    private final Board board;
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private boolean gameOver;

    public Game(int boardSize, String p1Name, String p2Name) {
        this.board = new Board(boardSize);
        this.player1 = new Player(p1Name, 'X', 1);
        this.player2 = new Player(p2Name, 'O', -1);
        this.currentPlayer = player1;
        this.gameOver = false;
    }

    public boolean play(int row, int col) {
        if (gameOver) {
            System.out.println("Game is already over!");
            return false;
        }

        System.out.println(currentPlayer.getName() + " plays at (" + row + "," + col + ")");
        boolean won = board.makeMove(row, col, currentPlayer);

        board.display();

        if (won) {
            System.out.println("🏆 " + currentPlayer.getName() + " WINS!");
            gameOver = true;
            return true;
        }

        if (board.isFull()) {
            System.out.println("It's a DRAW!");
            gameOver = true;
            return true;
        }

        // Switch turn
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        return false;
    }

    public Player getCurrentPlayer() { return currentPlayer; }
    public boolean isGameOver() { return gameOver; }
}

