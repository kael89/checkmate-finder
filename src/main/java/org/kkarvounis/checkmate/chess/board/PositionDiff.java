package org.kkarvounis.checkmate.chess.board;

public class PositionDiff {
    public int rowDiff;
    public int columnDiff;

    public PositionDiff(int rowDiff, int columnDiff) {
        this.rowDiff = rowDiff;
        this.columnDiff = columnDiff;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        PositionDiff that = (PositionDiff) o;
        return that.rowDiff == rowDiff && that.columnDiff == columnDiff;
    }

    @Override
    public int hashCode() {
        return 4 * rowDiff + columnDiff;
    }
}
