package org.kkarvounis.checkmate.chess.piece.Mover;

import org.kkarvounis.checkmate.chess.Move;
import org.kkarvounis.checkmate.chess.Position;
import org.kkarvounis.checkmate.chess.board.Board;
import org.kkarvounis.checkmate.chess.piece.AbstractPiece;

import java.util.ArrayList;

public interface MoverInterface {
    ArrayList<Move> detectMoves(Board board, AbstractPiece piece);

    boolean canCapture(Board board, AbstractPiece piece, Position target);
}
