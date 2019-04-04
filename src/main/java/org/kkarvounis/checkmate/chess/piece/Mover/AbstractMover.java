package org.kkarvounis.checkmate.chess.piece.Mover;

import org.kkarvounis.checkmate.chess.Move;
import org.kkarvounis.checkmate.chess.Position;
import org.kkarvounis.checkmate.chess.board.Board;
import org.kkarvounis.checkmate.chess.piece.AbstractPiece;

import java.util.ArrayList;

public abstract class AbstractMover {
    public ArrayList<Move> detectMoves(Board board, AbstractPiece piece) {
        return calculateMoves(board, piece);
    }

    public ArrayList<Move> detectMoves(
            Board board,
            AbstractPiece piece,
            ArrayList<Move> previousMoves,
            ArrayList<Move> lastPlayedMoves
    ) {
        return shouldCalculateMoves(piece, lastPlayedMoves) ? calculateMoves(board, piece) : previousMoves;
    }

    private boolean shouldCalculateMoves(AbstractPiece piece, ArrayList<Move> lastPlayedMoves) {
        if (lastPlayedMoves.size() < 2) {
            return true;
        }

        Move firstMove = lastPlayedMoves.get(0);
        Move secondMove = lastPlayedMoves.get(1);
        if (firstMove.target.equals(secondMove.target)) {
            lastPlayedMoves.remove(0);
        } else if (movedAtPreviousRound(piece, firstMove)) {
            return true;
        }

        // TODO refactor piece cloning to have different pieces per board?
        for (Move move : lastPlayedMoves) {
            if (isBlockedByMove(piece, move) || isUnblockedByMove(piece, move)) {
                return true;
            }
        }

        return false;
    }

    private boolean movedAtPreviousRound(AbstractPiece piece, Move moveAtPreviousRound) {
        return moveAtPreviousRound.target.equals(piece.getPosition());
    }

    abstract boolean isBlockedByMove(AbstractPiece piece, Move move);

    abstract boolean isUnblockedByMove(AbstractPiece piece, Move move);

    abstract ArrayList<Move> calculateMoves(Board board, AbstractPiece piece);

    public abstract boolean canCapture(Board board, AbstractPiece piece, Position target);
}
