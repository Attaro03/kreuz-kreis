package de.kreuzkreis.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Objects;

import javax.swing.JPanel;

import de.kreuzkreis.Game;

/**
 * Die gesamte Spielseite: linke Papierspalte, rechts das Brett.
 */
public final class AppPanel extends JPanel {

    private final Game game;
    private final BoardPanel boardPanel;
    private final SidePanel sidePanel;

    public AppPanel() {
        this(new Game());
    }

    public AppPanel(Game game) {
        this.game = Objects.requireNonNull(game);
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Palette.PAPER);
        setPreferredSize(new Dimension(1120, 780));
        setMinimumSize(new Dimension(960, 680));

        boardPanel = new BoardPanel(game, this::afterMove);
        sidePanel = new SidePanel(game, this::resetGame);
        add(sidePanel, BorderLayout.WEST);
        add(boardPanel, BorderLayout.CENTER);
    }

    private void afterMove() {
        sidePanel.refresh();
        repaint();
    }

    private void resetGame() {
        game.reset();
        boardPanel.refresh();
        sidePanel.refresh();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        PaperTexture.fillBackground(g, getWidth(), getHeight(), Palette.PAPER);
        g.dispose();
    }

    public Game getGame() {
        return game;
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    public SidePanel getSidePanel() {
        return sidePanel;
    }
}
