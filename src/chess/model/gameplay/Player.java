package chess.model.gameplay;

import chess.model.common.Colors;
import chess.model.piece.King;
import chess.model.piece.Piece;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Player {
    private String name;
    private ArrayList<Piece> pieces;
    private ArrayList<Piece> prisoners;
    private Colors color;
    private int startRow;

    public Player(String name, Colors color, int startRow) {
        this.name = name;
        this.color = color;
        this.startRow = startRow;
        this.pieces = new ArrayList<>();
        this.prisoners = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    public void addPrisoner(Piece prisoner) {
        if (prisoner.getOwner() != null) {
            prisoners.add(prisoner);
        }
    }

    public int getPiecesCount() {
        return pieces.size();
    }

    public String getPrisoners() {
        StringBuilder sb = new StringBuilder();
        prisoners.forEach(p -> sb.append(p).append(" "));
        return sb.toString();
    }

    public King getKing() {
        return (King) pieces.stream().filter(p -> p.toString().equals("K")).collect(Collectors.toList()).get(0);
    }

    public boolean piecesMakeCheck(Board board) {
        return pieces.stream().filter(p -> p.makesCheck(board)).collect(Collectors.toList()).size() > 0;
    }

    public void movePieceTo(Piece piece, Cell destination) {
        piece.moveTo(destination);
    }

    public Colors getColor() {
        return color;
    }

    public void removePiece(Piece killed) {
        pieces.remove(killed);
    }

    public int getStartRow() {
        return startRow;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }
}
