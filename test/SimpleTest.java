import opgave.SearchTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import oplossing.RedBlackTree;
import visualizer.IntegerTreeVisualizer;

import java.util.*;

public class SimpleTest {

    private static final int ADD_SEED = 123;
    private static final int REMOVE_SEED = 456;

    @Test
    public void RedBlackTreeAddRemove() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        // add
        int a = 50;

        Assertions.assertTrue(add(tree, generateKeys(a, ADD_SEED)));
        //IntegerTreeVisualizer.print(tree);

        Assertions.assertEquals(a, tree.size());

        // remove
        int r = 50;

        Assertions.assertTrue(remove(tree, generateKeys(r, REMOVE_SEED)));
        //IntegerTreeVisualizer.print(tree);

        Assertions.assertEquals(a - r, tree.size());
    }

    @Test
    public void RedBlackTreeRebuild() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        // add
        int a = 1;

        Assertions.assertTrue(add(tree, generateKeys(a, ADD_SEED)));
        //IntegerTreeVisualizer.print(tree);

        Assertions.assertEquals(a, tree.size());

        // rebuild
        tree.rebuild();
        IntegerTreeVisualizer.print(tree);

        Assertions.assertEquals(a, tree.size());
    }

    // generate keys from 1 to n (inclusive)
    public int[] generateKeys(int n, int seed) {
        List<Integer> keysList = new ArrayList<>();
        for (int i = 1; i <= n; i++)
            keysList.add(i);
        Collections.shuffle(keysList, new Random(seed));
        return keysList.stream().mapToInt(i -> i).toArray();
    }

    // add multiple keys at once in an integer tree, stops if a key can't be added
    public boolean add(SearchTree<Integer> tree, int... keys) {
        for (int key : keys) {
            System.out.println("Adding " + key);
            if (!tree.add(key)) {
                System.err.println("Adding of " + key + " failed");
                return false;
            }
            IntegerTreeVisualizer.print(tree);
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