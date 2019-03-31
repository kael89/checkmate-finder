package org.kkarvounis.checkmate.chess.piece.Mover;

import org.kkarvounis.checkmate.chess.Move;
import org.kkarvounis.checkmate.chess.Position;
import org.kkarvounis.checkmate.chess.board.Board;
import org.kkarvounis.checkmate.chess.board.PositionDiff;
import org.kkarvounis.checkmate.chess.piece.AbstractPiece;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class KnightMover implements MoverInterface {
    /**
     * The amount that should be added to a knight's row/column in order
     * to perform one of its available moves
     */
    private static final PositionDiff[] POSITION_DIFF = {
            new PositionDiff(2, 1),
            new PositionDiff(2, -1),
            new PositionDiff(-2, 1),
            new PositionDiff(-2, -1),
            new PositionDiff(1, 2),
            new PositionDiff(1, -2),
            new PositionDiff(-1, 2),
            new PositionDiff(-1, -2),
    };

    @Override
    public ArrayList<Move> detectMoves(Board board, AbstractPiece piece) {
        ArrayList<Position> positions = new ArrayList<>();

        Position position = piece.getPosition();
        for (PositionDiff diff : POSITION_DIFF) {
            Position currentPosition = position.clone();
            currentPosition.row += diff.rowDiff;
            currentPosition.column += diff.columnDiff;

            if (Board.contains(currentPosition) && canCapturePieceBasedOnColor(board, piece, currentPosition)) {
                positions.add(currentPosition);
            }
        }

        return piece.getMovesFromPositions(positions);
    }

    @Override
    public boolean canCapture(Board board, AbstractPiece piece, Position target) {
        return isPositionInRange(piece, target) && canCapturePieceBasedOnColor(board, piece, target);
    }

    private static boolean isPositionInRange(AbstractPiece piece, Position target) {
        Position position = piece.getPosition();
        PositionDiff diff = new PositionDiff(
                abs(target.row - position.row),
                abs(target.column - position.column)
        );

        return (diff.equals(new PositionDiff(1, 2))
                || diff.equals(new PositionDiff(2, 1)));
    }

    private static boolean canCapturePieceBasedOnColor(Board board, AbstractPiece piece, Position position) {
        AbstractPiece enemyPiece = board.getPieceAt(position);
        return enemyPiece == null || piece.hasOppositeColor(enemyPiece);
    }
}
