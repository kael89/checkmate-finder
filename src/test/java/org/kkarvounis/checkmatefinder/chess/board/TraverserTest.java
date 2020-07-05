package org.kkarvounis.checkmatefinder.chess.board;

import org.junit.jupiter.api.BeforeEach;

class TraverserTest {
    private Board board;
    private Traverser traverser;

    @BeforeEach
    void setUp() {
        // TODO init board from .json
        traverser = new Traverser(board);
    }

    // TODO implement
//    @Test
//    void testGet(Position start, Direction direction, ArrayList<Position> expectedResult) {
//        ArrayList<Position> positions = traverser
//                .startFrom(start)
//                .toDirection(direction)
//                .get();
//
//        assertEquals(expectedResult, positions);
//    }
//
//    @Test
//    void testCapture(Position start, Direction direction, ArrayList<Position> expectedResult) {
//        // TODO implement
//    }
}