package de.kreuzkreis.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Ein Knopf aus Karton: gelbe Oberflaeche, feste Unterkante, die sich beim
 * Druecken zusammenzieht. Keine Animation, damit reduzierte Bewegung nicht
 * extra abgefragt werden muss.
 */
public final class PaperButton extends JButton {

    private static final int BASE_Y = 4;
    private static final int BASE_HEIGHT = 54;
    private static final int RADIUS = 17;

    public PaperButton(String text) {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFocusable(true);
        setRolloverEnabled(true);
        setBorder(BorderFactory.createEmptyBorder());
        setFont(Fonts.ui(15f));
        setForeground(Palette.INK);
        setPreferredSize(new Dimension(218, 64));
        if (!GraphicsEnvironment.isHeadless()) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        getModel().addChangeListener(event -> repaint());
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        int width = getWidth();
        int edge = getModel().isPressed() ? 2 : (getModel().isRollover() ? 7 : 5);
        double x = 3;
        double baseWidth = width - 6;

        Shape shadow = PaperShapes.rounded(x + 2, BASE_Y + 5, baseWidth, BASE_HEIGHT, RADIUS);
        g.setColor(Palette.alpha(Palette.INK, 30));
        g.fill(shadow);

        Shape base = PaperShapes.rounded(x, BASE_Y, baseWidth, BASE_HEIGHT, RADIUS);
        g.setColor(Palette.ACCENT_DEEP);
        g.fill(base);

        double topY = BASE_Y + (7 - edge);
        double topHeight = BASE_HEIGHT - 7;
        Shape top = PaperShapes.rounded(x, topY, baseWidth, topHeight, RADIUS);
        PaperTexture.fill(g, top, Palette.ACCENT);

        g.setColor(new Color(255, 255, 255, 72));
        g.setStroke(new BasicStroke(1.1f));
        g.draw(new RoundRectangle2D.Double(x + 1, topY + 1, baseWidth - 2, topHeight - 2,
                RADIUS - 1, RADIUS - 1));

        String text = getText();
        g.setFont(getFont());
        FontMetrics metrics = g.getFontMetrics();
        int textX = (int) Math.round(x + (baseWidth - metrics.stringWidth(text)) / 2.0);
        int textY = (int) Math.round(topY + (topHeight - metrics.getHeight()) / 2.0 + metrics.getAscent());
        g.setColor(Palette.INK);
        g.drawString(text, textX, textY);

        if (hasFocus()) {
            g.setColor(Palette.FOCUS);
            g.setStroke(new BasicStroke(2.2f));
            g.draw(new RoundRectangle2D.Double(x - 2, BASE_Y - 2, baseWidth + 4, BASE_HEIGHT + 4,
                    RADIUS + 3, RADIUS + 3));
        }

        g.dispose();
    }
}
