package org.kkarvounis.checkmatefinder.chess.piece;

import org.kkarvounis.checkmatefinder.chess.Color;
import org.kkarvounis.checkmatefinder.chess.Position;
import org.kkarvounis.checkmatefinder.chess.piece.Mover.KnightMover;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Knight extends AbstractPiece {
    public static final KnightMover mover = new KnightMover();

    @JsonCreator
    public Knight(@JsonProperty("color") Color color, @JsonProperty("position") Position position) {
        super(color, position, mover);
    }
}
