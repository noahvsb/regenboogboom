package redblack;

import integer.IntegerNode;
import integer.IntegerTree;

import java.util.List;

public class RedBlackTree extends IntegerTree {

    private int removedAmount;

    public RedBlackTree (RedBlackNode root) {
        super(root);
    }

    @Override
    public boolean add(Integer key) {
        // node with key exists
        if (search(key))
            return false;

        List<IntegerNode> path = getSearchPath(key);
        RedBlackNode parent = (RedBlackNode) path.getLast();

        // node with key is a tombstone
        if (parent.getValue().equals(key))
            parent.changeRemoveState();

        // no issue
        if (parent.getColour() == 0) {
            RedBlackNode newNode = new RedBlackNode(key, false);
            if (key < parent.getValue())
                parent.setLeft(newNode);
            else
                parent.setRight(newNode);
            allNodes.add(newNode);
        }
        // issue
        else {
            // TODO
        }

        return true;
    }

    // add multiple keys at once, stops if a key can't be added
    public boolean add(Integer... keys) {
        for (int key : keys)
            if (!add(key))
                return false;
        return true;
    }

    @Override
    public boolean remove(Integer key) {
        // key exists (and thus also isn't a tombstone)
        if (search(key)) {
            List<IntegerNode> path = getSearchPath(key);
            RedBlackNode node = (RedBlackNode) path.getLast();
            // TODO: red leaf or black node with max 1 child and red parent
            if ((node.getColour() == 1 && node.isLeaf())
                || (node.getColour() == 0 && node.childrenCount() < 2 && path.get(path.size() - 2).getColour() == 1)) {

            }

            // turn black leaf with black parent into tombstone
            else if (node.getColour() == 0 && node.isLeaf() && path.get(path.size() - 2).getColour() == 0) {
                node.changeRemoveState();
                allNodes.remove(node);
                removedAmount++;
                if (removedAmount > size() / 2) {
                    removedAmount = 0;
                    rebuild();
                }
            }

            // TODO: intern node
            else {

            }

            return true;
        }
        return false;
    }

    @Override
    public void rebuild() {
        // TODO
    }
}