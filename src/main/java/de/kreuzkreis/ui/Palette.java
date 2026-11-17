package de.kreuzkreis.ui;

import java.awt.Color;

import de.kreuzkreis.Game.Player;

/**
 * Farbtokens des Hum-Materials aus dem Hallmark-Theme-Katalog.
 * Die Werte sind die sRGB-Entsprechungen der dortigen OKLCH-Tokens.
 * Einige Rollen der vollstaendigen Palette sind fuer spaetere Flaechen
 * reserviert und werden auf dieser Seite noch nicht benutzt.
 */
public final class Palette {

    public static final Color PAPER = new Color(0xF7F5EC);
    public static final Color PAPER_2 = new Color(0xF0EDE0);
    public static final Color PAPER_3 = new Color(0xE6E1D1);
    public static final Color CARD = new Color(0xFCFBF6);
    public static final Color FRINGE = new Color(0xFEFCF8);

    public static final Color INK = new Color(0x12161C);
    public static final Color INK_2 = new Color(0x272C32);
    public static final Color MUTED = new Color(0x565B63);

    public static final Color RULE = new Color(0xD7D4C9);
    public static final Color EDGE = new Color(0xD2C6AA);

    public static final Color ACCENT = new Color(0xF7D21A);
    public static final Color ACCENT_DEEP = new Color(0xC9A000);
    public static final Color BLUE = new Color(0x0064BE);

    /** Etwas tiefer als das Theme-Coral, damit das Symbol auf Papier lesbar bleibt. */
    public static final Color CORAL = new Color(0xE34B50);

    public static final Color MINT = new Color(0x70E093);
    public static final Color MINT_DEEP = new Color(0x00713E);
    public static final Color SUGAR = new Color(0xC6DFEF);
    public static final Color KRAFT = new Color(0xAE7D52);

    public static final Color KREUZ = BLUE;
    public static final Color KREIS = CORAL;
    public static final Color FOCUS = BLUE;

    private Palette() {
    }

    public static Color alpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static Color playerColor(Player player) {
        return player == Player.KREUZ ? KREUZ : KREIS;
    }
}
