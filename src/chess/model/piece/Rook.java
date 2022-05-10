package chess.model.piece;

import chess.model.common.Colors;
import chess.model.common.Position;
import chess.model.gameplay.Board;
import chess.model.gameplay.Player;

public class Rook extends QueenLikePiece {
    private boolean moved = false;

    public Rook(int positionX, int positionY, Colors color, Player owner) {
        super(positionX, positionY, color, owner);
    }

    @Override
    public String toString() {
        return "R";
    }

    @Override
    public boolean isValidMove(Board board, Position position, boolean lookingForCheck) {
        boolean valid = straightPieceMove(board, position, lookingForCheck);

        moved = moved || (valid && !lookingForCheck);

        return valid;
    }
}
