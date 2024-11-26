package oplossing;

import java.util.*;

public class RedBlackTree<E extends Comparable<E>> extends ColouredTree<E> {

    public RedBlackTree() {
        super();
        k = 2;
    }

    // make sure the issueSource, p1 and p2 nodes are removed from the path
    protected void fix2WrongColoursProblem(List<ColouredNode<E>> path, ColouredNode<E> issueSource, ColouredNode<E> p1, ColouredNode<E> p2, boolean allowOptimized) {
        boolean problem = true;

        while (problem) {
            boolean isLeft = p2.getLeft() != null && p2.getLeft().equals(p1);
            boolean useOptimized = allowOptimized && ((!isLeft && (p2.getLeft() == null || p2.getLeft().getColour() == 0))
                    || ((isLeft && (p2.getRight() == null || p2.getRight().getColour() == 0))));

            List<ColouredNode<E>> nodes = orderNodes(issueSource, p1, p2);
            ColouredNode<E> n1 = nodes.getFirst();
            ColouredNode<E> n2 = nodes.get(1);
            ColouredNode<E> n3 = nodes.get(2);

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

    protected boolean removeSpecialCases(ColouredNode<E> node, ColouredNode<E> parent) {
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

        // node with 1 child  => remove with changes in the tree
        // as long as the tree meets the conditions, the node will be black
        if (node.childrenCount() == 1) {
            // grab the child
            ColouredNode<E> child = node.getLeft() != null ? node.getLeft() : node.getRight();

            // attach to the parent instead of the node we want to remove
            if (parent == null)
                root = child;
            else if (parent.getLeft() == node)
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

        // intern node => swap with biggest in the left subtree and call remove recursively
        return swapInternNode(node);
    }

    @Override
    boolean tombstoneCheck(ColouredNode<E> swapNode, ColouredNode<E> swapNodeParent) {
        return swapNode.getColour() == 0 && swapNodeParent.getColour() == 0 && swapNode.isLeaf();
    }

    private void colourRed(ColouredNode<E> node) {
        node.setColour(1);

        ColouredNode<E> child = node.getLeft();
        if (child == null || child.getColour() == 0)
            child = node.getRight();

        // problem occurs
        if (child != null && child.getColour() == 1) {
            // I chose clean code over efficiëncy
            // the less efficiëncy is hardly noticeable tho
            List<ColouredNode<E>> path = getSearchPath(child.getValue());
            ColouredNode<E> issueSource = path.removeLast();
            ColouredNode<E> p1 = path.removeLast();
            ColouredNode<E> p2 = path.removeLast();
            fix2WrongColoursProblem(path, issueSource, p1, p2, false);
        }
    }

    @Override
    public void rebuild() {
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
            // the indexes for red leaf keys are the even indexes
            // but not all of them or else you would have too many red leaf keys
            int bottomKeysAmount = n - ((int) Math.pow(2, cbtDepth + 1) - 1);

            List<E> bottomKeys = new ArrayList<>();
            List<E> cbtKeys = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (i < 2 * bottomKeysAmount && i % 2 == 0)
                    bottomKeys.add(keys.get(i));
                else
                    cbtKeys.add(keys.get(i));
            }

            // build complete binary tree using the other keys
            List<List<ColouredNode<E>>> parentsPerLevel = buildCompleteBinaryTree(cbtKeys, cbtDepth);

            // add red leafs with some slight changes to minimize the amount of red nodes
            int i = 1;
            for (E key : bottomKeys) {
                ColouredNode<E> parent = parentsPerLevel.getFirst().get((i - 1) / 2);
                if (i % 2 == 1) {
                    parent.setLeft(new ColouredNode<>(key, 1));
                } else {
                    parent.setRight(new ColouredNode<>(key, 0));
                    parent.getLeft().setColour(0);
                    parent.setColour(1);
                    int j = 1;
                    while ((i / Math.pow(2, j)) % 2 == 0) {
                        parent.setColour(0);
                        parent = parentsPerLevel.get(j).get((i - 1) / (int) Math.pow(2, j + 1));
                        parent.getLeft().setColour(0);
                        parent.setColour(1);
                        j++;
                    }
                }
                values.add(key);
                i++;
            }
        }
    }

    private List<List<ColouredNode<E>>> buildCompleteBinaryTree(List<E> keys, int depth) {
        List<List<ColouredNode<E>>> parentsPerLevel = new ArrayList<>();
        for (int i = 0; i <= depth; i++) {
            parentsPerLevel.add(new ArrayList<>());
        }

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

        parentsPerLevel.getLast().add(root);

        for (E key : keys) {
            ColouredNode<E> node = new ColouredNode<>(key, 0);

            if (parentDepth != depth && lastNode.getLeft() == null) {
                lastNode.setLeft(node);

                parents.push(lastNode);
            } else {
                parentDepth--;
                ColouredNode<E> parent = parents.pop();
                while (parent.getRight() != null) {
                    parentDepth--;
                    parent = parents.pop();
                }

                parent.setRight(node);

                parents.push(parent);
            }

            parentsPerLevel.get(depth - (parentDepth + 1)).add(node);

            values.add(key);
            lastNode = node;
            parentDepth++;
        }

        return parentsPerLevel;
    }
}