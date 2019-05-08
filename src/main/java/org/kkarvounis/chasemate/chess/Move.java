package org.kkarvounis.chasemate.chess;

import org.kkarvounis.chasemate.GenericHelper;
import org.kkarvounis.chasemate.chess.piece.*;

import java.util.HashMap;

public class Move implements Cloneable {
    private static final HashMap<Class, String> PROMOTION_TO_INITIAL = new HashMap<Class, String>() {{
        put(Bishop.class, "B");
        put(King.class, "K");
        put(Knight.class, "N");
        put(Pawn.class, "P");
        put(Queen.class, "Q");
        put(Rook.class, "R");
    }};
    private static final HashMap<String, Class> INITIAL_TO_PROMOTION = GenericHelper.reverse(PROMOTION_TO_INITIAL);

    public AbstractPiece piece;
    public Position target;
    public Class<AbstractPiece> promotion;

    public Move(AbstractPiece piece, Position target) {
        this.piece = piece;
        this.target = target;
    }

    public Move(AbstractPiece piece, String targetString) {
        this.piece = piece;
        this.parseTargetString(targetString);
    }

    private void parseTargetString(String targetString) {
        String[] parts = targetString.split("=");
        this.target = new Position(parts[0]);
        if (parts.length > 1) {
            this.promotion = INITIAL_TO_PROMOTION.get(parts[1]);
        }
    }

    @Override
    public Move clone() {
        try {
            return (Move) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        Move that = (Move) o;
        return that.piece.equals(piece)
                && that.target.equals(target)
                && that.target.equals(target)
                && that.promotion == promotion;
    }


    @Override
    public int hashCode() {
        Position source = piece.getPosition();
        return 8 ^ 3 * source.row + 8 ^ 2 * source.column + 8 * target.row + target.column;
    }

    @Override
    public String toString() {
        String result = piece.getPosition().toString() + '-' + target.toString();
        if (isPromotion()) {
            result += '=' + PROMOTION_TO_INITIAL.get(promotion);
        }

        return result;
    }

    public void promotionTo(Class<AbstractPiece> promotion) {
        this.promotion = promotion;
    }

    public boolean isPromotion() {
        return this.promotion != null;
    }
}
