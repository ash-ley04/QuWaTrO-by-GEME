import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

abstract class ClimateTool {
    public abstract void displayInfo();
}

class ClimateData extends ClimateTool {
    private float temperature;
    private float rainfall;
    private float humidity;

    public float getTemperature() { return temperature; }
    public void setTemperature(float temperature) { this.temperature = temperature; }

    public float getRainfall() { return rainfall; }
    public void setRainfall(float rainfall) { this.rainfall = rainfall; }

    public float getHumidity() { return humidity; }
    public void setHumidity(float humidity) { this.humidity = humidity; }

    public void inputData(Scanner sc) {
        try {
            System.out.print("\nEnter Temperature (°C): ");
            setTemperature(sc.nextFloat());
            System.out.print("Enter Rainfall (mm): ");
            setRainfall(sc.nextFloat());
            System.out.print("Enter Humidity (%): ");
            setHumidity(sc.nextFloat());
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter numeric values only.");
            sc.nextLine();
        }
    }

    @Override
    public void displayInfo() {
        System.out.printf("\n--- Climate Data ---%n");
        System.out.printf("Temperature: %.2f °C%n", temperature);
        System.out.printf("Rainfall   : %.2f mm%n", rainfall);
        System.out.printf("Humidity   : %.2f %%%n", humidity);
    }
}

class AlertSystem extends ClimateTool {
    @Override
    public void displayInfo() {
        System.out.println("This system analyzes temperature, rainfall, and humidity to issue alerts.");
    }

    public void analyze(ClimateData data) {
        System.out.println("\n--- Alerts ---");
        boolean noAlert = true;

        if (data.getTemperature() > 38) {
            System.out.println("Heatwave Alert!");
            noAlert = false;
        }
        if (data.getRainfall() > 100) {
            System.out.println("Flood Risk Alert!");
            noAlert = false;
        }
        if (data.getHumidity() < 30) {
            System.out.println("Dry Spell Warning!");
            noAlert = false;
        }

        if (noAlert) {
            System.out.println("No critical alerts. Weather is stable.");
        }
    }
}

class Logger extends ClimateTool {
    @Override
    public void displayInfo() {
        System.out.println("Logger class saves all data entries to a text file.");
    }

    public void logToFile(ClimateData data) {
        try (FileWriter writer = new FileWriter("EcoPulse_Log.txt", true)) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            writer.write("=== " + dtf.format(now) + " ===\n");
            writer.write(String.format("Temperature: %.2f °C%n", data.getTemperature()));
            writer.write(String.format("Rainfall   : %.2f mm%n", data.getRainfall()));
            writer.write(String.format("Humidity   : %.2f %%%n", data.getHumidity()));
            writer.write("------------------------\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}

class ClimateHistory extends ClimateTool {
    private final ClimateData[] history;
    private int size = 0;

    public ClimateHistory(int capacity) {
        history = new ClimateData[capacity];
    }

    public void addEntry(ClimateData data) {
        if (size < history.length) {
            history[size++] = data;
            System.out.println("Entry added!");
        } else {
            System.out.println("Cannot add more entries — storage full.");
        }
    }

    public ClimateData[] getAllEntries() {
        return Arrays.copyOf(history, size);
    }

    public int getSize() {
        return size;
    }

    @Override
    public void displayInfo() {
        if (size == 0) {
            System.out.println("\nNo climate records yet.");
            return;
        }

        for (int i = 0; i < size; i++) {
            System.out.println("\n== Entry #" + (i + 1) + " ==");
            history[i].displayInfo();
        }
    }
}

public class EcoPulse {
    public static void showMenu() {
        System.out.println("\n===== EcoPulse: Real-Time Climate Impact Monitor =====");
        System.out.println("[1] Input New Climate Data");
        System.out.println("[2] Analyze All Data");
        System.out.println("[3] Save All Entries to Log File");
        System.out.println("[4] View Climate Entries");
        System.out.println("[5] About System (OOP Demo)");
        System.out.println("[6] Exit");
        System.out.print("Select an option: ");
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            ClimateHistory history = new ClimateHistory(100);
            AlertSystem alertSystem = new AlertSystem();
            Logger logger = new Logger();
            
            boolean running = true;
            
            while (running) {
                showMenu();
                
                try {
                    int choice = sc.nextInt();
                    
                    switch (choice) {
                        case 1 -> {
                            ClimateData newData = new ClimateData();
                            newData.inputData(sc);
                            history.addEntry(newData);
                        }
                        case 2 -> {
                            if (history.getSize() == 0) {
                                System.out.println("No data to analyze.");
                                break;
                            }
                            System.out.println("\nAnalyzing all entries...");
                            int i = 1;
                            for (ClimateData data : history.getAllEntries()) {
                                System.out.println("\n>> Entry #" + i++);
                                alertSystem.analyze(data);
                            }
                        }
                        case 3 -> {
                            if (history.getSize() == 0) {
                                System.out.println("⚠ No entries to save.");
                                break;
                            }
                            for (ClimateData data : history.getAllEntries()) {
                                logger.logToFile(data);
                            }
                            System.out.println("All entries saved to EcoPulse_Log.txt");
                        }
                        case 4 -> history.displayInfo();
                        case 5 -> {

                            ClimateTool[] tools = { new ClimateData(), alertSystem, logger, history };
                            System.out.println("\n--- OOP Principle Demonstration ---");
                            for (ClimateTool tool : tools) {
                                tool.displayInfo();
                            }
                        }
                        case 6 -> {
                            System.out.println("Exiting EcoPulse... Stay safe and eco-aware!");
                            running = false;
                        }
                        default -> System.out.println("Invalid option. Try again!");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1-6.");
                    sc.nextLine(); 
                }
            }
        }
    }
}
