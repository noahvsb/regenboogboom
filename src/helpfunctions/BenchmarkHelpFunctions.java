package helpfunctions;

import opgave.SearchTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BenchmarkHelpFunctions {
    public static void searchTreeAdd(SearchTree<Integer> tree, int... keys) {
        if (!add(tree,keys))
            System.err.println("Something went wrong");
    }

    public static void searchTreeRemove(SearchTree<Integer> tree, int... keys) {
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

    public static long getAverageTime(long[] times) {
        return Math.round(Arrays.stream(times).average().orElse(-1));
    }
}
