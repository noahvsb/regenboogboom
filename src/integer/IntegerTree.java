package integer;

import opgave.Node;
import opgave.SearchTree;
import redblack.RedBlackNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class IntegerTree implements SearchTree<Integer> {

    protected IntegerNode root;
    protected Set<IntegerNode> allNodes;

    public IntegerTree(IntegerNode root) {
        this.root = root;
        this.allNodes = new HashSet<>(Set.of(this.root));
    }

    @Override
    public int size() {
        return allNodes.size();
    }

    @Override
    public boolean search(Integer key) {
        for (IntegerNode n : allNodes)
            if (n.getValue().equals(key))
                return true;
        return false;
    }

    // This function serves 2 uses:
    // To get the search path (like you would expect).
    // But also if for a key search(key) returns true,
    // you can use getLast() on the returned list to get the actual Node with that key.
    public List<IntegerNode> getSearchPath(Integer key) {
        IntegerNode currentNode = root;
        List<IntegerNode> searchPath = new ArrayList<>();

        while (currentNode != null) {
            searchPath.add(currentNode);
            if (key < currentNode.getValue())
                currentNode = currentNode.getLeft();
            else if (key > currentNode.getValue())
                currentNode = currentNode.getRight();
            else
                currentNode = null;
        }

        return searchPath;
    }

    @Override
    public abstract boolean add(Integer key);

    @Override
    public abstract boolean remove(Integer key);

    @Override
    public abstract void rebuild();

    @Override
    public Node<Integer> root() {
        return root;
    }

    @Override
    public List<Integer> values() {
        return allNodes.stream().map(IntegerNode::getValue).toList();
    }
}
