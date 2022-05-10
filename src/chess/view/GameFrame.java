package chess.view;

import chess.model.common.Colors;
import chess.model.common.PlayerEnum;
import chess.model.gameplay.Board;
import chess.model.gameplay.Cell;
import chess.model.gameplay.Player;
import chess.model.piece.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

import static java.awt.BorderLayout.*;

public class GameFrame extends JFrame {
    private Board board;
    private CellButton[][] cells;
    private JPanel turns;
    private JPanel playArea;
    private JPanel prisoners;
    private JLabel currentPlayer;
    private JLabel player1Prisoners;
    private JLabel player2Prisoners;
    private JLabel messages;
    private int clickCount;
    private Cell from;

    private final Action click = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            CellButton b = (CellButton) e.getSource();
            if (clickCount == 0) {
                cells[b.getPosX()][b.getPosY()].setBorder(new LineBorder(Color.BLUE));
                from = board.getCell(b.getPosX(), b.getPosY());
                clickCount++;
            } else {
                cells[from.getPosition().getX()][from.getPosition().getY()].setBorder(null);
                Cell to = board.getCell(b.getPosX(), b.getPosY());
                if (board.executeMove(from, to)) {
                    displayCheck(board.foundCheck());
                    refreshCells(from, to);
                }

                from = null;
                clickCount = 0;
            }
        }
    };

    public GameFrame (Board board) {
        this.board = board;
        this.clickCount = 0;

        setTitle("Chess");
        setIconImage(new ImageIcon("src/resources/welcome.jpg").getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 800);
        setResizable(false);
        setLocationRelativeTo(null);

        initBoard();

        setVisible(true);
    }

    private void refreshCells(Cell from, Cell to) {
        int x = from.getPosition().getX();
        int y = from.getPosition().getY();

        Colors fromColor = from.getPieceOnCell().getColor();
        Colors toColor = to.getPieceOnCell().getColor();

        if (board.isCastling()) {
            int rookX = board.getPrevRookPos().getX();
            int rookY = board.getPrevRookPos().getY();
            cells[rookX][rookY].setPiece(new None(rookX, rookY, fromColor, null));

            Rook rook = board.getCastlingRook();
            rookX = rook.getPositionX();
            rookY = rook.getPositionY();
            cells[rookX][rookY].setPiece(rook);

            board.setPrevRookPos(null);
            board.setCastlingRook(null);
            board.setIsCastling(false);
        }

        cells[x][y].setPiece(new None(x, y, fromColor, null));
        x = to.getPosition().getX();
        y = to.getPosition().getY();

        if (board.getBreacher() != null) {
            String[] choices = {"Q", "R", "B", "KN"};
            String choice = (String) JOptionPane.showInputDialog(this, "Choose a preferred piece",
                    "Pawn reached opposite site", JOptionPane.DEFAULT_OPTION, null, choices, choices[0]);

            Piece add = null;
            switch (choice) {
                case "Q" -> {
                    add = new Queen(x, y, toColor, to.getPieceOnCell().getOwner());
                }
                case "R" -> {
                    add = new Rook(x, y, toColor, to.getPieceOnCell().getOwner());
                }
                case "B" -> {
                    add = new Bishop(x, y, toColor, to.getPieceOnCell().getOwner());
                }
                case "KN" -> {
                    add = new Knight(x, y, toColor, to.getPieceOnCell().getOwner());
                }
                default -> {
                    add = to.getPieceOnCell();
                }
            }

            board.setChosenPieceTo(x, y, add);
            board.setBreacher(null);
        }

        cells[x][y].setPiece(to.getPieceOnCell());
        Player curr = board.getCurrentPlayer();

        if (board.getCurrentPlayerEnum() == PlayerEnum.PLAYER1) {
            player1Prisoners.setText(board.getPlayer1().getName() + "'s prisoners: " + board.getPrisonersOf(curr));
        } else {
            player2Prisoners.setText(board.getPlayer2().getName() + "'s prisoners: " + board.getPrisonersOf(curr));
        }

        board.changePlayer();
        curr = board.getCurrentPlayer();
        currentPlayer.setText(curr.getName() + (curr.getColor() == Colors.BLACK ? " (black)" : " (white)"));
        repaint();
        SwingUtilities.updateComponentTreeUI(this);

        if (board.isGameOver()) {
            int restart = JOptionPane.showConfirmDialog(this, "Game Over! Want to restart?", "Game Over", JOptionPane.YES_NO_OPTION);
            if (restart == 0) {
                new StartFrame();
            }
            this.dispose();
        }
    }

    private void initBoard() {
        turns = new JPanel(new GridLayout(2, 1));
        Player curr = board.getCurrentPlayer();
        currentPlayer = new JLabel(curr.getName() + (curr.getColor() == Colors.BLACK ? " (black)" : " (white)"));
        currentPlayer.setHorizontalAlignment(JLabel.CENTER);
        currentPlayer.setVerticalAlignment(JLabel.CENTER);
        messages = new JLabel();
        messages.setHorizontalAlignment(JLabel.CENTER);
        messages.setVerticalAlignment(JLabel.CENTER);
        messages.setForeground(Color.RED);
        turns.add(currentPlayer);
        turns.add(messages);

        playArea = new JPanel(new GridLayout(board.getWIDTH(), board.getHEIGHT(), 0, 0));
        generateBoard(playArea);

        prisoners = new JPanel(new GridLayout(2, 1));
        player1Prisoners = new JLabel(board.getPlayer1().getName() + "'s prisoners: ");
        player2Prisoners = new JLabel(board.getPlayer2().getName() + "'s prisoners: ");
        prisoners.add(player1Prisoners);
        prisoners.add(player2Prisoners);

        this.add(turns, NORTH);
        this.add(playArea, CENTER);
        this.add(prisoners, SOUTH);
    }

    private void generateBoard(JPanel playArea) {
        cells = new CellButton[board.getWIDTH()][board.getHEIGHT()];
        for (int i = 0; i < board.getWIDTH(); i++) {
            for (int j = 0; j < board.getHEIGHT(); j++) {
                cells[i][j] = new CellButton(i, j, this.board.getColor(i, j), board.getPieceOnCell(i, j));
                cells[i][j].addActionListener(click);
                playArea.add(cells[i][j]);
            }
        }
    }

    private void displayCheck(boolean display) {
        if (display) {
            messages.setText("Check!");
        } else {
            messages.setText("");
        }
    }
}
