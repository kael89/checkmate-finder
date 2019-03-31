package org.kkarvounis.checkmate.chess.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Node {
    // TODO is global id required???
    private static int lastId = 0;

    // TODO make properties private?
    public int id;
    Node parent;
    private Map<Integer, Node> children = new HashMap<>();
    public Object data;

    Node(Object data) {
        this.data = data;
        this.id = ++lastId;
    }

    private Node(Object data, Node parent) {
        this(data);
        this.parent = parent;
    }

    ArrayList<Node> getChildren() {
        return new ArrayList<>(children.values());
    }

    Node addChild(Object data) {
        Node child = new Node(data, this);
        children.put(child.id, child);

        return child;
    }

    void removeChild(Node child) {
        children.remove(child.id);
    }

    boolean isRoot() {
        return parent == null;
    }
}
