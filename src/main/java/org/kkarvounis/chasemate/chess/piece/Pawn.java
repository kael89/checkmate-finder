package org.kkarvounis.chasemate.chess.piece;

import org.kkarvounis.chasemate.chess.Color;
import org.kkarvounis.chasemate.chess.Position;
import org.kkarvounis.chasemate.chess.piece.Mover.PawnMover;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class Pawn extends AbstractPiece {
    private static final Map<Color, PawnMover> movers = new HashMap<Color, PawnMover>() {{
        put(Color.black, new PawnMover(Color.black));
        put(Color.white, new PawnMover(Color.white));
    }};

    @JsonCreator
    public Pawn(@JsonProperty("color") Color color, @JsonProperty("position") Position position) {
        super(color, position, movers.get(color));
    }
}
