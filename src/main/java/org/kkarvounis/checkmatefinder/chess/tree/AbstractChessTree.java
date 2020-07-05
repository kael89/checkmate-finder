package org.kkarvounis.checkmatefinder.chess.tree;

import org.kkarvounis.checkmatefinder.json.JsonSerializableInterface;
import org.kkarvounis.checkmatefinder.chess.Color;
import org.kkarvounis.checkmatefinder.chess.board.Board;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @Type(value = GameTree.class, name = "gameTree"),
        @Type(value = ForcedMateTree.class, name = "forcedMateTree"),
})
abstract public class AbstractChessTree implements JsonSerializableInterface {
    private static final int MAX_DEPTH = 6;

    protected Board board;
    protected Color startingColor;
    protected int depth;
    protected int currentDepth = 0;

    AbstractChessTree(Board board, Color startingColor, int depth) {

        this.board = board;
        this.startingColor = startingColor;
        this.depth = sanitizeDepth(depth);
    }

    public static <T> T fromJson(String json) {
        return (T) JsonSerializableInterface.fromJson(json, GameTree.class);
    }

    @Override
    public String toString() {
        return startingColor.toString() + " plays first, depth: " + depth;
    }

    private int sanitizeDepth(int depth) {
        if (depth < 1 || depth > MAX_DEPTH) {
            depth = MAX_DEPTH;
        }

        return depth;
    }

    void resetDepth() {
        currentDepth = 0;
    }

    void incrementDepth() {
        currentDepth++;
    }

    void decrementDepth() {
        currentDepth--;
    }

    boolean isFirstMove() {
        return currentDepth == 1;
    }

    private boolean isLastMove() {
        return currentDepth == depth;
    }

    boolean isStartingPlayerMove() {
        return currentDepth % 2 == 1;
    }

    boolean isEnemyPlayerMove() {
        return !isStartingPlayerMove();
    }

    boolean isLastMoveOfStartingPlayer() {
        int offset = depth % 2 == 0 ? 1 : 0;
        return currentDepth == depth - offset;
    }

    abstract public Map<String, Object> get();
}
