package org.kkarvounis.chasemate.app.lambda;

public class GetTreeRequest {
    private String type;
    private String board;
    private String startingColor;
    private String depth;

    public GetTreeRequest() {
    }

    public GetTreeRequest(String type, String board, String startingColor, String depth) {
        this.type = type;
        this.board = board;
        this.startingColor = startingColor;
        this.depth = depth;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getStartingColor() {
        return startingColor;
    }

    public void setStartingColor(String startingColor) {
        this.startingColor = startingColor;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }
}
