package org.kkarvounis.chasemate.chess.piece;

import org.kkarvounis.chasemate.json.JsonSerializableInterface;
import org.kkarvounis.chasemate.chess.Color;
import org.kkarvounis.chasemate.chess.Move;
import org.kkarvounis.chasemate.chess.Position;
import org.kkarvounis.chasemate.chess.board.Board;
import org.kkarvounis.chasemate.chess.piece.Mover.MoverInterface;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;

@JsonAutoDetect(isGetterVisibility= Visibility.NONE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @Type(value = Bishop.class, name = "bishop"),
        @Type(value = King.class, name = "king"),
        @Type(value = Knight.class, name = "knight"),
        @Type(value = Pawn.class, name = "pawn"),
        @Type(value = Queen.class, name = "queen"),
        @Type(value = Rook.class, name = "rook"),
})
abstract public class AbstractPiece implements Cloneable, JsonSerializableInterface {
    protected Color color;
    protected Position position;
    protected MoverInterface mover;

    AbstractPiece(Color color, Position position, MoverInterface mover) {
        this.color = color;
        this.position = position;
        this.mover = mover;
    }

    public static <T> T fromJson(String json) {
        return (T) JsonSerializableInterface.fromJson(json, AbstractPiece.class);
    }

    @Override
    public AbstractPiece clone() {
        try {
            return (AbstractPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO better implementation?
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

        AbstractPiece that = (AbstractPiece) o;
        return that.color == color && that.position == position;
    }

    @Override
    public int hashCode() {
        return 8 ^ 2 * color.ordinal() + 8 * position.row + position.column;
    }

    @Override
    public String toString() {
        return String.join(" ", new String[]{
                color.toString(),
                getName(),
                "at",
                getPosition().toString()
        });
    }

    public Color getColor() {
        return color;
    }

    // TODO use this method among project
    public Color getCaptureColor() {
        return color.opposite();
    }

    public boolean hasColor(Color color) {
        return this.color == color;
    }

    public boolean hasOppositeColor(AbstractPiece piece) {
        return this.color != piece.color;
    }

    public boolean isWhite() {
        return hasColor(Color.white);
    }

    public boolean isBlack() {
        return hasColor(Color.black);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    private String getName() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    public ArrayList<Move> detectMoves(Board board) {
        return mover.detectMoves(board, this);
    }

    public ArrayList<Move> getMovesFromPositions(ArrayList<Position> positions) {
        ArrayList<Move> moves = new ArrayList<>();
        positions.forEach(position -> moves.add(new Move(this, position)));

        return moves;
    }

    public boolean canCapture(Board board, Position target) {
        return mover.canCapture(board, this, target);
    }
}
