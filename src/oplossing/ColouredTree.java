package oplossing;

import opgave.Node;
import opgave.SearchTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public abstract class ColouredTree<E extends Comparable<E>> implements SearchTree<E> {

    protected ColouredNode<E> root;
    protected final HashSet<E> values;

    protected int k;
    private int removedAmount;

    public ColouredTree() {
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

    public List<ColouredNode<E>> getSearchPath(E key) {
        ColouredNode<E> currentNode = root;
        List<ColouredNode<E>> searchPath = new ArrayList<>();

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
            root = new ColouredNode<>(key, 0);
            values.add(root.getValue());
            return true;
        }

        List<ColouredNode<E>> path = getSearchPath(key);
        ColouredNode<E> parent = path.getLast();

        // node with the key is a tombstone
        if (parent.getValue().equals(key)) {
            parent.changeRemoveState();
            values.add(parent.getValue());
            return true;
        }

        // no issue
        int parentColour = parent.getColour();
        if (parentColour != k - 1) {
            ColouredNode<E> node = new ColouredNode<>(key, parentColour + 1);
            if (key.compareTo(parent.getValue()) < 0)
                parent.setLeft(node);
            else
                parent.setRight(node);
            values.add(node.getValue());
            return true;
        }

        // issue
        ColouredNode<E> issueSource = new ColouredNode<>(key, 1);
        ColouredNode<E> p1 = path.removeLast();
        ColouredNode<E> p2 = path.removeLast();

        fix2WrongColoursProblem(path, issueSource, p1, p2, true);

        values.add(issueSource.getValue());
        return true;
    }

    abstract void fix2WrongColoursProblem(List<ColouredNode<E>> path, ColouredNode<E> issueSource, ColouredNode<E> p1, ColouredNode<E> p2, boolean allowOptimized);

    // get 3 nodes and order them + their children in the following way:
    //    1
    //  2   3
    // 4 5 6 7 <- their children
    protected List<ColouredNode<E>> orderNodes(ColouredNode<E> n1, ColouredNode<E> n2, ColouredNode<E> n3) {
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
            List<ColouredNode<E>> path = getSearchPath(key);
            ColouredNode<E> node = path.removeLast();
            ColouredNode<E> parent = path.isEmpty() ? null : path.getLast();

            return removeSpecialCases(node, parent);
        }

        // key doesn't exist
        return false;
    }

    abstract boolean removeSpecialCases(ColouredNode<E> node, ColouredNode<E> parent);

    protected boolean swapInternNode(ColouredNode<E> node) {
        // get the necessary nodes to perform the swap
        ColouredNode<E> swapNodeParent = node;
        boolean searchLeft = swapNodeParent.getLeft() != null;
        ColouredNode<E> swapNode = searchLeft ? swapNodeParent.getLeft() : swapNodeParent.getRight();

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
        if (tombstoneCheck(swapNode, swapNodeParent)) {
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

    abstract boolean tombstoneCheck(ColouredNode<E> swapNode, ColouredNode<E> swapNodeParent);

    protected void tombstone(ColouredNode<E> node) {
        node.changeRemoveState();
        values.remove(node.getValue());

        // rebuild if half or more are tombstones
        removedAmount++;
        if (removedAmount >= size()) {
            removedAmount = 0;
            rebuild();
        }
    }

    // always rounded down
    protected int log2(int n) {
        return (int) Math.floor(Math.log(n) / Math.log(2));
    }

    // the amount of elements in a list needs to be equal to 2^n - 1 with n >= 1 (n is going to be the depth + 1)
    // start with the middle element, then the middle element of the left elements, then the middle element of those left elements ...
    // then the middle element of the right elements ...
    // example: [a, b, c, d, e, f, g, h, i, j, k, l, m, n, o] => [h, d, b, a, c, f, e, g, l, j, i, k, n, m, o]
    protected List<E> specialSort(List<E> list) {
        if (list.size() == 1)
            return list;

        List<E> sorted = new ArrayList<>();

        int middle = list.size() / 2;
        sorted.add(list.get(middle));
        sorted.addAll(specialSort(list.subList(0, middle)));
        sorted.addAll(specialSort(list.subList(middle + 1, list.size())));

        return sorted;
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
