package de.kreuzkreis.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import de.kreuzkreis.Game;
import de.kreuzkreis.Game.Player;
import de.kreuzkreis.Game.State;

/**
 * Die linke Papierspalte: Titel, Regel, Zugstatus und der Karton-Knopf.
 * Bewusst nur eine Idee pro Flaeche - das Brett rechts ist die Hauptsache.
 */
public final class SidePanel extends JPanel {

    private static final int PAD = 48;
    private static final int BUTTON_HEIGHT = 64;

    private static final String RULE_ONE =
            "Wer drei eigene Zeichen direkt nebeneinander setzt – waagerecht, senkrecht oder diagonal – verliert sofort.";
    private static final String RULE_TWO =
            "Sind alle 16 Felder belegt, ohne dass jemand drei in einer Reihe hat, endet die Partie unentschieden.";

    private final Game game;
    private final PaperButton resetButton;

    public SidePanel(Game game, Runnable onReset) {
        this.game = game;
        setOpaque(false);
        setLayout(null);
        setPreferredSize(new Dimension(390, 720));

        resetButton = new PaperButton("Neue Runde");
        resetButton.addActionListener(event -> {
            if (onReset != null) {
                onReset.run();
            }
        });
        add(resetButton);
        refresh();
    }

    public void refresh() {
        resetButton.setText(game.getState().isOver() ? "Nochmal spielen" : "Neue Runde");
        resetButton.repaint();
        repaint();
    }

    public PaperButton getResetButton() {
        return resetButton;
    }

    @Override
    public void doLayout() {
        int width = Math.max(170, Math.min(218, getWidth() - 2 * PAD));
        int footerBaseline = getHeight() - 28;
        Font footerFont = Fonts.ui(12.5f);
        int footerAscent = getFontMetrics(footerFont).getAscent();
        int buttonY = footerBaseline - footerAscent - 22 - BUTTON_HEIGHT;
        resetButton.setBounds(PAD, Math.max(10, buttonY), width, BUTTON_HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        int x = PAD;
        int width = Math.max(120, getWidth() - 2 * PAD);

        Font titleFont = Fonts.display(52f);
        FontMetrics titleMetrics = g.getFontMetrics(titleFont);
        int titleBaselineOne = 58 + titleMetrics.getAscent();
        int titleBaselineTwo = titleBaselineOne + (int) Math.round(titleMetrics.getHeight() * 0.95);

        g.setFont(titleFont);
        g.setColor(Palette.INK);
        g.drawString("Kreuz", x, titleBaselineOne);
        g.drawString("& Kreis", x, titleBaselineTwo);

        Font ruleFont = Fonts.ui(15f);
        FontMetrics ruleMetrics = g.getFontMetrics(ruleFont);
        int ruleLineHeight = ruleMetrics.getHeight() + 4;
        List<String> ruleOneLines = wrap(RULE_ONE, ruleMetrics, width);
        List<String> ruleTwoLines = wrap(RULE_TWO, ruleMetrics, width);
        int ruleTop = titleBaselineTwo + titleMetrics.getDescent() + 34;

        drawLines(g, ruleOneLines, x, ruleTop, ruleLineHeight, ruleFont, Palette.INK_2);
        int ruleTwoTop = ruleTop + ruleOneLines.size() * ruleLineHeight + 12;
        drawLines(g, ruleTwoLines, x, ruleTwoTop, ruleLineHeight, ruleFont, Palette.MUTED);
        int ruleBottom = ruleTwoTop + ruleTwoLines.size() * ruleLineHeight;

        int dividerY = ruleBottom + 26;
        g.setColor(Palette.alpha(Palette.INK, 20));
        g.setStroke(new BasicStroke(1f));
        g.drawLine(x, dividerY, x + width, dividerY);

        int statusTop = dividerY + 34;
        Font statusFont = fitStatusFont(g, width);
        FontMetrics statusMetrics = g.getFontMetrics(statusFont);
        int statusBaseline = statusTop + statusMetrics.getAscent();
        Font subFont = Fonts.ui(13.5f);
        int subBaseline = statusBaseline + 31;

        State state = game.getState();
        Player shownPlayer = state == State.RUNNING ? game.getCurrentPlayer() : state.loser();
        boolean hasIcon = shownPlayer != null;
        int textX = x + 42;

        double iconCenterY = statusBaseline - statusMetrics.getAscent() / 2.0 + 2;
        if (hasIcon) {
            drawStatusSymbol(g, shownPlayer, x + 15, iconCenterY, 13.0);
        } else if (state == State.DRAW) {
            drawStatusSymbol(g, Player.KREUZ, x + 9, iconCenterY, 7.0);
            drawStatusSymbol(g, Player.KREIS, x + 25, iconCenterY, 6.5);
        }

        g.setFont(statusFont);
        g.setColor(Palette.INK);
        if (state == State.RUNNING) {
            String name = shownPlayer.displayName();
            String tail = " ist am Zug.";
            int nameWidth = statusMetrics.stringWidth(name);
            int bandTop = statusBaseline - statusMetrics.getAscent() + 3;
            int bandHeight = statusMetrics.getHeight() - 9;
            g.setColor(Palette.alpha(Palette.ACCENT, 145));
            g.fill(PaperShapes.rounded(textX, bandTop, nameWidth + 5, bandHeight, 6));
            g.setColor(Palette.INK);
            g.drawString(name, textX, statusBaseline);
            g.drawString(tail, textX + nameWidth, statusBaseline);
        } else if (state.isOver() && state != State.DRAW) {
            String name = shownPlayer.displayName();
            g.drawString(name + " verliert.", textX, statusBaseline);
        } else {
            g.drawString("Unentschieden.", textX, statusBaseline);
        }

        g.setFont(subFont);
        g.setColor(Palette.MUTED);
        String subline = switch (state) {
            case RUNNING -> "Klickt auf ein freies Feld.";
            case DRAW -> "Alle 16 Felder sind belegt.";
            default -> "Drei in einer Reihe.";
        };
        g.drawString(subline, textX, subBaseline);

        int footerBaseline = getHeight() - 28;
        g.setFont(Fonts.ui(12.5f));
        g.setColor(Palette.MUTED);
        g.drawString("Zwei Spieler, ein Bildschirm.", x, footerBaseline);

        g.dispose();
    }

    private Font fitStatusFont(Graphics2D g, int width) {
        float size = 24f;
        while (size > 18f) {
            Font font = Fonts.display(size);
            FontMetrics metrics = g.getFontMetrics(font);
            String text = statusText();
            if (metrics.stringWidth(text) + 42 <= width) {
                return font;
            }
            // Der Text beginnt immer hinter dem Symbolplatz, auch beim Remis.

            size -= 0.5f;
        }
        return Fonts.display(18f);
    }

    private String statusText() {
        State state = game.getState();
        if (state == State.RUNNING) {
            return game.getCurrentPlayer().displayName() + " ist am Zug.";
        }
        if (state == State.DRAW) {
            return "Unentschieden.";
        }
        return state.loser().displayName() + " verliert.";
    }

    private void drawStatusSymbol(Graphics2D g, Player player, double centerX, double centerY,
                                  double radius) {
        g.setColor(Palette.playerColor(player));
        float stroke = (float) Math.max(4.4, radius * 0.34);
        g.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        if (player == Player.KREUZ) {
            double d = radius * 0.85;
            g.draw(new java.awt.geom.Line2D.Double(centerX - d, centerY - d, centerX + d, centerY + d));
            g.draw(new java.awt.geom.Line2D.Double(centerX + d, centerY - d, centerX - d, centerY + d));
        } else {
            g.draw(new java.awt.geom.Ellipse2D.Double(
                    centerX - radius, centerY - radius * 0.98, radius * 2, radius * 1.96));
        }
    }

    private static void drawLines(Graphics2D g, List<String> lines, int x, int top,
                                  int lineHeight, Font font, Color color) {
        g.setFont(font);
        g.setColor(color);
        FontMetrics metrics = g.getFontMetrics();
        int baseline = top + metrics.getAscent();
        for (String line : lines) {
            g.drawString(line, x, baseline);
            baseline += lineHeight;
        }
    }

    private static List<String> wrap(String text, FontMetrics metrics, int maxWidth) {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String word : text.split(" ")) {
            String candidate = line.isEmpty() ? word : line + " " + word;
            if (!line.isEmpty() && metrics.stringWidth(candidate) > maxWidth) {
                lines.add(line.toString());
                line.setLength(0);
                line.append(word);
            } else {
                line.setLength(0);
                line.append(candidate);
            }
        }
        if (!line.isEmpty()) {
            lines.add(line.toString());
        }
        return lines;
    }
}
