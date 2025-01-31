import java.io.*;

// This is a utility class to provide functionality
public final class Utility {

    private Utility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }
    public static long processInputOutput(String inputFileName, String outputFileName) throws IOException {

        long currentTimeMillisStarting = System.currentTimeMillis();
        File inputFile = new File(inputFileName);
        File outputFile = new File(outputFileName);

        // If the program exits this try block, then the input file exists and
        // the output file is properly deleted if it had existed
        try {
            if (!inputFile.exists()) {
                throw new IOException("Input file does not exist.");
            }

            if (outputFile.exists())
                if (!outputFile.delete())
                    throw new IOException("Already existing output file couldn't be deleted properly.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Used try-with statement to close the resources automatically
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;

            while ((line = reader.readLine()) != null) {
                // Process the code given by the input file
                String outputValue = processLine(line);

                // If the process in the line requires any output, write that to the output file
                if (outputValue != null) {
                    writer.write(outputValue);
                    writer.newLine();
                }
            }
            reader.close();
            writer.close();
        }

        long currentTimeMillisEnding = System.currentTimeMillis();

        return currentTimeMillisEnding - currentTimeMillisStarting;

    }

    private static String processLine(String line) {
        String[] lineParts = line.split(" ");
        String commandType = lineParts[0];

        int capacityConstraint;
        int capacity;
        switch (commandType) {
            case "create_parking_lot":
                capacityConstraint = Integer.parseInt(lineParts[1]);
                int truckLimit = Integer.parseInt(lineParts[2]);

                Parking.addParkingLot(capacityConstraint, truckLimit);
                return null;

            case "delete_parking_lot":
                capacityConstraint = Integer.parseInt(lineParts[1]);
                Parking.deleteParkingLot(capacityConstraint);
                return null;

            case "add_truck":
                int truckId = Integer.parseInt(lineParts[1]);
                capacity = Integer.parseInt(lineParts[2]);
                return String.valueOf(Parking.addTruck(truckId, capacity));

            case "ready":
                capacity = Integer.parseInt(lineParts[1]);
                return Parking.readyCommand(capacity);

            case "load":
                capacity = Integer.parseInt(lineParts[1]);
                int loadAmount = Integer.parseInt(lineParts[2]);
                return Parking.receivingLoad(capacity, loadAmount);

            case "count":
                capacity = Integer.parseInt(lineParts[1]);
                int truckCount = Parking.count(capacity);
                return String.valueOf(truckCount);
        }

        return null;
    }

}
