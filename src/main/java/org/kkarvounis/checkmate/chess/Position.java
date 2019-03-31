package org.kkarvounis.checkmate.chess;

import org.kkarvounis.checkmate.json.JsonSerializableInterface;
import com.fasterxml.jackson.annotation.JsonValue;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Position implements Cloneable, JsonSerializableInterface {
    public int row;
    public int column;

    public Position(String name) {
        this.row = name.charAt(1) - '1';
        this.column = name.toUpperCase().charAt(0) - 'A';
    }

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public Position clone() {
        try {
            return (Position) super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO better implementation?
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        Position that = (Position) o;
        return that.row == row && that.column == column;
    }

    @Override
    public int hashCode() {
        return 8 * row + column;
    }

    public static String coordinatesToString(int row, int column) {
        return Character.toString((char) ('A' + column)) + (char) ('1' + row);
    }

    @JsonValue
    @Override
    public String toString() {
        return coordinatesToString(row, column);
    }

    public int distanceTo(Position target) {
        return max(abs(row - target.row), abs(column - target.column));
    }
}
