import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;

public class MisraGries implements Algorithm {
    private static final double y = GlobalVariables.MG_SNUM ;
    private static final int K = (int)(Math.ceil(1/y) - 1);

    private HashMap<Integer, Integer> counters = new HashMap<>();

    public void processNum(int num) {
        processNum(num, 1) ;
    }

    // Add a new method to process a single number
    public void processNum(int num, int c) {
        for ( int i=0 ; i<c ; i++ )
        {
            if (counters.containsKey(num) || counters.size() < K)
                counters.put(num, counters.getOrDefault(num, 0) + 1);
            else {
                Iterator<Integer> iterator = counters.keySet().iterator();
                while (iterator.hasNext()) {
                    int key = iterator.next();
                    counters.put(key, counters.get(key) - 1);
                    if (counters.get(key) == 0)
                        iterator.remove();
                }
            }
        }
    }

    public int estimate(int num){
        if ( counters.containsKey(num) )
            return counters.get(num) ;
        else
            return 0 ;
    }

    public void printCounters() {
        System.out.println( "We have " + K + " counters in total.");
        for (int key : counters.keySet())
            System.out.println("Element: " + key + ", Count: " + counters.get(key));
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