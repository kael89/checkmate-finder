package org.kkarvounis.checkmate.chess;

public enum Color {
    black,
    white;

    public Color opposite() {
        return this == Color.black ? Color.white : Color.black;
    }
}
