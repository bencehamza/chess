package chess.view;

import chess.model.common.Colors;
import chess.model.piece.Piece;

import javax.swing.*;
import java.awt.*;

public class CellButton extends JButton {
    private final int x;
    private final int y;

    public CellButton (int x, int y, Colors color, Piece piece) {
        this.x = x;
        this.y = y;
        this.setBackground(color == Colors.YELLOW ? Color.decode("#c29500") : Color.GRAY);
        this.setText(piece.toString());

        if (!piece.toString().isEmpty()) {
            this.setForeground(piece.getColor() == Colors.BLACK ? Color.black : Color.white);
        }
    }

    public void setPiece(Piece piece) {
        this.setText(piece.toString());

        if (!piece.toString().isEmpty()) {
            this.setForeground(piece.getColor() == Colors.BLACK ? Color.black : Color.white);
        }
    }

    public int getPosX() {
        return x;
    }

    public int getPosY() {
        return y;
    }
}
