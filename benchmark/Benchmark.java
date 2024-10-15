import opgave.SearchTree;
import oplossing.RedBlackTree;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Benchmark {
    public static void main(String[] args) {
        int t = 10;
        int n = 1000000;

        // RED/BLACK TREE
        System.out.print("RED/BLACK TREE\n\n");

        // add n nodes
        long[] times = new long[t];
        for (int i = 0; i < times.length; i++) {
            long start = System.currentTimeMillis();
            redBlackTreeAdd(n);
            long stop = System.currentTimeMillis();
            times[i] = stop - start;
        }
        long addAverage = getAverageTime(times);
        System.out.printf("add %d nodes:\n%dms\n", n, addAverage);

        // remove n nodes
        times = new long[t];
        for (int i = 0; i < times.length; i++) {
            long start = System.currentTimeMillis();
            redBlackTreeAddThenRemove(n);
            long stop = System.currentTimeMillis();
            times[i] = stop - start;
        }
        long removeAverage = getAverageTime(times) - addAverage;
        System.out.printf("remove %d nodes:\n%dms\n", n, removeAverage);

        // RAINBOW TREE
    }

    private static void redBlackTreeAdd(int n) {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        if (!add(tree, IntStream.range(1, n + 1).toArray()))
            System.err.println("Something went wrong");
    }

    private static void redBlackTreeAddThenRemove(int n) {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        if (!add(tree, IntStream.range(1, n + 1).toArray()))
            System.err.println("Something went wrong");
        if (!remove(tree, IntStream.range(1, n + 1).toArray()))
            System.err.println("Something went wrong");
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