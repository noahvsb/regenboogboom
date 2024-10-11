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
    public abstract IntegerNode getLeft();

    @Override
    public abstract IntegerNode getRight();
}
