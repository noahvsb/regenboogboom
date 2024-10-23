package oplossing;

// if any other value out of range is given as its colour, it defaults to black
public class RainbowNode<E extends Comparable<E>> extends ColouredNode<E> {

    private final int k;

    private RainbowNode<E> left;
    private RainbowNode<E> right;

    public RainbowNode(E key, int colour, int k) {
        super(key, colour < 0 || colour >= k ? 0 : colour);

        this.k = k;

        this.left = null;
        this.right = null;
    }

    @Override
    public void setColour(int colour) {
        if (colour < 0 || colour >= k)
            colour = 0;
        this.colour = colour;
    }

    @Override
    public RainbowNode<E> getLeft() {
        return left;
    }

    public void setLeft(RainbowNode<E> left) {
        this.left = left;
    }

    @Override
    public RainbowNode<E> getRight() {
        return right;
    }

    public void setRight(RainbowNode<E> right) {
        this.right = right;
    }
}
