package oplossing;

// if any other value than red or black is given as its colour, it defaults to black
public class RedBlackNode<E extends Comparable<E>> extends ColouredNode<E> {

    private RedBlackNode<E> left;
    private RedBlackNode<E> right;

    public RedBlackNode(E key, int colour) {
        super(key, colour != 1 ? 0 : 1);

        left = null;
        right = null;
    }

    @Override
    public void setColour(int colour) {
        if (colour != 1)
            colour = 0;
        this.colour = colour;
    }

    @Override
    public RedBlackNode<E> getLeft() {
        return left;
    }

    public void setLeft(RedBlackNode<E> left) {
        this.left = left;
    }

    @Override
    public RedBlackNode<E> getRight() {
        return right;
    }

    public void setRight(RedBlackNode<E> right) {
        this.right = right;
    }
}
