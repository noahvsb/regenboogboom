import org.junit.jupiter.api.Test;
import oplossing.RedBlackTree;
import visualizer.TreeVisualizer;

import java.util.stream.IntStream;

public class SimpleTest {
    @Test
    public void RedBlackTree() {
        RedBlackTree tree = new RedBlackTree();
        tree.add(IntStream.range(1, 51).toArray());

        TreeVisualizer.print(tree);
    }
}