package org.kkarvounis.chasemate.chess.piece;

import org.kkarvounis.chasemate.chess.Color;
import org.kkarvounis.chasemate.chess.Direction;
import org.kkarvounis.chasemate.chess.Position;
import org.kkarvounis.chasemate.chess.board.Board;
import org.kkarvounis.chasemate.chess.piece.Mover.LinearMover;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Iterator;

public class King extends AbstractPiece {
    private static Direction[] directions = Direction.values();
    private static final int range = 1;
    private static final LinearMover mover = new LinearMover(directions, range);

    @JsonCreator
    public King(@JsonProperty("color") Color color, @JsonProperty("position") Position position) {
        super(color, position, mover);
    }

    public boolean isChecked(Board board) {
        Iterator<AbstractPiece> enemyPieceIt = board.iterator(getCaptureColor());
        while (enemyPieceIt.hasNext()) {
            if (enemyPieceIt.next().canCapture(board, position)) {
                return true;
            }
        }

        return  false;
    }
}
