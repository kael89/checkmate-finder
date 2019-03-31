package org.kkarvounis.checkmate.chess.piece.Mover;

import org.kkarvounis.checkmate.GenericHelper;
import org.kkarvounis.checkmate.chess.Color;
import org.kkarvounis.checkmate.chess.Direction;
import org.kkarvounis.checkmate.chess.Move;
import org.kkarvounis.checkmate.chess.Position;
import org.kkarvounis.checkmate.chess.board.Board;
import org.kkarvounis.checkmate.chess.piece.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PawnMover implements MoverInterface {
    private static final Class[] promotionClasses = {
            Bishop.class,
            Knight.class,
            Queen.class,
            Rook.class
    };
    private final int startingRow;
    private final int promotionRow;
    private final Direction moveDirection;
    private final Direction[] captureDirections;

    public PawnMover(Color color) {
        if (color == Color.white) {
            startingRow = 1;
            promotionRow = 6;
            moveDirection = Direction.up;
            captureDirections = new Direction[] {
                    Direction.up_left, Direction.up_right
            };
        } else {
            startingRow = 6;
            promotionRow = 1;
            moveDirection = Direction.down;
            captureDirections = new Direction[] {
                    Direction.down_left, Direction.down_right
            };
        }
    }

    @Override
    public ArrayList<Move> detectMoves(Board board, AbstractPiece piece) {
        ArrayList<Move> moves = piece.getMovesFromPositions(detectMovePositions(board, piece));
        if (!isNextMoveAPromotion(piece.getPosition())) {
            return moves;
        }

        ArrayList<Move> promotions = new ArrayList<>();
        moves.forEach(move -> {
            for (Class<AbstractPiece> promotionClass : promotionClasses) {
                Move newMove = move.clone();
                newMove.promotionTo(promotionClass);
                promotions.add(newMove);
            }
        });

        return promotions;
    }

    private ArrayList<Position> detectMovePositions(Board board, AbstractPiece piece) {
        Position position = piece.getPosition();
        int range = isInStartingPosition(position) ? 2 : 1;

        ArrayList<Position> movePositions = board
                .startFrom(position)
                .toDirection(moveDirection)
                .range(range)
                .get();

        ArrayList<Position> capturePositions = board
                .startFrom(position)
                .toDirections(captureDirections)
                .range(1)
                .capture(piece.getCaptureColor())
                .get()
                .stream()
                .filter(currentPosition -> {
                    if (!board.hasPieceAtPosition(currentPosition)) {
                        return false;
                    }
                    // TODO is this check really required, since capture has been enabled in traverser??
                    return piece.hasOppositeColor(board.getPieceAt(currentPosition));
                })
                .collect(Collectors.toCollection(ArrayList::new));


        return GenericHelper.merge(movePositions, capturePositions);
    }

    private boolean isInStartingPosition(Position position) {
        return position.row == startingRow;
    }

    private boolean isNextMoveAPromotion(Position position) {
        return position.row == promotionRow;
    }

    @Override
    public boolean canCapture(Board board, AbstractPiece piece, Position target) {
        return detectMovePositions(board, piece).contains(target);
    }
}
