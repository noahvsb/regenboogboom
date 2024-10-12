import integer.IntegerTree;
import org.junit.jupiter.api.Test;
import redblack.RedBlackNode;
import redblack.RedBlackTree;
import visualizer.TreeVisualizer;

public class SimpleTest {
    @Test
    public void RedBlackTree() {
        RedBlackNode root = new RedBlackNode(1, true);
        RedBlackTree tree = new RedBlackTree(root);
        tree.add(2, 3, 4, 5, 6, 7, 8, 9, 10);

        TreeVisualizer.print(tree);
    }

    public int getColour(IntegerTree tree, Integer key) {
        if (tree.search(key))
            return tree.getSearchPath(key).getLast().getColour();
        return -1;
    }
}