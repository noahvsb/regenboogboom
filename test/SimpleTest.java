import oplossing.RainbowTree;
import oplossing.RedBlackTree;
import helpfunctions.TestHelpFunctions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import visualizer.IntegerTreeVisualizer;

public class SimpleTest {

    private static final int ADD_SEED = 123;
    private static final int REMOVE_SEED = 456;

    @Test
    public void RedBlackTreeAddRemove() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        // add
        int a = 50;

        Assertions.assertTrue(TestHelpFunctions.add(tree, true, TestHelpFunctions.generateKeys(a, ADD_SEED)));

        Assertions.assertEquals(a, tree.size());

        // remove
        int r = 50;

        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, TestHelpFunctions.generateKeys(r, REMOVE_SEED)));

        Assertions.assertEquals(a - r, tree.size());
    }

    @Test
    public void RedBlackTreeRebuild() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        // add
        int a = 49;

        Assertions.assertTrue(TestHelpFunctions.add(tree, false, TestHelpFunctions.generateKeys(a, ADD_SEED)));

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

        Assertions.assertTrue(TestHelpFunctions.add(tree, true, TestHelpFunctions.generateKeys(a, ADD_SEED)));

        Assertions.assertEquals(a, tree.size());

        // remove
        int r = 50;

        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, TestHelpFunctions.generateKeys(a, REMOVE_SEED)));

        Assertions.assertEquals(a - r, tree.size());
    }

    @Test
    public void RainbowTreeRebuild() {
        int k = 5;

        RainbowTree<Integer> tree = new RainbowTree<>(k);

        // add
        int a = 49;

        Assertions.assertTrue(TestHelpFunctions.add(tree, false, TestHelpFunctions.generateKeys(a, ADD_SEED)));

        Assertions.assertEquals(a, tree.size());

        // rebuild
        tree.rebuild();
        IntegerTreeVisualizer.print(tree);

        Assertions.assertEquals(a, tree.size());

        System.out.printf("amount of nodes of colour %d: %d\n", 1, TestHelpFunctions.getAmountOfNodesOfColour(tree, 1));
    }
}