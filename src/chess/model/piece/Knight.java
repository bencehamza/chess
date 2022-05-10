package chess.model.piece;

import chess.model.common.Colors;
import chess.model.common.Position;
import chess.model.gameplay.Board;
import chess.model.gameplay.Player;

import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(int positionX, int positionY, Colors color, Player owner) {
        super(positionX, positionY, color, owner);
    }

    @Override
    public String toString() {
        return "KN";
    }

    @Override
    public boolean isValidMove(Board board, Position position, boolean lookingForCheck) {
        boolean valid = false;

        int distanceX = Math.abs(this.getPositionX() - position.getX());
        int distanceY = Math.abs(this.getPositionY() - position.getY());
        boolean can = canStepOnCell(board, position, lookingForCheck);

        if (can && distanceY == 2) {
            valid = distanceX == 1;
        }

        if (can && distanceX == 2) {
            valid = distanceY == 1;
        }

        this.checkPath = new ArrayList<>();
        if (valid && lookingForCheck) {
            checkPath.add(board.getCell(this.position));
        }

        return valid;
    }
}
