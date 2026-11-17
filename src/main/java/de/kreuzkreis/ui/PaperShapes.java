package de.kreuzkreis.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;

/**
 * Zuschnitte aus Papier: abgerundete Karten und Blaetter mit gerissener
 * Unterkante. Die gerissene Kante wird mit einem festen Seed erzeugt, damit
 * sie beim Neuzeichnen nicht wandert.
 */
public final class PaperShapes {

    private PaperShapes() {
    }

    public static Shape rounded(double x, double y, double width, double height, double radius) {
        double r = Math.max(0, Math.min(radius, Math.min(width, height) / 2.0));
        return new RoundRectangle2D.Double(x, y, width, height, r, r);
    }

    /**
     * Rechteck mit gerader Oberkante und einer gerissenen Unterkante.
     */
    public static Path2D tornSheet(double x, double y, double width, double height, long seed) {
        Random random = new Random(seed);
        double radius = Math.min(18.0, width * 0.035);
        double tornHeight = Math.max(6.0, Math.min(13.0, height * 0.018));

        Path2D.Double path = new Path2D.Double();
        path.moveTo(x + radius, y);
        path.lineTo(x + width - radius, y);
        path.quadTo(x + width, y, x + width, y + radius);
        path.lineTo(x + width, y + height - tornHeight);

        int steps = Math.max(9, (int) Math.round(width / 17.0));
        for (int i = 1; i <= steps; i++) {
            double px = x + width - width * i / steps;
            double py = y + height - random.nextDouble() * tornHeight;
            if (random.nextDouble() < 0.13) {
                py += 2.0 + random.nextDouble() * 3.0;
            }
            path.lineTo(px, py);
        }

        path.lineTo(x, y + radius);
        path.quadTo(x, y, x + radius, y);
        path.closePath();
        return path;
    }

    /**
     * Mehrere weiche, nach unten rechts versetzte Schichten statt eines
     * CSS-Drop-Shadows. Light comes from the top left.
     */
    public static void shadow(Graphics2D g, Shape shape, int layers) {
        AffineTransform base = g.getTransform();
        for (int i = 1; i <= layers; i++) {
            g.setTransform(base);
            g.translate(i * 1.1, i * 2.3 + 1.0);
            int alpha = 4 + (layers - i + 1) * 2;
            g.setColor(Palette.alpha(Palette.INK, alpha));
            g.fill(shape);
        }
        g.setTransform(base);
    }

    public static void paper(Graphics2D g, Shape shape, Color base) {
        PaperTexture.fill(g, shape, base);
        g.setColor(Palette.alpha(Palette.INK, 22));
        g.setStroke(new java.awt.BasicStroke(1f));
        g.draw(shape);
    }

    public static void rotate(Graphics2D g, double radians, double centerX, double centerY) {
        g.rotate(radians, centerX, centerY);
    }
}
