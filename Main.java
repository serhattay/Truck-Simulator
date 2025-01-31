import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

// 462
public class Main {
    public static void main(String[] args) throws IOException {
        String inputFilePath = args[0];
        String outputFilePath = args[1];

        try {
            runner(inputFilePath, outputFilePath);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void runner(String inputName, String outputName) throws IOException {
        long timeMillisStarting = System.currentTimeMillis();
        Utility.processInputOutput(inputName, outputName);
        long timeMillisEnding = System.currentTimeMillis();
        System.out.println(convertToSeconds(timeMillisEnding - timeMillisStarting) + " seconds");
    }

    private static double convertToSeconds(long timeTakes) {
        return timeTakes / 1000.0;
    }

}