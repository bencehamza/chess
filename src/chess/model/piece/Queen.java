package chess.model.piece;

import chess.model.common.Colors;
import chess.model.common.Position;
import chess.model.gameplay.Board;
import chess.model.gameplay.Player;

public class Queen extends QueenLikePiece {
    public Queen(int positionX, int positionY, Colors color, Player owner) {
        super(positionX, positionY, color, owner);
    }

    @Override
    public String toString() {
        return "Q";
    }

    @Override
    public boolean isValidMove(Board board, Position position, boolean lookingForCheck) {
        boolean valid = false;

        if (this.position.getX() == position.getX() || this.position.getY() == position.getY()) {
            valid = straightPieceMove(board, position, lookingForCheck);
        } else if (Math.abs(this.position.getX() - position.getX()) == Math.abs(this.position.getY() - position.getY())) {
            valid = crossPieceMove(board, position, lookingForCheck);
        }

        return valid;
    }
}
