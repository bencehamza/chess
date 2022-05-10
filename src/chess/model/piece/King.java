package chess.model.piece;

import chess.model.common.Colors;
import chess.model.common.Position;
import chess.model.gameplay.Board;
import chess.model.gameplay.Player;

public class King extends Piece {

    private boolean moved = false;
    private boolean isCastling = false;
    private Rook castlingRook = null;

    public King(int positionX, int positionY, Colors color, Player owner) {
        super(positionX, positionY, color, owner);
        isCastling = false;
    }

    @Override
    public String toString() {
        return "K";
    }

    //Rule: Two kings can't be next to each other
    private boolean nearbyKing(Board board, Position position) {
        boolean near = false;

        int x = position.getX();
        int y = position.getY();
        for (int i = x - 1; i <= x + 1 && !near; i++) {
            for (int j = y - 1; j <= y + 1 && !near; j++) {
                if (0 <= i && i < board.getHEIGHT() && 0 <= j && j < board.getWIDTH() && !(getPositionX() == i && getPositionY() == j) ) {
                    near = board.getCell(i,j).getPieceOnCell().toString().equals("K");
                }
            }
        }

        return near;
    }

    @Override
    public boolean isValidMove(Board board, Position position, boolean lookingForCheck) {
        boolean valid = true;

        int absRow = Math.abs(position.getX() - getPositionX());
        int absCol = Math.abs(position.getY() - getPositionY());

        isCastling = absCol == 2 && absRow == 0 && !moved;
        if (isCastling) {
            int dir = this.position.getY() < position.getY() ? 1 : -1;

            int ind = getPositionY() + 1 * dir;
            while (ind != position.getY() && valid) {
                valid = board.getCell(getPositionX(), ind).isEmpty();
                ind += 1 * dir;
            }

            int yPos = dir == 1 ? board.getWIDTH() - 1 : 0;
            valid &= !board.getCell(getPositionX(), yPos).getPieceOnCell().moved();
            if (valid) {
                castlingRook = (Rook) board.getCell(getPositionX(), yPos).getPieceOnCell();
            } else {
                isCastling = false;
            }
        } else {
            valid = false;
            if ( (absRow == 0 || absRow == 1) && (absCol == 0 || absCol == 1)) {
                valid = canStepOnCell(board, position, false) && !nearbyKing(board, position);
            }
        }

        moved = moved || (valid && !lookingForCheck);

        return valid;
    }

    @Override
    public boolean makesCheck(Board board) {
        return false;
    }

    public boolean isCastling() {
        return isCastling;
    }

    public Rook getCastlingRook() {
        return castlingRook;
    }
}
