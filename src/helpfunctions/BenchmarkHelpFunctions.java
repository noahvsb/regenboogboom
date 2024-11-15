package helpfunctions;

import opgave.SearchTree;

import java.util.*;

public class BenchmarkHelpFunctions {
    public static void searchTreeAdd(SearchTree<Integer> tree, long[] times, int timesIndex, int... keys) {
        for (int key : keys) {
            long start = System.currentTimeMillis();
            boolean add = tree.add(key);
            long end = System.currentTimeMillis();
            times[timesIndex] += end - start;

            if (!add)
                System.err.println("Something went wrong");
        }
    }

    public static void searchTreeRemove(SearchTree<Integer> tree, long[] times, int timesIndex, int... keys) {
        for (int key : keys) {
            long start = System.currentTimeMillis();
            boolean remove = tree.remove(key);
            long end = System.currentTimeMillis();
            times[timesIndex] += end - start;

            if (!remove)
                System.err.println("Something went wrong");
        }
    }

    // generate random keys with a bound, add by default, but if you can't, remove it instead
    public static void searchTreeMix(SearchTree<Integer> tree, int n, int max, long[] times, int timesIndex) {
        Random r = new Random();

        for (int i = 0; i < n; i++) {
            int key = r.nextInt(max);

            // add
            if (!tree.search(key)) {
                long start = System.currentTimeMillis();
                tree.add(key);
                long end = System.currentTimeMillis();
                times[timesIndex] += end - start;
            }
            // remove
            else {
                long start = System.currentTimeMillis();
                tree.remove(key);
                long end = System.currentTimeMillis();
                times[timesIndex] += end - start;
            }
        }
    }

    // generate keys from 1 to n (inclusive)
    public static int[] generateKeys(int n) {
        List<Integer> keysList = new ArrayList<>();
        for (int i = 1; i <= n; i++)
            keysList.add(i);
        Collections.shuffle(keysList);
        return keysList.stream().mapToInt(i -> i).toArray();
    }

    public static long getAverageTime(long[] times) {
        return Math.round(Arrays.stream(times).average().orElse(-1));
    }
}
