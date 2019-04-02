package org.kkarvounis.checkmate.chess.piece.Mover;

import org.kkarvounis.checkmate.chess.Direction;
import org.kkarvounis.checkmate.chess.ChessHelper;
import org.kkarvounis.checkmate.chess.Move;
import org.kkarvounis.checkmate.chess.Position;
import org.kkarvounis.checkmate.chess.board.Board;
import org.kkarvounis.checkmate.chess.piece.AbstractPiece;

import java.util.ArrayList;
import java.util.Arrays;

public class LinearMover extends AbstractMover {
    private Direction[] directions;
    private int range;

    public LinearMover(Direction[] directions, int range) {
        this.directions = directions;
        this.range = range;
    }

    boolean isBlockedByMove(AbstractPiece piece, Move move) {
        Direction directionToTarget = ChessHelper.detectDirection(piece.getPosition(), move.target);
        return hasMoveDirection(directionToTarget);
    }

    boolean isUnblockedByMove(AbstractPiece piece, Move move) {
        Direction directionToSource = ChessHelper.detectDirection(piece.getPosition(), move.getSource());
        // TODO this has been calculated again in shouldCalculateMoves
        return hasMoveDirection(directionToSource) && !isBlockedByMove(piece, move);
    }

    ArrayList<Move> calculateMoves(Board board, AbstractPiece piece) {
        ArrayList<Position> positions = board
                .startFrom(piece.getPosition())
                .toDirections(directions)
                .range(range)
                .capture(piece.getCaptureColor())
                .get();

        return piece.getMovesFromPositions(positions);
    }

    @Override
    public boolean canCapture(Board board, AbstractPiece piece, Position target) {
        Position source = piece.getPosition();
        Direction direction = ChessHelper.detectDirection(source, target);

        if (!hasMoveDirection(direction) || !isPositionInRange(source, target)) {
            // Target position is out of reach
            return false;
        }

        ArrayList<Position> positions = board
                .startFrom(source)
                .toDirection(direction)
                .capture(piece.getCaptureColor())
                .get();

        return positions.contains(target);
    }

    private boolean hasMoveDirection(Direction direction) {
        return Arrays.asList(directions).contains(direction);
    }

    private boolean isPositionInRange(Position source, Position target) {
        return source.distanceTo(target) <= range;
    }
}
