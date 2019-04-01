package org.kkarvounis.checkmate.chess;

import org.kkarvounis.checkmate.chess.board.Board;
import org.kkarvounis.checkmate.chess.piece.AbstractPiece;
import org.kkarvounis.checkmate.chess.piece.King;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GameState {
    private Board board;
    private Color playerColor;
    private HashMap<Color, King> kings;
    private HashMap<Color, Boolean> kingCheckedByColor;
    private ArrayList<Move> moves;
    private HashMap<Position, ArrayList<Move>> movesByPosition;
    // An array of the last two played moves
    private ArrayList<Move> lastPlayedMoves;
    private GameStatus status;

    public GameState(Board board, Color playerColor) {
        this.board = board;
        this.playerColor = playerColor;
        initKings();
    }

    private void initKings() {
        initKing(Color.black);
        initKing(Color.white);
    }

    private void initKing(Color color) {
        kingCheckedByColor.put(color, null);

        for (AbstractPiece piece : board.getPieces(color)) {
            if (piece instanceof King) {
                kings.put(color, (King) piece);
                return;
            }
        }
    }

    public ArrayList<Move> getMoves() {
        if (moves == null) {
            calculateMoves();
        }

        return moves;
    }

    private void calculateMoves() {
        moves = new ArrayList<>();

        for (AbstractPiece piece : board.getPieces(playerColor)) {
            Position position = piece.getPosition();

            ArrayList<Move> previousMoves = movesByPosition.containsKey(position)
                    ? movesByPosition.get(position)
                    : new ArrayList<>();
            ArrayList<Move> pieceMoves = filterLegalMoves(piece.detectMoves(board, previousMoves, lastPlayedMoves));

            moves.addAll(pieceMoves);
            movesByPosition.put(position, pieceMoves);
        }
    }

    private ArrayList<Move> filterLegalMoves(ArrayList<Move> moves) {
        return moves
                .stream()
                .filter(move -> {
                    GameState newState = deriveNewState(move);
                    return !newState.isKingChecked(playerColor);
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public GameState deriveNewState(Move move) {
        GameState newState = new GameState(board.doMove(move), playerColor.opposite());

        newState.movesByPosition = copyMovesByPosition(movesByPosition);
        newState.movesByPosition.remove(move.piece.getPosition());

        newState.lastPlayedMoves = Move.copyList(lastPlayedMoves);
        newState.lastPlayedMoves.add(move);
        if (newState.lastPlayedMoves.size() > 2) {
            newState.lastPlayedMoves.remove(0);
        }

        return newState;
    }

    private HashMap<Position, ArrayList<Move>> copyMovesByPosition(HashMap<Position, ArrayList<Move>> movesByPosition) {
        HashMap<Position, ArrayList<Move>> result = new HashMap<>();
        for (Map.Entry<Position, ArrayList<Move>> entry : movesByPosition.entrySet()) {
            result.put(entry.getKey().clone(), Move.copyList(entry.getValue()));
        }

        return result;
    }

    private boolean isKingChecked(Color color) {
        if (kingCheckedByColor.get(color) == null) {
            calculateKingCheckedByColor(color);
        }

        return kingCheckedByColor.get(color);
    }

    private void calculateKingCheckedByColor(Color color) {
        boolean isChecked = kings.containsKey(color) && kings.get(color).isChecked(board);
        kingCheckedByColor.put(color, isChecked);
    }
}
