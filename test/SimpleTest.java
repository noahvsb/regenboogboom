import integer.IntegerTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import redblack.RedBlackNode;
import redblack.RedBlackTree;

public class SimpleTest {
    @Test
    public void RedBlackTree() {
        RedBlackNode root = new RedBlackNode(10, true);
        RedBlackTree tree = new RedBlackTree(root);
        Assertions.assertTrue(tree.add(12, 5));
        Assertions.assertEquals(3, tree.size());
        Assertions.assertTrue(tree.remove(10));
        Assertions.assertEquals(2, tree.size());
        System.out.println(tree.values());
        System.out.println(tree.getSearchPath(5));
    }

    public int getColour(IntegerTree tree, Integer key) {
        if (tree.search(key))
            return tree.getSearchPath(key).getLast().getColour();
        return -1;
    }
}