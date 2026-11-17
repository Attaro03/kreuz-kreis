package de.kreuzkreis;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import de.kreuzkreis.ui.AppPanel;
import de.kreuzkreis.ui.Fonts;
import de.kreuzkreis.ui.Palette;

/**
 * Rendert die Swing-Seite ohne Fenster in PNGs. Damit lassen sich die
 * Zustaende auch auf einem Rechner ohne sichtbaren Desktop pruefen.
 */
public final class Preview {

    private static final int WIDTH = 1120;
    private static final int HEIGHT = 780;

    private Preview() {
    }

    public static void main(String[] args) throws Exception {
        System.setProperty("java.awt.headless", "true");
        SwingUtilities.invokeAndWait(() -> {
            try {
                Fonts.load();

                render("docs/preview-start.png", new Game());
                render("docs/preview-mid.png", midGame());
                render("docs/preview-loss.png", lostGame());
                render("docs/preview-draw.png", drawGame());
                render("docs/preview-compact.png", midGame(), 1000, 680);
            } catch (Exception error) {
                throw new RuntimeException(error);
            }
        });
        System.out.println("Vorschauen geschrieben.");
    }

    private static Game midGame() {
        Game game = new Game();
        play(game, 1, 1);
        play(game, 2, 1);
        play(game, 1, 2);
        play(game, 2, 2);
        play(game, 0, 3);
        play(game, 3, 0);
        return game;
    }

    private static Game lostGame() {
        Game game = new Game();
        int[][] kreuz = {
                {0, 0}, {0, 2}, {1, 0}, {1, 1},
                {1, 3}, {2, 1}, {2, 3}, {3, 0}
        };
        int[][] kreis = {
                {0, 1}, {0, 3}, {1, 2}, {2, 0},
                {2, 2}, {3, 1}, {3, 3}
        };
        for (int i = 0; i < kreis.length; i++) {
            play(game, kreuz[i][0], kreuz[i][1]);
            play(game, kreis[i][0], kreis[i][1]);
        }
        play(game, 3, 0);
        play(game, 3, 2);
        return game;
    }

    private static Game drawGame() {
        Game game = new Game();
        int[][] kreuz = {
                {0, 1}, {0, 3}, {1, 0}, {1, 1},
                {2, 2}, {2, 3}, {3, 0}, {3, 1}
        };
        int[][] kreis = {
                {0, 0}, {0, 2}, {1, 2}, {1, 3},
                {2, 0}, {2, 1}, {3, 2}, {3, 3}
        };
        for (int i = 0; i < kreuz.length; i++) {
            play(game, kreuz[i][0], kreuz[i][1]);
            play(game, kreis[i][0], kreis[i][1]);
        }
        return game;
    }

    private static void play(Game game, int row, int col) {
        if (!game.play(row, col)) {
            throw new IllegalStateException("Vorschau-Zug wurde abgelehnt: " + row + "/" + col
                    + " in Zustand " + game.getState());
        }
    }

    private static void render(String path, Game game) throws Exception {
        render(path, game, WIDTH, HEIGHT);
    }

    private static void render(String path, Game game, int width, int height) throws Exception {
        AppPanel panel = new AppPanel(game);
        panel.setSize(width, height);
        layout(panel);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Palette.PAPER);
        g.fillRect(0, 0, width, height);
        panel.printAll(g);
        g.dispose();

        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        ImageIO.write(image, "png", file);
        System.out.println("  " + path + "  state=" + game.getState());
    }

    private static void layout(Component component) {
        component.doLayout();
        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                layout(child);
            }
        }
    }
}
