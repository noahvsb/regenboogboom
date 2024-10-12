package oplossing;

// if any other value than red or black is given as its colour, it defaults to black
public class RedBlackNode<E extends Comparable<E>> extends ColouredNode<E> {

    public RedBlackNode(E key, int colour) {
        super(key, colour != 1 ? 0 : 1);
    }

    @Override
    public void setColour(int colour) {
        if (colour != 1)
            colour = 0;
        this.colour = colour;
    }
}
