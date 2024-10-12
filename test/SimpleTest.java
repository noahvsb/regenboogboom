import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import redblack.RedBlackNode;
import redblack.RedBlackTree;
import visualizer.TreeVisualizer;

import java.util.stream.IntStream;

public class SimpleTest {
    @Test
    public void RedBlackTree() {
        RedBlackNode root = new RedBlackNode(1, true);
        RedBlackTree tree = new RedBlackTree(root);
        tree.add(IntStream.range(2, 51).toArray());

        TreeVisualizer.print(tree);
    }
}