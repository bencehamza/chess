package chess.model.piece;

import chess.model.common.Colors;
import chess.model.common.Position;
import chess.model.gameplay.Board;
import chess.model.gameplay.Player;

public class None extends Piece {
    public None(int positionX, int positionY, Colors color, Player owner) {
        super(positionX, positionY, color, owner);
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public boolean isValidMove(Board board, Position position, boolean lookingForCheck) {
        return false;
    }

    @Override
    public boolean makesCheck(Board board) {
        return false;
    }
}
