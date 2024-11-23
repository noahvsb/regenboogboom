import oplossing.ColouredNode;
import oplossing.RainbowTree;
import oplossing.RedBlackTree;
import helpfunctions.TestHelpFunctions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import visualizer.IntegerTreeVisualizer;

public class DetailedTest {

    @Test
    public void RedBlackTreeAdd() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        // add root
        Assertions.assertTrue(TestHelpFunctions.add(tree, false, 5));
        Assertions.assertEquals(1, tree.size());

        // try to add a key that already exists
        Assertions.assertFalse(TestHelpFunctions.add(tree, false, 5));
        Assertions.assertEquals(1, tree.size());

        // add key that's a tombstone
        tree.tombstone((ColouredNode<Integer>) tree.root());
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 5));
        Assertions.assertEquals(1, tree.size());

        // add a key that should result in no issue (coloured red)
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 3));
        Assertions.assertEquals(2, tree.size());

        // add a key that should result in a simple issue where the optimized fix can occur
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 4));
        Assertions.assertEquals(3, tree.size());

        // add a key that should result in a simple issue where the optimized fix can't occur
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 6));
        Assertions.assertEquals(4, tree.size());

        // add a key that should result in a more complex issue
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 1, 2, 7, 8, 10, 12, 9)); // preparation work
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 11));
        Assertions.assertEquals(12, tree.size());
    }

    @Test
    public void RedBlackTreeRemove() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();

        // try to remove a key that doesn't exist yet
        Assertions.assertFalse(TestHelpFunctions.remove(tree, false, 5));
        Assertions.assertEquals(0, tree.size());

        // remove the root in a tree with only a root
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 5)); // preparation work
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 5));
        Assertions.assertEquals(0, tree.size());

        // remove a red leaf
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 5, 3)); // preparation work
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 3));
        Assertions.assertEquals(1, tree.size());

        // remove a black node with 1 child
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 3)); // preparation work
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 5));
        Assertions.assertEquals(1, tree.size());

        // remove a black leaf with red parent
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 1, 6, 11, 16, 14, 20)); // preparation work
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 20));
        Assertions.assertEquals(6, tree.size());

        // remove a black leaf with black parent
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 7, 8, 9, 10, 12, 13)); // preparation work
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 13));
        Assertions.assertEquals(11, tree.size());

        // remove an intern node (normal)
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 9));
        Assertions.assertEquals(10, tree.size());

        // remove an intern node (special case, where swap would result in an incorrect tree)
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 14));
        Assertions.assertEquals(9, tree.size());

        // create enough tombstones so rebuild happens
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 16, 10, 11)); // preparation work
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 12));
        Assertions.assertEquals(5, tree.size());
    }

    @Test
    public void RedBlackTreeRebuild() {
        for (int n = 1; n <= 50; n++) {
            RedBlackTree<Integer> tree = new RedBlackTree<>();

            Assertions.assertTrue(TestHelpFunctions.add(tree, n, 123, false)); // preparation work

            tree.rebuild();

            Assertions.assertTrue(TestHelpFunctions.checkConditions(tree));
            Assertions.assertEquals(n, tree.size());
            Assertions.assertEquals(TestHelpFunctions.getSmallestAmountOfRedNodes(n), TestHelpFunctions.getAmountOfNodesOfColour(tree , 1));

            System.out.printf("amount of red nodes: %d\n", TestHelpFunctions.getAmountOfNodesOfColour(tree, 1));
            IntegerTreeVisualizer.print(tree);
        }
    }

    @Test
    public void RainbowTreeAdd() {
        RainbowTree<Integer> tree = new RainbowTree<>(4);

        // add root
        Assertions.assertTrue(TestHelpFunctions.add(tree, false, 16));
        Assertions.assertEquals(1, tree.size());

        // try to add a key that already exists
        Assertions.assertFalse(TestHelpFunctions.add(tree, false, 16));
        Assertions.assertEquals(1, tree.size());

        // add key that's a tombstone
        tree.tombstone((ColouredNode<Integer>) tree.root());
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 16));
        Assertions.assertEquals(1, tree.size());

        // add keys that should result in no issue
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 10, 6, 9, 12, 14));
        Assertions.assertEquals(6, tree.size());

        // add a key that should result in a complex issue
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 13));
        Assertions.assertEquals(7, tree.size());

        // add a key that should result in a simple issue
        // the resulted tree from the complex issue makes it way easier to test for a simple issue
        // that's why I did complex first
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 7));
        Assertions.assertEquals(8, tree.size());
    }

    @Test
    public void RainbowTreeRemove() {
        RainbowTree<Integer> tree = new RainbowTree<>(3);

        // try to remove a key that doesn't exist yet
        Assertions.assertFalse(TestHelpFunctions.remove(tree, false, 5));
        Assertions.assertEquals(0, tree.size());

        // remove the root in a tree with only a root
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 5)); // preparation work
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 5));
        Assertions.assertEquals(0, tree.size());

        // remove a non-black leaf
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 5, 3)); // preparation work
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 3));
        Assertions.assertEquals(1, tree.size());

        // remove a black node with 1 child
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 3)); // preparation work
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 5));
        Assertions.assertEquals(1, tree.size());

        // remove a non-black node with 1 child (should perform the same as with a black node with 1 child)
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 8, 5)); // preparation work
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 8));
        Assertions.assertEquals(2, tree.size());

        // remove a black leaf
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 6, 11)); // preparation work
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 11));
        Assertions.assertEquals(3, tree.size());

        // remove an intern node (normal)
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 6));
        Assertions.assertEquals(2, tree.size());

        // remove an intern node (special case, where swap would result in an incorrect tree)
        Assertions.assertTrue(TestHelpFunctions.add(tree, true, 8, 15));
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 5));
        Assertions.assertEquals(3, tree.size());

        // create enough tombstones so rebuild happens
        Assertions.assertTrue(TestHelpFunctions.remove(tree, true, 3));
        Assertions.assertEquals(2, tree.size());
    }

    @Test
    public void RainbowTreeRebuild() {
        for (int n = 1; n <= 50; n++) {
            RainbowTree<Integer> tree = new RainbowTree<>(4);

            Assertions.assertTrue(TestHelpFunctions.add(tree, n, 123, false)); // preparation work

            tree.rebuild();

            Assertions.assertTrue(TestHelpFunctions.checkConditions(tree));
            Assertions.assertEquals(n, tree.size());

            System.out.printf("amount of nodes of colour %d: %d\n", 1, TestHelpFunctions.getAmountOfNodesOfColour(tree, 1));
            IntegerTreeVisualizer.print(tree);
        }
    }
}
