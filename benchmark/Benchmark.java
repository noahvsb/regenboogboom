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
        RedBlackTree tree = new RedBlackTree();
        tree.add(IntStream.range(1, n + 1).toArray());
    }

    private static long getAverageTime(long[] times) {
        return Math.round(Arrays.stream(times).average().orElse(-1));
    }
}