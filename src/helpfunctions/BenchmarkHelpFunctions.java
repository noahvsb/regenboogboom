package helpfunctions;

import opgave.SearchTree;
import oplossing.RainbowTree;
import oplossing.RedBlackTree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
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
        redBlackTree(t, n, null);
    }

    public static void redBlackTree(int t, int n, String writer) {
        // add n nodes
        long[] times = new long[t];
        for (int i = 0; i < t; i++) {
            BenchmarkHelpFunctions.searchTreeAdd(new RedBlackTree<>(), times, i, BenchmarkHelpFunctions.generateKeys(n));
        }
        long addAverage = BenchmarkHelpFunctions.getAverageTime(times);
        if (writer == null)
            System.out.printf("add %d nodes:\n%dms\n", n, addAverage);

        // remove n nodes
        times = new long[t];
        for (int i = 0; i < t; i++) {
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            BenchmarkHelpFunctions.searchTreeAdd(tree, new long[t], i, BenchmarkHelpFunctions.generateKeys(n));

            BenchmarkHelpFunctions.searchTreeRemove(tree, times, i, BenchmarkHelpFunctions.generateKeys(n));
        }
        long removeAverage = BenchmarkHelpFunctions.getAverageTime(times);
        if (writer == null)
            System.out.printf("remove %d nodes:\n%dms\n", n, removeAverage);

        // mix n nodes
        times = new long[t];
        for (int i = 0; i < t; i++) {
            BenchmarkHelpFunctions.searchTreeMix(new RedBlackTree<>(), n, n / 2, times, i);
        }
        long mixAverage = BenchmarkHelpFunctions.getAverageTime(times);
        if (writer == null)
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
        if (writer == null)
            System.out.printf("rebuild %d nodes:\n%dms\n\n", n, rebuildAverage);

        if (writer != null) {
            String addLine = String.format("0;2;%d;0;%d", n, addAverage);
            String removeLine = String.format("0;2;%d;1;%d", n, removeAverage);
            String mixLine = String.format("0;2;%d;2;%d", n, mixAverage);
            String rebuildLine = String.format("0;2;%d;3;%d", n, rebuildAverage);
            writeToFile(writer, addLine, removeLine, mixLine, rebuildLine);
            System.out.printf("wrote benchmark result for red black tree with %d nodes to \"%s\"\n", n, writer);
        }
    }

    public static void rainbowTree(int k, int t, int n) {
        rainbowTree(k, t, n, null);
    }

    public static void rainbowTree(int k, int t, int n, String filePath) {
        // add n nodes
        long[] times = new long[t];
        for (int i = 0; i < t; i++)
            BenchmarkHelpFunctions.searchTreeAdd(new RainbowTree<>(k), times, i, BenchmarkHelpFunctions.generateKeys(n));
        long addAverage = BenchmarkHelpFunctions.getAverageTime(times);
        if (filePath == null)
            System.out.printf("add %d nodes:\n%dms\n", n, addAverage);

        // remove n nodes
        times = new long[t];
        for (int i = 0; i < t; i++) {
            RainbowTree<Integer> tree = new RainbowTree<>(k);
            BenchmarkHelpFunctions.searchTreeAdd(tree, new long[t], i, BenchmarkHelpFunctions.generateKeys(n));

            BenchmarkHelpFunctions.searchTreeRemove(tree, times, i, BenchmarkHelpFunctions.generateKeys(n));
        }
        long removeAverage = BenchmarkHelpFunctions.getAverageTime(times);
        if (filePath == null)
            System.out.printf("remove %d nodes:\n%dms\n", n, removeAverage);

        // mix n nodes
        times = new long[t];
        for (int i = 0; i < t; i++) {
            BenchmarkHelpFunctions.searchTreeMix(new RainbowTree<>(k), n, n / 2, times, i);
        }
        long mixAverage = BenchmarkHelpFunctions.getAverageTime(times);
        if (filePath == null)
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
        if (filePath == null)
            System.out.printf("rebuild %d nodes:\n%dms\n\n", n, rebuildAverage);

        if (filePath != null) {
            String addLine = String.format("1;%d;%d;0;%d", k, n, addAverage);
            String removeLine = String.format("1;%d;%d;1;%d", k, n, removeAverage);
            String mixLine = String.format("1;%d;%d;2;%d", k, n, mixAverage);
            String rebuildLine = String.format("1;%d;%d;3;%d", k, n, rebuildAverage);
            writeToFile(filePath, addLine, removeLine, mixLine, rebuildLine);
            System.out.printf("wrote benchmark result for rainbow tree with k = %d and %d nodes to \"%s\"\n", k, n, filePath);
        }
    }

    public static void clearFile(String filePath) {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.print("");
        } catch (Exception e) {
            throw new Error("Couldn't write to file: " + e);
        }
    }

    public static void writeToFile(String filePath, String... lines) {
        try (FileOutputStream fos = new FileOutputStream(filePath, true); PrintWriter writer = new PrintWriter(fos)) {
            for (String line : lines)
                writer.println(line);
        } catch (Exception e) {
            throw new Error("Couldn't write to file: " + e);
        }
    }
}
