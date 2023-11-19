public interface Algorithm {
    void processNum(int num);
    void processNum(int num, int c);
    int estimate(int num);
    void printCounters();
    void saveCounters(String dataDistribution, String algorithm);
}