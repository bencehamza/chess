package chess.model.gameplay;

import chess.model.common.Colors;
import chess.model.common.PlayerEnum;
import chess.model.common.Position;
import chess.model.piece.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Board {
    private final int WIDTH = 8;
    private final int HEIGHT = 8;
    private final File path = new File("src/resources/board.txt");

    private final Player player1;
    private final Player player2;
    private Cell[][] cells;
    private Player currentPlayer;
    private boolean foundCheck;
    private boolean isCastling;
    private Rook castlingRook;
    private Position prevRookPos;
    private boolean gameOver;
    private Pawn breacher;

    public Board(String player1, String player2) {
        this.player1 = new Player(player1, Colors.BLACK, 0);
        this.player2 = new Player(player2, Colors.WHITE, 7);
        this.currentPlayer = this.player1;
        this.foundCheck = false;
        this.isCastling = false;
        this.castlingRook = null;
        this.prevRookPos = null;
        this.gameOver = false;
        this.breacher = null;

        initBoard();
    }

    private void initBoard() {
        cells = new Cell[WIDTH][HEIGHT];
        try (Scanner sc = new Scanner(path)) {
            String line;
            int i = 0;
            while (sc.hasNext()) {
                line = sc.nextLine();
                String[] tmp = line.split("\t");
                for (int j = 0; j < tmp.length; j++) {
                    Colors c = (i + j) % 2 == 0 ? Colors.YELLOW : Colors.GRAY;
                    cells[i][j] = new Cell(makePiece(tmp[j], i, j), c, new Position(i, j));

                    if (i < 2) {
                        player1.addPiece(cells[i][j].getPieceOnCell());
                    }

                    if (i >= HEIGHT - 2) {
                        player2.addPiece(cells[i][j].getPieceOnCell());
                    }
                }
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public boolean executeMove(Cell from, Cell to) {
        Piece p = from.getPieceOnCell();
        boolean execute = currentPlayer == from.getPieceOnCell().getOwner() &&
                          from.getPieceOnCell().isValidMove(this, to.getPosition(),false);

        Player opponent = currentPlayer == player1 ? player2 : player1;
        Piece killed = to.getPieceOnCell();
        if (execute) {
            simulateStep(p, to, killed, currentPlayer, opponent);
            boolean ownKingInCheck = opponent.piecesMakeCheck(this);
            if (!ownKingInCheck) {
                if (isKing(p) ) {
                    King king = (King) p;
                    if (king.isCastling()) {
                        this.isCastling = true;
                        Rook rook = king.getCastlingRook();
                        this.prevRookPos = rook.getPosition();
                        this.castlingRook = rook;
                        int dir = to.getPosition().getY() > from.getPosition().getY() ? 1 : -1;
                        int x = to.getPosition().getX();
                        int y = to.getPosition().getY() + 1 * -dir;
                        rook.moveTo(getCell(new Position(x, y)));
                    }
                }

                breacher = p instanceof Pawn && ((Pawn) p).isReached() ? (Pawn) p : null;
                foundCheck = p.makesCheck(this);
                currentPlayer.addPrisoner(killed);
            } else {
                reverseSimulateStep(to.getPieceOnCell(), from, killed, currentPlayer, opponent);
                to.setPiece(killed);
                execute = false;
            }

            checkGameOver(opponent, p);
        }

        return execute;
    }

    private void checkGameOver(Player defender, Piece moved) {
        if (foundCheck()) {
            King king = defender.getKing();
            gameOver = !kingAbleToMove(king) && !piecesCanBlock(defender, moved);
        } else if (player1.getPiecesCount() == 1 && player2.getPiecesCount() == 1) {
            this.gameOver = true;
        }
    }

    private boolean piecesCanBlock(Player defender, Piece moved) {
        boolean can = false;
        ArrayList<Cell> opportunities = moved.getCheckPath();
        ArrayList<Piece> defenderPieces = defender.getPieces();

        for (int i = 0; i < defenderPieces.size() && !can; ++i) {
            Piece saviour = defenderPieces.get(i);
            if (!(saviour instanceof King)) {
                can = canBlock(saviour, opportunities);
            }
        }

        return can;
    }

    private boolean canBlock(Piece saviour, ArrayList<Cell> opportunities) {
        boolean can = false;

        for (int i = 0; i < opportunities.size() && !can; ++i) {
            Cell c = opportunities.get(i);
            Piece pieceOnCell = c.getPieceOnCell();
            int x = saviour.getPositionX();
            int y = saviour.getPositionY();

            if (saviour.isValidMove(this, c.getPosition(), false)) {
                simulateStep(saviour,  c, pieceOnCell, saviour.getOwner(), currentPlayer);
                can = !currentPlayer.piecesMakeCheck(this);
                reverseSimulateStep(saviour, cells[x][y], pieceOnCell, saviour.getOwner(), currentPlayer);
                c.setPiece(pieceOnCell);
            }
        }

        return can;
    }

    private boolean kingAbleToMove(King king) {
        boolean able = false;
        int x = king.getPositionX();
        int y = king.getPositionY();

        for (int r = x - 1; r <= x + 1 && !able; ++r) {
            for (int c = y - 1; c <= y + 1 && !able; ++c) {
                if (r >= 0 && r < this.HEIGHT && c >= 0 && c < this.WIDTH && king.isValidMove(this, new Position(r, c), false)) {
                    Piece pieceOnCell = cells[r][c].getPieceOnCell();
                    simulateStep(king, cells[r][c] , pieceOnCell, king.getOwner(), currentPlayer);
                    able = !currentPlayer.piecesMakeCheck(this);
                    reverseSimulateStep(king, cells[x][y], pieceOnCell, king.getOwner(), currentPlayer);
                    cells[r][c].setPiece(pieceOnCell);
                }
            }
        }

        return able;
    }

    private boolean isKing(Piece p) {
        return p.toString().equals("K");
    }

    private void simulateStep(Piece moving, Cell to, Piece killed, Player attacker,  Player defender) {
        if (killed != null && !killed.toString().isEmpty()) {
            defender.removePiece(killed);
        }
        attacker.movePieceTo(moving, to);
    }

    private void reverseSimulateStep(Piece moving, Cell to, Piece killed, Player attacker,  Player defender) {
        if (killed != null && !killed.toString().isEmpty()) {
            defender.addPiece(killed);
        }
        attacker.movePieceTo(moving, to);
    }

    public void changePlayer() {
        this.currentPlayer = this.currentPlayer == player1 ? player2 : player1;
    }

    private Piece makePiece(String pi, int x, int y) {
        Piece piece;
        Player p = x < 2 ? player1 : player2;
        Colors color = p.getColor();

        switch (pi) {
            case "P" -> piece = new Pawn(x, y, color, p);
            case "R" -> piece = new Rook(x, y, color, p);
            case "N" -> piece = new Knight(x, y, color, p);
            case "B" -> piece = new Bishop(x, y, color, p);
            case "Q" -> piece = new Queen(x, y, color, p);
            case "K" -> piece = new King(x, y, color, p);
            default -> piece = new None(x, y, color, null);
        }

        return piece;
    }

    public Colors getColor(int i, int j) {
        return cells[i][j].getColor();
    }

    public Piece getPieceOnCell(int i, int j) {
        return cells[i][j].getPieceOnCell();
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Cell getCell(int i, int j) {
        return  cells[i][j];
    }

    public Cell getCell(Position position) {
        return cells[position.getX()][position.getY()];
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public PlayerEnum getCurrentPlayerEnum() {
        return currentPlayer == player1 ? PlayerEnum.PLAYER1 : PlayerEnum.PLAYER2;
    }

    public King getOpponentKing(Player owner) {
        return owner == player1 ? player2.getKing() : player1.getKing();
    }

    public boolean foundCheck() {
        return foundCheck;
    }

    public String getPrisonersOf(Player curr) {
        return curr.getPrisoners();
    }

    public void setIsCastling (boolean is) {
        this.isCastling = is;
    }

    public boolean isCastling () {
        return this.isCastling;
    }

    public Rook getCastlingRook () {
        return this.castlingRook;
    }

    public void setCastlingRook (Rook rook) {
        this.castlingRook = rook;
    }

    public Position getPrevRookPos () {
        return this.prevRookPos;
    }

    public void setPrevRookPos (Position pos) {
        this.prevRookPos = pos;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Pawn getBreacher () {
        return breacher;
    }

    public void setBreacher (Pawn breacher) {
        this.breacher = breacher;
    }

    public void setChosenPieceTo(int x, int y, Piece add) {
        cells[x][y].setPiece(add);
    }
}