package integer;

import opgave.SearchTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class IntegerTree implements SearchTree<Integer> {

    protected IntegerNode root;
    protected Set<IntegerNode> allNodes; // excludes removed nodes

    public IntegerTree() {
        this.root = null;
        this.allNodes = new HashSet<>();
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

    // add multiple keys at once, stops if a key can't be added
    public boolean add(int... keys) {
        for (int key : keys)
            if (!add(key))
                return false;
        return true;
    }

    @Override
    public abstract boolean remove(Integer key);

    @Override
    public abstract void rebuild();

    @Override
    public IntegerNode root() {
        return root;
    }

    @Override
    public List<Integer> values() {
        return allNodes.stream().map(IntegerNode::getValue).toList();
    }

    public long maxDepth() {
        if (root == null)
            return 0;
        return 2 * log2(size() + 1) - 1;
    }

    private long log2(int n) {
        return Math.round(Math.log(n) / Math.log(2));
    }
}
