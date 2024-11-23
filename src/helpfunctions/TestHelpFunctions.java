package helpfunctions;

import opgave.Node;
import opgave.SearchTree;
import visualizer.IntegerTreeVisualizer;

import java.util.*;

public class TestHelpFunctions {

    // generate keys from 1 to n (inclusive)
    private static int[] generateKeys(int n, int seed) {
        List<Integer> keysList = new ArrayList<>();
        for (int i = 1; i <= n; i++)
            keysList.add(i);
        Collections.shuffle(keysList, new Random(seed));
        return keysList.stream().mapToInt(i -> i).toArray();
    }

    // add multiple keys at once in an integer tree, stops if a key can't be added
    public static boolean add(SearchTree<Integer> tree, boolean print, int... keys) {
        for (int key : keys) {
            if (!tree.add(key)) {
                if (print)
                    System.err.println("Adding of " + key + " failed");
                return false;
            }
            if (print) {
                System.out.println("Added " + key);
                IntegerTreeVisualizer.print(tree);
            }
            if (!checkConditions(tree))
                return false;
        }
        return true;
    }

    // first generate keys, then add them like before
    public static boolean add(SearchTree<Integer> tree, int n, int seed, boolean print) {
        return add(tree, print, generateKeys(n, seed));
    }

    // remove multiple keys at once in an integer tree, stops if a key can't be removed
    public static boolean remove(SearchTree<Integer> tree, boolean print, int... keys) {
        for (int key : keys) {
            if (!tree.remove(key)) {
                if (print)
                    System.err.println("Removal of " + key + " failed");
                return false;
            }
            if (print) {
                System.out.println("Removed " + key);
                IntegerTreeVisualizer.print(tree);
            }
            if (!checkConditions(tree))
                return false;
        }
        return true;
    }

    // first generate keys, then remove them like before
    public static boolean remove(SearchTree<Integer> tree, int n, int seed, boolean print) {
        return remove(tree, print, generateKeys(n, seed));
    }

    // generate random keys with a bound, add by default, but if you can't, remove it instead
    public static boolean mix(SearchTree<Integer> tree, int n, int max, int seed, boolean print) {
        Random r = new Random(seed);

        for (int i = 0; i < n; i++) {
            int key = r.nextInt(max);

            // add
            if (tree.add(key)) {
                if (print) {
                    System.out.println("Added " + key);
                    IntegerTreeVisualizer.print(tree);
                }
            }
            // remove
            else {
                tree.remove(key);
                if (print) {
                    System.out.println("Removed " + key);
                    IntegerTreeVisualizer.print(tree);
                }
            }

            if (!checkConditions(tree))
                return false;
        }

        return true;
    }

    // check if a tree meets the conditions
    public static boolean checkConditions(SearchTree<Integer> tree) {
        if (tree.root() == null)
            return true;

        if (tree.root().getColour() != 0) {
            System.err.println("Root is not black");
            return false;
        }

        int maxDepth = (int) maxDepth(tree);
        int depth = 0;
        List<Node<Integer>> lastLevelNodes = Collections.singletonList(tree.root());

        for (int i = 1; i <= maxDepth; i++) {
            List<Node<Integer>> thisLevelNodes = new ArrayList<>();

            for (Node<Integer> node : lastLevelNodes)
                for (Node<Integer> child : Arrays.asList(node.getLeft(), node.getRight()))
                    if (child != null) {
                        thisLevelNodes.add(child);

                        if (child.getColour() != 0 && child.getColour() <= node.getColour()) {
                            System.err.println("There's a non-black node with incorrect children");
                            return false;
                        }
                    }

            lastLevelNodes = thisLevelNodes;
            if (!lastLevelNodes.isEmpty())
                depth++;
        }

        List<List<Boolean>> sequences = generateBooleanSequences(depth);
        int z = -1;
        for (List<Boolean> sequence : sequences) {
            Node<Integer> node = tree.root();
            int currentZ = 1;

            for (Boolean left : sequence) {
                if (left)
                    node = node.getLeft();
                else
                    node = node.getRight();

                if (node == null)
                    break;

                if (node.getColour() == 0)
                    currentZ++;
            }

            if (z == -1)
                z = currentZ;
            else if (z != currentZ) {
                System.out.println("z: " + z + " -- current z: " + currentZ);
                System.err.println("Not every path from the root to a null-pointer has the same amount of black nodes");
                return false;
            }
        }

        return true;
    }

    private static List<List<Boolean>> generateBooleanSequences(int n) {
        if (n == 0)
            return new ArrayList<>();
        if (n == 1)
            return new ArrayList<>(List.of(
                    new ArrayList<>(List.of(true)),
                    new ArrayList<>(List.of(false))
            ));

        List<List<Boolean>> recursion = generateBooleanSequences(n - 1);
        List<List<Boolean>> result = new ArrayList<>();

        for (List<Boolean> sequence : recursion) {
            List<Boolean> clone = new ArrayList<>(sequence);
            clone.add(true);
            sequence.add(false);

            result.add(clone); result.add(sequence);
        }

        return result;
    }

    // get the amount of nodes in a tree of a colour
    public static int getAmountOfNodesOfColour(SearchTree<Integer> tree, int colour) {
        int count = 0;

        int maxDepth = maxDepth(tree);
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

    // get smallest amount of red nodes in a coloured tree with n keys
    public static int getSmallestAmountOfRedNodes(int n) {
        int cbtDepth = log2(n);
        if (n != (int) Math.pow(2, cbtDepth + 1) - 1)
            cbtDepth--;

        int bottomKeysAmount = n - ((int) Math.pow(2, cbtDepth + 1) - 1);

        int redKeysAmount = 0;

        for (int i = cbtDepth; i >= 0; i--) {
            int pow = (int) Math.pow(2, i);
            if (bottomKeysAmount >= pow) {
                redKeysAmount++;
                bottomKeysAmount -= pow;
            }
        }

        return redKeysAmount;
    }

    // rounded down
    private static int log2(int n) {
        return (int) Math.floor(Math.log(n) / Math.log(2));
    }

    public static int maxDepth(SearchTree<Integer> tree) {
        if (tree.root() == null)
            return 0;
        return 2 * log2(tree.size() + 1) - 1;
    }

}