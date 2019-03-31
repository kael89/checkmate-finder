package org.kkarvounis.checkmate.chess.piece;

import org.kkarvounis.checkmate.chess.Color;
import org.kkarvounis.checkmate.chess.Position;
import org.kkarvounis.checkmate.chess.piece.Mover.MoverInterface;
import org.kkarvounis.checkmate.chess.piece.Mover.PawnMover;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.Mockito.withSettings;

class AbstractPieceTest {
    private static final Color COLOR = Color.black;
    private static final String POSITION_NAME = "H4";
    private static final Position POSITION = new Position(POSITION_NAME);
    private static final MoverInterface MOVER = new PawnMover(COLOR);

    private AbstractPiece piece;

    private static AbstractPiece createMock(Color color, Position position) {
        return Mockito.mock(
                AbstractPiece.class,
                withSettings().useConstructor(color, position, MOVER).defaultAnswer(CALLS_REAL_METHODS)
        );
    }

    @BeforeEach
    void setUp() {
        piece = createMock(COLOR, POSITION);
    }

    @Test
    void testGetColor() {
        Assertions.assertEquals(COLOR, piece.getColor());
    }

    @Test
    void testHasColor() {
        assertTrue(piece.hasColor(COLOR));
        assertFalse(piece.hasColor(COLOR.opposite()));
    }

    @ParameterizedTest
    @CsvSource({
            "black, false",
            "white, true",
    })
    void testIsWhite(ArgumentsAccessor args) {
        // TODO create helper for getting color from string argument?
        Color color = Color.valueOf(args.getString(0));
        AbstractPiece piece = createMock(color, POSITION);
        boolean expectedResult = args.getBoolean(1);

        assertEquals(expectedResult, piece.isWhite());
    }

    @ParameterizedTest
    @CsvSource({
            "black, true",
            "white, false",
    })
    void testIsBlack(ArgumentsAccessor args) {
        Color color = Color.valueOf(args.getString(0));
        AbstractPiece piece = createMock(color, POSITION);
        boolean expectedResult = args.getBoolean(1);

        assertEquals(expectedResult, piece.isBlack());
    }

    @Test
    void testGetPosition() {
        Assertions.assertEquals(new Position(POSITION_NAME), piece.getPosition());
    }

    @Test
    void testSetPosition() {
        Position newPosition = new Position("F5");
        piece.setPosition(newPosition);

        Assertions.assertEquals(newPosition, piece.getPosition());
    }

    @Test
    void testToString() {
        Pawn pawn = new Pawn(COLOR, POSITION);
        assertEquals(COLOR + " pawn at " + POSITION, pawn.toString());
    }
}