import opgave.SearchTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import oplossing.RedBlackTree;
import visualizer.IntegerTreeVisualizer;

import java.util.*;

public class SimpleTest {

    private static final int SEED = 123;

    @Test
    public void RedBlackTree() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        int a = 100;

        Assertions.assertTrue(add(tree, generateKeys(a, true)));
        IntegerTreeVisualizer.print(tree);

        Assertions.assertEquals(a, tree.size());

        int r = 100;

        Assertions.assertTrue(remove(tree, generateKeys(r, true)));
        IntegerTreeVisualizer.print(tree);

        Assertions.assertEquals(a - r, tree.size());
    }

    // generate keys from 1 to n (inclusive)
    public int[] generateKeys(int n, boolean shuffled) {
        List<Integer> keysList = new ArrayList<>();
        for (int i = 1; i <= n; i++)
            keysList.add(i);
        if (shuffled)
            Collections.shuffle(keysList, new Random(SEED));
        return keysList.stream().mapToInt(i -> i).toArray();
    }

    // add multiple keys at once in an integer tree, stops if a key can't be added
    public boolean add(SearchTree<Integer> tree, int... keys) {
        for (int key : keys)
            if (!tree.add(key)) {
                System.err.println("Adding " + key + " failed");
                return false;
            }
        return true;
    }

    // remove multiple keys at once in an integer tree, stops if a key can't be removed
    public boolean remove(SearchTree<Integer> tree, int... keys) {
        for (int key : keys) {
            System.out.println("Removing " + key);
            if (!tree.remove(key)) {
                System.err.println("Removal of " + key + " failed");
                return false;
            }
            IntegerTreeVisualizer.print(tree);
        }
        return true;
    }
}