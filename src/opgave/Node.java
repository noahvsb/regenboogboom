package opgave;

/**
 * A read-only view on the node of a simple binary search tree with an associated color.
 *
 * @param <E> the type of elements that can be contained in the tree
 */
public interface Node<E extends Comparable<E>> {

    /**
     * @return the current value of this node
     */
    E getValue();

    /**
     * @return whether this node is a tombstone and the value has been removed from this tree
     */
    boolean isRemoved();

    /**
     * Return the colour of this node.
     *
     * @return an integer representing the color of this node
     */
    int getColour();

    /**
     * @return the left child of this node, or <tt>null</tt> if the node has no left child
     */
    Node<E> getLeft();

    /**
     * @return the right child of this node, or <tt>null</tt> if the node has no right child
     */
    Node<E> getRight();
}
