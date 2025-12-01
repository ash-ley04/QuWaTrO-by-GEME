
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

abstract class EnvironmentalTool {

    @SuppressWarnings("unused")
    abstract void displayInfo();
}

class ClimateAwareness extends EnvironmentalTool {

    private final List<String> facts = Arrays.asList(
            "1. The Earth is heating up, causing extreme weather and ecosystem disruptions.",
            "2. Water scarcity affects over 2 billion people due to climate change.",
            "3. Deforestation increases carbon emissions and destroys habitats.",
            "4. Switching to renewable energy can reduce global warming.",
            "5. Rising sea levels threaten coastal communities worldwide."
    );

    @Override
    public void displayInfo() {
        System.out.println("\n========= Climate Awareness =========");
        for (String fact : facts) {
            System.out.println(fact + "\n");
        }
    }
}

class HeatIndexCalculator extends EnvironmentalTool {

    private double temperature;
    private double humidity;
    private double heatIndex;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getHeatIndex() {
        return heatIndex;
    }

    @Override
    public void displayInfo() {
        System.out.printf("Temperature: %.2f°C | Humidity: %.2f%% | Heat Index: %.2f°C%n",
                temperature, humidity, heatIndex);
    }

    public void calculate() {
        heatIndex = -42.379 + 2.04901523 * temperature + 10.14333127 * humidity
                - 0.22475541 * temperature * humidity - 0.00683783 * Math.pow(temperature, 2)
                - 0.05481717 * Math.pow(humidity, 2) + 0.00122874 * Math.pow(temperature, 2) * humidity
                + 0.00085282 * temperature * Math.pow(humidity, 2)
                - 0.00000199 * Math.pow(temperature, 2) * Math.pow(humidity, 2);
    }
}

class DataLogger extends HeatIndexCalculator {

    public void logData() {
        DecimalFormat df = new DecimalFormat("0.00");
        try (FileWriter writer = new FileWriter("TempTerra_Knowledge_Hub_log.txt", true)) {
            writer.write("Temperature: " + df.format(getTemperature()) + "°C, ");
            writer.write("Humidity: " + df.format(getHumidity()) + "%, ");
            writer.write("Heat Index: " + df.format(getHeatIndex()) + "°C\n");
            System.out.println("Data logged successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void viewLogs() {
        File file = new File("TempTerra_Knowledge_Hub_log.txt");
        if (!file.exists()) {
            System.out.println("No logs found.");
            return;
        }

        System.out.println("\n--- Saved Logs ---");
        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                System.out.println(reader.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading log file.");
        }
    }
}

class HeatIndexManager extends DataLogger {

    private final double[] heatIndexes = new double[100];
    private int size = 0;

    public void insert(double hi) {
        if (size < heatIndexes.length) {
            heatIndexes[size++] = hi;
            System.out.println("Inserted Heat Index: " + hi + "°C");
        } else {
            System.out.println("Array full. Cannot insert more data.");
        }
    }

    public void delete(int index) {
        if (index >= 0 && index < size) {
            for (int i = index; i < size - 1; i++) {
                heatIndexes[i] = heatIndexes[i + 1];
            }
            size--;
            System.out.println("Deleted index " + index + " from array.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    public void search(double value) {
        boolean found = false;
        for (int i = 0; i < size; i++) {
            if (heatIndexes[i] == value) {
                System.out.println("Found at index " + i + ": " + heatIndexes[i] + "°C");
                found = true;
            }
        }
        if (!found) {
            System.out.println("Heat Index " + value + "°C not found.");
        }
    }

    public void sort() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if (heatIndexes[i] > heatIndexes[j]) {
                    double temp = heatIndexes[i];
                    heatIndexes[i] = heatIndexes[j];
                    heatIndexes[j] = temp;
                }
            }
        }
        System.out.println("Heat Index array sorted.");
    }

    public void displayAll() {
        if (size == 0) {
            System.out.println("No entries to display.");
            return;
        }

        System.out.println("\nStored Heat Indexes:");
        for (int i = 0; i < size; i++) {
            System.out.println(i + ". " + heatIndexes[i] + "°C");
        }
    }

    public int getSize() {
        return size;
    }
}

public class TempTerraKnowledgeHub {

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            HeatIndexManager manager = new HeatIndexManager();
            ClimateAwareness awareness = new ClimateAwareness();

            boolean running = true;

            while (running) {
                System.out.println("\n========= TempTerra Knowledge Hub =========");
                System.out.println("1. Calculate Heat Index");
                System.out.println("2. View Saved Logs");
                System.out.println("3. Delete Entry");
                System.out.println("4. Climate Facts");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                try {
                    int choice = sc.nextInt();
                    switch (choice) {
                        case 1 -> {
                            System.out.print("Enter temperature (°C): ");
                            double temp = sc.nextDouble();
                            System.out.print("Enter humidity (%): ");
                            double hum = sc.nextDouble();

                            manager.setTemperature(temp);
                            manager.setHumidity(hum);
                            manager.calculate();
                            double hi = manager.getHeatIndex();

                            System.out.printf("Calculated Heat Index: %.2f°C%n", hi);

                            manager.logData();
                            manager.insert(hi);

                            manager.displayAll();
                            manager.sort();
                            manager.displayAll();

                            System.out.print("\nSearch for Heat Index value (in °C): ");
                            double searchVal = sc.nextDouble();
                            manager.search(searchVal);
                        }

                        case 2 ->
                            manager.viewLogs();

                        case 3 -> {
                            if (manager.getSize() == 0) {
                                System.out.println("No entries to delete.");
                            } else {
                                manager.displayAll();
                                System.out.print("\nDelete Heat Index at index (0 to " + (manager.getSize() - 1) + "): ");
                                int index = sc.nextInt();
                                manager.delete(index);
                                manager.displayAll();
                            }
                        }

                        case 4 ->
                            awareness.displayInfo();

                        case 5 -> {
                            System.out.println("Exiting TempTerra Knowledge Hub. Goodbye!");
                            running = false;
                        }

                        default ->
                            System.out.println("Invalid option. Please try again.");
                    }

                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter a number.");
                    sc.nextLine();
                }
            }
        }
    }
}
