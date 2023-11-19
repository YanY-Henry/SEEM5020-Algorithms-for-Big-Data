import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class SpaceSaving implements Algorithm {

    private class Element implements Comparable<Element> {
        int num;
        int count;

        public Element(int num, int count) {
            this.num = num;
            this.count = count;
        }

        @Override
        public int compareTo(Element other) {
            return this.count - other.count; // Ascending order
        }
    }

    private static final double y = GlobalVariables.SS_SNUM;
    private static final int K = (int)(Math.ceil(1/y) - 1);

    private Map<Integer, Element> counters = new HashMap<>();
    private PriorityQueue<Element> queue = new PriorityQueue<>();

    public void processNum(int num) {
        processNum(num, 1) ;
    }

    // Add a new method to process a single number
    public void processNum(int num, int c) {
        if (counters.containsKey(num)) {
            Element element = counters.get(num);
            queue.remove(element); // Remove from queue to update
            element.count++;
            queue.add(element); // Add back to queue with updated count
        } else if (counters.size() < K) {
            Element element = new Element(num, 1);
            counters.put(num, element);
            queue.add(element);
        } else {
            Element minElement = queue.poll(); // Get and remove min element
            counters.remove(minElement.num); // Remove from counter map
            minElement.num = num; // Update num and count
            minElement.count++;
            counters.put(num, minElement); // Add back to counter map
            queue.add(minElement); // Add back to queue
        }
    }

    public int estimate(int num){
        return counters.containsKey(num) ? counters.get(num).count : 0;
    }

    public void printCounters() {
        System.out.println( "We have " + K + " counters in total.");
        for (Element element : counters.values())
            System.out.println("Element: " + element.num + ", Count: " + element.count);
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