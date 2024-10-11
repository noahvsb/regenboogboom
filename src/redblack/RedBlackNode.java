package redblack;

import integer.IntegerNode;

public class RedBlackNode extends IntegerNode {

    private RedBlackNode leftChild;
    private RedBlackNode rightChild;

    public RedBlackNode(int key, boolean black) {
        super(key, black ? 0 : 1);

        leftChild = null;
        rightChild = null;
    }

    public void setColour(boolean black) {
        colour = black ? 0 : 1;
    }

    @Override
    public RedBlackNode getLeft() {
        return leftChild;
    }

    public void setLeft(RedBlackNode leftChild) {
        this.leftChild = leftChild;
    }

    @Override
    public RedBlackNode getRight() {
        return rightChild;
    }

    public void setRight(RedBlackNode rightChild) {
        this.rightChild = rightChild;
    }
}
