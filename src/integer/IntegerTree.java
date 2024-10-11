package integer;

import opgave.Node;
import opgave.SearchTree;

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
