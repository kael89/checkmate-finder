package org.kkarvounis.checkmatefinder.chess.tree;

import org.kkarvounis.checkmatefinder.chess.Color;
import org.kkarvounis.checkmatefinder.chess.Move;
import org.kkarvounis.checkmatefinder.chess.board.Board;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class GameTree extends AbstractChessTree {
    @JsonCreator
    public GameTree(
            @JsonProperty("board") Board board,
            @JsonProperty("startingColor") Color startingColor,
            @JsonProperty("depth")  int depth
    ) {
        super(board, startingColor, depth);
    }

    @Override
    public Map<String, Object> get() {
        Map<String, Object> tree = new HashMap<>();
        addNodesToTree(tree, board, startingColor);

        return tree;
    }

    private void addNodesToTree(Map<String, Object> tree, Board board, Color playerColor) {
        incrementDepth();

        if (currentDepth > depth) {
            decrementDepth();
            return;
        }

        for (Move move : board.detectMoves(playerColor)) {
            String key = move.toString();
            Map<String, Object> subTree = new HashMap<>();

            tree.put(key, subTree);
            addNodesToTree(subTree, board.doMove(move), playerColor.opposite());
        }

        decrementDepth();
    }
}
