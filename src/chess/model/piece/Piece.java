package chess.model.piece;

import chess.model.common.Colors;
import chess.model.common.Position;
import chess.model.gameplay.Board;
import chess.model.gameplay.Cell;
import chess.model.gameplay.Player;

import java.util.ArrayList;

public abstract class Piece {
    protected Position position;
    protected boolean moved = false;
    protected ArrayList<Cell> checkPath;

    private final Colors color;
    private final Player owner;
    private Cell cell;

    public Piece(int positionX, int positionY, Colors color, Player owner) {
        this.position = new Position(positionX, positionY);
        this.color = color;
        this.owner = owner;
        this.checkPath = new ArrayList<>();
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getPositionX() {
        return position.getX();
    }

    public int getPositionY() {
        return position.getY();
    }

    public Colors getColor() {
        return color;
    }

    public boolean moved() {
        return moved;
    }

    public abstract boolean isValidMove(Board board, Position position, boolean lookingForCheck);

    public boolean makesCheck(Board board) {
        King k = board.getOpponentKing(getOwner());
        return this.isValidMove(board, k.getPosition(), true);
    }

    protected boolean canStepOnCell(Board board, Position position, boolean lookingForCheck) {
        Cell destination = board.getCell(position);
        boolean can = destination.getPieceOnCell().getOwner() != this.owner;

        if (!lookingForCheck && can) {
            can &= !destination.getPieceOnCell().toString().equals("K");
        } else if (lookingForCheck && can) {
            can &= destination.getPieceOnCell().toString().equals("K");
        }

        return can;
    }

    public void moveTo (Cell destination) {
        cell.deletePiece();
        cell = destination;
        position = cell.getPosition();
        cell.setPiece(this);
    }

    public Player getOwner() {
        return owner;
    }

    public Position getPosition() {
        return position;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public ArrayList<Cell> getCheckPath() {
        return checkPath;
    }
}
