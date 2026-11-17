package de.kreuzkreis;

import java.util.List;

import de.kreuzkreis.Game.Player;
import de.kreuzkreis.Game.State;

/**
 * Klassischer Test-Runner ohne externe Abhaengigkeiten.
 *
 * <p>Ausfuehren mit: java -cp out de.kreuzkreis.GameTest</p>
 */
public final class GameTest {

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        run("Neue Partie startet leer, Kreuz ist am Zug", GameTest::newGameStartsEmpty);
        run("Spieler wechseln sich ab", GameTest::playersAlternate);
        run("Belegtes Feld wird abgelehnt", GameTest::occupiedCellRejected);
        run("Felder ausserhalb des Bretts werden abgelehnt", GameTest::outOfBoundsRejected);
        run("Drei waagerecht nebeneinander: der Setzende verliert", GameTest::horizontalTripleLoses);
        run("Drei senkrecht nebeneinander: der Setzende verliert", GameTest::verticalTripleLoses);
        run("Drei diagonal nach rechts unten: der Setzende verliert", GameTest::diagonalDownRightLoses);
        run("Drei diagonal nach links unten: der Setzende verliert", GameTest::diagonalDownLeftLoses);
        run("Volles Feld ohne Verlustreihe ist unentschieden", GameTest::fullBoardWithoutTripleIsDraw);
        run("Der letzte Zug kann zwei Reihen schliessen und verliert", GameTest::lastMoveCanCompleteTwoTriples);
        run("Eine Luecke verhindert eine Verlustreihe", GameTest::gapInRowDoesNotLose);
        run("Eine Luecke in einer Viererzeile schliesst zwei Reihen", GameTest::gapInLineCompletesTwoTriples);
        run("Neustart leert das Brett und setzt Kreuz an den Zug", GameTest::resetClearsFinishedGame);

        System.out.println();
        if (failed > 0) {
            System.out.println(failed + " Test(s) fehlgeschlagen, " + passed + " bestanden.");
            System.exit(1);
        }
        System.out.println("Alle " + passed + " Tests bestanden.");
    }

    private static void newGameStartsEmpty() {
        Game game = new Game();

        assertState(State.RUNNING, game);
        assertPlayer(Player.KREUZ, game.getCurrentPlayer(), "Startspieler");
        assertEquals(0, game.getMoveCount(), "Zugzahl");
        assertTrue(game.getLosingTriples().isEmpty(), "Es darf noch keine Verlustreihe geben");

        for (int row = 0; row < Game.SIZE; row++) {
            for (int col = 0; col < Game.SIZE; col++) {
                assertTrue(game.isFree(row, col), "Feld " + row + "/" + col + " muss frei sein");
                assertPlayer(null, game.playerAt(row, col), "Feld " + row + "/" + col);
            }
        }
    }

    private static void playersAlternate() {
        Game game = new Game();

        playOk(game, 0, 0);
        assertEquals(1, game.getMoveCount(), "Zugzahl nach dem ersten Zug");
        assertPlayer(Player.KREUZ, game.playerAt(0, 0), "Symbol auf Feld 0/0");
        assertPlayer(Player.KREIS, game.getCurrentPlayer(), "Spieler nach dem ersten Zug");

        playOk(game, 1, 1);
        assertEquals(2, game.getMoveCount(), "Zugzahl nach dem zweiten Zug");
        assertPlayer(Player.KREIS, game.playerAt(1, 1), "Symbol auf Feld 1/1");
        assertPlayer(Player.KREUZ, game.getCurrentPlayer(), "Spieler nach dem zweiten Zug");
    }

    private static void occupiedCellRejected() {
        Game game = new Game();
        playOk(game, 0, 0);

        assertFalse(game.play(0, 0), "Ein belegtes Feld darf nicht bespielt werden");
        assertEquals(1, game.getMoveCount(), "Zugzahl darf sich nicht erhoehen");
        assertPlayer(Player.KREIS, game.getCurrentPlayer(), "Der Spieler darf nicht wechseln");
        assertPlayer(Player.KREUZ, game.playerAt(0, 0), "Das vorhandene Symbol bleibt stehen");
    }

    private static void outOfBoundsRejected() {
        Game game = new Game();

        assertFalse(game.play(-1, 0), "Negative Zeile muss abgelehnt werden");
        assertFalse(game.play(0, -1), "Negative Spalte muss abgelehnt werden");
        assertFalse(game.play(Game.SIZE, 0), "Zeile ausserhalb muss abgelehnt werden");
        assertFalse(game.play(0, Game.SIZE), "Spalte ausserhalb muss abgelehnt werden");
        assertEquals(0, game.getMoveCount(), "Kein Zug darf gezaehlt werden");
        assertPlayer(Player.KREUZ, game.getCurrentPlayer(), "Kreuz bleibt am Zug");
    }

    private static void horizontalTripleLoses() {
        Game game = new Game();
        playOk(game, 0, 0);
        playOk(game, 1, 1);
        playOk(game, 0, 1);
        playOk(game, 1, 2);
        playOk(game, 0, 2);

        assertState(State.KREUZ_LOST, game);
        assertEquals(5, game.getMoveCount(), "Zugzahl");
        assertPlayer(Player.KREUZ, game.playerAt(0, 2), "Das dritte Kreuz bleibt stehen");
        assertEquals(1, game.getLosingTriples().size(), "Anzahl der Verlustreihen");
        assertTriple(game.getLosingTriples().get(0),
                new Game.Cell(0, 0), new Game.Cell(0, 1), new Game.Cell(0, 2));
        assertFalse(game.play(2, 2), "Nach dem Verlust darf nicht weitergespielt werden");
    }

    private static void verticalTripleLoses() {
        Game game = new Game();
        playOk(game, 0, 0);
        playOk(game, 1, 1);
        playOk(game, 1, 0);
        playOk(game, 2, 2);
        playOk(game, 2, 0);

        assertState(State.KREUZ_LOST, game);
        assertEquals(1, game.getLosingTriples().size(), "Anzahl der Verlustreihen");
        assertTriple(game.getLosingTriples().get(0),
                new Game.Cell(0, 0), new Game.Cell(1, 0), new Game.Cell(2, 0));
    }

    private static void diagonalDownRightLoses() {
        Game game = new Game();
        playOk(game, 0, 1);
        playOk(game, 3, 0);
        playOk(game, 1, 2);
        playOk(game, 3, 1);
        playOk(game, 2, 3);

        assertState(State.KREUZ_LOST, game);
        assertEquals(1, game.getLosingTriples().size(), "Anzahl der Verlustreihen");
        assertTriple(game.getLosingTriples().get(0),
                new Game.Cell(0, 1), new Game.Cell(1, 2), new Game.Cell(2, 3));
    }

    private static void diagonalDownLeftLoses() {
        Game game = new Game();
        playOk(game, 0, 3);
        playOk(game, 0, 0);
        playOk(game, 1, 2);
        playOk(game, 1, 0);
        playOk(game, 2, 1);

        assertState(State.KREUZ_LOST, game);
        assertEquals(1, game.getLosingTriples().size(), "Anzahl der Verlustreihen");
        assertTriple(game.getLosingTriples().get(0),
                new Game.Cell(0, 3), new Game.Cell(1, 2), new Game.Cell(2, 1));
    }

    private static void fullBoardWithoutTripleIsDraw() {
        Game game = new Game();
        int[][] kreuzMoves = {
                {0, 1}, {0, 3}, {1, 0}, {1, 1},
                {2, 2}, {2, 3}, {3, 0}, {3, 1}
        };
        int[][] kreisMoves = {
                {0, 0}, {0, 2}, {1, 2}, {1, 3},
                {2, 0}, {2, 1}, {3, 2}, {3, 3}
        };

        for (int i = 0; i < kreuzMoves.length; i++) {
            playOk(game, kreuzMoves[i][0], kreuzMoves[i][1]);
            playOk(game, kreisMoves[i][0], kreisMoves[i][1]);
        }

        assertState(State.DRAW, game);
        assertEquals(16, game.getMoveCount(), "Zugzahl");
        assertTrue(game.getLosingTriples().isEmpty(), "Es darf keine Verlustreihe geben");
        for (int row = 0; row < Game.SIZE; row++) {
            for (int col = 0; col < Game.SIZE; col++) {
                assertFalse(game.isFree(row, col), "Feld " + row + "/" + col + " muss belegt sein");
            }
        }
        assertFalse(game.play(0, 0), "Nach dem Unentschieden darf nicht weitergespielt werden");
    }

    private static void lastMoveCanCompleteTwoTriples() {
        Game game = new Game();
        int[][] kreuzMoves = {
                {0, 0}, {0, 2}, {1, 0}, {1, 1},
                {1, 3}, {2, 1}, {2, 3}, {3, 0}
        };
        int[][] kreisMoves = {
                {0, 1}, {0, 3}, {1, 2}, {2, 0},
                {2, 2}, {3, 1}, {3, 3}
        };

        for (int i = 0; i < kreisMoves.length; i++) {
            playOk(game, kreuzMoves[i][0], kreuzMoves[i][1]);
            playOk(game, kreisMoves[i][0], kreisMoves[i][1]);
        }
        playOk(game, 3, 0);
        assertState(State.RUNNING, game);
        assertPlayer(Player.KREIS, game.getCurrentPlayer(), "Kreis ist vor dem letzten Zug am Zug");

        playOk(game, 3, 2);

        assertState(State.KREIS_LOST, game);
        assertEquals(16, game.getMoveCount(), "Zugzahl");
        assertEquals(2, game.getLosingTriples().size(), "Anzahl der Verlustreihen");
        assertTriple(game.getLosingTriples().get(0),
                new Game.Cell(3, 1), new Game.Cell(3, 2), new Game.Cell(3, 3));
        assertTriple(game.getLosingTriples().get(1),
                new Game.Cell(1, 2), new Game.Cell(2, 2), new Game.Cell(3, 2));
        assertPlayer(Player.KREIS, game.getState().loser(), "Verlierer");
    }

    private static void gapInRowDoesNotLose() {
        Game game = new Game();
        playOk(game, 0, 0);
        playOk(game, 0, 2);
        playOk(game, 0, 1);
        playOk(game, 1, 0);
        playOk(game, 0, 3);

        assertState(State.RUNNING, game);
        assertEquals(5, game.getMoveCount(), "Zugzahl");
        assertTrue(game.getLosingTriples().isEmpty(), "X X O X ist keine Verlustreihe");
        assertPlayer(Player.KREIS, game.getCurrentPlayer(), "Kreis ist am Zug");
    }

    private static void gapInLineCompletesTwoTriples() {
        Game game = new Game();
        playOk(game, 0, 0);
        playOk(game, 1, 0);
        playOk(game, 0, 1);
        playOk(game, 1, 1);
        playOk(game, 0, 3);
        playOk(game, 2, 0);
        assertState(State.RUNNING, game);

        playOk(game, 0, 2);

        assertState(State.KREUZ_LOST, game);
        assertEquals(2, game.getLosingTriples().size(), "Anzahl der Verlustreihen");
        assertTriple(game.getLosingTriples().get(0),
                new Game.Cell(0, 0), new Game.Cell(0, 1), new Game.Cell(0, 2));
        assertTriple(game.getLosingTriples().get(1),
                new Game.Cell(0, 1), new Game.Cell(0, 2), new Game.Cell(0, 3));
    }

    private static void resetClearsFinishedGame() {
        Game game = new Game();
        playOk(game, 0, 0);
        playOk(game, 1, 1);
        playOk(game, 0, 1);
        playOk(game, 1, 2);
        playOk(game, 0, 2);
        assertState(State.KREUZ_LOST, game);

        game.reset();

        assertState(State.RUNNING, game);
        assertPlayer(Player.KREUZ, game.getCurrentPlayer(), "Startspieler nach Neustart");
        assertEquals(0, game.getMoveCount(), "Zugzahl nach Neustart");
        assertTrue(game.getLosingTriples().isEmpty(), "Keine Verlustreihe nach Neustart");
        for (int row = 0; row < Game.SIZE; row++) {
            for (int col = 0; col < Game.SIZE; col++) {
                assertTrue(game.isFree(row, col), "Feld " + row + "/" + col + " muss frei sein");
            }
        }
    }

    private static void assertTriple(Game.Triple triple, Game.Cell... expected) {
        List<Game.Cell> actualCells = triple.cells();
        if (!actualCells.equals(List.of(expected))) {
            throw new AssertionError("Erwartete Reihe " + List.of(expected) + ", war " + actualCells);
        }
    }

    private static void playOk(Game game, int row, int col) {
        assertTrue(game.play(row, col), "Zug auf " + row + "/" + col + " muss erlaubt sein");
    }

    private static void assertState(State expected, Game game) {
        State actual = game.getState();
        if (actual != expected) {
            throw new AssertionError("Erwarteter Zustand " + expected + ", war " + actual);
        }
    }

    private static void assertPlayer(Player expected, Player actual, String what) {
        if (expected != actual) {
            throw new AssertionError(what + ": erwartet " + expected + ", war " + actual);
        }
    }

    private static void assertEquals(int expected, int actual, String what) {
        if (expected != actual) {
            throw new AssertionError(what + ": erwartet " + expected + ", war " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    private static void run(String name, Runnable test) {
        try {
            test.run();
            passed++;
            System.out.println("[PASS] " + name);
        } catch (AssertionError error) {
            failed++;
            System.out.println("[FAIL] " + name + " -> " + error.getMessage());
        }
    }
}
