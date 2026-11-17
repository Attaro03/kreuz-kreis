package de.kreuzkreis.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import de.kreuzkreis.Game;
import de.kreuzkreis.Game.Cell;
import de.kreuzkreis.Game.Player;
import de.kreuzkreis.Game.State;
import de.kreuzkreis.Game.Triple;

/**
 * Das Spielfeld: ein Blatt Papier auf einem hellblauen Papiermatte, vier mal
 * vier Felder, Symbole wie gestempelt. Maus und Tastatur teilen sich dieselbe
 * Zuglogik.
 */
public final class BoardPanel extends JPanel {

    private static final int OUTER_PADDING = 34;
    private static final double MAX_SHEET = 660.0;

    private final Game game;
    private final Runnable onMove;

    private int hoverRow = -1;
    private int hoverCol = -1;
    private int focusRow;
    private int focusCol;
    private boolean showFocus;

    private Cursor handCursor;
    private Cursor defaultCursor;

    public BoardPanel(Game game, Runnable onMove) {
        this.game = Objects.requireNonNull(game);
        this.onMove = onMove;

        setOpaque(false);
        setFocusable(true);
        setPreferredSize(new Dimension(720, 720));
        setToolTipText(null);

        if (!GraphicsEnvironment.isHeadless()) {
            handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            defaultCursor = Cursor.getDefaultCursor();
            setCursor(defaultCursor);
        }

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent event) {
                handleMouseMove(event.getPoint());
            }

            @Override
            public void mouseExited(MouseEvent event) {
                setHover(-1, -1);
                updateCursor();
            }

            @Override
            public void mousePressed(MouseEvent event) {
                if (!SwingUtilities.isLeftMouseButton(event)) {
                    return;
                }
                Cell cell = cellAt(event.getPoint());
                if (cell == null) {
                    return;
                }
                if (game.getState() == State.RUNNING) {
                    requestFocusInWindow();
                    showFocus = false;
                    focusRow = cell.row();
                    focusCol = cell.col();
                    attemptPlay(cell);
                }
            }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent event) {
                // Der Fokusring erscheint erst bei der Tastaturbedienung, nicht
                // schon nach einem Mausklick.
                repaint();
            }

            @Override
            public void focusLost(FocusEvent event) {
                showFocus = false;
                repaint();
            }
        });

        installKeyBindings();
        getAccessibleContext().setAccessibleName("Spielfeld");
        getAccessibleContext().setAccessibleDescription(
                "Vier mal vier Felder. Mit den Pfeiltasten bewegen, mit Enter ein Zeichen setzen.");
    }

    public void refresh() {
        setHover(-1, -1);
        updateCursor();
        repaint();
    }

    /**
     * Mittelpunkt einer Zelle in Komponentenkoordinaten. Wird von der
     * Oberflaechenpruefung und spaeteren Automatisierungen genutzt.
     */
    public Point cellCenter(int row, int col) {
        if (row < 0 || row >= Game.SIZE || col < 0 || col >= Game.SIZE) {
            throw new IllegalArgumentException("Zelle ausserhalb des Bretts: " + row + "/" + col);
        }
        Geometry geometry = geometry();
        return new Point(
                (int) Math.round(centerX(geometry, col)),
                (int) Math.round(centerY(geometry, row)));
    }

    private void installKeyBindings() {
        bind("LEFT", () -> moveFocus(0, -1));
        bind("RIGHT", () -> moveFocus(0, 1));
        bind("UP", () -> moveFocus(-1, 0));
        bind("DOWN", () -> moveFocus(1, 0));
        bind("ENTER", this::activateFocus);
        bind("SPACE", this::activateFocus);
        bind("NUMPADENTER", this::activateFocus);
    }

    private void bind(String keyStroke, Runnable action) {
        String name = "board-" + keyStroke;
        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(keyStroke), name);
        getActionMap().put(name, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                action.run();
            }
        });
    }

    private void moveFocus(int deltaRow, int deltaCol) {
        focusRow = clamp(focusRow + deltaRow);
        focusCol = clamp(focusCol + deltaCol);
        showFocus = true;
        repaint();
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(Game.SIZE - 1, value));
    }

    private void activateFocus() {
        if (game.getState() != State.RUNNING) {
            return;
        }
        showFocus = true;
        attemptPlay(new Cell(focusRow, focusCol));
    }

    private void attemptPlay(Cell cell) {
        if (!game.isFree(cell.row(), cell.col())) {
            return;
        }
        if (game.play(cell.row(), cell.col())) {
            if (game.getState() != State.RUNNING) {
                showFocus = false;
            }
            setHover(-1, -1);
            updateCursor();
            if (onMove != null) {
                onMove.run();
            }
            repaint();
        }
    }

    private void handleMouseMove(Point point) {
        Cell cell = cellAt(point);
        if (game.getState() == State.RUNNING
                && cell != null
                && game.isFree(cell.row(), cell.col())) {
            setHover(cell.row(), cell.col());
        } else {
            setHover(-1, -1);
        }
        updateCursor();
    }

    private void setHover(int row, int col) {
        if (hoverRow == row && hoverCol == col) {
            return;
        }
        hoverRow = row;
        hoverCol = col;
        repaint();
    }

    private void updateCursor() {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        boolean playable = game.getState() == State.RUNNING
                && hoverRow >= 0
                && game.isFree(hoverRow, hoverCol);
        setCursor(playable ? handCursor : defaultCursor);
    }

    private Cell cellAt(Point point) {
        Geometry geometry = geometry();
        if (!geometry.gridBounds().contains(point)) {
            return null;
        }
        int col = (int) ((point.x - geometry.gridX()) / geometry.cell());
        int row = (int) ((point.y - geometry.gridY()) / geometry.cell());
        if (row < 0 || row >= Game.SIZE || col < 0 || col >= Game.SIZE) {
            return null;
        }
        return new Cell(row, col);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Geometry geometry = geometry();
        paintMat(g, geometry);
        paintSheet(g, geometry);
        paintHoverFill(g, geometry);
        paintGrid(g, geometry);
        paintLosingLines(g, geometry);
        paintSymbols(g, geometry);
        paintHoverGhost(g, geometry);
        paintFocus(g, geometry);
        paintSticker(g, geometry);

        g.dispose();
    }

    private void paintMat(Graphics2D g, Geometry geometry) {
        double pad = Math.max(15.0, geometry.sheetSize() * 0.024);
        double x = geometry.sheetX() - pad;
        double y = geometry.sheetY() - pad;
        double size = geometry.sheetSize() + 2 * pad;
        Shape mat = PaperShapes.tornSheet(x, y, size, size, 0x5A6E2L);

        Graphics2D mg = (Graphics2D) g.create();
        PaperShapes.rotate(mg, -0.022, x + size / 2, y + size / 2);
        PaperShapes.shadow(mg, mat, 4);

        Shape fringe = PaperShapes.tornSheet(x, y + 3, size, size, 0x5A6E2L);
        mg.setColor(Palette.FRINGE);
        mg.fill(fringe);
        PaperShapes.paper(mg, mat, Palette.SUGAR);
        mg.dispose();
    }

    private void paintSheet(Graphics2D g, Geometry geometry) {
        Shape sheet = PaperShapes.tornSheet(
                geometry.sheetX(), geometry.sheetY(), geometry.sheetSize(), geometry.sheetSize(), 0x0C1A5L);
        PaperShapes.shadow(g, sheet, 5);

        Shape fringe = PaperShapes.tornSheet(
                geometry.sheetX(), geometry.sheetY() + 3, geometry.sheetSize(), geometry.sheetSize(), 0x0C1A5L);
        g.setColor(Palette.FRINGE);
        g.fill(fringe);
        PaperShapes.paper(g, sheet, Palette.PAPER);
    }

    private void paintHoverFill(Graphics2D g, Geometry geometry) {
        if (hoverRow < 0 || game.getState() != State.RUNNING || !game.isFree(hoverRow, hoverCol)) {
            return;
        }
        double x = geometry.gridX() + hoverCol * geometry.cell();
        double y = geometry.gridY() + hoverRow * geometry.cell();
        Shape shape = PaperShapes.rounded(x + 5, y + 5, geometry.cell() - 10, geometry.cell() - 10, 12);
        g.setColor(Palette.alpha(Palette.ACCENT, 80));
        g.fill(shape);
        g.setColor(Palette.alpha(Palette.ACCENT_DEEP, 55));
        g.setStroke(new BasicStroke(1.2f));
        g.draw(shape);
    }

    private void paintGrid(Graphics2D g, Geometry geometry) {
        g.setStroke(new BasicStroke(1.1f));
        g.setColor(Palette.alpha(Palette.INK, 62));
        for (int i = 1; i < Game.SIZE; i++) {
            double x = geometry.gridX() + i * geometry.cell();
            g.draw(new Line2D.Double(x, geometry.gridY(), x, geometry.gridY() + geometry.gridSize()));
            double y = geometry.gridY() + i * geometry.cell();
            g.draw(new Line2D.Double(geometry.gridX(), y, geometry.gridX() + geometry.gridSize(), y));
        }

        g.setColor(Palette.alpha(Palette.INK, 104));
        g.setStroke(new BasicStroke(1.4f));
        g.draw(new Rectangle2D.Double(
                geometry.gridX(), geometry.gridY(), geometry.gridSize(), geometry.gridSize()));
    }

    private void paintLosingLines(Graphics2D g, Geometry geometry) {
        if (game.getLosingTriples().isEmpty()) {
            return;
        }
        for (Triple triple : game.getLosingTriples()) {
            Cell first = triple.first();
            Cell last = triple.third();
            double x1 = centerX(geometry, first.col());
            double y1 = centerY(geometry, first.row());
            double x2 = centerX(geometry, last.col());
            double y2 = centerY(geometry, last.row());

            float band = (float) (geometry.cell() * 0.64);
            g.setStroke(new BasicStroke(band, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(Palette.alpha(Palette.ACCENT, 155));
            g.draw(new Line2D.Double(x1, y1, x2, y2));

            g.setStroke(new BasicStroke(band * 0.56f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(Palette.alpha(Palette.ACCENT, 190));
            g.draw(new Line2D.Double(x1, y1 + 1, x2, y2 + 1));
        }
    }

    private void paintSymbols(Graphics2D g, Geometry geometry) {
        for (int row = 0; row < Game.SIZE; row++) {
            for (int col = 0; col < Game.SIZE; col++) {
                Player player = game.playerAt(row, col);
                if (player == null) {
                    continue;
                }
                drawSymbol(g, player,
                        centerX(geometry, col),
                        centerY(geometry, row),
                        geometry.cell() * 0.27,
                        Palette.playerColor(player),
                        1f);
            }
        }
    }

    private void paintHoverGhost(Graphics2D g, Geometry geometry) {
        if (hoverRow < 0
                || game.getState() != State.RUNNING
                || !game.isFree(hoverRow, hoverCol)) {
            return;
        }
        drawSymbol(g, game.getCurrentPlayer(),
                centerX(geometry, hoverCol),
                centerY(geometry, hoverRow),
                geometry.cell() * 0.245,
                Palette.playerColor(game.getCurrentPlayer()),
                0.24f);
    }

    private void paintFocus(Graphics2D g, Geometry geometry) {
        if (!showFocus || game.getState() != State.RUNNING) {
            return;
        }
        double x = geometry.gridX() + focusCol * geometry.cell() + 4;
        double y = geometry.gridY() + focusRow * geometry.cell() + 4;
        Shape ring = PaperShapes.rounded(x, y, geometry.cell() - 8, geometry.cell() - 8, 13);

        g.setColor(Palette.PAPER);
        g.setStroke(new BasicStroke(4.5f));
        g.draw(ring);
        g.setColor(Palette.FOCUS);
        g.setStroke(new BasicStroke(2.2f));
        g.draw(ring);
    }

    private void paintSticker(Graphics2D g, Geometry geometry) {
        if (!game.getState().isOver()) {
            return;
        }
        boolean draw = game.getState() == State.DRAW;
        String text = draw ? "Unentschieden" : "Drei!";
        Font font = Fonts.display(19f);
        FontMetrics metrics = g.getFontMetrics(font);
        double width = metrics.stringWidth(text) + 42;
        double height = 56;
        double x = Math.min(geometry.sheetX() + geometry.sheetSize() - width + 8,
                getWidth() - width - 8);
        x = Math.max(8, x);
        double y = Math.max(8, geometry.sheetY() - 34);

        Graphics2D sg = (Graphics2D) g.create();
        sg.rotate(-0.08, x + width / 2, y + height / 2);

        sg.setColor(Palette.alpha(Palette.INK, 46));
        sg.fill(PaperShapes.rounded(x + 3, y + 6, width, height, 18));
        sg.setColor(Palette.FRINGE);
        sg.fill(PaperShapes.rounded(x - 5, y - 5, width + 10, height + 10, 23));
        PaperTexture.fill(sg, PaperShapes.rounded(x, y, width, height, 18),
                draw ? Palette.INK : Palette.CORAL);

        sg.setFont(font);
        sg.setColor(Palette.PAPER);
        int textX = (int) Math.round(x + (width - metrics.stringWidth(text)) / 2.0);
        int textY = (int) Math.round(y + (height - metrics.getHeight()) / 2.0 + metrics.getAscent());
        sg.drawString(text, textX, textY);
        sg.dispose();
    }

    private void drawSymbol(Graphics2D g, Player player, double centerX, double centerY,
                            double radius, Color color, float opacity) {
        Graphics2D sg = (Graphics2D) g.create();
        if (opacity < 1f) {
            sg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        }
        sg.setColor(color);
        float stroke = Math.max(6.8f, (float) (radius * 0.34));
        sg.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        if (player == Player.KREUZ) {
            double d = radius * 0.9;
            sg.draw(new Line2D.Double(centerX - d, centerY - d, centerX + d, centerY + d));
            sg.draw(new Line2D.Double(centerX + d, centerY - d, centerX - d, centerY + d));
        } else {
            sg.draw(new Ellipse2D.Double(
                    centerX - radius, centerY - radius * 0.98,
                    radius * 2, radius * 1.96));
        }
        sg.dispose();
    }

    private double centerX(Geometry geometry, int col) {
        return geometry.gridX() + (col + 0.5) * geometry.cell();
    }

    private double centerY(Geometry geometry, int row) {
        return geometry.gridY() + (row + 0.5) * geometry.cell();
    }

    private Geometry geometry() {
        double available = Math.min(getWidth() - 2.0 * OUTER_PADDING,
                getHeight() - 2.0 * OUTER_PADDING);
        double sheetSize = Math.max(280.0, Math.min(MAX_SHEET, available));
        double sheetX = (getWidth() - sheetSize) / 2.0;
        double sheetY = (getHeight() - sheetSize) / 2.0;
        double inset = Math.max(24.0, Math.min(38.0, sheetSize * 0.052));
        double gridSize = sheetSize - 2 * inset;
        double cell = gridSize / Game.SIZE;
        return new Geometry(
                sheetX, sheetY, sheetSize,
                sheetX + inset, sheetY + inset, gridSize, cell);
    }

    private record Geometry(
            double sheetX,
            double sheetY,
            double sheetSize,
            double gridX,
            double gridY,
            double gridSize,
            double cell) {

        Rectangle2D gridBounds() {
            return new Rectangle2D.Double(gridX, gridY, gridSize, gridSize);
        }
    }
}
