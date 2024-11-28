import helpfunctions.BenchmarkHelpFunctions;

public class BenchmarkRainbowVSRedBlack {
    public static void main(String[] args) {
        String filePath = "benchmark/benchmark_rainbow_vs_redblack.csv";
        BenchmarkHelpFunctions.clearFile(filePath);
        BenchmarkHelpFunctions.writeToFile(filePath, "tree;k;n;operation;time");

        // RED BLACK TREE
        BenchmarkHelpFunctions.redBlackTree(100, 10, filePath);
        BenchmarkHelpFunctions.redBlackTree(100, 100, filePath);
        BenchmarkHelpFunctions.redBlackTree(100, 1000, filePath);
        BenchmarkHelpFunctions.redBlackTree(100, 10000, filePath);
        BenchmarkHelpFunctions.redBlackTree(10, 100000, filePath);
        BenchmarkHelpFunctions.redBlackTree(5, 1000000, filePath);

        // RAINBOW TREE WITH MOST EFFICIENT K
        int k = 3;
        BenchmarkHelpFunctions.rainbowTree(k, 100, 10, filePath);
        BenchmarkHelpFunctions.rainbowTree(k, 100, 100, filePath);
        BenchmarkHelpFunctions.rainbowTree(k, 100, 1000, filePath);
        BenchmarkHelpFunctions.rainbowTree(k, 100, 10000, filePath);
        BenchmarkHelpFunctions.rainbowTree(k, 10, 100000, filePath);
        BenchmarkHelpFunctions.rainbowTree(k, 5, 1000000, filePath);
    }
}
