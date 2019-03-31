package org.kkarvounis.checkmate.chess.piece;

import org.kkarvounis.checkmate.chess.Color;
import org.kkarvounis.checkmate.chess.Direction;
import org.kkarvounis.checkmate.chess.Position;
import org.kkarvounis.checkmate.chess.board.Board;
import org.kkarvounis.checkmate.chess.piece.Mover.LinearMover;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rook extends AbstractPiece {
    private static Direction[] directions = Direction.orthogonal();
    private static final int range = Board.SIZE;
    private static final LinearMover mover = new LinearMover(directions, range);

    @JsonCreator
    public Rook(@JsonProperty("color") Color color, @JsonProperty("position") Position position) {
        super(color, position, mover);
    }
}
