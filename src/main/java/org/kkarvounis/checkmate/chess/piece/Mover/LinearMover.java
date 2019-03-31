package org.kkarvounis.checkmate.chess.piece.Mover;

import org.kkarvounis.checkmate.chess.Direction;
import org.kkarvounis.checkmate.chess.ChessHelper;
import org.kkarvounis.checkmate.chess.Move;
import org.kkarvounis.checkmate.chess.Position;
import org.kkarvounis.checkmate.chess.board.Board;
import org.kkarvounis.checkmate.chess.piece.AbstractPiece;

import java.util.ArrayList;
import java.util.Arrays;

public class LinearMover implements MoverInterface {
    private Direction[] directions;
    private int range;

    public LinearMover(Direction[] directions, int range) {
        this.directions = directions;
        this.range = range;
    }

    @Override
    public ArrayList<Move> detectMoves(Board board, AbstractPiece piece) {
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
