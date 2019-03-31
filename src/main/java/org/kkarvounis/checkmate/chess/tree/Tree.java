package org.kkarvounis.checkmate.chess.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Tree {
    private Map<Integer, Node> treeRoots = new HashMap<>();

    Tree(ArrayList<Node> treeRoots) {
        int i = 1;
        for (Node treeRoot : treeRoots) {
            treeRoot.id = i;
            this.treeRoots.put(treeRoot.id, treeRoot);
            i++;
        }
    }

    ArrayList<Node> getTreeRoots() {
        return new ArrayList<>(treeRoots.values());
    }

    void deleteParent(Node startNode, int levelsUp) {
        Node node = startNode;
        for (int i = 0; i < levelsUp; i++) {
            if (node.isRoot()) {
                break;
            }
            node = node.parent;
        }

        deleteNode(node);
    }

    /**
     * @param node The node to be deleted
     * @return True if node was deleted, false otherwise
     */
    boolean deleteNode(Node node) {
        if (!node.isRoot()) {
            node.parent.removeChild(node);
            return true;
        }

        if (treeRoots.containsKey(node.id)) {
            deleteRoot(node);
            return true;
        }

        return false;
    }

    void deleteRoot(Node root) {
        treeRoots.remove(root.id);
    }
}
