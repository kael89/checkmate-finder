package org.kkarvounis.checkmatefinder.testing;

import org.kkarvounis.checkmatefinder.GenericHelper;
import org.kkarvounis.checkmatefinder.chess.Color;
import org.kkarvounis.checkmatefinder.chess.Move;
import org.kkarvounis.checkmatefinder.chess.Position;
import org.kkarvounis.checkmatefinder.chess.board.Board;
import org.kkarvounis.checkmatefinder.chess.piece.AbstractPiece;
import org.kkarvounis.checkmatefinder.json.JsonSerializableInterface;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NON_PRIVATE;

@JsonAutoDetect(fieldVisibility = NON_PRIVATE)
class BoardData {
    public Board board;
    Map<Position, ArrayList<String>> availableMoves = new HashMap<>();
    Map<Position, ArrayList<String>> canAlsoCapture = new HashMap<>();
}

@JsonAutoDetect(getterVisibility = NONE)
public class BoardScenario {
    public String id;
    public String name;
    public BoardData data;

    private static BoardScenario[] fromJsonArray(File json) {
        return (BoardScenario[]) JsonSerializableInterface.fromJson(json, BoardScenario[].class);
    }

    public static Iterator<BoardScenario> iterator(File dataFile) {
        BoardScenario[] scenarios = BoardScenario.fromJsonArray(dataFile);
        return Arrays.asList(scenarios).iterator();
    }

    public Board getBoard() {
        return data.board;
    }

    public Map<Color, ArrayList<Move>> getAvailableMoves() {
        Map<Color, ArrayList<Move>> availableMoves = new HashMap<Color, ArrayList<Move>>() {{
            put(Color.black, new ArrayList<>());
            put(Color.white, new ArrayList<>());
        }};

        for (Map.Entry<AbstractPiece, ArrayList<Move>> entry : getAvailableMovesPerPiece().entrySet()) {
            AbstractPiece piece = entry.getKey();
            ArrayList<Move> movesPerPiece = entry.getValue();
            availableMoves.get(piece.getColor()).addAll(movesPerPiece);
        }

        return availableMoves;
    }

    Map<AbstractPiece, ArrayList<Move>> getAvailableMovesPerPiece() {
        Map<AbstractPiece, ArrayList<Move>> availableMoves = new HashMap<>();
        for (Map.Entry<Position, ArrayList<String>> entry : data.availableMoves.entrySet()) {
            Position position = entry.getKey();
            ArrayList<String> targetStrings = entry.getValue();

            AbstractPiece piece = data.board.getPieceAt(position);
            ArrayList<Move> moves = targetStrings
                    .stream()
                    .map(targetString -> new Move(piece, targetString))
                    .collect(Collectors.toCollection(ArrayList::new));

            availableMoves.put(piece, moves);
        }

        return availableMoves;
    }

    Map<AbstractPiece, ArrayList<Position>> getCapturePositionsPerPiece() {
        Map<AbstractPiece, ArrayList<Position>> capturePositions = new HashMap<>();
        for (Map.Entry<Position, ArrayList<String>> entry : data.availableMoves.entrySet()) {
            Position position = entry.getKey();
            ArrayList<String> targetStrings = entry.getValue();

            AbstractPiece piece = data.board.getPieceAt(position);
            ArrayList<String> canAlsoCaptureStrings = data.canAlsoCapture.getOrDefault(
                    piece.getPosition(),
                    new ArrayList<>()
            );
            ArrayList<Position> positions = GenericHelper.merge(targetStrings, canAlsoCaptureStrings)
                    .stream()
                    .map(Position::new)
                    .collect(Collectors.toCollection(ArrayList::new));

            capturePositions.put(piece, positions);
        }

        return capturePositions;
    }
}
