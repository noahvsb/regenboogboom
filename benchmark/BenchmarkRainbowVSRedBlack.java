import helpfunctions.BenchmarkHelpFunctions;

public class BenchmarkRainbowVSRedBlack {
    public static void main(String[] args) {
        String filePath = "benchmark/result/benchmark_rainbow_vs_redblack.csv";
        BenchmarkHelpFunctions.clearFile(filePath);
        BenchmarkHelpFunctions.writeToFile(filePath, "tree;k;n;operation;time");

        // RED BLACK TREE
        BenchmarkHelpFunctions.redBlackTree(100, 1000, filePath);
        BenchmarkHelpFunctions.redBlackTree(100, 10000, filePath);
        BenchmarkHelpFunctions.redBlackTree(100, 100000, filePath);
        BenchmarkHelpFunctions.redBlackTree(100, 500000, filePath);
        BenchmarkHelpFunctions.redBlackTree(100, 1000000, filePath);

        // RAINBOW TREE WITH MOST EFFICIENT K
        int k = 9;
        BenchmarkHelpFunctions.rainbowTree(k, 100, 1000, filePath);
        BenchmarkHelpFunctions.rainbowTree(k, 100, 10000, filePath);
        BenchmarkHelpFunctions.rainbowTree(k, 100, 100000, filePath);
        BenchmarkHelpFunctions.rainbowTree(k, 100, 500000, filePath);
        BenchmarkHelpFunctions.rainbowTree(k, 100, 1000000, filePath);
    }
}
