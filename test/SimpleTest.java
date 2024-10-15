import opgave.SearchTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import oplossing.RedBlackTree;
import visualizer.IntegerTreeVisualizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class SimpleTest {
    @Test
    public void RedBlackTree() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        int a = 30;

        Assertions.assertTrue(add(tree, generateKeys(a, true)));
        IntegerTreeVisualizer.print(tree);

        Assertions.assertEquals(a, tree.size());

        int r = 25;

        Assertions.assertTrue(remove(tree, generateKeys(r, true)));
        IntegerTreeVisualizer.print(tree);

        Assertions.assertEquals(a - r, tree.size());
    }

    public int[] generateKeys(int n, boolean shuffled) {
        List<Integer> keysList = new ArrayList<>();
        for (int i = 1; i <= n; i++)
            keysList.add(i);
        if (shuffled)
            Collections.shuffle(keysList);
        return keysList.stream().mapToInt(i -> i).toArray();
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