import opgave.SearchTree;
import oplossing.RedBlackTree;

import java.util.*;

public class Benchmark {
    public static void main(String[] args) {
        // RED/BLACK TREE
        System.out.print("RED/BLACK TREE\n\n");

        redBlackTree(1000, 100);
        redBlackTree(1000, 1000);
        redBlackTree(100, 10000);
        redBlackTree(100, 100000);


        // RAINBOW TREE
    }

    private static void redBlackTree(int t, int n) {
        // add n nodes
        long[] times = new long[t];
        for (int i = 0; i < times.length; i++) {
            int[] keys = generateKeys(n, true);
            long start = System.currentTimeMillis();
            redBlackTreeAdd(keys);
            long stop = System.currentTimeMillis();
            times[i] = stop - start;
        }
        long addAverage = getAverageTime(times);
        System.out.printf("add %d nodes:\n%dms\n", n, addAverage);

        // remove n nodes
        times = new long[t];
        for (int i = 0; i < times.length; i++) {
            int[] keys = generateKeys(n, true);
            long start = System.currentTimeMillis();
            redBlackTreeAddThenRemove(keys);
            long stop = System.currentTimeMillis();
            times[i] = stop - start;
        }
        long removeAverage = getAverageTime(times) - addAverage;
        System.out.printf("remove %d nodes:\n%dms\n\n", n, removeAverage);
    }

    private static void redBlackTreeAdd(int... keys) {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        if (!add(tree,keys))
            System.err.println("Something went wrong");
    }

    private static void redBlackTreeAddThenRemove(int... keys) {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
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