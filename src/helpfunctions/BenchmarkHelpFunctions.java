package helpfunctions;

import opgave.SearchTree;
import oplossing.RainbowTree;
import oplossing.RedBlackTree;

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

    public static void redBlackTree(int t, int n) {
        // add n nodes
        long[] times = new long[t];
        for (int i = 0; i < t; i++) {
            BenchmarkHelpFunctions.searchTreeAdd(new RedBlackTree<>(), times, i, BenchmarkHelpFunctions.generateKeys(n));
        }
        long addAverage = BenchmarkHelpFunctions.getAverageTime(times);
        System.out.printf("add %d nodes:\n%dms\n", n, addAverage);

        // remove n nodes
        times = new long[t];
        for (int i = 0; i < t; i++) {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            BenchmarkHelpFunctions.searchTreeAdd(tree, new long[t], i, BenchmarkHelpFunctions.generateKeys(n));

            BenchmarkHelpFunctions.searchTreeRemove(tree, times, i, BenchmarkHelpFunctions.generateKeys(n));
        }
        long removeAverage = BenchmarkHelpFunctions.getAverageTime(times);
        System.out.printf("remove %d nodes:\n%dms\n", n, removeAverage);

        // mix n nodes
        times = new long[t];
        for (int i = 0; i < t; i++) {
            BenchmarkHelpFunctions.searchTreeMix(new RedBlackTree<>(), n, n / 2, times, i);
        }
        long mixAverage = BenchmarkHelpFunctions.getAverageTime(times);
        System.out.printf("mix %d nodes:\n%dms\n", n, mixAverage);

        // rebuild a tree with n nodes
        times = new long[t];
        for (int i = 0; i < t; i++) {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            BenchmarkHelpFunctions.searchTreeAdd(tree, times, i, BenchmarkHelpFunctions.generateKeys(n));

            long start = System.currentTimeMillis();
            tree.rebuild();
            long stop = System.currentTimeMillis();

            times[i] = stop - start;
        }
        long rebuildAverage = BenchmarkHelpFunctions.getAverageTime(times);
        System.out.printf("rebuild %d nodes:\n%dms\n\n", n, rebuildAverage);
    }

    public static void rainbowTree(int k, int t, int n) {
        // add n nodes
        long[] times = new long[t];
        for (int i = 0; i < t; i++)
            BenchmarkHelpFunctions.searchTreeAdd(new RainbowTree<>(k), times, i, BenchmarkHelpFunctions.generateKeys(n));
        long addAverage = BenchmarkHelpFunctions.getAverageTime(times);
        System.out.printf("add %d nodes:\n%dms\n", n, addAverage);

        // remove n nodes
        times = new long[t];
        for (int i = 0; i < t; i++) {
            RainbowTree<Integer> tree = new RainbowTree<>(k);
            BenchmarkHelpFunctions.searchTreeAdd(tree, new long[t], i, BenchmarkHelpFunctions.generateKeys(n));

            BenchmarkHelpFunctions.searchTreeRemove(tree, times, i, BenchmarkHelpFunctions.generateKeys(n));
        }
        long removeAverage = BenchmarkHelpFunctions.getAverageTime(times);
        System.out.printf("remove %d nodes:\n%dms\n", n, removeAverage);

        // mix n nodes
        times = new long[t];
        for (int i = 0; i < t; i++) {
            BenchmarkHelpFunctions.searchTreeMix(new RainbowTree<>(k), n, n / 2, times, i);
        }
        long mixAverage = BenchmarkHelpFunctions.getAverageTime(times);
        System.out.printf("mix %d nodes:\n%dms\n", n, mixAverage);

        // rebuild a tree with n nodes
        times = new long[t];
        for (int i = 0; i < t; i++) {
            RainbowTree<Integer> tree = new RainbowTree<>(k);
            BenchmarkHelpFunctions.searchTreeAdd(tree, times, i, BenchmarkHelpFunctions.generateKeys(n));

            long start = System.currentTimeMillis();
            tree.rebuild();
            long stop = System.currentTimeMillis();

            times[i] = stop - start;
        }
        long rebuildAverage = BenchmarkHelpFunctions.getAverageTime(times);
        System.out.printf("rebuild %d nodes:\n%dms\n\n", n, rebuildAverage);
    }
}
