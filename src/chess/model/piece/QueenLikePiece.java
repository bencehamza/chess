package chess.model.piece;

import chess.model.common.Colors;
import chess.model.common.Position;
import chess.model.gameplay.Board;
import chess.model.gameplay.Cell;
import chess.model.gameplay.Player;

import java.util.ArrayList;

public abstract class QueenLikePiece extends Piece {
    public QueenLikePiece (int positionX, int positionY, Colors color, Player owner) {
        super(positionX, positionY, color, owner);
    }

    protected boolean crossPieceMove(Board board, Position position, boolean lookingForCheck) {
        boolean valid = Math.abs(this.position.getX() - position.getX()) == Math.abs(this.position.getY() - position.getY()) &&
                !this.position.equals(position);
        Position startPosition = this.position;

        int iDir = position.getX() < startPosition.getX() ? -1 : 1;
        int jDir = position.getY() < startPosition.getY() ? -1 : 1;

        int i = startPosition.getX() + (1 * iDir);
        int j = startPosition.getY() + (1 * jDir);

        this.checkPath = new ArrayList<>();
        while (valid && i != position.getX()) {
            Cell c = board.getCell(i, j);
            valid = c.isEmpty();
            i += 1 * iDir;
            j += 1 * jDir;

            if (valid && lookingForCheck) {
                checkPath.add(c);
            }
        }

        valid &= canStepOnCell(board, position, lookingForCheck);

        if (valid && lookingForCheck) {
            checkPath.add(board.getCell(this.position));
            checkPath.add(board.getCell(position));
        }

        return valid;
    }

    protected boolean straightPieceMove(Board board, Position position, boolean lookingForCheck) {
        boolean valid = (this.position.getX() == position.getX() || this.position.getY() == position.getY()) &&
                !this.position.equals(position);

        Position minPosition = this.position;
        Position maxPosition = position;
        if (this.position.getX() > position.getX()) {
            minPosition = position;
            maxPosition = this.position;
        }

        if (this.position.getY() > position.getY()) {
            minPosition = position;
            maxPosition = this.position;
        }

        int vertical = this.position.getY() == position.getY() ? minPosition.getX() + 1 : minPosition.getX();
        int horizontal = this.position.getX() == position.getX() ? minPosition.getY() + 1 : minPosition.getY();
        boolean inBorders = this.position.getY() == position.getY() ? vertical <= maxPosition.getX() - 1 : horizontal <= maxPosition.getY() - 1;

        this.checkPath = new ArrayList<>();
        while (valid && inBorders) {
            Cell c;
            if (this.position.getY() == position.getY()) {
                c = board.getCell(vertical++, minPosition.getY());
                valid = c.isEmpty();
                inBorders = vertical <= maxPosition.getX() - 1;
            } else {
                c = board.getCell(minPosition.getX(), horizontal++);
                valid = c.isEmpty();
                inBorders = horizontal <= maxPosition.getY() - 1;
            }

            if (valid && lookingForCheck) {
                checkPath.add(c);
            }
        }

        valid &= canStepOnCell(board, position, lookingForCheck);

        if (valid && lookingForCheck) {
            checkPath.add(board.getCell(this.position));
            checkPath.add(board.getCell(position));
        }

        return valid;
    }
}
