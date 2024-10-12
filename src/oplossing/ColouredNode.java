package oplossing;

import opgave.Node;

public class ColouredNode<E extends Comparable<E>> implements Node<E> {
    private final E key;
    protected int colour;
    private boolean isRemoved;
    private RedBlackNode<E> leftChild;
    private RedBlackNode<E> rightChild;

    public ColouredNode(E key, int colour) {
        this.key = key;
        this.colour = colour;
        isRemoved = false;

        leftChild = null;
        rightChild = null;
    }

    @Override
    public E getValue() {
        return key;
    }

    @Override
    public boolean isRemoved() {
        return isRemoved;
    }

    public void changeRemoveState() {
        isRemoved = !isRemoved;
    }

    @Override
    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    @Override
    public RedBlackNode<E> getLeft() {
        return leftChild;
    }

    public void setLeft(RedBlackNode<E> leftChild) {
        this.leftChild = leftChild;
    }

    @Override
    public RedBlackNode<E> getRight() {
        return rightChild;
    }

    public void setRight(RedBlackNode<E> rightChild) {
        this.rightChild = rightChild;
    }

    public int childrenCount() {
        int count = 0;
        if (getLeft() != null)
            count++;
        if (getRight() != null)
            count++;
        return count;
    }

    public boolean isLeaf() {
        return childrenCount() == 0;
    }

    @Override
    public String toString() {
        if (isRemoved)
            return String.format("(removed - key: %s)", key);
        return String.format("(key: %s, colour: %d)", key, colour);
    }
}
