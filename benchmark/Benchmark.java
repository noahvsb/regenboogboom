import opgave.SearchTree;
import oplossing.RedBlackTree;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Benchmark {
    public static void main(String[] args) {
        // red/black tree
        long[] times = new long[3];
        for (int i = 0; i < times.length; i++) {
            long start = System.currentTimeMillis();
            RedBlackTree(10000);
            long stop = System.currentTimeMillis();
            times[i] = stop - start;
        }
        System.out.printf("red/black tree with 10000 nodes:\n%dms\n", getAverageTime(times));

        // rainbow tree
    }

    private static void RedBlackTree(int n) {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        add(tree, IntStream.range(1, n + 1).toArray());
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