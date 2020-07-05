package org.kkarvounis.checkmatefinder.app.api;

import org.kkarvounis.checkmatefinder.chess.Color;
import org.kkarvounis.checkmatefinder.chess.board.Board;
import org.kkarvounis.checkmatefinder.chess.tree.ForcedMateTree;
import org.kkarvounis.checkmatefinder.chess.tree.GameTree;

import java.net.URLDecoder;
import java.util.HashMap;

public class GetTreeApi extends AbstractApi {
    public HashMap<String, Object> run(HashMap<String, String> input) {
        // TODO can also get from json decoder?
        String error = "";
        Object data = new Object();

        try {
            Board board = Board.fromJson(URLDecoder.decode(input.get("board"), "UTF-8"));
            Color startingColor = Color.valueOf(input.get("startingColor"));
            int depth = Integer.parseInt(input.get("depth"));

            switch (input.get("type")) {
                case "game":
                    data = (new GameTree(board, startingColor, depth)).get();
                    break;
                case "forcedMate":
                    data = (new ForcedMateTree(board, startingColor, depth)).get();
                    break;
                default:
                    throw new Exception("Invalid tree type");
            }
        } catch (Exception e) {
            error = e.getMessage();
        }

        return buildOutput(data, error);
    }
}
