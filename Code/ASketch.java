import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ASketch implements Algorithm {
    private static final double y = GlobalVariables.AS_SNUM;
    private static final int K = (int)(Math.ceil(1/y) - 1);
    private static final int p = GlobalVariables.AS_PRIME; // a large prime number
    private static final int d = GlobalVariables.AS_ROWNUM; // the number of hash functions
    private static final int w = GlobalVariables.AS_RANGE; // the width of the sketch

    private final PriorityQueue<Map.Entry<Integer, Integer>> filter = new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));
    private final Set<Integer> filterSet = new HashSet<>();
    private final int[][] sketch = new int[d][w];
    private final long[] hashA = new long[d];
    private final long[] hashB = new long[d];
    private final Map<Integer, Integer> newCount = new HashMap<>();
    private final Map<Integer, Integer> oldCount = new HashMap<>();

    public ASketch() {
        Random rand = new Random(GlobalVariables.SEED);
        for (int i = 0; i < d; i++) {
            hashA[i] = rand.nextInt(p-1) + 1;
            hashB[i] = rand.nextInt(p);
        }
    }

    public void processNum(int num) {
        processNum(num, 1);
    }

    public void processNum(int num, int c) {
        if (filterSet.contains(num)) {
            newCount.put(num, newCount.get(num) + c);
        } else if (filter.size() < K) {
            newCount.put(num, c);
            oldCount.put(num, 0);
            Map.Entry<Integer, Integer> entry = new AbstractMap.SimpleImmutableEntry<>(num, c);
            filter.add(entry);
            filterSet.add(num);
        } else {
            for (int i = 0; i < d; i++) {
                long index = ((hashA[i] * num + hashB[i]) % p % w);
                sketch[i][(int)index] += c;
            }
            int estimatedFreq = estimatedFrequency(num);
            Map.Entry<Integer, Integer> minEntryInFilter = filter.peek();
            if (minEntryInFilter != null && estimatedFreq > minEntryInFilter.getValue()) {
                int ki = minEntryInFilter.getKey();
                if (newCount.get(ki) - oldCount.get(ki) > 0) {
                    for (int i = 0; i < d; i++) {
                        long index = ((hashA[i] * ki + hashB[i]) % p % w);
                        sketch[i][(int)index] += newCount.get(ki) - oldCount.get(ki);
                    }
                }
                filter.poll();
                filterSet.remove(ki);
                Map.Entry<Integer, Integer> entry = new AbstractMap.SimpleImmutableEntry<>(num, estimatedFreq);
                filter.add(entry);
                filterSet.add(num);
                newCount.put(num, estimatedFreq);
                oldCount.put(num, estimatedFreq);
            }
        }
    }

    private int estimatedFrequency(int num) {
        int minFreq = Integer.MAX_VALUE;
        for (int i = 0; i < d; i++) {
            long index = ((hashA[i] * num + hashB[i]) % p % w);
            minFreq = Math.min(minFreq, sketch[i][(int)index]);
        }
        return minFreq;
    }

    public int estimate(int num){
        if ( filterSet.contains(num) )
            return newCount.get(num) ;
        else
            return 0 ;
    }

    public void printCounters() {
        System.out.println("We have " + K + " counters in total.");
        for (Map.Entry<Integer, Integer> entry : filter)
            System.out.println("Element: " + entry.getKey() + ", Count: " + entry.getValue());
    }

    public void saveCounters(String dataDistribution, String algorithm) {
        String directory = "Code/Datasets/Results/Single-based/";
        String filename = algorithm + "_" + dataDistribution + "_count.csv";
        String pathStr = directory + filename;

        // Ensure the directory exists
        try {
            if (!Files.exists(Paths.get(directory))) {
                Files.createDirectories(Paths.get(directory));
            }
        } catch (IOException e) {
            System.err.println("Failed to create directory '" + directory + "'");
            e.printStackTrace();
            return;
        }

        // Write data to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(pathStr))) {
            for (int key = 0; key <= GlobalVariables.DATARANGE; key++) {
                if (filterSet.contains(key)) {
                    writer.println(key + "," + newCount.get(key));
                } else {
                    writer.println(key + ",0");
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to write to file '" + pathStr + "'");
            e.printStackTrace();
        }
    }
}