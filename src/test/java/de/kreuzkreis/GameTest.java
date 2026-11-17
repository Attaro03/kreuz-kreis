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
