package oplossing;

import opgave.Node;
import opgave.SearchTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RainbowTree<E extends Comparable<E>> implements SearchTree<E> {

    private RainbowNode<E> root;
    private final HashSet<E> values;
    private int removedAmount;

    public RainbowTree() {
        root = null;
        values = new HashSet<>();
        removedAmount = 0;
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean search(E key) {
        return values.contains(key);
    }

    public List<RainbowNode<E>> getSearchPath(E key) {
        RainbowNode<E> currentNode = root;
        List<RainbowNode<E>> searchPath = new ArrayList<>();

        while (currentNode != null) {
            searchPath.add(currentNode);
            if (key.compareTo(currentNode.getValue()) < 0)
                currentNode = currentNode.getLeft();
            else if (key.compareTo(currentNode.getValue()) > 0)
                currentNode = currentNode.getRight();
            else
                currentNode = null;
        }

        return searchPath;
    }

    @Override
    public boolean add(E o) {
        return false;
    }

    @Override
    public boolean remove(E e) {
        return false;
    }

    @Override
    public void rebuild() {

    }

    @Override
    public Node<E> root() {
        return root;
    }

    @Override
    public List<E> values() {
        return values.stream().sorted().toList();
    }
}
