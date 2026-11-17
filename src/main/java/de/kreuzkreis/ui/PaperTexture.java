package de.kreuzkreis.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Sehr feine Papierkoernung und einzelne Fasern. Die Textur wird einmal
 * erzeugt und dann als TexturePaint ueber die Papierflaechen gelegt.
 */
public final class PaperTexture {

    private static final int TILE = 256;
    private static TexturePaint grain;

    private PaperTexture() {
    }

    public static TexturePaint grain() {
        if (grain == null) {
            grain = createGrain();
        }
        return grain;
    }

    public static void fill(Graphics2D g, Shape shape, Color base) {
        g.setColor(base);
        g.fill(shape);
        g.setPaint(grain());
        g.fill(shape);
        g.setPaint(null);
    }

    public static void fillBackground(Graphics2D g, int width, int height, Color base) {
        g.setColor(base);
        g.fillRect(0, 0, width, height);
        g.setPaint(grain());
        g.fillRect(0, 0, width, height);
        g.setPaint(null);
    }

    private static TexturePaint createGrain() {
        BufferedImage image = new BufferedImage(TILE, TILE, BufferedImage.TYPE_INT_ARGB);
        Random random = new Random(0x4B524555L);

        for (int y = 0; y < TILE; y++) {
            for (int x = 0; x < TILE; x++) {
                if (random.nextInt(100) < 46) {
                    continue;
                }
                int alpha = 2 + random.nextInt(8);
                boolean dark = random.nextInt(100) < 64;
                Color color = dark ? Palette.INK : Palette.FRINGE;
                image.setRGB(x, y, Palette.alpha(color, alpha).getRGB());
            }
        }

        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(1f));
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(TILE);
            int y = random.nextInt(TILE);
            int length = 18 + random.nextInt(72);
            int drift = random.nextInt(5) - 2;
            g.setColor(Palette.alpha(Palette.INK, 3 + random.nextInt(5)));
            g.drawLine(x, y, x + length, y + drift);
        }
        g.dispose();

        return new TexturePaint(image, new Rectangle(0, 0, TILE, TILE));
    }
}
