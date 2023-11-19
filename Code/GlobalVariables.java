import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalVariables {
    public static int DATAAMOUNT = 10000000 ;
    public static int DATARANGE = 100000 ;
    public static int INTERVALNUM = 5 ;
    private static int[] array1 = {0, 1000};
    private static int[] array2 = {24500, 25500};
    private static int[] array3 = {49500, 50500};
    private static int[] array4 = {74500, 75500};
    private static int[] array5 = {99000, 100000};
    public  static List<int[]> INTERVALS = new ArrayList<>(Arrays.asList(array1, array2, array3, array4, array5));
    public static long SEED = 5020L ;

    public static double SNUM = 150.0/DATAAMOUNT ; // \gamma
    public static int RANGE = (int)Math.ceil(1/SNUM);  // w = range of hash functions
    public static int ROWNUM = 30;  // d = number of hash functions
    public static int PRIME = 100003 ; // p = the smallest prime bigger than DATARANGE

    // MisraGries
    public static double MG_SNUM = SNUM ; // \gamma
    // SpaceSaving
    public static double SS_SNUM = SNUM ; // \gamma
    // CountMin
    public static int CM_RANGE = 10000;//RANGE ;  // w = range of hash functions
    public static int CM_ROWNUM = ROWNUM ;  // d = number of hash functions
    public static int CM_PRIME = PRIME ; // p = the smallest prime bigger than DATARANGE
    // CountSketch
    public static int CS_RANGE = 10000;//RANGE ;  // w = range of hash functions
    public static int CS_ROWNUM = ROWNUM ;  // d = number of hash functions
    public static int CS_PRIME = PRIME ; // p = the smallest prime bigger than DATARANGE
    // Augmented Sketch
    public static double AS_SNUM = SNUM ; // \gamma
    public static int AS_RANGE = 10000;//RANGE ;  // w = range of hash functions
    public static int AS_ROWNUM = ROWNUM ;  // d = number of hash functions
    public static int AS_PRIME = PRIME ; // p = the smallest prime bigger than DATARANGE
}
