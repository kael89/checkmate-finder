package org.kkarvounis.checkmate.testing;

import org.kkarvounis.checkmate.chess.tree.AbstractChessTree;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(PER_CLASS)
abstract public class ChessTreeBaseTest {
    @ParameterizedTest(name = "{2}")
    @MethodSource
    void testGet(AbstractChessTree chessTree, Map<String, Object> expectedResults, String description) {
        assertEquals(expectedResults, chessTree.get());
    }

    ArrayList<Arguments> testGet() {
        File dataFile = TestingHelper.getDataFile(getDataFileName());
        Iterator<ChessTreeScenario> it = ChessTreeScenario.iterator(dataFile);

        ArrayList<Arguments> data = new ArrayList<>();
        while (it.hasNext()) {
            ChessTreeScenario scenario = it.next();
            String description = scenario.name + ", " + scenario.getChessTree();

            data.add(arguments(scenario.getChessTree(), scenario.getGetResults(), description));
        }

        return data;
    }

    abstract protected String getDataFileName();
}
