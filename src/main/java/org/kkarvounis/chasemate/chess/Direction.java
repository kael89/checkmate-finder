package org.kkarvounis.chasemate.chess;

public enum Direction {
    right,
    left,
    up,
    down,
    up_right,
    down_left,
    down_right,
    up_left;

    public static Direction[] orthogonal() {
        return new Direction[]{
                right,
                left,
                up,
                down,
        };
    }

    public static Direction[] diagonal() {
        return new Direction[]{
                up_right,
                up_left,
                down_right,
                down_left,
        };
    }
}
