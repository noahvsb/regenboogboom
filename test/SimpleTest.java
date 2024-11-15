import oplossing.RainbowTree;
import oplossing.RedBlackTree;
import helpfunctions.TestHelpFunctions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import visualizer.IntegerTreeVisualizer;

public class SimpleTest {

    private static final int ADD_SEED = 123;
    private static final int REMOVE_SEED = 456;
    private static final int MIX_SEED = 789;

    @Test
    public void RedBlackTreeAddRemove() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        // add
        int a = 50;

        Assertions.assertTrue(TestHelpFunctions.add(tree, a, ADD_SEED, true));

        Assertions.assertEquals(a, tree.size());

        // remove
        int r = 50;

        Assertions.assertTrue(TestHelpFunctions.remove(tree, r, REMOVE_SEED, true));

        Assertions.assertEquals(a - r, tree.size());
    }

    @Test
    public void RedBlackTreeMix() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        int n = 150;
        int max = 50;

        Assertions.assertTrue(TestHelpFunctions.mix(tree, n, max, MIX_SEED, true));
    }

    @Test
    public void RedBlackTreeRebuild() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        // add
        int a = 49;

        Assertions.assertTrue(TestHelpFunctions.add(tree, a, ADD_SEED, false));

        Assertions.assertEquals(a, tree.size());

        // rebuild
        tree.rebuild();
        IntegerTreeVisualizer.print(tree);

        Assertions.assertEquals(a, tree.size());

        System.out.printf("amount of nodes of colour %d: %d\n", 1, TestHelpFunctions.getAmountOfNodesOfColour(tree, 1));
    }

    @Test
    public void RainbowTreeAddRemove() {
        int k = 5;

        RainbowTree<Integer> tree = new RainbowTree<>(k);

        // add
        int a = 50;

        Assertions.assertTrue(TestHelpFunctions.add(tree, a, ADD_SEED, true));

        Assertions.assertEquals(a, tree.size());

        // remove
        int r = 50;

        Assertions.assertTrue(TestHelpFunctions.remove(tree, r, REMOVE_SEED, true));

        Assertions.assertEquals(a - r, tree.size());
    }

    @Test
    public void RainbowTreeMix() {
        int k = 5;

        RainbowTree<Integer> tree = new RainbowTree<>(k);

        int n = 150;
        int max = 50;

        Assertions.assertTrue(TestHelpFunctions.mix(tree, n, max, MIX_SEED, true));
    }

    @Test
    public void RainbowTreeRebuild() {
        int k = 5;

        RainbowTree<Integer> tree = new RainbowTree<>(k);

        // add
        int a = 49;

        Assertions.assertTrue(TestHelpFunctions.add(tree, a, ADD_SEED, false));

        Assertions.assertEquals(a, tree.size());

        // rebuild
        tree.rebuild();
        IntegerTreeVisualizer.print(tree);

        Assertions.assertEquals(a, tree.size());

        System.out.printf("amount of nodes of colour %d: %d\n", 1, TestHelpFunctions.getAmountOfNodesOfColour(tree, 1));
    }
}