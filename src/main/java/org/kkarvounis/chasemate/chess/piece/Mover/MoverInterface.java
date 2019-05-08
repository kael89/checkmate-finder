package org.kkarvounis.chasemate.chess.piece.Mover;

import org.kkarvounis.chasemate.chess.Move;
import org.kkarvounis.chasemate.chess.Position;
import org.kkarvounis.chasemate.chess.board.Board;
import org.kkarvounis.chasemate.chess.piece.AbstractPiece;

import java.util.ArrayList;

public interface MoverInterface {
    ArrayList<Move> detectMoves(Board board, AbstractPiece piece);

    boolean canCapture(Board board, AbstractPiece piece, Position target);
}
