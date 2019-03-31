package org.kkarvounis.checkmate.chess.board;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import org.kkarvounis.checkmate.GenericHelper;
import org.kkarvounis.checkmate.chess.Color;
import org.kkarvounis.checkmate.chess.Move;
import org.kkarvounis.checkmate.chess.Position;
import org.kkarvounis.checkmate.chess.piece.AbstractPiece;
import org.kkarvounis.checkmate.chess.piece.King;
import org.kkarvounis.checkmate.json.JsonSerializableInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

@JsonAutoDetect(getterVisibility = Visibility.NONE)
public class Board implements Cloneable, JsonSerializableInterface {
    public static final int SIZE = 8;

    private Map<Color, ArrayList<AbstractPiece>> pieces = new HashMap<Color, ArrayList<AbstractPiece>>() {{
        put(Color.black, new ArrayList<>());
        put(Color.white, new ArrayList<>());
    }};

    private Map<Color, King> kings = new HashMap<Color, King>() {{
        put(Color.black, null);
        put(Color.white, null);
    }};

    private AbstractPiece[][] squares = new AbstractPiece[SIZE][SIZE];
    private Traverser traverser;

    @JsonCreator
    public Board(ArrayList<AbstractPiece> pieces) {
        pieces.forEach(this::addPiece);
        traverser = new Traverser(this);
    }

    public static Board fromJson(String json) {
        return (Board) JsonSerializableInterface.fromJson(json, Board.class);
    }

    @Override
    public Board clone() {
        return new Board(getPieces());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        Board that = (Board) o;
        return that.pieces.equals(pieces);
    }

    @Override
    public int hashCode() {
        int pieceHashSum = 0;
        Iterator<AbstractPiece> it = iterator();

        while (it.hasNext()) {
            pieceHashSum += it.next().hashCode();
        }

        return pieceHashSum;
    }

    AbstractPiece[][] getSquares() {
        return squares;
    }

    public static boolean contains(Position position) {
        return GenericHelper.inRange(position.row, 0, SIZE - 1)
                && GenericHelper.inRange(position.column, 0, SIZE - 1);
    }

    @JsonGetter("pieces")
    public ArrayList<AbstractPiece> getPieces() {
        return GenericHelper.merge(pieces.get(Color.black), pieces.get(Color.white));
    }

    ArrayList<AbstractPiece> getPieces(Color color) {
        return pieces.get(color);
    }

    public AbstractPiece getPieceAt(Position position) {
        return contains(position) ? squares[position.row][position.column] : null;
    }

    public boolean hasPieceAtPosition(Position position) {
        return squares[position.row][position.column] != null;
    }

    Iterator<AbstractPiece> iterator() {
        return getPieces().iterator();
    }

    public Iterator<AbstractPiece> iterator(Color color) {
        return getPieces(color).iterator();
    }

    private void addPiece(AbstractPiece piece) {
        Position position = piece.getPosition();
        Color color = piece.getColor();

        if (hasPieceAtPosition(position)) {
            AbstractPiece replacedPiece = this.getPieceAt(position);
            removePiece(replacedPiece);
        }

        squares[position.row][position.column] = piece;
        getPieces(color).add(piece);

        if (piece instanceof King) {
            kings.put(color, (King) piece);
        }
    }

    private void removePiece(AbstractPiece piece) {
        Color color = piece.getColor();
        Position position = piece.getPosition();

        squares[position.row][position.column] = null;
        pieces.get(color).remove(piece);
    }

    public Traverser startFrom(Position position) {
        return traverser.startFrom(position);
    }

    public ArrayList<Move> detectMoves(Color playerColor) {
        ArrayList<Move> moves = new ArrayList<>();
        Iterator<AbstractPiece> it = iterator(playerColor);
        while (it.hasNext()) {
            ArrayList<Move> newMoves = it.next().detectMoves(this);
            moves.addAll(filterLegalMoves(newMoves, playerColor));
        }

        return moves;
    }

    private ArrayList<Move> filterLegalMoves(ArrayList<Move> moves, Color playerColor) {
        return moves
                .stream()
                .filter(move -> {
                    Board newBoard = doMove(move);
                    return !newBoard.isKingChecked(playerColor);
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Board doMove(Move move) {
        Board newBoard = clone();
        newBoard.removePiece(move.piece);

        AbstractPiece newPiece = move.isPromotion() ? promotePiece(move.piece, move.promotion) : move.piece.clone();
        newPiece.setPosition(move.target);
        newBoard.addPiece(newPiece);

        return newBoard;
    }

    public boolean isKingChecked(Color kingColor) {
        King king = kings.get(kingColor);
        return king != null && king.isChecked(this);
    }

    private static AbstractPiece promotePiece(AbstractPiece piece, Class<AbstractPiece> promotion) {
        try {
            return promotion
                    .getConstructor(Color.class, Position.class)
                    .newInstance(piece.getColor(), piece.getPosition());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
