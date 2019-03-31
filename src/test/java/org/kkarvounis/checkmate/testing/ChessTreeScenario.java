package org.kkarvounis.checkmate.testing;

import org.kkarvounis.checkmate.chess.tree.AbstractChessTree;
import org.kkarvounis.checkmate.json.JsonSerializableInterface;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NON_PRIVATE;

@JsonAutoDetect(fieldVisibility = NON_PRIVATE)
class ChessTreeData {
    public AbstractChessTree chessTree;
    public Map<String, Object> getResults;
}

// TODO abstract scenario classes?
public class ChessTreeScenario {
    public String id;
    public String name;
    public String description;
    public ChessTreeData data;

    private static ChessTreeScenario[] fromJsonArray(File json) {
        return (ChessTreeScenario[]) JsonSerializableInterface.fromJson(json, ChessTreeScenario[].class);
    }

    static Iterator<ChessTreeScenario> iterator(File dataFile) {
        ChessTreeScenario[] scenarios = ChessTreeScenario.fromJsonArray(dataFile);
        return Arrays.asList(scenarios).iterator();
    }

    public AbstractChessTree getChessTree() {
        return data.chessTree;
    }

    public Map<String, Object>  getGetResults() {
        return data.getResults;
    }
}
