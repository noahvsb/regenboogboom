import helpfunctions.BenchmarkHelpFunctions;
import oplossing.RainbowTree;
import oplossing.RedBlackTree;

public class Benchmark {
    public static void main(String[] args) {
        // RED/BLACK TREE
        System.out.print("RED/BLACK TREE\n\n");

        redBlackTree(100, 10);
        redBlackTree(100, 100);
        redBlackTree(100, 1000);
        redBlackTree(100, 10000);
        redBlackTree(30, 100000);


        // RAINBOW TREE
        System.out.print("\n\n\nRAINBOW TREE\n");
        for (int k = 2; k <= 8; k++) {
            System.out.printf("\nk = %d\n\n", k);

            rainbowTree(k, 100, 10);
            rainbowTree(k, 100, 100);
            rainbowTree(k, 100, 1000);
            rainbowTree(k, 100, 10000);
            rainbowTree(k, 30, 100000);
        }
    }

    private static void redBlackTree(int t, int n) {
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

    private static void rainbowTree(int k, int t, int n) {
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