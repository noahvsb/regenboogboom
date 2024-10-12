package visualizer;

import opgave.Node;
import opgave.SearchTree;
import oplossing.RedBlackTree;

import java.util.*;

// visualize any integer tree with maximum 6 colours
public class IntegerTreeVisualizer {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[40m";
    public static final String ANSI_RED = "\u001B[41m";
    public static final String ANSI_GREEN = "\u001B[42m";
    public static final String ANSI_YELLOW = "\u001B[43m";
    public static final String ANSI_BLUE = "\u001B[44m";
    public static final String ANSI_PURPLE = "\u001B[45m";
    public static final String[] colorIntToString =
            {ANSI_BLACK, ANSI_RED, ANSI_GREEN, ANSI_YELLOW, ANSI_BLUE, ANSI_PURPLE};

    public static void print(SearchTree<Integer> tree) {
        print(tree, 2);
    }
    public static void print(SearchTree<Integer> tree, int digits) {
        int maxDepth = (int) maxDepth(tree);
        int actualDepth = maxDepth;

        String[][] lines = new String[maxDepth + 1][];
        lines[0] = new String[]{nodeToString(tree.root(), digits)};

        List<Node<Integer>> lastLevelNodes = Collections.singletonList(tree.root());

        for (int lineLevel = 1; lineLevel <= maxDepth; lineLevel++) {

            // fill a list with all nodes in this level, which are the children of the nodes from the last level
            List<Node<Integer>> thisLevelNodes = new ArrayList<>();
            for (Node<Integer> node : lastLevelNodes) {
                if (node == null) {
                    thisLevelNodes.add(null);
                    thisLevelNodes.add(null);
                } else {
                    thisLevelNodes.add(node.getLeft());
                    thisLevelNodes.add(node.getRight());
                }
            }

            // create the string array using all nodes on this level and fill it unless all nodes are null
            lines[lineLevel] = new String[thisLevelNodes.size()];
            if (thisLevelNodes.stream().filter(Objects::nonNull).toList().isEmpty())
                actualDepth--;
            else
                for (int i = 0; i < thisLevelNodes.size(); i++)
                    lines[lineLevel][i] = nodeToString(thisLevelNodes.get(i), digits);

            lastLevelNodes = thisLevelNodes;
        }

        // print out all lines with spacing
        for (int i = 0; i <= actualDepth; i++) {
            String[] line = lines[i];

            int nodesOnHighestDepth = (int) Math.round(Math.pow(2, actualDepth));
            int startSpacing = (nodesOnHighestDepth * 2) - 2 * i;
            System.out.print(" ".repeat(startSpacing));

            for (String node : line)
                System.out.print(node + "  ".repeat(2 * (actualDepth - i)));
            System.out.println();
        }
    }

    private static long maxDepth(SearchTree<Integer> tree) {
        if (tree.root() == null)
            return 0;
        return 2 * log2(tree.size() + 1) - 1;
    }

    private static long log2(int n) {
        return Math.round(Math.log(n) / Math.log(2));
    }

    // convert a node to a colouredString
    private static String nodeToString(Node<Integer> n, int digits) {
        if (n == null)
            return " ".repeat(digits);
        String v = n.getValue().toString();
        v = "0".repeat(digits - v.length()) + v;
        return colorIntToString[n.getColour()] + v + ANSI_RESET;
    }
}
