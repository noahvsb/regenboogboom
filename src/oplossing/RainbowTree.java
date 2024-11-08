package oplossing;

import opgave.Node;
import opgave.SearchTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class RainbowTree<E extends Comparable<E>> implements SearchTree<E> {

    private final int k;

    private RainbowNode<E> root;
    private final HashSet<E> values;
    private int removedAmount;

    public RainbowTree(int k) {
        this.k = k;

        root = null;
        values = new HashSet<>();
        removedAmount = 0;
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean search(E key) {
        return values.contains(key);
    }

    public List<RainbowNode<E>> getSearchPath(E key) {
        RainbowNode<E> currentNode = root;
        List<RainbowNode<E>> searchPath = new ArrayList<>();

        while (currentNode != null) {
            searchPath.add(currentNode);
            if (key.compareTo(currentNode.getValue()) < 0)
                currentNode = currentNode.getLeft();
            else if (key.compareTo(currentNode.getValue()) > 0)
                currentNode = currentNode.getRight();
            else
                currentNode = null;
        }

        return searchPath;
    }

    @Override
    public boolean add(E key) {
        // node with key exists
        if (search(key))
            return false;

        // tree doesn't have a root yet
        if (root == null) {
            root = new RainbowNode<>(key, 0, k);
            values.add(root.getValue());
            return true;
        }

        List<RainbowNode<E>> path = getSearchPath(key);
        RainbowNode<E> parent = path.getLast();

        // node with the key is a tombstone
        if (parent.getValue().equals(key)) {
            parent.changeRemoveState();
            values.add(parent.getValue());
            return true;
        }

        // no issue
        int parentColour = parent.getColour();
        if (parentColour != k - 1) {
            RainbowNode<E> node = new RainbowNode<>(key, parentColour + 1, k);
            if (key.compareTo(parent.getValue()) < 0)
                parent.setLeft(node);
            else
                parent.setRight(node);
            values.add(node.getValue());
            return true;
        }

        // issue
        RainbowNode<E> issueSource = new RainbowNode<>(key, 1, k);
        RainbowNode<E> p1 = path.removeLast();
        RainbowNode<E> p2 = path.removeLast();

        fix2WrongColoursProblem(path, issueSource, p1, p2);

        values.add(issueSource.getValue());
        return true;
    }

    // make sure the issueSource, p1 and p2 nodes are removed from the path
    private void fix2WrongColoursProblem(List<RainbowNode<E>> path, RainbowNode<E> issueSource, RainbowNode<E> p1, RainbowNode<E> p2) {
        boolean problem = true;

        while (problem) {
            List<RainbowNode<E>> nodes = orderNodes(issueSource, p1, p2);
            RainbowNode<E> n1 = nodes.getFirst();
            RainbowNode<E> n2 = nodes.get(1);
            RainbowNode<E> n3 = nodes.get(2);

            // set colours
            n1.setColour(1);
            n2.setColour(p2.getColour());
            n3.setColour(p2.getColour());

            // build subtree
            if (path.isEmpty()) {
                root = n1;
            } else {
                RainbowNode<E> p3 = path.getLast();
                if (n1.getValue().compareTo(p3.getValue()) < 0)
                    p3.setLeft(n1);
                else
                    p3.setRight(n1);
            }

            n1.setLeft(n2);
            n1.setRight(n3);
            n2.setLeft(nodes.get(3));
            n2.setRight(nodes.get(4));
            n3.setLeft(nodes.get(5));
            n3.setRight(nodes.get(6));

            // check if done and else prepare for the next cycle
            if (p2.getColour() != 1) {
                issueSource = nodes.getFirst();

                // issue source is root
                if (path.isEmpty()) {
                    issueSource.setColour(0);
                    problem = false;
                } else {
                    p1 = path.removeLast();
                    if (p1.getColour() == 0 || p1.getColour() < issueSource.getColour())
                        problem = false;
                    else
                        p2 = path.removeLast();
                }
            }
            // if the grandparent was red, then our issue source and parents will be different
            // we also don't need to check if done, because we know there's an issue
            else {
                issueSource = nodes.get(2);
                p1 = nodes.getFirst();
                p2 = path.removeLast();
            }
        }
    }

    // get 3 nodes and order them + their children in the following way:
    //    1
    //  2   3
    // 4 5 6 7 <- their children
    private List<RainbowNode<E>> orderNodes(RainbowNode<E> n1, RainbowNode<E> n2, RainbowNode<E> n3) {
        if (n1.getValue().compareTo(n2.getValue()) < 0) {
            if (n1.getValue().compareTo(n3.getValue()) < 0)
                return Arrays.asList(n2, n1, n3, n1.getLeft(), n1.getRight(), n2.getRight(), n3.getRight());
            return Arrays.asList(n1, n3, n2, n3.getLeft(), n1.getLeft(), n1.getRight(), n2.getRight());
        }
        if (n1.getValue().compareTo(n3.getValue()) < 0)
            return Arrays.asList(n1, n2, n3, n2.getLeft(), n1.getLeft(), n1.getRight(), n3.getRight());
        return Arrays.asList(n2, n3, n1, n3.getLeft(), n2.getLeft(), n1.getLeft(), n1.getRight());
    }

    @Override
    public boolean remove(E key) {
        // key exists
        if (search(key)) {
            List<RainbowNode<E>> path = getSearchPath(key);
            RainbowNode<E> node = path.removeLast();
            RainbowNode<E> parent = path.isEmpty() ? null : path.getLast();

            return removeSpecialCases(node, parent);
        }

        // key doesn't exist
        return false;
    }

    private boolean removeSpecialCases(RainbowNode<E> node, RainbowNode<E> parent) {
        // root in a tree with 1 node
        if (parent == null && node.isLeaf()) {
            root = null;
            values.remove(node.getValue());
            return true;
        }

        // non-black leaf => remove safely
        if (node.getColour() != 0 && node.isLeaf()) {
            if (parent.getLeft() == node)
                parent.setLeft(null);
            else
                parent.setRight(null);
            values.remove(node.getValue());
            return true;
        }

        // black node with 1 child => remove with changes in the tree
        if (node.getColour() == 0 && node.childrenCount() == 1) {
            // grab the child
            RainbowNode<E> child = node.getLeft() != null ? node.getLeft() : node.getRight();

            // attach to the parent instead of the node we want to remove
            if (parent == null)
                root = child;
            else if (parent.getLeft().equals(node))
                parent.setLeft(child);
            else
                parent.setRight(child);

            // set child's colour to black (child will always be red, otherwise one of the conditions isn't met)
            child.setColour(0);

            values.remove(node.getValue());
            return true;
        }

        // black leaf => tombstone
        if (node.getColour() == 0 && node.isLeaf()) {
            tombstone(node);
            return true;
        }

        // intern node => swap with biggest in the left or smallest in the right subtree and call remove recursively

        // get the necessary nodes to perform the swap
        RainbowNode<E> swapNodeParent = node;
        boolean searchLeft = node.getLeft() != null;
        RainbowNode<E> swapNode = searchLeft ? node.getLeft() : node.getRight();

        boolean found = false;
        while (!found) {
            if (searchLeft) {
                if (swapNode.getRight() != null) {
                    swapNodeParent = swapNode;
                    swapNode = swapNode.getRight();
                } else
                    found = true;
            } else {
                if (swapNode.getLeft() != null) {
                    swapNodeParent = swapNode;
                    swapNode = swapNode.getLeft();
                } else
                    found = true;
            }
        }

        // if swapNode and swapNodeParent are black, the node wouldn't actually be removed (tombstone)
        // and the tree wouldn't be a search tree anymore
        // so the node gets turned into a tombstone directly instead of a swap and then a tombstone
        if (swapNode.getColour() == 0 && swapNode.isLeaf()) {
            tombstone(node);
            return true;
        }

        // perform swap
        E key = node.getValue();
        node.setValue(swapNode.getValue());
        swapNode.setValue(key);

        if (swapNode.isRemoved()) {
            node.changeRemoveState();
            swapNode.changeRemoveState();
        }

        // recursion
        return removeSpecialCases(swapNode, swapNodeParent != swapNode ? swapNodeParent : node);
    }

    private void tombstone(RainbowNode<E> node) {
        node.changeRemoveState();
        values.remove(node.getValue());

        // rebuild if half or more are tombstones
        removedAmount++;
        if (removedAmount > size()) {
            removedAmount = 0;
            rebuild();
        }
    }

    @Override
    public void rebuild() {
        List<E> keys = values();

        root = null;
        values.clear();

        for (E key : keys)
            add(key);
    }

    @Override
    public Node<E> root() {
        return root;
    }

    @Override
    public List<E> values() {
        return values.stream().sorted().toList();
    }
}
