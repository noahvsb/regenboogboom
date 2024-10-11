import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import redblack.RedBlackNode;
import redblack.RedBlackTree;

public class SimpleTest {
    @Test
    public void RedBlackTree() {
        RedBlackNode root = new RedBlackNode(10, true);
        RedBlackTree tree = new RedBlackTree(root);
        Assertions.assertTrue(tree.search(10));
    }
}