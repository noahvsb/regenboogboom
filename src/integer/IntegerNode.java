package integer;

import opgave.Node;

public abstract class IntegerNode implements Node<Integer> {

    private final int key;
    protected int colour;
    private boolean isRemoved;

    public IntegerNode(int key, int colour) {
        this.key = key;
        this.colour = colour;
        isRemoved = false;
    }

    @Override
    public Integer getValue() {
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

    @Override
    public abstract IntegerNode getLeft(); // if I would implement this function here, I have to keep casting when using it

    @Override
    public abstract IntegerNode getRight(); // same here

    public int childrenCount() {
        int count = 0;
        if (getLeft() == null || getLeft().isRemoved)
            count++;
        if (getRight() == null || getRight().isRemoved)
            count++;
        return count;
    }

    public boolean isLeaf() {
        return childrenCount() == 0;
    }

    @Override
    public String toString() {
        if (isRemoved)
            return String.format("(removed - key: %d)", key);
        return String.format("(key: %d, colour: %d)", key, colour);
    }
}
