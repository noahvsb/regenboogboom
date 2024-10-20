package oplossing;

import opgave.Node;
import opgave.SearchTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class RedBlackTree<E extends Comparable<E>> implements SearchTree<E> {

    private RedBlackNode<E> root;
    private final HashSet<E> values;
    private int removedAmount;

    public RedBlackTree() {
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
            values.add(root.getValue());
            return true;
        }

        List<RedBlackNode<E>> path = getSearchPath(key);
        RedBlackNode<E> parent = path.getLast();

        // node with the key is a tombstone
        if (parent.getValue().equals(key)) {
            parent.changeRemoveState();
            values.add(parent.getValue());
            return true;
        }

        // no issue
        if (parent.getColour() == 0) {
            RedBlackNode<E> node = new RedBlackNode<>(key, 1);
            if (key.compareTo(parent.getValue()) < 0)
                parent.setLeft(node);
            else
                parent.setRight(node);
            values.add(node.getValue());
            return true;
        }

        // issue
        RedBlackNode<E> issueSource = new RedBlackNode<>(key, 1);
        RedBlackNode<E> p1 = path.removeLast();
        RedBlackNode<E> p2 = path.removeLast();

        fix2RedsProblem(path, issueSource, p1, p2, true);

        values.add(issueSource.getValue());
        return true;
    }

    // make sure the issueSource, p1 and p2 nodes are removed from the path
    private void fix2RedsProblem(List<RedBlackNode<E>> path, RedBlackNode<E> issueSource, RedBlackNode<E> p1, RedBlackNode<E> p2, boolean allowOptimized) {
        boolean problem = true;

        while (problem) {
            boolean isLeft = p2.getLeft() != null && p2.getLeft().equals(p1);
            boolean useOptimized = allowOptimized && ((!isLeft && (p2.getLeft() == null || p2.getLeft().getColour() == 0))
                    || ((isLeft && (p2.getRight() == null || p2.getRight().getColour() == 0))));

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
                RedBlackNode<E> p3 = path.getLast();
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

    @Override
    public boolean remove(E key) {
        // key exists
        if (search(key)) {
            List<RedBlackNode<E>> path = getSearchPath(key);
            RedBlackNode<E> node = path.removeLast();
            RedBlackNode<E> parent = path.isEmpty() ? null : path.getLast();

            return removeSpecialCases(node, parent);
        }

        // key doesn't exist
        return false;
    }

    private boolean removeSpecialCases(RedBlackNode<E> node, RedBlackNode<E> parent) {
        // root in a tree with 1 node
        if (node.equals(root) && node.isLeaf()) {
            root = null;
            values.remove(node.getValue());
            return true;
        }

        // red leaf => remove safely
        if (node.getColour() == 1 && node.isLeaf()) {
            if (parent.getLeft() == node)
                parent.setLeft(null);
            else
                parent.setRight(null);
            values.remove(node.getValue());
            return true;
        }

        // black node with 1 child and red parent => remove with changes in the tree
        if (node.getColour() == 0 && node.childrenCount() == 1 && parent != null && parent.getColour() == 1) {
            // grab the child
            RedBlackNode<E> child = node.getLeft() != null ? node.getLeft() : node.getRight();

            // attach to the parent instead of the node we want to remove
            if (parent.getLeft() == node)
                parent.setLeft(child);
            else
                parent.setRight(child);

            // set child's colour to black (child will always be red, otherwise one of the conditions isn't met)
            child.setColour(0);

            values.remove(node.getValue());
            return true;
        }

        // black leaf with red parent => remove with changes in the tree
        if (node.getColour() == 0 && node.isLeaf() && parent != null && parent.getColour() == 1) {
            // colour the parent black
            // colour the other child of the parent red and fix possible problems that occurred because of this
            if (parent.getLeft() == node) {
                parent.setLeft(null);
                parent.setColour(0);
                colourRed(parent.getRight());
            }
            else {
                parent.setRight(null);
                parent.setColour(0);
                colourRed(parent.getLeft());
            }
            values.remove(node.getValue());
            return true;
        }

        // black leaf with black parent => tombstone
        if (node.getColour() == 0 && node.isLeaf() && parent != null && parent.getColour() == 0) {
            tombstone(node);
            return true;
        }

        // intern node => swap with biggest in the left or smallest in the right subtree and call remove recursively

        // get the necessary nodes to perform the swap
        RedBlackNode<E> swapNodeParent = node;
        boolean searchLeft = node.getLeft() != null;
        RedBlackNode<E> swapNode = searchLeft ? node.getLeft() : node.getRight();

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

        if (swapNode.getColour() == 0 && swapNodeParent.getColour() == 0) {
            // if left was tried, try right as well for a second chance
            boolean secondChance = false;
            if (searchLeft && node.getRight() != null) {
                swapNodeParent = node;
                swapNode = node.getRight();
                found = false;
                while (!found) {
                    if (swapNode.getLeft() != null) {
                        swapNodeParent = swapNode;
                        swapNode = swapNode.getLeft();
                    } else
                        found = true;
                }
                if (swapNode.getColour() != 0 || swapNodeParent.getColour() != 0)
                    secondChance = true;
            }

            if (!secondChance) {
                // if swapNode and swapNodeParent are black, the node wouldn't actually be removed (tombstone)
                // and the tree wouldn't be a search tree anymore
                // so the node gets turned into a tombstone directly instead of a swap and then a tombstone
                tombstone(node);
                return true;
            }
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

    private void tombstone(RedBlackNode<E> node) {
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
        // get data
        int n = size();
        List<E> keys = values();

        if (n > 2) {
            // clear tree
            root = null;
            values.clear();

            // calculate depth of the complete binary tree
            int cbtDepth = log2(n);
            if (n != (int) Math.pow(2, cbtDepth + 1) - 1)
                cbtDepth--;

            // separate red leaf keys from other keys
            // the indexes for red leaf keys are the even indexes
            // but not all of them or else you would have too many red leaf keys
            int redLeafsAmount = n - ((int) Math.pow(2, cbtDepth + 1) - 1);

            List<E> redLeafKeys = new ArrayList<>();
            List<E> otherKeys = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (i < 2 * redLeafsAmount && i % 2 == 0)
                    redLeafKeys.add(keys.get(i));
                else
                    otherKeys.add(keys.get(i));
            }

            // build complete binary tree using the other keys
            buildCompleteBinaryTree(otherKeys);

            // add red leafs
            for (E key : redLeafKeys)
                add(key);
        }
    }

    // always rounded down
    private int log2(int n) {
        return (int) Math.floor(Math.log(n) / Math.log(2));
    }

    private void buildCompleteBinaryTree(List<E> keys) {
        // perform a special sort (see specialSort() for more details)
        keys = specialSort(keys);

        // set the root
        E first = keys.removeFirst();
        root = new RedBlackNode<>(first, 0);
        values.add(first);

        // add the rest like you would in a normal binary search tree
        for (E key : keys) {
            RedBlackNode<E> parent = root;
            boolean added = false;
            while (!added) {
                if (parent.getValue().compareTo(key) > 0) {
                    if (parent.getLeft() == null) {
                        parent.setLeft(new RedBlackNode<>(key, 0));
                        added = true;
                    } else
                        parent = parent.getLeft();
                } else {
                    if (parent.getRight() == null) {
                        parent.setRight(new RedBlackNode<>(key, 0));
                        added = true;
                    } else
                        parent = parent.getRight();
                }
            }
            values.add(key);
        }
    }

    // the amount of elements in a list needs to be equal to 2^n - 1 with n >= 1 (n is going to be the depth + 1)
    // start with the middle element, then the middle element of the left elements, then the middle element of those left elements ...
    // then the middle element of the right elements ...
    // example: [a, b, c, d, e, f, g, h, i, j, k, l, m, n, o] => [h, d, b, a, c, f, e, g, l, j, i, k, n, m, o]
    private List<E> specialSort(List<E> list) {
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