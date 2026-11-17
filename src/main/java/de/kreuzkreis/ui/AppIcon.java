package de.kreuzkreis.ui;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 * Kleines Fenstersymbol: Kreuz und Kreis auf einem Creme-Feld.
 */
public final class AppIcon {

    private AppIcon() {
    }

    public static BufferedImage create() {
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        PaperTexture.fill(g, PaperShapes.rounded(3, 3, 58, 58, 14), Palette.PAPER);
        g.setColor(Palette.alpha(Palette.INK, 40));
        g.setStroke(new BasicStroke(1.2f));
        g.draw(PaperShapes.rounded(3.5, 3.5, 57, 57, 14));

        g.setStroke(new BasicStroke(5.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(Palette.KREUZ);
        g.draw(new Line2D.Double(13, 21, 31, 43));
        g.draw(new Line2D.Double(31, 21, 13, 43));

        g.setColor(Palette.KREIS);
        g.draw(new Ellipse2D.Double(35, 20, 23, 23));

        g.dispose();
        return image;
    }
}
