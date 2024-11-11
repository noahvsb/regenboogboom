package oplossing;

import java.util.*;

public class RainbowTree<E extends Comparable<E>> extends ColouredTree<E> {

    public RainbowTree(int k) {
        super();
        this.k = k;
    }

    // make sure the issueSource, p1 and p2 nodes are removed from the path
    protected void fix2WrongColoursProblem(List<ColouredNode<E>> path, ColouredNode<E> issueSource, ColouredNode<E> p1, ColouredNode<E> p2, boolean allowOptimized) {
        boolean problem = true;

        while (problem) {
            List<ColouredNode<E>> nodes = orderNodes(issueSource, p1, p2);
            ColouredNode<E> n1 = nodes.getFirst();
            ColouredNode<E> n2 = nodes.get(1);
            ColouredNode<E> n3 = nodes.get(2);

            // set colours
            n1.setColour(1);
            n2.setColour(p2.getColour());
            n3.setColour(p2.getColour());

            // build subtree
            if (path.isEmpty()) {
                root = n1;
            } else {
                ColouredNode<E> p3 = path.getLast();
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

    protected boolean removeSpecialCases(ColouredNode<E> node, ColouredNode<E> parent) {
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
            ColouredNode<E> child = node.getLeft() != null ? node.getLeft() : node.getRight();

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
        return swapInternNode(node);
    }

    @Override
    boolean tombstoneCheck(ColouredNode<E> swapNode, ColouredNode<E> swapNodeParent) {
        return swapNode.getColour() == 0 && swapNode.isLeaf();
    }

    @Override
    public void rebuild() {
        // in the comments for this method I assumed red = colour 1

        // get data
        int n = size();
        List<E> keys = values();

        if (n > 0) {
            // clear tree
            root = null;
            values.clear();

            // calculate depth of the complete binary tree
            int cbtDepth = log2(n);
            if (n != (int) Math.pow(2, cbtDepth + 1) - 1)
                cbtDepth--;

            // separate red leaf keys from other keys
            // the indexes for those keys are the even indexes
            // but not all of them or else you would have too many red leaf keys
            int redLeafAmount = n - ((int) Math.pow(2, cbtDepth + 1) - 1);

            List<E> redLeafKeys = new ArrayList<>();
            List<E> otherKeys = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (i < 2 * redLeafAmount && i % 2 == 0)
                    redLeafKeys.add(keys.get(i));
                else
                    otherKeys.add(keys.get(i));
            }

            List<ColouredNode<E>> cbtRedNodes = new ArrayList<>(); // all red nodes in the complete binary tree, except the ones on the bottom level, if there are any

            // build complete binary tree using the other keys
            List<ColouredNode<E>> bottomLevel = buildCompleteBinaryTree(otherKeys, cbtDepth, redLeafAmount, cbtRedNodes);

            // add red leafs with some slight changes to minimize the amount of red nodes
            int i = 0;
            for (E key : redLeafKeys) {
                ColouredNode<E> parent = bottomLevel.get(i / 2);
                if (i % 2 == 0)
                    parent.setLeft(new ColouredNode<>(key, 1));
                else
                    parent.setRight(new ColouredNode<>(key, 1));
                values.add(key);
                i++;
            }

            // maximize the amount of red nodes
            for (i = 0; i < cbtDepth / 2; i++)
                for (ColouredNode<E> node : cbtRedNodes) {
                    // because of the way I built my cbt, I only need to check the left child
                    ColouredNode<E> left = node.getLeft();
                    if (left != null && left.getColour() == 0
                            && (left.getLeft() == null || left.getLeft().getColour() == 0)
                            && (left.getRight() == null || left.getRight().getColour() == 0)) {
                        node.setColour(0);
                        left.setColour(1);
                        node.getRight().setColour(1);
                    }
                }
        }
    }

    private List<ColouredNode<E>> buildCompleteBinaryTree(List<E> keys, int depth, int a, List<ColouredNode<E>> redNodes) {
        List<ColouredNode<E>> bottomLevel = new ArrayList<>();

        // perform a special sort (see specialSort() for more details)
        keys = specialSort(keys);

        // set the root
        E first = keys.removeFirst();
        root = new ColouredNode<>(first, 0);
        values.add(first);

        // add the rest like you would in a normal binary search tree
        Stack<ColouredNode<E>> parents = new Stack<>();
        ColouredNode<E> lastNode = root;
        int parentDepth = 0;

        if (depth == parentDepth)
            bottomLevel.add(lastNode);

        for (E key : keys) {
            ColouredNode<E> parent;
            boolean left;
            if (parentDepth != depth && lastNode.getLeft() == null) {
                parent = lastNode;
                left = true;
            } else {
                parentDepth--;
                parent = parents.pop();
                while (parent.getRight() != null) {
                    parentDepth--;
                    parent = parents.pop();
                }
                left = false;
            }

            int colour = (a == 0 && depth % 2 != parentDepth % 2) || (a != 0 && depth % 2 == parentDepth % 2) ? 1 : 0;
            ColouredNode<E> node = new ColouredNode<>(key, colour);
            if (left)
                parent.setLeft(node);
            else
                parent.setRight(node);

            parents.push(parent);

            if (colour == 1 && depth != parentDepth + 1)
                redNodes.add(node);
            else if (depth == parentDepth + 1)
                bottomLevel.add(node);

            values.add(key);
            lastNode = node;
            parentDepth++;
        }

        return bottomLevel;
    }
}
