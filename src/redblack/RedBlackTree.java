package redblack;

import integer.IntegerNode;
import integer.IntegerTree;
import visualizer.TreeVisualizer;

import java.util.Arrays;
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
            RedBlackNode issueSource = new RedBlackNode(key, false);

            RedBlackNode p1 = (RedBlackNode) path.removeLast();
            RedBlackNode p2 = (RedBlackNode) path.removeLast();
            boolean problem = true;

            while (problem) {
                boolean useOptimized = p2.getLeft() == null || p2.getLeft().getColour() == 0
                        || p2.getRight() == null || p2.getRight().getColour() == 0;

                List<RedBlackNode> nodes = orderNodes(issueSource, p1, p2);
                RedBlackNode n1 = nodes.getFirst();
                RedBlackNode n2 = nodes.get(1);
                RedBlackNode n3 = nodes.get(2);

                // set colours
                if (useOptimized) {
                    n1.setColour(true);
                    n2.setColour(false);
                    n3.setColour(false);
                } else {
                    n1.setColour(false);
                    n2.setColour(true);
                    n3.setColour(true);
                }

                // build subtree
                if (path.isEmpty()) {
                    root = n1;
                }
                else {
                    RedBlackNode p = (RedBlackNode) path.getLast();
                    if (n1.getValue() < p.getValue())
                        p.setLeft(n1);
                    else
                        p.setRight(n1);
                }

                System.out.println(nodes);
                n1.setLeft(n2);
                n1.setRight(n3);
                n2.setLeft(nodes.get(3));
                n2.setRight(nodes.get(4));
                n3.setLeft(nodes.get(5));
                n3.setLeft(nodes.get(6));

                // check if done
                if (useOptimized) {
                    problem = false;
                } else {
                    issueSource = nodes.getFirst();
                    // issue is root
                    if (path.isEmpty()) {
                        issueSource.setColour(true);
                        problem = false;
                    }
                    else {
                        p1 = (RedBlackNode) path.removeLast();
                        // parent is black
                        if (p1.getColour() == 0)
                            problem = false;
                        else
                            p2 = (RedBlackNode) path.removeLast();
                    }
                }
                TreeVisualizer.print(this);
            }
        }
        return true;
    }

    private List<RedBlackNode> orderNodes(RedBlackNode n1, RedBlackNode n2, RedBlackNode n3) {
        if (n1.getValue() < n2.getValue()) {
            if (n1.getValue() < n3.getValue()) {
                return Arrays.asList(n2, n1, n3, n1.getLeft(), n1.getRight(), n2.getRight(), n3.getRight());
            }
            return Arrays.asList(n1, n3,n2, n3.getLeft(), n1.getLeft(), n1.getRight(), n2.getRight());
        }
        if (n1.getValue() < n3.getValue()) {
            return Arrays.asList(n1, n2, n3, n2.getLeft(), n1.getLeft(), n1.getRight(), n3.getRight());
        }
        return Arrays.asList(n2, n3, n1, n3.getLeft(), n2.getLeft(), n1.getLeft(), n1.getRight());
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