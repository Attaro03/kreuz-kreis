package de.kreuzkreis;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import de.kreuzkreis.ui.AppIcon;
import de.kreuzkreis.ui.AppPanel;
import de.kreuzkreis.ui.Fonts;

/**
 * Startpunkt der Anwendung.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Die eigene Malerei funktioniert auch mit dem Standard-Look.
            }
            Fonts.load();

            JFrame frame = new JFrame("Kreuz & Kreis");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new AppPanel());
            frame.setIconImage(AppIcon.create());
            frame.pack();

            Rectangle usable = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getMaximumWindowBounds();
            Dimension packed = frame.getSize();
            int width = Math.min(packed.width, Math.max(960, usable.width - 24));
            int height = Math.min(packed.height, Math.max(680, usable.height - 24));
            frame.setSize(width, height);
            frame.setMinimumSize(new Dimension(Math.min(980, width), Math.min(720, height)));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
