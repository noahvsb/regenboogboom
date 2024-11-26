import helpfunctions.BenchmarkHelpFunctions;

public class BenchmarkPrint {
    public static void main(String[] args) {
        // RED/BLACK TREE
        System.out.print("RED/BLACK TREE\n\n");

        BenchmarkHelpFunctions.redBlackTree(100, 10);
        BenchmarkHelpFunctions.redBlackTree(100, 100);
        BenchmarkHelpFunctions.redBlackTree(100, 1000);
        BenchmarkHelpFunctions.redBlackTree(10, 10000);
        BenchmarkHelpFunctions.redBlackTree(5, 100000);


        // RAINBOW TREE
        System.out.print("------------------\n\nRAINBOW TREE\n");
        for (int k = 2; k <= 10; k++) {
            System.out.printf("\nk = %d\n\n", k);

            BenchmarkHelpFunctions.rainbowTree(k, 100, 10);
            BenchmarkHelpFunctions.rainbowTree(k, 100, 100);
            BenchmarkHelpFunctions.rainbowTree(k, 100, 1000);
            BenchmarkHelpFunctions.rainbowTree(k, 10, 10000);
            BenchmarkHelpFunctions.rainbowTree(k, 5, 100000);
        }
        for (int k = 50; k <= 500; k += 50) {
            System.out.printf("\nk = %d\n\n", k);

            BenchmarkHelpFunctions.rainbowTree(k, 100, 10);
            BenchmarkHelpFunctions.rainbowTree(k, 100, 100);
            BenchmarkHelpFunctions.rainbowTree(k, 100, 1000);
            BenchmarkHelpFunctions.rainbowTree(k, 10, 10000);
            BenchmarkHelpFunctions.rainbowTree(k, 5, 100000);
        }
    }
}