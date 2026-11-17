package de.kreuzkreis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Spielmodell fuer Kreuz & Kreis.
 *
 * <p>Regel: Wer drei eigene Zeichen direkt nebeneinander setzt - waagerecht,
 * senkrecht oder diagonal - verliert sofort. Sind alle Felder belegt, ohne
 * dass jemand verliert, endet die Partie unentschieden.</p>
 */
public final class Game {

    public static final int SIZE = 4;

    public enum Player {
        KREUZ("Kreuz"),
        KREIS("Kreis");

        private final String displayName;

        Player(String displayName) {
            this.displayName = displayName;
        }

        public String displayName() {
            return displayName;
        }

        public Player other() {
            return this == KREUZ ? KREIS : KREUZ;
        }
    }

    public enum State {
        RUNNING,
        KREUZ_LOST,
        KREIS_LOST,
        DRAW;

        public boolean isOver() {
            return this != RUNNING;
        }

        public Player loser() {
            return switch (this) {
                case KREUZ_LOST -> Player.KREUZ;
                case KREIS_LOST -> Player.KREIS;
                default -> null;
            };
        }
    }

    public record Cell(int row, int col) {
    }

    public record Triple(Cell first, Cell second, Cell third) {
        public List<Cell> cells() {
            return List.of(first, second, third);
        }
    }

    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};

    private final Player[][] board = new Player[SIZE][SIZE];
    private Player currentPlayer = Player.KREUZ;
    private State state = State.RUNNING;
    private List<Triple> losingTriples = List.of();
    private int moveCount;

    public Player playerAt(int row, int col) {
        return isInside(row, col) ? board[row][col] : null;
    }

    public boolean isFree(int row, int col) {
        return isInside(row, col) && board[row][col] == null;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public State getState() {
        return state;
    }

    public List<Triple> getLosingTriples() {
        return losingTriples;
    }

    public int getMoveCount() {
        return moveCount;
    }

    /**
     * Setzt das Zeichen des aktuellen Spielers, wenn der Zug erlaubt ist.
     *
     * @return true, wenn der Zug ausgefuehrt wurde
     */
    public boolean play(int row, int col) {
        if (state.isOver() || !isFree(row, col)) {
            return false;
        }

        Player mover = currentPlayer;
        board[row][col] = mover;
        moveCount++;

        List<Triple> triples = findTriplesThrough(row, col, mover);

        if (!triples.isEmpty()) {
            losingTriples = List.copyOf(triples);
            state = mover == Player.KREUZ ? State.KREUZ_LOST : State.KREIS_LOST;
            return true;
        }

        if (moveCount == SIZE * SIZE) {
            state = State.DRAW;
            return true;
        }

        currentPlayer = mover.other();
        return true;
    }

    public void reset() {
        for (int row = 0; row < SIZE; row++) {
            Arrays.fill(board[row], null);
        }
        currentPlayer = Player.KREUZ;
        state = State.RUNNING;
        losingTriples = List.of();
        moveCount = 0;
    }

    private List<Triple> findTriplesThrough(int row, int col, Player player) {
        List<Triple> triples = new ArrayList<>();

        for (int[] direction : DIRECTIONS) {
            int dr = direction[0];
            int dc = direction[1];
            for (int offset = -2; offset <= 0; offset++) {
                int startRow = row + offset * dr;
                int startCol = col + offset * dc;
                int endRow = startRow + 2 * dr;
                int endCol = startCol + 2 * dc;
                if (!isInside(startRow, startCol) || !isInside(endRow, endCol)) {
                    continue;
                }
                if (board[startRow][startCol] == player
                        && board[startRow + dr][startCol + dc] == player
                        && board[endRow][endCol] == player) {
                    triples.add(new Triple(
                            new Cell(startRow, startCol),
                            new Cell(startRow + dr, startCol + dc),
                            new Cell(endRow, endCol)));
                }
            }
        }
        return triples;
    }

    private static boolean isInside(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }
}
