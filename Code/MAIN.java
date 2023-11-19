public class MAIN {
    public static void main(String[] args) {
        // Choose the dataset from "uniform", "gaussian", "exponential"
        String[] distributions = {"uniform", "gaussian", "exponential"};
        // Choose the algorithms from "Misra-Gries", "Space-Saving", "Count-Min", "Count-Sketch"
        String[] algorithms = {"Misra-Gries", "Space-Saving", "Count-Min", "Count-Sketch", "ASketch"};
        // Choose the analysis type from "Frequency", "Range-based Frequency"
        String[] types = {"Frequency", "Range-based Frequency"};

        Analysis analysis = new Analysis();
        // analysis.Estimation("uniform", "Space-Saving", "Range-based Frequency");

        for (String type : types)
            for (String dataDistribution : distributions)
                for (String algorithm : algorithms)
                    analysis.Estimation(dataDistribution, algorithm, type);
    }
}