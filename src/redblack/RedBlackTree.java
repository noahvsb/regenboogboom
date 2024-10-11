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
        // key exists
        if (search(key))
            return false;

        List<IntegerNode> path = getSearchPath(key);
        RedBlackNode parent = (RedBlackNode) path.getLast();

        // key is a gravestone
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
        // key exists
        if (search(key)) {
            RedBlackNode nodeToBeRemoved = (RedBlackNode) getSearchPath(key).getLast();
            nodeToBeRemoved.changeRemoveState();
            allNodes.remove(nodeToBeRemoved); // to make sure the key isn't being found
            removedAmount++;
            if (removedAmount > size() / 2) {
                removedAmount = 0;
                rebuild();
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