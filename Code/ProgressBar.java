public class ProgressBar {
    private static final int PROGRESS_BAR_WIDTH = 50; // Width of the progress bar

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i <= 100; i++) {
            printProgress(i, startTime);
        }
    }

    // Function to print the progress bar, time elapsed and percentage
    public static void printProgress(int progress, long startTime) {
        String bar = createBar(progress);
        long timeElapsed = System.currentTimeMillis() - startTime;
        System.out.print("\r" + bar + " " + formatTime(timeElapsed) + " " + progress + "% ");
    }

    // Function to create the progress bar string
    public static String createBar(int progress) {
        int width = progress * PROGRESS_BAR_WIDTH / 100;
        String bar = "[";
        for (int i = 0; i < PROGRESS_BAR_WIDTH; i++) {
            if (i < width) {
                bar += "=";
            } else if (i == width) {
                bar += ">";
            } else {
                bar += " ";
            }
        }
        bar += "]";
        return bar;
    }

    // Function to format time in milliseconds to a string
    public static String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}