package org.kkarvounis.checkmate.chess;

import org.kkarvounis.checkmate.chess.board.PositionDiff;
import org.kkarvounis.checkmate.chess.board.Traverser;

import static java.lang.Math.abs;

public class ChessHelper {
    public static Direction detectDirection(Position source, Position target) {
        int rowDiff = target.row - source.row;
        int columnDiff = target.column - source.column;
        if (rowDiff == 0 && columnDiff == 0) {
            // Comparing a position with itself
            return null;
        }

        int normalizer = (rowDiff != 0) ? abs(rowDiff) : abs(columnDiff);
        PositionDiff diff = new PositionDiff(rowDiff / normalizer, columnDiff / normalizer);

        return Traverser.DIRECTION_BY_POSITION_DIFF.get(diff);
    }
}
