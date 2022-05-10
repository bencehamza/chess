package chess.model.piece;

import chess.model.common.Colors;
import chess.model.common.Position;
import chess.model.gameplay.Board;
import chess.model.gameplay.Cell;
import chess.model.gameplay.Player;

import java.util.ArrayList;

public class Pawn extends Piece {
    private int move = 0;
    private int direction;
    private boolean reached;

    public Pawn(int positionX, int positionY, Colors color, Player owner) {
        super(positionX, positionY, color, owner);
        this.direction = positionX < 2 ? 1 : -1;
        this.reached = false;
    }

    @Override
    public String toString() {
        return "P";
    }

    @Override
    public boolean isValidMove(Board board, Position position, boolean lookingForCheck) {
        boolean valid = false;

        int distanceX = Math.abs(this.position.getX() - position.getX());
        int distanceY = Math.abs(this.position.getY() - position.getY());

        Cell c = board.getCell(position);
        if (validDirection(position) && position.getY() == this.position.getY()) {
            if ( (distanceX == 2 && move == 0) || distanceX == 1) {
                valid = c.isEmpty();
            }
        } else if (!c.isEmpty()) {
            valid = validDirection(position) && distanceX == 1 && distanceY == 1;
        }

        valid &= canStepOnCell(board, position, lookingForCheck);

        this.checkPath = new ArrayList<>();
        if (valid && lookingForCheck) {
            checkPath.add(board.getCell(this.position));
        }

        if (valid && !lookingForCheck)
        {
            move++;
        }

        reached = !reached && valid && this.getOwner().getStartRow() + position.getX() == board.getWIDTH() - 1;

        return valid;
    }

    private boolean validDirection(Position position) {
        return (position.getX() - this.position.getX()) * direction >= 0;
    }

    public boolean isReached() {
        return reached;
    }
}
