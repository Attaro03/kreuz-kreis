package de.kreuzkreis;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.SwingUtilities;

import de.kreuzkreis.Game.Player;
import de.kreuzkreis.Game.State;
import de.kreuzkreis.ui.AppPanel;
import de.kreuzkreis.ui.BoardPanel;
import de.kreuzkreis.ui.Fonts;

/**
 * Oberflaechen-Rauchtest ohne sichtbares Fenster: Klick, Tastatur, Neustart
 * und der Zustandswechsel nach einer verlorenen Partie.
 */
public final class UiTest {

    private static final int WIDTH = 1120;
    private static final int HEIGHT = 780;

    private static int passed;
    private static int failed;

    private UiTest() {
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "true");
        Fonts.load();
        SwingUtilities.invokeAndWait(() -> {
            run("Klick setzt das richtige Zeichen und wechselt den Spieler", UiTest::clickPlacesSymbol);
            run("Klick auf ein belegtes Feld aendert nichts", UiTest::occupiedClickIgnored);
            run("Nach dem Verlust sind keine Klicks mehr moeglich", UiTest::clickAfterLossIgnored);
            run("Der Knopf startet eine neue Runde", UiTest::resetButtonStartsNewRound);
            run("Pfeiltasten und Enter setzen ein Zeichen", UiTest::keyboardPlacesSymbol);
            run("Der Knopf heisst nach dem Verlust Nochmal spielen", UiTest::buttonTextAfterLoss);
        });

        System.out.println();
        if (failed > 0) {
            System.out.println(failed + " UI-Test(s) fehlgeschlagen, " + passed + " bestanden.");
            System.exit(1);
        }
        System.out.println("Alle " + passed + " UI-Tests bestanden.");
    }

    private static void clickPlacesSymbol() {
        AppPanel panel = panel(new Game());
        Game game = panel.getGame();
        BoardPanel board = panel.getBoardPanel();

        click(board, 0, 0);
        assertPlayer(Player.KREUZ, game.playerAt(0, 0), "Kreuz auf 0/0");
        assertPlayer(Player.KREIS, game.getCurrentPlayer(), "Kreis ist danach am Zug");

        click(board, 1, 1);
        assertPlayer(Player.KREIS, game.playerAt(1, 1), "Kreis auf 1/1");
        assertPlayer(Player.KREUZ, game.getCurrentPlayer(), "Kreuz ist danach am Zug");
    }

    private static void occupiedClickIgnored() {
        AppPanel panel = panel(new Game());
        Game game = panel.getGame();
        BoardPanel board = panel.getBoardPanel();

        click(board, 0, 0);
        click(board, 0, 0);

        assertEquals(1, game.getMoveCount(), "Zugzahl");
        assertPlayer(Player.KREIS, game.getCurrentPlayer(), "Der Zugwechsel bleibt aus");
    }

    private static void clickAfterLossIgnored() {
        Game game = new Game();
        play(game, 0, 0);
        play(game, 1, 1);
        play(game, 0, 1);
        play(game, 1, 2);
        play(game, 0, 2);
        assertState(State.KREUZ_LOST, game);

        AppPanel panel = panel(game);
        click(panel.getBoardPanel(), 2, 2);

        assertEquals(5, game.getMoveCount(), "Kein Zug nach dem Verlust");
        assertState(State.KREUZ_LOST, game);
    }

    private static void resetButtonStartsNewRound() {
        AppPanel panel = panel(new Game());
        Game game = panel.getGame();
        BoardPanel board = panel.getBoardPanel();

        click(board, 0, 0);
        click(board, 1, 1);
        panel.getSidePanel().getResetButton().doClick(0);

        assertState(State.RUNNING, game);
        assertPlayer(Player.KREUZ, game.getCurrentPlayer(), "Kreuz beginnt nach dem Neustart");
        assertEquals(0, game.getMoveCount(), "Zugzahl nach dem Neustart");
        assertTrue(game.isFree(0, 0), "Feld 0/0 ist wieder frei");
        assertTrue(game.isFree(1, 1), "Feld 1/1 ist wieder frei");
    }

    private static void keyboardPlacesSymbol() {
        AppPanel panel = panel(new Game());
        Game game = panel.getGame();
        BoardPanel board = panel.getBoardPanel();

        board.getActionMap().get("board-RIGHT")
                .actionPerformed(new ActionEvent(board, ActionEvent.ACTION_PERFORMED, "right"));
        board.getActionMap().get("board-ENTER")
                .actionPerformed(new ActionEvent(board, ActionEvent.ACTION_PERFORMED, "enter"));

        assertPlayer(Player.KREUZ, game.playerAt(0, 1), "Kreuz auf 0/1");
        assertPlayer(Player.KREIS, game.getCurrentPlayer(), "Kreis ist danach am Zug");
    }

    private static void buttonTextAfterLoss() {
        Game game = new Game();
        play(game, 0, 0);
        play(game, 1, 1);
        play(game, 0, 1);
        play(game, 1, 2);
        play(game, 0, 2);

        AppPanel panel = panel(game);
        assertEquals("Nochmal spielen", panel.getSidePanel().getResetButton().getText(),
                "Knopftext nach dem Verlust");
    }

    private static AppPanel panel(Game game) {
        AppPanel panel = new AppPanel(game);
        panel.setSize(WIDTH, HEIGHT);
        layout(panel);
        return panel;
    }

    private static void click(BoardPanel board, int row, int col) {
        Point point = board.cellCenter(row, col);
        MouseEvent event = new MouseEvent(board, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0, point.x, point.y, 1, false, MouseEvent.BUTTON1);
        for (MouseListener listener : board.getMouseListeners()) {
            listener.mousePressed(event);
        }
    }

    private static void play(Game game, int row, int col) {
        if (!game.play(row, col)) {
            throw new AssertionError("Zug " + row + "/" + col + " wurde abgelehnt");
        }
    }

    private static void layout(Component component) {
        component.doLayout();
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                layout(child);
            }
        }
    }

    private static void assertState(State expected, Game game) {
        if (game.getState() != expected) {
            throw new AssertionError("Erwarteter Zustand " + expected + ", war " + game.getState());
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

    private static void assertEquals(String expected, String actual, String what) {
        if (!expected.equals(actual)) {
            throw new AssertionError(what + ": erwartet \"" + expected + "\", war \"" + actual + "\"");
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
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
        } catch (RuntimeException error) {
            failed++;
            System.out.println("[FAIL] " + name + " -> " + error);
        }
    }
}
