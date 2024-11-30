import helpfunctions.BenchmarkHelpFunctions;

public class BenchmarkRainbow {
    public static void main(String[] args) {
        String filePath = "benchmark/result/benchmark_rainbow.csv";
        BenchmarkHelpFunctions.clearFile(filePath);
        BenchmarkHelpFunctions.writeToFile(filePath, "tree;k;n;operation;time");

        for (int k = 2; k < 30; k++) // 28 runs
            BenchmarkHelpFunctions.rainbowTree(k, 100, 100000, filePath);

        for (int k = 30; k < 50; k += 4) // 5 runs
            BenchmarkHelpFunctions.rainbowTree(k, 100, 100000, filePath);

        for (int k = 50; k < 150; k += 25) // 4 runs
            BenchmarkHelpFunctions.rainbowTree(k, 100, 100000, filePath);

        for (int k = 150; k < 300; k += 50) // 3 runs
            BenchmarkHelpFunctions.rainbowTree(k, 100, 100000, filePath);

        for (int k = 300; k < 1000; k += 100) // 7 runs
            BenchmarkHelpFunctions.rainbowTree(k, 100, 100000, filePath);

        for (int k = 1000; k <= 1000000; k *= 10) // 4 runs
            BenchmarkHelpFunctions.rainbowTree(k, 100, 100000, filePath);

        // total of 51 runs from k = 2 to k = 1 000 000
    }
}
