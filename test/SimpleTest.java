import opgave.SearchTree;
import oplossing.RainbowTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import oplossing.RedBlackTree;
import visualizer.IntegerTreeVisualizer;

import java.util.*;

public class SimpleTest {

    private static final int ADD_SEED = 1234;
    private static final int REMOVE_SEED = 5678;

    //@Disabled
    @Test
    public void RedBlackTreeAddRemove() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        // add
        int a = 50;

        Assertions.assertTrue(add(tree, true, generateKeys(a, ADD_SEED)));

        Assertions.assertEquals(a, tree.size());

        // remove
        int r = 50;

        Assertions.assertTrue(remove(tree, true, generateKeys(r, REMOVE_SEED)));

        Assertions.assertEquals(a - r, tree.size());
    }

    //@Disabled
    @Test
    public void RedBlackTreeRebuild() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        // add
        int a = 49;

        Assertions.assertTrue(add(tree, false, generateKeys(a, ADD_SEED)));

        Assertions.assertEquals(a, tree.size());

        // rebuild
        tree.rebuild();
        IntegerTreeVisualizer.print(tree);

        Assertions.assertEquals(a, tree.size());
    }

    //@Disabled
    @Test
    public void RainbowTreeAddRemove() {
        int k = 8;

        RainbowTree<Integer> tree = new RainbowTree<>(k);

        // add
        int a = 20;

        Assertions.assertTrue(add(tree, true, generateKeys(a, ADD_SEED)));

        Assertions.assertEquals(a, tree.size());

        // remove
        // TODO
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
    public boolean add(SearchTree<Integer> tree, boolean print, int... keys) {
        for (int key : keys) {
            if (!tree.add(key)) {
                System.err.println("Adding of " + key + " failed");
                return false;
            }
            if (print) {
                System.out.println("Added " + key);
                IntegerTreeVisualizer.print(tree);
            }
        }
        return true;
    }

    // remove multiple keys at once in an integer tree, stops if a key can't be removed
    public boolean remove(SearchTree<Integer> tree, boolean print, int... keys) {
        for (int key : keys) {

            if (!tree.remove(key)) {
                System.err.println("Removal of " + key + " failed");
                return false;
            }
            if (print) {
                System.out.println("Removed " + key);
                IntegerTreeVisualizer.print(tree);
            }
        }
        return true;
    }
}