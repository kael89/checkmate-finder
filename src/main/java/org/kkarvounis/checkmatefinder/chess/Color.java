package org.kkarvounis.checkmatefinder.chess;

public enum Color {
    black,
    white;

    public Color opposite() {
        return this == Color.black ? Color.white : Color.black;
    }
}
