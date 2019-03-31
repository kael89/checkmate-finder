package org.kkarvounis.checkmate.testing;

import org.kkarvounis.checkmate.GenericHelper;
import org.kkarvounis.checkmate.chess.Move;
import org.kkarvounis.checkmate.chess.Position;
import org.kkarvounis.checkmate.chess.board.Board;
import org.kkarvounis.checkmate.chess.piece.AbstractPiece;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(PER_CLASS)
abstract public class PieceBaseTest {
    private Iterator<BoardScenario> scenarioIterator() {
        File dataFile = TestingHelper.getDataFile(getDataFileName());
        return BoardScenario.iterator(dataFile);
    }

    @ParameterizedTest(name = "{3}")
    @MethodSource
    void testDetectMoves(Board board, AbstractPiece piece, ArrayList<Move> expectedMoves, String description) {
        // TODO create equal content arrays assertion
        Assertions.assertTrue(GenericHelper.equalContents(expectedMoves, piece.detectMoves(board)));
    }

    ArrayList<Arguments> testDetectMoves() {
        Iterator<BoardScenario> it = scenarioIterator();
        ArrayList<Arguments> data = new ArrayList<>();

        while (it.hasNext()) {
            BoardScenario scenario = it.next();
            for (Map.Entry<AbstractPiece, ArrayList<Move>> entry : scenario.getAvailableMovesPerPiece().entrySet()) {
                AbstractPiece piece = entry.getKey();
                ArrayList<Move> expectedMoves = entry.getValue();
                String description = scenario.name + ", " + piece.toString();

                data.add(arguments(scenario.data.board, piece, expectedMoves, description));
            }
        }

        return data;
    }

    @ParameterizedTest(name = "{4}")
    @MethodSource
    void testCanCapture(Board board, AbstractPiece piece, Position target, boolean expectedResult, String description) {
        assertSame(expectedResult, piece.canCapture(board, target));
    }

    ArrayList<Arguments> testCanCapture() {
        Iterator<BoardScenario> scenarioIterator = scenarioIterator();
        ArrayList<Arguments> data = new ArrayList<>();

        while (scenarioIterator.hasNext()) {
            BoardScenario scenario = scenarioIterator.next();
            for (Map.Entry<AbstractPiece, ArrayList<Position>> entry : scenario.getCapturePositionsPerPiece().entrySet()) {
                AbstractPiece piece = entry.getKey();
                ArrayList<Position> capturePositions = entry.getValue();

                // TODO create board square iterator ??
                for (int i = 0; i < Board.SIZE; i++) {
                    for (int j = 0; j < Board.SIZE; j++) {
                        Position position = new Position(i, j);
                        boolean expectedResult = capturePositions.contains(position);
                        String verb = expectedResult ? "can" : "cannot";
                        String description = String.join(" ", new String[]{
                                scenario.name + ",",
                                piece.toString(),
                                verb,
                                "capture",
                                position.toString()
                        });

                        data.add(arguments(scenario.data.board, piece, position, expectedResult, description));
                    }
                }
            }
        }

        return data;
    }

    abstract protected String getDataFileName();
}
