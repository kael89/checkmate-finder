package org.kkarvounis.checkmate.chess.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.kkarvounis.checkmate.GenericHelper;
import org.kkarvounis.checkmate.chess.Color;
import org.kkarvounis.checkmate.chess.Move;
import org.kkarvounis.checkmate.chess.Position;
import org.kkarvounis.checkmate.chess.piece.AbstractPiece;
import org.kkarvounis.checkmate.chess.piece.Pawn;
import org.kkarvounis.checkmate.testing.BoardScenario;
import org.kkarvounis.checkmate.testing.TestingHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class BoardTest {
    private static final String DATA_FILE = "board.json";
    private static final Position EMPTY_POSITION = new Position("A6");

    private Board board;
    private ArrayList<AbstractPiece> pieces;

    @BeforeEach
    void setUp() {
        ArrayList<AbstractPiece> blackPieces = new ArrayList<AbstractPiece>() {{
            add(new Pawn(Color.black, new Position("D4")));
        }};
        ArrayList<AbstractPiece> whitePieces = new ArrayList<AbstractPiece>() {{
            add(new Pawn(Color.white, new Position("E5")));
        }};
        pieces = GenericHelper.merge(blackPieces, whitePieces);

        board = new Board(GenericHelper.merge(blackPieces, whitePieces));
    }

    @Test
    void testGetSquares() {
        // TODO extract into Board.getPiecesByPosition()?
        Map<String, AbstractPiece> piecesByPosition = new HashMap<>();
        Iterator<AbstractPiece> pit= board.iterator();
        while (pit.hasNext()) {
            AbstractPiece piece = pit.next();
            piecesByPosition.put(piece.getPosition().toString(), piece);
        }

        AbstractPiece[][] squares = board.getSquares();
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                String positionName = Position.coordinatesToString(i, j);
                assertEquals(piecesByPosition.get(positionName), squares[i][j]);
            }
        }
    }

    @Test
    void testGetPieces() {
        assertEquals(pieces, board.getPieces());
    }

    @Test
    void testGetPieceAt() {
        Iterator<AbstractPiece> it= board.iterator();
        while (it.hasNext()) {
            AbstractPiece piece = it.next();
            assertEquals(piece, board.getPieceAt(piece.getPosition()));
        }

        assertNull(board.getPieceAt(EMPTY_POSITION));
    }

    @Test
    void testHasPieceAtPosition() {
        assertTrue(board.hasPieceAtPosition(new Position("E5")));
        assertFalse(board.hasPieceAtPosition(EMPTY_POSITION));
    }

    @Test
    void testStartFrom() {
        // TODO implement
    }

    @ParameterizedTest(name = "{3}")
    @MethodSource
    void testDetectMoves(Board board, Color playerColor, ArrayList<Move> expectedMoves, String description) {
        assertTrue(GenericHelper.equalContents(expectedMoves, board.detectMoves(playerColor)));
    }

    static ArrayList<Arguments> testDetectMoves() {
        File dataFile = TestingHelper.getDataFile(DATA_FILE);
        Iterator<BoardScenario> it = BoardScenario.iterator(dataFile);

        ArrayList<Arguments> data = new ArrayList<>();
        while (it.hasNext()) {
            BoardScenario scenario = it.next();
            for (Map.Entry<Color, ArrayList<Move>> entry : scenario.getAvailableMoves().entrySet()) {
                Color playerColor = entry.getKey();
                ArrayList<Move> expectedMoves = entry.getValue();
                String description = scenario.name + ", " + playerColor.toString() + " plays";

                data.add(arguments(scenario.getBoard(), playerColor, expectedMoves, description));
            }
        }

        return data;
    }

    @ParameterizedTest
    @CsvSource({
            "D4, D5",
            "D4, E5",
            "E5, E4",
            "E5, D4",
    })
    void testDoMove(ArgumentsAccessor args) {
        Position origin = args.get(0, Position.class);
        Position target = args.get(1, Position.class);

        ArrayList<AbstractPiece> newPieces = new ArrayList<>();
        AbstractPiece movedPiece = null;

        for (AbstractPiece piece : board.getPieces()) {
            AbstractPiece newPiece = piece.clone();
            Position newPiecePosition = newPiece.getPosition();

            if (newPiecePosition.equals(target)) {
                continue;
            }
            if (newPiecePosition.equals(origin)) {
                movedPiece = piece;
                newPiece.setPosition(target);
            }

            newPieces.add(newPiece);
        }

        Board newBoard = new Board(newPieces);
        Move move = new Move(movedPiece, target);

        assertEquals(newBoard, board.doMove(move));
    }

    @Test
    void testIsKingChecked() {
        // TODO implement
    }
}