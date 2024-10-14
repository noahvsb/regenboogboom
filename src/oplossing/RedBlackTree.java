package oplossing;

import opgave.Node;
import opgave.SearchTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class RedBlackTree<E extends Comparable<E>> implements SearchTree<E> {

    private RedBlackNode<E> root;
    private final HashSet<RedBlackNode<E>> nodes;
    private final HashSet<E> values;
    private int removedAmount;

    public RedBlackTree() {
        root = null;
        nodes = new HashSet<>();
        values = new HashSet<>();
        removedAmount = 0;
    }

    @Override
    public int size() {
        return nodes.size();
    }

    @Override
    public boolean search(E key) {
        return values.contains(key);
    }

    // This function serves 2 uses:
    // To get the search path (like you would expect).
    // But also if for a key search(key) returns true,
    // you can use getLast() on the returned list to get the actual Node with that key.
    public List<RedBlackNode<E>> getSearchPath(E key) {
        RedBlackNode<E> currentNode = root;
        List<RedBlackNode<E>> searchPath = new ArrayList<>();

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
            root = new RedBlackNode<>(key, 0);
            addToSets(root);
            return true;
        }

        List<RedBlackNode<E>> path = getSearchPath(key);
        RedBlackNode<E> parent = path.getLast();

        // node with the key is a tombstone
        if (parent.getValue().equals(key)) {
            parent.changeRemoveState();
            addToSets(parent);
            return true;
        }

        // no issue
        if (parent.getColour() == 0) {
            RedBlackNode<E> newNode = new RedBlackNode<>(key, 1);
            if (key.compareTo(parent.getValue()) < 0)
                parent.setLeft(newNode);
            else
                parent.setRight(newNode);
            addToSets(newNode);
            return true;
        }

        // issue
        RedBlackNode<E> issueSource = new RedBlackNode<>(key, 1);
        RedBlackNode<E> p1 = path.removeLast();
        RedBlackNode<E> p2 = path.removeLast();

        fix2RedsProblem(path, issueSource, p1, p2, true);

        addToSets(issueSource);
        return true;
    }

    // make sure the issueSource, p1 and p2 nodes are removed from the path
    private void fix2RedsProblem(List<RedBlackNode<E>> path, RedBlackNode<E> issueSource, RedBlackNode<E> p1, RedBlackNode<E> p2, boolean allowOptimized) {
        boolean problem = true;

        while (problem) {
            boolean useOptimized = allowOptimized && (p2.getLeft() == null || p2.getLeft().getColour() == 0
                    || p2.getRight() == null || p2.getRight().getColour() == 0);

            List<RedBlackNode<E>> nodes = orderNodes(issueSource, p1, p2);
            RedBlackNode<E> n1 = nodes.getFirst();
            RedBlackNode<E> n2 = nodes.get(1);
            RedBlackNode<E> n3 = nodes.get(2);

            // set colours
            if (useOptimized) {
                n1.setColour(0);
                n2.setColour(1);
                n3.setColour(1);
            } else {
                n1.setColour(1);
                n2.setColour(0);
                n3.setColour(0);
            }

            // build subtree
            if (path.isEmpty()) {
                root = n1;
            } else {
                RedBlackNode<E> p = path.getLast();
                if (n1.getValue().compareTo(p.getValue()) < 0)
                    p.setLeft(n1);
                else
                    p.setRight(n1);
            }

            n1.setLeft(n2);
            n1.setRight(n3);
            n2.setLeft(nodes.get(3));
            n2.setRight(nodes.get(4));
            n3.setLeft(nodes.get(5));
            n3.setRight(nodes.get(6));

            // check if done and else prepare for the next cycle
            if (useOptimized) {
                problem = false;
            } else {
                issueSource = nodes.getFirst();

                // issue source is root
                if (path.isEmpty()) {
                    issueSource.setColour(0);
                    problem = false;
                } else {
                    p1 = path.removeLast();
                    if (p1.getColour() == 0)
                        problem = false;
                    else
                        p2 = path.removeLast();
                }
            }
        }
    }

    // get 3 nodes and order them + their children in the following way:
    //    1
    //  2   3
    // 4 5 6 7 <- their children
    private List<RedBlackNode<E>> orderNodes(RedBlackNode<E> n1, RedBlackNode<E> n2, RedBlackNode<E> n3) {
        if (n1.getValue().compareTo(n2.getValue()) < 0) {
            if (n1.getValue().compareTo(n3.getValue()) < 0)
                return Arrays.asList(n2, n1, n3, n1.getLeft(), n1.getRight(), n2.getRight(), n3.getRight());
            return Arrays.asList(n1, n3, n2, n3.getLeft(), n1.getLeft(), n1.getRight(), n2.getRight());
        }
        if (n1.getValue().compareTo(n3.getValue()) < 0)
            return Arrays.asList(n1, n2, n3, n2.getLeft(), n1.getLeft(), n1.getRight(), n3.getRight());
        return Arrays.asList(n2, n3, n1, n3.getLeft(), n2.getLeft(), n1.getLeft(), n1.getRight());
    }

    private void addToSets(RedBlackNode<E> node) {
        nodes.add(node);
        values.add(node.getValue());
    }

    @Override
    public boolean remove(E key) {
        // key exists (and thus also isn't a tombstone)
        if (search(key)) {
            List<RedBlackNode<E>> path = getSearchPath(key);
            RedBlackNode<E> node = path.getLast();
            RedBlackNode<E> parent = path.get(path.size() - 2);

            // red leaf => remove safely
            if (node.getColour() == 1 && node.isLeaf()) {
                if (key.compareTo(parent.getValue()) < 0)
                    parent.setLeft(null);
                else
                    parent.setRight(null);
                removeFromSets(node);
                return true;
            }

            // black node with 1 child and red parent => remove with changes in the tree
            if (node.getColour() == 0 && node.childrenCount() == 1 && parent.getColour() == 1) {
                // grab the child
                RedBlackNode<E> child = node.getLeft();
                if (child == null)
                    child = node.getRight();

                // attach to the parent instead of the node we want to remove
                if (key.compareTo(parent.getValue()) < 0)
                    parent.setLeft(child);
                else
                    parent.setRight(child);

                // child is red
                if (child.getColour() == 1)
                    child.setColour(0);
                // child is black
                else {
                    parent.setColour(0);

                    if (key.compareTo(parent.getValue()) < 0)
                        colourRed(parent.getRight());
                    else
                        colourRed(parent.getLeft());
                }
                removeFromSets(node);
                return true;
            }

            // black leaf with red parent => remove with changes in the tree
            if (node.getColour() == 0 && node.isLeaf() && parent.getColour() == 1) {
                if (key.compareTo(parent.getValue()) < 0) {
                    parent.setLeft(null);
                    parent.setColour(0);
                    colourRed(parent.getRight());
                }
                else {
                    parent.setRight(null);
                    parent.setColour(0);
                    colourRed(parent.getLeft());
                }
                removeFromSets(node);
                return true;
            }

            // black leaf with black parent => tombstone
            if (node.getColour() == 0 && node.isLeaf() && parent.getColour() == 0) {
                node.changeRemoveState();
                removeFromSets(node);

                removedAmount++;
                if (removedAmount > size() / 2) {
                    removedAmount = 0;
                    rebuild();
                }

                return true;
            }

            // intern node => swap with a leaf node and call remove recursive
            // TODO
        }
        return false;
    }

    private void colourRed(RedBlackNode<E> node) {
        node.setColour(1);

        RedBlackNode<E> child = node.getLeft();
        if (child == null || child.getColour() == 0)
            child = node.getRight();

        // problem occurs
        if (child != null && child.getColour() == 1) {
            List<RedBlackNode<E>> path = getSearchPath(child.getValue());
            RedBlackNode<E> issueSource = path.removeLast();
            RedBlackNode<E> p1 = path.removeLast();
            RedBlackNode<E> p2 = path.removeLast();
            fix2RedsProblem(path, issueSource, p1, p2, false);
        }
    }

    public void removeFromSets(RedBlackNode<E> node) {
        nodes.remove(node);
        values.remove(node.getValue());
    }

    @Override
    public void rebuild() {
        // idea:
        // if you have n nodes
        // make a complete binary tree of depth log2(n) ? rounded down with all black nodes
        // then add the remaining nodes as red leafs
        // TODO
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