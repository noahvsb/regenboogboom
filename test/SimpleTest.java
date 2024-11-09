import opgave.Node;
import opgave.SearchTree;
import oplossing.RainbowTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import oplossing.RedBlackTree;
import visualizer.IntegerTreeVisualizer;

import java.util.*;

import static visualizer.IntegerTreeVisualizer.maxDepth;

public class SimpleTest {

    private static final int ADD_SEED = 123;
    private static final int REMOVE_SEED = 456;

    @Disabled
    @Test
    public void RedBlackTreeAddRemove() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        // add
        int a = 100;

        Assertions.assertTrue(add(tree, false, generateKeys(a, ADD_SEED)));

        Assertions.assertEquals(a, tree.size());

        // remove
        int r = 100;

        Assertions.assertTrue(remove(tree, true, generateKeys(r, REMOVE_SEED)));

        Assertions.assertEquals(a - r, tree.size());
    }

    @Disabled
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

        System.out.printf("amount of nodes of colour %d: %d\n", 1, getAmountOfNodesOfColour(tree, 1));
    }

    @Disabled
    @Test
    public void RainbowTreeAddRemove() {
        int k = 5;

        RainbowTree<Integer> tree = new RainbowTree<>(k);

        // add
        int a = 50;

        Assertions.assertTrue(add(tree, true, generateKeys(a, ADD_SEED)));

        Assertions.assertEquals(a, tree.size());

        // remove
        int r = 50;

        Assertions.assertTrue(remove(tree, true, generateKeys(a, REMOVE_SEED)));

        Assertions.assertEquals(a - r, tree.size());
    }

    //@Disabled
    @Test
    public void RainbowTreeRebuild() {
        int k = 5;

        RainbowTree<Integer> tree = new RainbowTree<>(k);

        // add
        int a = 50;

        Assertions.assertTrue(add(tree, false, generateKeys(a, ADD_SEED)));

        Assertions.assertEquals(a, tree.size());

        // rebuild
        tree.rebuild();
        IntegerTreeVisualizer.print(tree);

        Assertions.assertEquals(a, tree.size());

        System.out.printf("amount of nodes of colour %d: %d\n", 1, getAmountOfNodesOfColour(tree, 1));
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

    // get the amount of nodes in a tree of a colour
    public int getAmountOfNodesOfColour(SearchTree<Integer> tree, int colour) {
        int count = 0;

        int maxDepth = (int) maxDepth(tree);
        List<Node<Integer>> lastLevelNodes = Collections.singletonList(tree.root());

        for (int i = 1; i <= maxDepth; i++) {
            List<Node<Integer>> thisLevelNodes = new ArrayList<>();
            for (Node<Integer> node : lastLevelNodes)
                for (Node<Integer> child : Arrays.asList(node.getLeft(), node.getRight()))
                    if (child != null) {
                        thisLevelNodes.add(child);
                        if (child.getColour() == colour)
                            count++;
                    }
            lastLevelNodes = thisLevelNodes;
        }

        return count;
    }
}