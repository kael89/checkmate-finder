package org.kkarvounis.checkmate.chess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class PositionTest {
    private static final int ROW = 6;
    private static final int COLUMN = 4;
    private static final String NAME = "E7";

    private Position position;

    @BeforeEach
    void setUp() {
        position = new Position(ROW, COLUMN);
    }

    @Test
    void testPositionFromString() {
        assertEquals(position.row, ROW);
        assertEquals(position.column, COLUMN);
    }

    @Test
    void testPositionFromCoordinates() {
        Position position = new Position(NAME);
        assertEquals(position.row, ROW);
        assertEquals(position.column, COLUMN);
    }

    @Test
    void testClone() {
        Position clone = position.clone();
        assertEquals(clone, position);
        assertNotSame(clone, position);
    }

    @ParameterizedTest
    @CsvSource({
            "A1, B1, 1, right",
            "F6, F4, 2, left",
            "A1, A6, 5, up",
            "D7, D1, 6, down",
            "D4, F6, 2, up_right",
            "G7, D3, 4, down_left",
            "D3, D1, 2, down_right",
            "F6, H8, 2, up_left",
            "H8, H8, 0",
    })
    void testToString(ArgumentsAccessor args) {
        Position source = args.get(0, Position.class);
        Position target = args.get(1, Position.class);
        int expectedResult = args.getInteger(2);

        assertEquals(expectedResult, source.distanceTo(target));
        assertEquals(expectedResult, target.distanceTo(source));
    }
}