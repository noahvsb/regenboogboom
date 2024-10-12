package visualizer;

import integer.IntegerNode;
import integer.IntegerTree;

import java.util.ArrayList;
import java.util.List;

// allows you to visualize simple trees with keys smaller than 100
public class TreeVisualizer {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[40m";
    public static final String ANSI_RED = "\u001B[41m";
    public static final String ANSI_GREEN = "\u001B[42m";
    public static final String ANSI_YELLOW = "\u001B[43m";
    public static final String ANSI_BLUE = "\u001B[44m";
    public static final String ANSI_PURPLE = "\u001B[45m";
    public static final String ANSI_CYAN = "\u001B[46m";
    public static final String ANSI_WHITE = "\u001B[47m";
    public static final String[] colorIntToString =
            {ANSI_BLACK, ANSI_RED, ANSI_GREEN, ANSI_YELLOW, ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN, ANSI_WHITE};

    public static void print(IntegerTree tree) {
        int depth = (int) tree.maxDepth();
        String[][] lines = new String[depth + 1][];
        lines[0] = new String[]{colouredNode(tree.root())};

        List<IntegerNode> lastLevelNodes = List.of(tree.root());
        for (int lineLevel = 1; lineLevel <= tree.maxDepth(); lineLevel++) {
            List<IntegerNode> thisLevelNodes = new ArrayList<>();
            for (IntegerNode node : lastLevelNodes) {
                if (node == null) {
                    thisLevelNodes.add(null);
                    thisLevelNodes.add(null);
                } else {
                    thisLevelNodes.add(node.getLeft());
                    thisLevelNodes.add(node.getRight());
                }
            }

            lines[lineLevel] = new String[thisLevelNodes.size()];
            for (int i = 0; i < thisLevelNodes.size(); i++)
                lines[lineLevel][i] = colouredNode(thisLevelNodes.get(i));

            lastLevelNodes = thisLevelNodes;
        }

        for (int i = 0; i < lines.length; i++) {
            String[] line = lines[i];

            int nodesOnHighestDepth = (int) Math.round(Math.pow(2, depth));
            int startSpacing = (nodesOnHighestDepth * 2) - 2 * i;
            System.out.print(" ".repeat(startSpacing));

            for (String node : line)
                System.out.print(node + "  ");
            System.out.println();
        }
    }

    private static String colouredNode(IntegerNode n) {
        if (n == null)
            return "";
        String v = n.getValue().toString();
        if (v.length() == 1)
            v = "0" + v;
        return colorIntToString[n.getColour()] + v + ANSI_RESET;
    }
}
