import java.util.Random;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CountMin implements Algorithm {
    private static final int w = GlobalVariables.CM_RANGE;  // range of hash functions
    private static final int d = GlobalVariables.CM_ROWNUM;  // number of hash functions
    private static final int p = GlobalVariables.CM_PRIME ; // the smallest prime bigger than DATARANGE
    private int[][] sketch = new int[d][w];
    private long[] hashA = new long[d];  // Randomly generated constants for hash functions
    private long[] hashB = new long[d];  // Randomly generated constants for hash functions

    public CountMin() {
        Random rand = new Random(GlobalVariables.SEED);
        for (int i = 0; i < d; i++) {
            hashA[i] = rand.nextInt(p-1) + 1 ;
            hashB[i] = rand.nextInt(p) ;
        }
    }

    public void processNum(int num) {
        processNum(num, 1) ;
    }

    public void processNum(int num, int c) {
        for (int i = 0; i < d; i++) {
            long index = ((hashA[i] * num + hashB[i]) % p % w);
            sketch[i][(int)index] += c;
        }
    }

    public int estimate(int num) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < d; i++) {
            long index = ((hashA[i] * num + hashB[i]) % p % w);
            min = Math.min(min, sketch[i][(int)index]);
        }
        return min;
    }

    public void printCounters() {
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < w; j++) {
                System.out.print(sketch[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void saveCounters(String dataDistribution, String algorithm) {
        String directory = "Code/Datasets/Results/Single-based/";
        String filename = algorithm + "_" + dataDistribution + "_count.csv";
        String pathStr = directory + filename;

        // Ensure the directory exists
        Path dirPath = Paths.get(directory);
        try {
            if(!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
        } catch (IOException e) {
            System.err.println("Failed to create directory '" + directory + "'");
            e.printStackTrace();
            return;
        }

        // Write data to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(pathStr))) {
            for (int key = 0; key <= GlobalVariables.DATARANGE; key++) {
                writer.println(key + "," + estimate(key));
            }
        } catch (IOException e) {
            System.err.println("Failed to write to file '" + pathStr + "'");
            e.printStackTrace();
        }
    }
}

