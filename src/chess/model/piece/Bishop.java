package chess.model.piece;

import chess.model.common.Colors;
import chess.model.common.Position;
import chess.model.gameplay.Board;
import chess.model.gameplay.Player;

public class Bishop extends QueenLikePiece {
    public Bishop(int positionX, int positionY, Colors color, Player owner) {
        super(positionX, positionY, color, owner);
    }

    @Override
    public String toString() {
        return "B";
    }

    @Override
    public boolean isValidMove(Board board, Position position, boolean lookingForCheck) {
        return crossPieceMove(board, position, lookingForCheck);
    }
}
