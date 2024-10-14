import opgave.SearchTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import oplossing.RedBlackTree;
import visualizer.SimpleIntegerTreeVisualizer;

import java.util.stream.IntStream;

public class SimpleTest {
    @Test
    public void RedBlackTree() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        Assertions.assertTrue(add(tree, IntStream.range(1, 50).toArray()));
        SimpleIntegerTreeVisualizer.print(tree);

        Assertions.assertTrue(remove(tree, IntStream.range(1, 50).toArray()));
        SimpleIntegerTreeVisualizer.print(tree);
    }

    // add multiple keys at once in an integer tree, stops if a key can't be added
    public boolean add(SearchTree<Integer> tree, int... keys) {
        for (int key : keys)
            if (!tree.add(key))
                return false;
        return true;
    }

    // remove multiple keys at once in an integer tree, stops if a key can't be removed
    public boolean remove(SearchTree<Integer> tree, int... keys) {
        for (int key : keys)
            if (!tree.remove(key))
                return false;
        return true;
    }
}