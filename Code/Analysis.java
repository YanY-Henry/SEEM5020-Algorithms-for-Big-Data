import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Analysis {
    public void Estimation(String dataDistribution, String algorithm, String type) {
        if (type.equals("Frequency"))
            FrequencyEstimation(dataDistribution, algorithm);
        else if (type.equals("Range-based Frequency"))
            RBFrequencyEstimation(dataDistribution, algorithm);
        else
            System.out.println("Error!");
    }
    public void FrequencyEstimation(String dataDistribution, String algorithm) {
        Algorithm algo ;
        if (algorithm.equals("Misra-Gries")) {
            algo = new MisraGries();
        } else if (algorithm.equals("Space-Saving")) {
            algo = new SpaceSaving();
        } else if (algorithm.equals("Count-Min")) {
            algo = new CountMin();
        } else if (algorithm.equals("Count-Sketch")) {
            algo = new CountSketch();
        } else if (algorithm.equals("ASketch")) {
            algo = new ASketch();
        } else {
            System.out.println("Error!");
            return;
        }

        try {
            File file = new File("Code/Datasets/" + dataDistribution + "_data.csv");

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            System.out.println("\nUsing "+algorithm+" to process the "+dataDistribution+" dataset (frequency)...");
            int processedLines = 0;
            int timeToPrint = GlobalVariables.DATAAMOUNT / 100 ;
            long startTime = System.currentTimeMillis();
            while ((line = br.readLine()) != null) {
                int num = Integer.parseInt(line);
                // System.out.println(num);
                algo.processNum(num);
                processedLines++;
                if ( processedLines % timeToPrint == 0 ) {
                    int progress = 100 * processedLines / GlobalVariables.DATAAMOUNT;
                    ProgressBar.printProgress(progress, startTime);
                } else if (processedLines == 1 ) {
                    ProgressBar.printProgress(0, startTime);
                }
            }
            br.close();
            System.out.print("\nData processing completed! ");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // algo.printCounters();
        System.out.println("Now saving the result data...");
        algo.saveCounters(dataDistribution, algorithm);
        System.out.println("Successful saving!");
    }

    public void RBFrequencyEstimation( String dataDistribution, String algorithm ) {
        int L = (int)Math.ceil(Math.log(GlobalVariables.CS_RANGE) / Math.log(2));

        Algorithm[] algos = new Algorithm[L];
        if (algorithm.equals("Misra-Gries")) {
            for (int i=0; i<L ; i++ )
                algos[i] = new MisraGries();
        } else if (algorithm.equals("Space-Saving")) {
            for (int i=0; i<L ; i++ )
                algos[i] = new SpaceSaving();
        } else if (algorithm.equals("Count-Min")) {
            for (int i=0; i<L ; i++ )
                algos[i] = new CountMin();
        } else if (algorithm.equals("Count-Sketch")) {
            for (int i=0; i<L ; i++ )
                algos[i] = new CountSketch();
        } else if (algorithm.equals("ASketch")) {
            for (int i=0; i<L ; i++ )
                algos[i] = new ASketch();
        } else {
            System.out.println("Error!");
            return;
        }

        try {
            File file = new File("Code/Datasets/" + dataDistribution + "_data.csv");

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            System.out.println("\nUsing "+algorithm+" to process the "+dataDistribution+" dataset (range-base frequency)...");
            int processedLines = 0;
            int timeToPrint = GlobalVariables.DATAAMOUNT / 100 ;
            long startTime = System.currentTimeMillis();
            while ((line = br.readLine()) != null) {
                int num = Integer.parseInt(line);
                // System.out.println(num);
                for ( int i=0 ; i<L ; i++ )
                {
                    int power = (int)Math.pow(2, i) ;
                    algos[i].processNum(num / power);
                }
                processedLines++;
                if ( processedLines % timeToPrint == 0 ) {
                    int progress = 100 * processedLines / GlobalVariables.DATAAMOUNT;
                    ProgressBar.printProgress(progress, startTime);
                } else if (processedLines == 1 ) {
                    ProgressBar.printProgress(0, startTime);
                }
            }
            br.close();
            System.out.print("\nData processing completed! ");
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] EstimationCounter = new int[GlobalVariables.INTERVALNUM];
        for( int i=0; i<GlobalVariables.INTERVALNUM ; i++ )
        {
            int lower_bound = GlobalVariables.INTERVALS.get(i)[0];
            int upper_bound = GlobalVariables.INTERVALS.get(i)[1];

            EstimationCounter[i] = EstimationCounterRB( lower_bound , upper_bound , algos );
        }

        // printRBCounters( EstimationCounter );
        System.out.println("Now saving the result data...");
        saveRBCounters( dataDistribution , algorithm , EstimationCounter );
        System.out.println("Successful saving!");
    }

    private static List<int[]> calcDyadicIntervals(int a, int b)
    {
        List<int[]> intervals = new ArrayList<>();
        while (b >= a) {
            int powerOfTwo = 1;
            while ((b + 1) % (2 * powerOfTwo) == 0 && 2 * powerOfTwo <= b - a + 1) {
                powerOfTwo *= 2;
            }
            intervals.add(new int[]{b - powerOfTwo + 1, b});
            b -= powerOfTwo;
        }
        Collections.reverse(intervals);
        return intervals ;
    }

    public int EstimationCounterRB(int lower_bound , int upper_bound , Algorithm[] algos )
    {
        int result = 0 ;
        List<int[]> Intervals = calcDyadicIntervals( lower_bound , upper_bound );
        for ( int i=0 ; i<Intervals.size() ; i++ )
        {
            int a = Intervals.get(i)[0];
            int b = Intervals.get(i)[1];
            int l = (int)(Math.log(b-a+1) / Math.log(2));

            result += algos[l].estimate(a/(int)Math.pow(2,l));
        }
        return result ;
    }

    public void printRBCounters(int[] EstimationCounter) {
        System.out.println();
        for( int i=0; i<GlobalVariables.INTERVALNUM ; i++ )
        {
            int lower_bound = GlobalVariables.INTERVALS.get(i)[0];
            int upper_bound = GlobalVariables.INTERVALS.get(i)[1];

            System.out.println("["+lower_bound+", "+upper_bound+"]: "+EstimationCounter );
        }
    }

    public void saveRBCounters(String dataDistribution, String algorithm , int[] EstimationCounter) {
        String directory = "Code/Datasets/Results/Range-based/";
        String filename = algorithm + "_" + dataDistribution + "_RBsketch.csv";
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
            for (int i = 0; i < GlobalVariables.INTERVALNUM; i++) {
                writer.println("["+ GlobalVariables.INTERVALS.get(i)[0] + " " + GlobalVariables.INTERVALS.get(i)[1] + "]," + EstimationCounter[i]);
            }
        } catch (IOException e) {
            System.err.println("Failed to write to file '" + pathStr + "'");
            e.printStackTrace();
        }
    }
}