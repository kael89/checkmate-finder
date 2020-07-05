package org.kkarvounis.checkmatefinder.chess.tree;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kkarvounis.checkmatefinder.chess.Color;
import org.kkarvounis.checkmatefinder.chess.GameStatus;
import org.kkarvounis.checkmatefinder.chess.Move;
import org.kkarvounis.checkmatefinder.chess.board.Board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class NodeData {
    Node node;
    Board board;

    NodeData(Node node, Board board) {
        this.node = node;
        this.board = board;
    }
}

class MoveData {
    NodeData nodeData;
    ArrayList<Move> moves;

    MoveData(NodeData nodeData, ArrayList<Move> moves) {
        this.nodeData = nodeData;
        this.moves = moves;
    }
}

// TODO refactor AbstractChessTree to use the same code base?
public class ForcedMateTree extends AbstractChessTree {
    private Tree tree;
    private Color currentPlayerColor;
    // The latest level of nodes to be added in the tree
    private Map<Node, ArrayList<NodeData>> currentNodeData;

    public ForcedMateTree(
            @JsonProperty("board") Board board,
            @JsonProperty("startingColor") Color startingColor,
            @JsonProperty("depth") int depth
    ) {
        super(board, startingColor, depth);
        currentPlayerColor = startingColor;
    }

    private Node createNode(Move move) {
        return new Node(move.toString());
    }

    private Node addChildNode(Node parent, Move move) {
        return parent.addChild(move.toString());
    }

    @Override
    public Map<String, Object> get() {
        resetDepth();

        for (int i = 0; i < depth; i++) {
            addNewTreeLevel();
            if (currentNodeData.size() == 0) {
                break;
            }
        }

        Map<String, Object> result = new HashMap<>();
        buildTree(result, this.tree.getTreeRoots());

        return result;
    }

    private void addNewTreeLevel() {
        incrementDepth();
        if (isFirstMove()) {
            initTree();
        } else {
            addTreeNodes();
        }
        currentPlayerColor = currentPlayerColor.opposite();
    }

    private void initTree() {
        resetCurrentNodeData();

        ArrayList<Move> moves = board.detectMoves(startingColor);
        moves = filterCurrentMoves(board, moves);

        ArrayList<Node> treeRoots = new ArrayList<>();
        for (Move move : moves) {
            Node root = createNode(move);
            treeRoots.add(root);

            NodeData nodeData = new NodeData(root, board.doMove(move));
            addCurrentNodeData(root, nodeData);
        }

        tree = new Tree(treeRoots);
    }

    private void resetCurrentNodeData() {
        currentNodeData = new HashMap<>();
    }

    private ArrayList<Move> filterCurrentMoves(Board board, ArrayList<Move> moves) {
        return moves
                .stream()
                .filter(move -> !isLastMoveOfStartingPlayer() || isEnemyForceMated(board, move, currentPlayerColor))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private boolean isEnemyForceMated(Board board, Move move, Color playerColor) {
        Color enemyColor = playerColor.opposite();
        Board newBoard = board.doMove(move);

        return isPlayerCheckMated(newBoard, newBoard.detectMoves(enemyColor), enemyColor);
    }

    private boolean isPlayerCheckMated(Board board, ArrayList<Move> playerMoves, Color playerColor) {
        GameStatus status = deduceGameStatus(board, playerMoves, playerColor);
        return status == GameStatus.checkmate;
    }

    private GameStatus deduceGameStatus(Board board, ArrayList<Move> availableMoves, Color playerColor) {
        return availableMoves.size() > 0 ? GameStatus.playing :
                board.isKingChecked(playerColor) ? GameStatus.checkmate : GameStatus.draw;
    }

    private void addCurrentNodeData(Node root, NodeData nodeData) {
        if (!currentNodeData.containsKey(root)) {
            currentNodeData.put(root, new ArrayList<>());
        }

        currentNodeData.get(root).add(nodeData);
    }

    private void addTreeNodes() {
        Map<Node, ArrayList<MoveData>> currentMoveData = getCurrentMoveDataFromNodeData();

        resetCurrentNodeData();
        for (Map.Entry<Node, ArrayList<MoveData>> moveDataEntry : currentMoveData.entrySet()) {
            Node root = moveDataEntry.getKey();
            for (MoveData moveData : moveDataEntry.getValue()) {
                for (Move move : moveData.moves) {
                    Node newNode = addChildNode(moveData.nodeData.node, move);
                    Board newBoard = moveData.nodeData.board.doMove(move);

                    NodeData newNodeData = new NodeData(newNode, newBoard);
                    addCurrentNodeData(root, newNodeData);
                }
            }
        }
    }

    private Map<Node, ArrayList<MoveData>> getCurrentMoveDataFromNodeData() {
        Map<Node, ArrayList<MoveData>> moveData = new HashMap<>();
        ArrayList<Node> forcedMateRoots = new ArrayList<>();

        for (Map.Entry<Node, ArrayList<NodeData>> currentNodeDataEntry : currentNodeData.entrySet()) {
            Node root = currentNodeDataEntry.getKey();
            ArrayList<NodeData> nodeDataPerRoot = currentNodeDataEntry.getValue();
            // Assume a forced mate unless proved otherwise.
            // Forced mates are checked in enemy player's round
            boolean isForcedMate = isEnemyPlayerMove();

            moveData.put(root, new ArrayList<>());
            for (NodeData nodeData : nodeDataPerRoot) {
                Node node = nodeData.node;
                Board board = nodeData.board;

                ArrayList<Move> moves = board.detectMoves(currentPlayerColor);

                if (isForcedMate && !isPlayerCheckMated(board, moves, currentPlayerColor)) {
                    isForcedMate = false;
                    if (forcedMateRoots.size() > 0) {
                        // Skip to next root since quicker forced mates have been found,
                        // thus current root will be deleted
                        continue;
                    }
                }

                if (shouldDeletePreviousMove(board, moves)) {
                    deletePreviousMove(node);
                    continue;
                }

                moves = filterCurrentMoves(board, moves);
                if (shouldDeleteRoot(moves)) {
                    moveData.remove(root);
                    tree.deleteRoot(root);
                    break;
                }

                MoveData moveDataEntry = new MoveData(nodeData, moves);
                moveData.get(root).add(moveDataEntry);
            }

            if (isForcedMate) {
                forcedMateRoots.add(root);
            }
        }

        return cleanMoveDataAndRoots(moveData, forcedMateRoots);
    }

    private boolean shouldDeletePreviousMove(Board board, ArrayList<Move> moves) {
        ArrayList<GameStatus> acceptableStatuses = getAcceptableGameStatuses();
        GameStatus status = deduceGameStatus(board, moves, currentPlayerColor);

        return !acceptableStatuses.contains(status);
    }

    private boolean shouldDeleteRoot(ArrayList<Move> moves) {
        return isLastMoveOfStartingPlayer() && moves.size() == 0;
    }

    private ArrayList<GameStatus> getAcceptableGameStatuses() {
        ArrayList<GameStatus> acceptableStatuses = new ArrayList<GameStatus>() {{
            add(GameStatus.playing);
        }};
        if (isEnemyPlayerMove()) {
            acceptableStatuses.add(GameStatus.checkmate);
        }

        return acceptableStatuses;
    }

    private void deletePreviousMove(Node previousNode) {
        int levelsUp = isStartingPlayerMove() ? 1 : 0;
        tree.deleteParent(previousNode, levelsUp);
    }

    private Map<Node, ArrayList<MoveData>> cleanMoveDataAndRoots(
            Map<Node, ArrayList<MoveData>> moveData,
            ArrayList<Node> forcedMateRoots) {
        if (forcedMateRoots.size() == 0) {
            return moveData;
        }

        return moveData
                .entrySet()
                .stream()
                .filter(entry -> {
                    Node root = entry.getKey();
                    if (!forcedMateRoots.contains(root)) {
                        tree.deleteRoot(root);
                        return false;
                    }

                    return true;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void buildTree(Map<String, Object> subTree, ArrayList<Node> nodes) {
        for (Node node : nodes) {
            HashMap<String, Object> nodeChildren = new HashMap<>();
            subTree.put((String) node.data, nodeChildren);
            buildTree(nodeChildren, node.getChildren());
        }
    }
}
