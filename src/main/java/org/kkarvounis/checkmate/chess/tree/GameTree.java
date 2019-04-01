package org.kkarvounis.checkmate.chess.tree;

import org.kkarvounis.checkmate.chess.Color;
import org.kkarvounis.checkmate.chess.GameState;
import org.kkarvounis.checkmate.chess.Move;
import org.kkarvounis.checkmate.chess.board.Board;
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
        addNodesToTree(tree, initialState);

        return tree;
    }

    private void addNodesToTree(Map<String, Object> tree, GameState gameState) {
        incrementDepth();

        if (currentDepth > depth) {
            decrementDepth();
            return;
        }

        for (Move move : gameState.getMoves()) {
            String key = move.toString();
            Map<String, Object> subTree = new HashMap<>();

            tree.put(key, subTree);
            addNodesToTree(subTree, gameState.deriveNewState(move));
        }

        decrementDepth();
    }
}
