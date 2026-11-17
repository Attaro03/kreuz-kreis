package de.kreuzkreis;

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

        board[row][col] = currentPlayer;
        moveCount++;
        currentPlayer = currentPlayer.other();
        return true;
    }

    public void reset() {
        for (int row = 0; row < SIZE; row++) {
            java.util.Arrays.fill(board[row], null);
        }
        currentPlayer = Player.KREUZ;
        state = State.RUNNING;
        losingTriples = List.of();
        moveCount = 0;
    }

    private static boolean isInside(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }
}
