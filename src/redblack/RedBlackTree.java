package redblack;

import integer.IntegerTree;

import java.util.ArrayList;
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
        
        List<RedBlackNode> path = getSearchPath(key);
        RedBlackNode parent = path.getLast();

        // if the key doesn't exist, but is a gravestone
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

    public List<RedBlackNode> getSearchPath(Integer key) {
        RedBlackNode currentNode = (RedBlackNode) root;
        List<RedBlackNode> searchPath = new ArrayList<>();

        while (currentNode != null) {
            searchPath.add(currentNode);
            if (key < currentNode.getValue())
                currentNode = currentNode.getLeft();
            else if (key < currentNode.getValue())
                currentNode = currentNode.getRight();
            else
                currentNode = null;
        }

        return searchPath;
    }

    @Override
    public boolean remove(Integer key) {
        // key exists
        if (search(key)) {
            RedBlackNode nodeToBeRemoved = getSearchPath(key).getLast();
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