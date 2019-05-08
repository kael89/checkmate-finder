package org.kkarvounis.chasemate.chess.board;

import org.kkarvounis.chasemate.GenericHelper;
import org.kkarvounis.chasemate.chess.Color;
import org.kkarvounis.chasemate.chess.Direction;
import org.kkarvounis.chasemate.chess.Position;

import java.util.ArrayList;
import java.util.HashMap;

// TODO transform into Board Iterator ? (RowIterator, ColumnIterator etc)
public class Traverser {
    /**
     * The amount that should be added to a position's row/column in order
     * to move towards a specific direction
     */
    public static final HashMap<Direction, PositionDiff> POSITION_DIFF_BY_DIRECTION = new HashMap<Direction, PositionDiff>() {{
        put(Direction.right, new PositionDiff(0, 1));
        put(Direction.left, new PositionDiff(0, -1));
        put(Direction.up, new PositionDiff(1, 0));
        put(Direction.down, new PositionDiff(-1, 0));
        put(Direction.up_right, new PositionDiff(1, 1));
        put(Direction.down_left, new PositionDiff(-1, -1));
        put(Direction.down_right, new PositionDiff(-1, 1));
        put(Direction.up_left, new PositionDiff(1, -1));
    }};
    public static final HashMap<PositionDiff, Direction> DIRECTION_BY_POSITION_DIFF = GenericHelper.reverse(POSITION_DIFF_BY_DIRECTION);

    private Board board;
    private Position start;
    private Position currentPosition;
    private ArrayList<Direction> directions;
    private int range = Board.SIZE;
    private Color captureColor;

    Traverser(Board board) {
        this.board = board;
    }

    Traverser startFrom(Position position) {
        reset();
        start = position.clone();

        return this;
    }

    public Traverser toDirection(Direction direction) {
        addDirection(direction);
        return this;
    }

    public Traverser toDirections(Direction[] directions) {
        for (Direction direction : directions) {
            this.addDirection(direction);
        }
        return this;
    }

    public Traverser range(int range) {
        this.range = range;
        return this;
    }

    public Traverser capture(Color color) {
        captureColor = color;
        return this;
    }

    public ArrayList<Position> get() {
        ArrayList<Position> positions = new ArrayList<>();
        directions.forEach(direction -> positions.addAll(traverseByDirection(direction)));

        reset();
        return positions;
    }

    private void reset() {
        start = null;
        directions = new ArrayList<>();
        range = Board.SIZE;
        captureColor = null;
    }

    private void addDirection(Direction direction) {
        GenericHelper.addUnique(directions, direction);
    }

    private ArrayList<Position> traverseByDirection(Direction direction) {
        ArrayList<Position> positions = new ArrayList<>();
        // TODO find better way to ensure that startForm() is called first
        if (start == null) {
            return positions;
        }

        resetPosition();
        advancePosition(direction);
        int currentRange = 0;
        while (currentRange < range && Board.contains(currentPosition)) {
            if (canCapturePosition()) {
                positions.add(currentPosition);
            }
            if (isTraversalBlocked()) {
                break;
            }

            advancePosition(direction);
            currentRange++;
        }

        return positions;
    }

    private void resetPosition() {
        currentPosition = start.clone();
    }

    private void advancePosition(Direction direction) {
        PositionDiff diff = POSITION_DIFF_BY_DIRECTION.get(direction);
        currentPosition = currentPosition.clone();
        currentPosition.row += diff.rowDiff;
        currentPosition.column += diff.columnDiff;
    }

    private boolean canCapturePosition() {
        if (!isTraversalBlocked()) {
            return true;
        }

        return board.getPieceAt(currentPosition).hasColor(captureColor);
    }

    private boolean isTraversalBlocked() {
        return board.hasPieceAtPosition(currentPosition);
    }
}
