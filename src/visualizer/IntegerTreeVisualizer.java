package visualizer;

import opgave.Node;
import opgave.SearchTree;

import java.util.*;

// visualize any integer tree
// with maximum 6 colours
public class IntegerTreeVisualizer {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[0;30m";
    public static final String ANSI_BLACK_BG = "\u001B[40m";
    public static final String ANSI_RED_BG = "\u001B[41m";
    public static final String ANSI_GREEN_BG = "\u001B[42m";
    public static final String ANSI_YELLOW_BG = "\u001B[43m";
    public static final String ANSI_BLUE_BG = "\u001B[44m";
    public static final String ANSI_PURPLE_BG = "\u001B[45m";
    public static final String[] colorIntToString =
            {
                    ANSI_BLACK_BG, ANSI_BLACK + ANSI_RED_BG, ANSI_BLACK + ANSI_GREEN_BG,
                    ANSI_BLACK + ANSI_YELLOW_BG, ANSI_BLUE_BG, ANSI_BLACK + ANSI_PURPLE_BG
            };

    public static void print(SearchTree<Integer> tree) {
        int max = 0;
        for (int v : tree.values())
            if (v > max)
                max = v;

        int digits = 0;
        while (max != 0) {
            max /= 10;
            digits++;
        }

        print(tree, digits);
    }

    public static void print(SearchTree<Integer> tree, int digits) {
        if (tree.size() == 0) {
            System.out.println("empty");
        } else {
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

                int startSpacing = getSpacing(i + 1, actualDepth, digits);
                int inBetweenSpacing = getSpacing(i, actualDepth, digits);
                System.out.print(" ".repeat(startSpacing));

                for (String node : line)
                    System.out.print(node + " ".repeat(inBetweenSpacing));
                System.out.println();
            }

            // extra 2 newlines
            System.out.print("\n\n");
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
        if (n.isRemoved())
            return colorIntToString[n.getColour()] + "-".repeat(digits) + ANSI_RESET;
        String v = n.getValue().toString();
        v = "0".repeat(digits - v.length()) + v;
        return colorIntToString[n.getColour()] + v + ANSI_RESET;
    }

    private static int getSpacing(int currentDepth, int depth, int digits) {
        if (currentDepth > depth)
            return 0;
        return 2 * getSpacing(currentDepth + 1, depth, digits) + digits;
    }
}
