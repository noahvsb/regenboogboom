import opgave.Node;

public class BlackNode implements Node {


    @Override
    public Comparable getValue() {
        return null;
    }

    @Override
    public boolean isRemoved() {
        return false;
    }

    @Override
    public int getColour() {
        return 0;
    }

    @Override
    public Node getLeft() {
        return null;
    }

    @Override
    public Node getRight() {
        return null;
    }
}
