package chess.model.gameplay;

import chess.model.common.Colors;
import chess.model.common.Position;
import chess.model.piece.None;
import chess.model.piece.Piece;

public class Cell {
    private Piece piece;
    private final Colors color;
    private final Position position;

    public Cell(Piece piece, Colors color, Position position) {
        this.piece = piece;
        this.color = color;
        this.position = position;
        piece.setCell(this);
    }

    public Piece getPieceOnCell() {
        return this.piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        piece.setCell(this);
    }

    public void deletePiece() {
        this.piece = new None(position.getX(), position.getY(), this.color, null);
    }

    public boolean isEmpty() {
        return piece instanceof None;
    }

    public Colors getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }
}
