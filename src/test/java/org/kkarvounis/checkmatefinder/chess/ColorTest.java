package org.kkarvounis.checkmatefinder.chess;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @ParameterizedTest
    @CsvSource({
            "white, black",
            "black, white",
    })
    void testOpposite(ArgumentsAccessor args) {
        Color color = Color.valueOf(args.getString(0));
        Color expectedResult = Color.valueOf(args.getString(1));

        assertEquals(expectedResult, color.opposite());
    }
}