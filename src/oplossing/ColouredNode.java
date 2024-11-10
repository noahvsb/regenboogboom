package oplossing;

import opgave.Node;

public class ColouredNode<E extends Comparable<E>> implements Node<E> {
    private E key;
    protected int colour;
    private boolean isRemoved;
    private ColouredNode<E> left;
    private ColouredNode<E> right;

    public ColouredNode(E key, int colour) {
        this.key = key;
        this.colour = colour;
        isRemoved = false;
    }

    @Override
    public E getValue() {
        return key;
    }

    public void setValue(E key) {
        this.key = key;
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
    public ColouredNode<E> getLeft() {
        return left;
    }

    public void setLeft(ColouredNode<E> left) {
        this.left = left;
    }

    @Override
    public ColouredNode<E> getRight() {
        return right;
    }

    public void setRight(ColouredNode<E> right) {
        this.right = right;
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
