import opgave.SearchTree;
import oplossing.RainbowTree;
import oplossing.RedBlackTree;

import java.util.*;

public class Benchmark {
    public static void main(String[] args) {
        // RED/BLACK TREE
        System.out.print("RED/BLACK TREE\n\n");

        redBlackTree(1000, 100);
        redBlackTree(1000, 1000);
        redBlackTree(100, 10000);
        redBlackTree(30, 100000);


        // RAINBOW TREE
        System.out.print("\n\nRAINBOW TREE\n");
        for (int k = 2; k <= 8; k++) {
            System.out.printf("\nk = %d\n\n", k);

            rainbowTree(k, 1000, 100);
            rainbowTree(k, 1000, 1000);
            rainbowTree(k, 100, 10000);
            rainbowTree(k, 30, 100000);
        }
    }

    private static void redBlackTree(int t, int n) {
        // add n nodes
        long[] times = new long[t];
        for (int i = 0; i < times.length; i++) {
            int[] keys = generateKeys(n, true);
            RedBlackTree<Integer> tree = new RedBlackTree<>();

            long start = System.currentTimeMillis();
            searchTreeAdd(tree, keys);
            long stop = System.currentTimeMillis();

            times[i] = stop - start;
        }
        long addAverage = getAverageTime(times);
        System.out.printf("add %d nodes:\n%dms\n", n, addAverage);

        // remove n nodes
        times = new long[t];
        for (int i = 0; i < times.length; i++) {
            int[] keys = generateKeys(n, true);
            RedBlackTree<Integer> tree = new RedBlackTree<>();

            long start = System.currentTimeMillis();
            searchTreeAddThenRemove(tree, keys);
            long stop = System.currentTimeMillis();

            times[i] = stop - start;
        }
        long removeAverage = getAverageTime(times) - addAverage;
        System.out.printf("remove %d nodes:\n%dms\n\n", n, removeAverage);
    }

    private static void rainbowTree(int k, int t, int n) {
        // add n nodes
        long[] times = new long[t];
        for (int i = 0; i < times.length; i++) {
            int[] keys = generateKeys(n, true);
            RainbowTree<Integer> tree = new RainbowTree<>(k);

            long start = System.currentTimeMillis();
            searchTreeAdd(tree, keys);
            long stop = System.currentTimeMillis();

            times[i] = stop - start;
        }
        long addAverage = getAverageTime(times);
        System.out.printf("add %d nodes:\n%dms\n", n, addAverage);

        // remove n nodes
        times = new long[t];
        for (int i = 0; i < times.length; i++) {
            int[] keys = generateKeys(n, true);
            RainbowTree<Integer> tree = new RainbowTree<>(k);

            long start = System.currentTimeMillis();
            searchTreeAddThenRemove(tree, keys);
            long stop = System.currentTimeMillis();

            times[i] = stop - start;
        }
        long removeAverage = getAverageTime(times) - addAverage;
        System.out.printf("remove %d nodes:\n%dms\n\n", n, removeAverage);
    }

    private static void searchTreeAdd(SearchTree<Integer> tree, int... keys) {
        if (!add(tree,keys))
            System.err.println("Something went wrong");
    }

    private static void searchTreeAddThenRemove(SearchTree<Integer> tree, int... keys) {
        if (!add(tree, keys))
            System.err.println("Something went wrong");
        if (!remove(tree, keys))
            System.err.println("Something went wrong");
    }

    // generate keys from 1 to n (inclusive)
    public static int[] generateKeys(int n, boolean shuffled) {
        List<Integer> keysList = new ArrayList<>();
        for (int i = 1; i <= n; i++)
            keysList.add(i);
        if (shuffled)
            Collections.shuffle(keysList);
        return keysList.stream().mapToInt(i -> i).toArray();
    }

    // add multiple keys at once in an integer tree, stops if a key can't be added
    public static boolean add(SearchTree<Integer> tree, int... keys) {
        for (int key : keys)
            if (!tree.add(key))
                return false;
        return true;
    }

    // remove multiple keys at once in an integer tree, stops if a key can't be removed
    public static boolean remove(SearchTree<Integer> tree, int... keys) {
        for (int key : keys)
            if (!tree.remove(key))
                return false;
        return true;
    }

    private static long getAverageTime(long[] times) {
        return Math.round(Arrays.stream(times).average().orElse(-1));
    }
}