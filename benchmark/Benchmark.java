import helpfunctions.BenchmarkHelpFunctions;
import oplossing.RainbowTree;
import oplossing.RedBlackTree;

public class Benchmark {
    public static void main(String[] args) {
        // RED/BLACK TREE
        System.out.print("RED/BLACK TREE\n\n");

        redBlackTree(1000, 100);
        redBlackTree(1000, 1000);
        redBlackTree(100, 10000);
        redBlackTree(30, 100000);
        redBlackTree(5, 1000000);


        // RAINBOW TREE
        System.out.print("\n\nRAINBOW TREE\n");
        for (int k = 2; k <= 8; k++) {
            System.out.printf("\nk = %d\n\n", k);

            rainbowTree(k, 1000, 100);
            rainbowTree(k, 1000, 1000);
            rainbowTree(k, 100, 10000);
            rainbowTree(k, 30, 100000);
            rainbowTree(k, 5, 1000000);
        }
    }

    private static void redBlackTree(int t, int n) {
        // add n nodes
        long[] times = new long[t];
        for (int i = 0; i < times.length; i++) {
            int[] keys = BenchmarkHelpFunctions.generateKeys(n, true);
            RedBlackTree<Integer> tree = new RedBlackTree<>();

            long start = System.currentTimeMillis();
            BenchmarkHelpFunctions.searchTreeAdd(tree, keys);
            long stop = System.currentTimeMillis();

            times[i] = stop - start;
        }
        long addAverage = BenchmarkHelpFunctions.getAverageTime(times);
        System.out.printf("add %d nodes:\n%dms\n", n, addAverage);

        // remove n nodes
        times = new long[t];
        for (int i = 0; i < times.length; i++) {
            int[] addKeys = BenchmarkHelpFunctions.generateKeys(n, false);
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            BenchmarkHelpFunctions.searchTreeAdd(tree, addKeys);

            int[] removeKeys = BenchmarkHelpFunctions.generateKeys(n, true);
            long start = System.currentTimeMillis();
            BenchmarkHelpFunctions.searchTreeRemove(tree, removeKeys);
            long stop = System.currentTimeMillis();

            times[i] = stop - start;
        }
        long removeAverage = BenchmarkHelpFunctions.getAverageTime(times);
        System.out.printf("remove %d nodes:\n%dms\n", n, removeAverage);

        // rebuild a tree with n nodes
        times = new long[t];
        for (int i = 0; i < times.length; i++) {
            int[] keys = BenchmarkHelpFunctions.generateKeys(n, false);
            RedBlackTree<Integer> tree = new RedBlackTree<>();
            BenchmarkHelpFunctions.searchTreeAdd(tree, keys);

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
        for (int i = 0; i < times.length; i++) {
            int[] keys = BenchmarkHelpFunctions.generateKeys(n, true);
            RainbowTree<Integer> tree = new RainbowTree<>(k);

            long start = System.currentTimeMillis();
            BenchmarkHelpFunctions.searchTreeAdd(tree, keys);
            long stop = System.currentTimeMillis();

            times[i] = stop - start;
        }
        long addAverage = BenchmarkHelpFunctions.getAverageTime(times);
        System.out.printf("add %d nodes:\n%dms\n", n, addAverage);

        // remove n nodes
        times = new long[t];
        for (int i = 0; i < times.length; i++) {
            int[] addKeys = BenchmarkHelpFunctions.generateKeys(n, false);
            RainbowTree<Integer> tree = new RainbowTree<>(k);
            BenchmarkHelpFunctions.searchTreeAdd(tree, addKeys);

            int[] removeKeys = BenchmarkHelpFunctions.generateKeys(n, true);
            long start = System.currentTimeMillis();
            BenchmarkHelpFunctions.searchTreeRemove(tree, removeKeys);
            long stop = System.currentTimeMillis();

            times[i] = stop - start;
        }
        long removeAverage = BenchmarkHelpFunctions.getAverageTime(times);
        System.out.printf("remove %d nodes:\n%dms\n", n, removeAverage);

        // rebuild a tree with n nodes
        times = new long[t];
        for (int i = 0; i < times.length; i++) {
            int[] keys = BenchmarkHelpFunctions.generateKeys(n, false);
            RainbowTree<Integer> tree = new RainbowTree<>(k);
            BenchmarkHelpFunctions.searchTreeAdd(tree, keys);

            long start = System.currentTimeMillis();
            tree.rebuild();
            long stop = System.currentTimeMillis();

            times[i] = stop - start;
        }
        long rebuildAverage = BenchmarkHelpFunctions.getAverageTime(times);
        System.out.printf("rebuild %d nodes:\n%dms\n\n", n, rebuildAverage);
    }
}