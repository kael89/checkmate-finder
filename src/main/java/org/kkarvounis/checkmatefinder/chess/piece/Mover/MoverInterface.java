package org.kkarvounis.checkmatefinder.chess.piece.Mover;

import org.kkarvounis.checkmatefinder.chess.Move;
import org.kkarvounis.checkmatefinder.chess.Position;
import org.kkarvounis.checkmatefinder.chess.board.Board;
import org.kkarvounis.checkmatefinder.chess.piece.AbstractPiece;

import java.util.ArrayList;

public interface MoverInterface {
    ArrayList<Move> detectMoves(Board board, AbstractPiece piece);

    boolean canCapture(Board board, AbstractPiece piece, Position target);
}
