package de.kreuzkreis.ui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Chillax wird mitgeliefert, damit die Oberflaeche auf jedem Rechner gleich
 * aussieht. Falls die Dateien fehlen, bleibt die App mit einer Systemschrift
 * benutzbar.
 */
public final class Fonts {

    private static Font medium;
    private static Font semibold;
    private static boolean loaded;

    private Fonts() {
    }

    public static synchronized void load() {
        if (loaded) {
            return;
        }
        medium = loadFont("/fonts/Chillax-Medium.ttf", Font.PLAIN);
        semibold = loadFont("/fonts/Chillax-Semibold.ttf", Font.BOLD);
        loaded = true;
    }

    private static Font loadFont(String resource, int fallbackStyle) {
        try (InputStream in = Fonts.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IOException("Ressource nicht gefunden: " + resource);
            }
            return Font.createFont(Font.TRUETYPE_FONT, in);
        } catch (IOException | FontFormatException error) {
            System.err.println("Chillax konnte nicht geladen werden: " + error.getMessage());
            return new Font("Segoe UI", fallbackStyle, 16);
        }
    }

    public static Font ui(float size) {
        load();
        return medium.deriveFont(size);
    }

    public static Font display(float size) {
        load();
        return semibold.deriveFont(size)
                .deriveFont(Map.of(TextAttribute.TRACKING, -0.02f));
    }
}
