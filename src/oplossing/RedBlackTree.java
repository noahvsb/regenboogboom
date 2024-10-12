package oplossing;

import opgave.Node;
import opgave.SearchTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class RedBlackTree<E extends Comparable<E>> implements SearchTree<E> {

    private RedBlackNode<E> root;
    private final HashSet<RedBlackNode<E>> allNodes;
    private int removedAmount;

    public RedBlackTree() {
        root = null;
        allNodes = new HashSet<>();
        removedAmount = 0;
    }

    @Override
    public int size() {
        return allNodes.size();
    }

    @Override
    public boolean search(E key) {
        for (RedBlackNode<E> node : allNodes)
            if (node.getValue().equals(key))
                return true;
        return false;
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
        if (root == null) {
            root = new RedBlackNode<>(key, 0);
            allNodes.add(root);
            return true;
        }
        // node with key exists
        if (search(key))
            return false;

        List<RedBlackNode<E>> path = getSearchPath(key);
        RedBlackNode<E> parent = path.getLast();

        // node with key is a tombstone
        if (parent.getValue().equals(key)) {
            parent.changeRemoveState();
            allNodes.add(parent);
        }

        // no issue
        if (parent.getColour() == 0) {
            RedBlackNode<E> newNode = new RedBlackNode<>(key, 1);
            allNodes.add(newNode);
            if (key.compareTo(parent.getValue()) < 0)
                parent.setLeft(newNode);
            else
                parent.setRight(newNode);
        }
        // issue
        else {
            RedBlackNode<E> issueSource = new RedBlackNode<>(key, 1);
            allNodes.add(issueSource);

            RedBlackNode<E> p1 = path.removeLast();
            RedBlackNode<E> p2 = path.removeLast();
            boolean problem = true;

            while (problem) {
                boolean useOptimized = p2.getLeft() == null || p2.getLeft().getColour() == 0
                        || p2.getRight() == null || p2.getRight().getColour() == 0;

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
                }
                else {
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

                // check if done
                if (useOptimized) {
                    problem = false;
                } else {
                    issueSource = nodes.getFirst();
                    // issue is root
                    if (path.isEmpty()) {
                        issueSource.setColour(0);
                        problem = false;
                    }
                    else {
                        p1 = path.removeLast();
                        // parent is black
                        if (p1.getColour() == 0)
                            problem = false;
                        else
                            p2 = path.removeLast();
                    }
                }
            }
        }
        return true;
    }

    private List<RedBlackNode<E>> orderNodes(RedBlackNode<E> n1, RedBlackNode<E> n2, RedBlackNode<E> n3) {
        if (n1.getValue().compareTo(n2.getValue()) < 0) {
            if (n1.getValue().compareTo(n3.getValue()) < 0) {
                return Arrays.asList(n2, n1, n3, n1.getLeft(), n1.getRight(), n2.getRight(), n3.getRight());
            }
            return Arrays.asList(n1, n3,n2, n3.getLeft(), n1.getLeft(), n1.getRight(), n2.getRight());
        }
        if (n1.getValue().compareTo(n3.getValue()) < 0) {
            return Arrays.asList(n1, n2, n3, n2.getLeft(), n1.getLeft(), n1.getRight(), n3.getRight());
        }
        return Arrays.asList(n2, n3, n1, n3.getLeft(), n2.getLeft(), n1.getLeft(), n1.getRight());
    }

    @Override
    public boolean remove(E key) {
        // key exists (and thus also isn't a tombstone)
        if (search(key)) {
            List<RedBlackNode<E>> path = getSearchPath(key);
            RedBlackNode<E> node = path.getLast();

            // red leaf or black node with max 1 child and red parent
            if ((node.getColour() == 1 && node.isLeaf())
                || (node.getColour() == 0 && node.childrenCount() < 2 && path.get(path.size() - 2).getColour() == 1)) {
                // TODO
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

            // intern node
            else {
                // TODO
            }

            return true;
        }
        return false;
    }

    @Override
    public void rebuild() {
        // TODO
    }

    @Override
    public Node<E> root() {
        return root;
    }

    @Override
    public List<E> values() {
        return allNodes.stream().map(RedBlackNode::getValue).sorted().toList();
    }
}