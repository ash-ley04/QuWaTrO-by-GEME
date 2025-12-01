import java.io.*;
import java.time.LocalDateTime;
import java.text.DecimalFormat;
import java.util.*;

// =================== QUAKEGUARD MODULE ===================
class InvalidRiskLevelException extends Exception {
    public InvalidRiskLevelException(String message) { super(message); }
}

abstract class Location {
    private String name;
    private String riskLevel;
    private int historicalQuakes;
    private double lastMagnitude;
    private double distanceToFaultLineKm;

    public Location(String name, String riskLevel, int historicalQuakes, double lastMagnitude, double distanceToFaultLineKm) {
        this.name = name;
        this.riskLevel = riskLevel;
        this.historicalQuakes = historicalQuakes;
        this.lastMagnitude = lastMagnitude;
        this.distanceToFaultLineKm = distanceToFaultLineKm;
    }

    public String getName() { return name; }
    public String getRiskLevel() { return riskLevel; }
    public int getHistoricalQuakes() { return historicalQuakes; }
    public double getLastMagnitude() { return lastMagnitude; }
    public double getDistanceToFaultLineKm() { return distanceToFaultLineKm; }
    public abstract String getType();

    public String getDetails() {
        return String.format(
            "\nLocation: %s (%s)\nRisk Level: %s\nHistorical Earthquakes: %d\nLast Major Magnitude: %.1f\nDistance to Fault Line: %.1f km\n",
            getName(), getType(), getRiskLevel(), getHistoricalQuakes(), getLastMagnitude(), getDistanceToFaultLineKm()
        );
    }
}

class CityLocation extends Location {
    public CityLocation(String name, String riskLevel, int historicalQuakes, double lastMagnitude, double distanceToFaultLineKm) {
        super(name, riskLevel, historicalQuakes, lastMagnitude, distanceToFaultLineKm);
    }
    @Override public String getType() { return "City"; }
}

class ProvinceLocation extends Location {
    public ProvinceLocation(String name, String riskLevel, int historicalQuakes, double lastMagnitude, double distanceToFaultLineKm) {
        super(name, riskLevel, historicalQuakes, lastMagnitude, distanceToFaultLineKm);
    }
    @Override public String getType() { return "Province"; }
}

class QuakeGuard {
    private final ArrayList<Location> database = new ArrayList<>();
    private final Scanner scanner;
    private final String[] allowedRiskLevels = {"Low", "Moderate", "High"};

    public QuakeGuard(Scanner scanner) { this.scanner = scanner; seedSampleData(); }

    private void seedSampleData() {
        database.add(new ProvinceLocation("Abra", "Moderate", 25, 6.3, 22.0));
        database.add(new ProvinceLocation("Albay", "High", 42, 6.9, 12.5));
        database.add(new CityLocation("Angeles City", "Moderate", 19, 6.0, 28.0));
        database.add(new CityLocation("Antipolo City", "High", 31, 6.7, 10.0));
        sortDatabase();
    }

    private String validateRisk(String input) throws InvalidRiskLevelException {
        if (input == null) throw new InvalidRiskLevelException("Risk level cannot be null.");
        String trimmed = input.trim();
        for (String risk : allowedRiskLevels)
            if (risk.equalsIgnoreCase(trimmed)) return risk;
        throw new InvalidRiskLevelException("Allowed: Low, Moderate, High only.");
    }

    private void addLocation() {
        try {
            System.out.print("\nEnter Location Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) { System.out.println("Name cannot be empty."); return; }

            System.out.print("Enter Risk Level (Low/Moderate/High): ");
            String risk = validateRisk(scanner.nextLine());

            System.out.print("Historical Earthquakes: ");
            int quakes = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Last Major Magnitude: ");
            double mag = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Distance to Fault Line (km): ");
            double dist = Double.parseDouble(scanner.nextLine().trim());

            database.add(new CityLocation(name, risk, quakes, mag, dist));
            sortDatabase();
            System.out.println("Location added successfully!");
        } catch (InvalidRiskLevelException | NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Location search(String name) {
        for (Location loc : database)
            if (loc.getName().equalsIgnoreCase(name)) return loc;
        return null;
    }

    private void searchLocation() {
        System.out.print("\nEnter location to search: ");
        Location loc = search(scanner.nextLine().trim());
        if (loc != null) {
            System.out.println(loc.getDetails());
            showPreparednessTips(loc.getRiskLevel());
        } else {
            System.out.println("Location not found.");
        }
    }

    private void filterByRisk() {
        try {
            System.out.print("\nEnter risk level to filter (Low/Moderate/High): ");
            String risk = validateRisk(scanner.nextLine());
            System.out.println("\nLocations with Risk Level [" + risk + "]:");
            boolean found = false;
            for (Location loc : database)
                if (loc.getRiskLevel().equalsIgnoreCase(risk)) { System.out.println("- " + loc.getName()); found = true; }
            if (!found) System.out.println("No locations found with that risk level.");
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    private void listAll() {
        System.out.println("\nAll Locations (Alphabetical):");
        for (Location loc : database)
            System.out.println("- " + loc.getName() + " (" + loc.getRiskLevel() + ")");
    }

    private void showPreparednessTips(String risk) {
        System.out.println("\nPreparedness Tips:");
        if (risk.equalsIgnoreCase("High") || risk.equalsIgnoreCase("Moderate")) {
            System.out.println("- Keep a Go-Bag ready (food, water, medicine).");
            System.out.println("- Know safe spots in your home.");
            System.out.println("- Secure heavy objects and join drills.");
        } else {
            System.out.println("- Stay alert and aware of PHIVOLCS updates.");
        }
    }

    private void sortDatabase() { database.sort(Comparator.comparing(l -> l.getName().toLowerCase())); }

    public void start() {
        int choice;
        do {
            System.out.println("                                 W E L C O M E  T O\n");
            System.out.println("\u001B[38;5;214m ██████╗ ██╗   ██╗ █████╗ ██╗  ██╗███████╗ ██████╗ ██╗   ██╗ █████╗ ██████╗ ██████╗ ");
            System.out.println("\u001B[38;5;214m██╔═══██╗██║   ██║██╔══██╗██║ ██╔╝██╔════╝██╔════╝ ██║   ██║██╔══██╗██╔══██╗██╔══██╗");
            System.out.println("\u001B[38;5;214m██║   ██║██║   ██║███████║█████╔╝ ███████╗██║  ███╗██║   ██║███████║██████╔╝██║  ██║");
            System.out.println("\u001B[38;5;214m██║▄▄ ██║██║   ██║██╔══██║██╔═██╗ ██╔════╝██║   ██║██║   ██║██╔══██║██╔══██╗██║  ██║");
            System.out.println("\u001B[38;5;214m╚██████╔╝╚██████╔╝██║  ██║██║  ██╗███████║╚██████╔╝╚██████╔╝██║  ██║██║  ██║██████╔╝");
            System.out.println("\u001B[38;5;214m ╚══▀▀═╝ ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝  ╚═════╝ ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ");
            System.out.print("\u001B[0m");
            System.out.println("   S T A Y  S T R O N G,  S T A Y  S T E A D Y,  T R U S T  Q U A K E G U A R D!\n");
            System.out.println("1. Search Location Info");
            System.out.println("2. Add New Location");
            System.out.println("3. View All Locations");
            System.out.println("4. Filter by Risk Level");
            System.out.println("5. Back to Modules");
            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> searchLocation();
                    case 2 -> addLocation();
                    case 3 -> listAll();
                    case 4 -> filterByRisk();
                    case 5 -> System.out.println("Returning to Main Menu...");
                    default -> System.out.println("Invalid choice!");
                }
            } catch (NumberFormatException e) { choice = 0; }
        } while (choice != 5);
    }
}

// =================== WAVER MODULE ===================
abstract class UsageCategory {
    protected String name;
    protected float litersUsed;
    public UsageCategory(String name, float litersUsed) { this.name = name; this.litersUsed = litersUsed; }
    public abstract float calculateUsage();
    public String getName() { return name; }
    public float getLitersUsed() { return litersUsed; }
    public void setLitersUsed(float litersUsed) { this.litersUsed = litersUsed; }
    public void displayInfo() { System.out.println(name + ": " + litersUsed + " liters"); }
}

class Shower extends UsageCategory { public Shower(float litersUsed) { super("Shower", litersUsed); } @Override public float calculateUsage() { return litersUsed; } }
class Laundry extends UsageCategory { public Laundry(float litersUsed) { super("Laundry", litersUsed); } @Override public float calculateUsage() { return litersUsed; } }
class Dishwashing extends UsageCategory { public Dishwashing(float litersUsed) { super("Dishwashing", litersUsed); } @Override public float calculateUsage() { return litersUsed; } }
class Toilet extends UsageCategory { public Toilet(float litersUsed) { super("Toilet", litersUsed); } @Override public float calculateUsage() { return litersUsed; } }
class Irrigation extends UsageCategory { public Irrigation(float litersUsed) { super("Irrigation", litersUsed); } @Override public float calculateUsage() { return litersUsed; } }

class WaterUsage {
    private String date;
    private UsageCategory[] categories;
    public WaterUsage(String date, UsageCategory[] categories) { this.date = date; this.categories = categories; }
    public String getDate() { return date; }
    public UsageCategory[] getCategories() { return categories; }
    public float getTotalUsage() { float total = 0; for (UsageCategory c : categories) total += c.calculateUsage(); return total; }
}

class WaterUsageManager {
    private static final String FILE_NAME = "usage.txt";
    private static final float HIGH_USAGE_THRESHOLD = 500.0f;
    private final Scanner sc;
    public WaterUsageManager(Scanner sc) { this.sc = sc; }

    public void start() {
        int choice;
        do {
            System.out.println("\n                       W E L C O M E   T O   \n");
            System.out.println("\u001B[36m            ██╗    ██╗ █████╗ ██╗   ██╗███████╗██████╗ ");
            System.out.println("\u001B[36m            ██║    ██║██╔══██╗██║   ██║██╔════╝██╔══██╗");   
            System.out.println("\u001B[36m            ██║ █╗ ██║███████║██║   ██║█████╗  ██████╔╝");
            System.out.println("\u001B[36m            ██║███╗██║██╔══██║╚██╗ ██╔╝██╔══╝  ██╔══██╗");
            System.out.println("\u001B[36m            ╚███╔███╔╝██║  ██║ ╚████╔╝ ███████╗██║  ██║");
            System.out.println("\u001B[36m             ╚══╝╚══╝ ╚═╝  ╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝");
            System.out.print("\u001B[0m");
            System.out.println("B E   W I S E,   S T O P   W A S T E,   W O R K   W I T H   W A V E R!\n");
            System.out.println("1. Log Daily Water Usage");
            System.out.println("2. View Usage Summary");
            System.out.println("3. Check for High Usage Days");
            System.out.println("4. View Water-Saving Tips");
            System.out.println("5. Back to Modules");
            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
                switch (choice) {
                    case 1 -> logDailyUsage();
                    case 2 -> viewUsageSummary();
                    case 3 -> checkHighUsage();
                    case 4 -> displayWaterSavingTips();
                    case 5 -> System.out.println("Returning to Main Menu...");
                    default -> System.out.println("Invalid choice. Please enter 1-5.");
                }
            } catch (NumberFormatException e) { choice = 0; }
        } while (choice != 5);
    }

    private void logDailyUsage() {
        try {
            System.out.print("\nEnter today's date (YYYY-MM-DD): ");
            String date = sc.nextLine().trim();
            if (date.isEmpty()) { System.out.println("Date cannot be empty."); return; }

            UsageCategory[] categories = new UsageCategory[5];
            categories[0] = new Shower(getLitersInput("Enter shower water usage (liters): "));
            categories[1] = new Laundry(getLitersInput("Enter laundry water usage (liters): "));
            categories[2] = new Dishwashing(getLitersInput("Enter dishwashing water usage (liters): "));
            categories[3] = new Toilet(getLitersInput("Enter toilet water usage (liters): "));
            categories[4] = new Irrigation(getLitersInput("Enter irrigation/gardening water usage (liters): "));

            WaterUsage usage = new WaterUsage(date, categories);
            displayUsageData(usage);

            System.out.print("Is this information correct? (y/n): ");
            String line = sc.nextLine().trim().toLowerCase();
            char confirm = line.isEmpty() ? 'n' : line.charAt(0);

            if (confirm == 'y') {
                saveToFile(usage);
                System.out.println("Data saved successfully!");
                System.out.println("Total usage today: " + usage.getTotalUsage() + " liters");
                if (usage.getTotalUsage() > HIGH_USAGE_THRESHOLD) {
                    System.out.println("Warning: High water usage detected!");
                    displayWaterSavingTips();
                }
            } else {
                System.out.println("Data not saved. Please re-enter if needed.");
            }

        } catch (Exception e) {
            System.out.println("Error logging data: " + e.getMessage());
        }
    }

    private float getLitersInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Float.parseFloat(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void saveToFile(WaterUsage usage) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(usage.getDate());
            for (UsageCategory c : usage.getCategories()) writer.write("," + c.getLitersUsed());
            writer.newLine();
        } catch (IOException e) { System.out.println("Error saving to file: " + e.getMessage()); }
    }

    private void viewUsageSummary() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            float grandTotal = 0;
            int count = 0;
            System.out.println("\n=== Water Usage Summary ===");
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6) continue;
                String date = data[0];
                float[] liters = new float[5];
                for (int i = 0; i < 5; i++) liters[i] = Float.parseFloat(data[i + 1]);
                UsageCategory[] categories = { new Shower(liters[0]), new Laundry(liters[1]), new Dishwashing(liters[2]), new Toilet(liters[3]), new Irrigation(liters[4]) };
                WaterUsage usage = new WaterUsage(date, categories);
                displayUsageData(usage);
                grandTotal += usage.getTotalUsage();
                count++;
            }
            if (count > 0) System.out.println("\nAverage daily usage: " + (grandTotal / count) + " liters");
            else System.out.println("No usage data available.");
        } catch (FileNotFoundException e) { System.out.println("No usage data file found. Log some usage first."); }
        catch (IOException e) { System.out.println("Error reading file: " + e.getMessage()); }
    }

    private void displayUsageData(WaterUsage usage) {
        System.out.println("\nDate: " + usage.getDate());
        for (UsageCategory c : usage.getCategories()) c.displayInfo();
        System.out.println("Total: " + usage.getTotalUsage() + " liters");
    }

    private void checkHighUsage() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean found = false;
            System.out.println("\nDays with high water usage (>500 liters):");
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6) continue;
                float total = 0;
                for (int i = 1; i < data.length; i++) total += Float.parseFloat(data[i]);
                if (total > HIGH_USAGE_THRESHOLD) { System.out.println("- " + data[0] + ": " + total + " liters"); found = true; }
            }
            if (!found) System.out.println("None");
        } catch (FileNotFoundException e) { System.out.println("No usage data file found. Log some usage first."); }
        catch (IOException e) { System.out.println("Error reading file: " + e.getMessage()); }
    }

    private void displayWaterSavingTips() {
        System.out.println("\n=== Water-Saving Tips ===");
        System.out.println("- Take shorter showers");
        System.out.println("- Fix leaking faucets immediately");
        System.out.println("- Use washing machines with full loads");
        System.out.println("- Reuse water for gardening if possible");
    }
}

// =================== TEMPERRA MODULE ===================
// Abstract base class
abstract class EnvironmentalTool {
    abstract void displayInfo();
}

// Climate awareness facts
class ClimateAwareness extends EnvironmentalTool {
    private final List<String> facts = Arrays.asList(
            "1. The Earth is heating up, causing extreme weather and ecosystem disruptions.",
            "2. Water scarcity affects over 2 billion people due to climate change.",
            "3. Deforestation increases carbon emissions and destroys habitats.",
            "4. Switching to renewable energy can reduce global warming.",
            "5. Rising sea levels threaten coastal communities worldwide."
    );

    @Override
    void displayInfo() {
        System.out.println("\n========= Climate Awareness =========");
        for (String fact : facts) {
            System.out.println(fact + "\n");
        }
    }
}

// Heat Index calculator
class HeatIndexCalculator extends EnvironmentalTool {
    private double temperature;
    private double humidity;
    private double heatIndex;

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public double getHumidity() { return humidity; }
    public void setHumidity(double humidity) { this.humidity = humidity; }

    public double getHeatIndex() { return heatIndex; }

    @Override
    void displayInfo() {
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

// Data logger
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

// Heat Index manager with array operations
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
        if (!found) System.out.println("Heat Index " + value + "°C not found.");
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

    public int getSize() { return size; }
}

// Unified TempTerra class
class TempTerra {
    private final Scanner sc;
    private final HeatIndexManager manager = new HeatIndexManager();
    private final ClimateAwareness awareness = new ClimateAwareness();

    public TempTerra(Scanner sc) { this.sc = sc; }

    public void start() {
        int choice;
        do {
            System.out.println("\n                              W E L C O M E  T O\n");
            System.out.println("\u001B[36m ████████╗███████╗███╗   ███╗██████╗ ████████╗███████╗██████╗ ██████╗  █████╗ ");
            System.out.println("\u001B[36m ╚══██╔══╝██╔════╝████╗ ████║██╔══██╗╚══██╔══╝██╔════╝██╔══██╗██╔══██╗██╔══██╗");
            System.out.println("\u001B[36m    ██║   █████╗  ██╔████╔██║██████╔╝   ██║   █████╗  ██████╔╝██████╔╝███████║");
            System.out.println("\u001B[36m    ██║   ██╔══╝  ██║╚██╔╝██║██╔═══╝    ██║   ██╔══╝  ██╔══██╗██╔══██╗██╔══██║");
            System.out.println("\u001B[36m    ██║   ███████╗██║ ╚═╝ ██║██║        ██║   ███████╗██║  ██║██║  ██║██║  ██║");
            System.out.println("\u001B[36m    ╚═╝   ╚══════╝╚═╝     ╚═╝╚═╝        ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝");
            System.out.print("\u001B[0m");
            System.out.println("          L E A R N,  L O G,  L E A D  A  G R E E N E R  F U T U R E\n");
            System.out.println("1. Calculate Heat Index");
            System.out.println("2. View Saved Logs");
            System.out.println("3. Delete Entry");
            System.out.println("4. Climate Facts");
            System.out.println("5. Back to Modules");
            System.out.print("Enter your choice: ");
            try { choice = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) { choice = -1; }

            switch (choice) {
                case 1 -> calculateHeatIndex();
                case 2 -> manager.viewLogs();
                case 3 -> deleteEntry();
                case 4 -> awareness.displayInfo();
                case 5 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 5);
    }

    private void calculateHeatIndex() {
        try {
            System.out.print("Enter temperature (°C): ");
            double temp = Double.parseDouble(sc.nextLine());
            System.out.print("Enter humidity (%): ");
            double hum = Double.parseDouble(sc.nextLine());

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

            System.out.print("Search for Heat Index value (°C): ");
            double searchVal = Double.parseDouble(sc.nextLine());
            manager.search(searchVal);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private void deleteEntry() {
        if (manager.getSize() == 0) {
            System.out.println("No entries to delete.");
        } else {
            manager.displayAll();
            try {
                System.out.print("Delete Heat Index at index (0 to " + (manager.getSize() - 1) + "): ");
                int index = Integer.parseInt(sc.nextLine());
                manager.delete(index);
                manager.displayAll();
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }

}
// =================== ECOPULSE MODULE ===================
abstract class ClimateTool { public abstract void displayInfo(); }

class ClimateData extends ClimateTool {
    private float temperature, rainfall, humidity;
    public float getTemperature() { return temperature; }
    public void setTemperature(float t) { temperature = t; }
    public float getRainfall() { return rainfall; }
    public void setRainfall(float r) { rainfall = r; }
    public float getHumidity() { return humidity; }
    public void setHumidity(float h) { humidity = h; }

    public void inputData(Scanner sc) {
        try {
            System.out.print("Enter Temperature (°C): "); setTemperature(Float.parseFloat(sc.nextLine().trim()));
            System.out.print("Enter Rainfall (mm): "); setRainfall(Float.parseFloat(sc.nextLine().trim()));
            System.out.print("Enter Humidity (%): "); setHumidity(Float.parseFloat(sc.nextLine().trim()));
        } catch (Exception e) { System.out.println("Invalid input."); }
    }

    @Override public void displayInfo() {
        System.out.println("Temperature: " + temperature + "°C, Rainfall: " + rainfall + "mm, Humidity: " + humidity + "%");
    }
}

class AlertSystem extends ClimateTool {
    private String message = "";
    public void setMessage(String msg) { message = msg; }
    @Override public void displayInfo() { System.out.println("Alert: " + message); }
}

class Logger extends ClimateTool {
    private ArrayList<String> logs = new ArrayList<>();
    public void addLog(String log) { logs.add(log); }
    @Override public void displayInfo() { System.out.println("Logs: " + logs); }
}

class ClimateHistory extends ClimateTool {
    private ArrayList<ClimateData> history = new ArrayList<>();
    public void addHistory(ClimateData data) { history.add(data); }
    @Override public void displayInfo() { System.out.println("Historical Data Points: " + history.size()); }
}

class EcoPulse {
    private final Scanner sc;
    private final ClimateData data = new ClimateData();
    private final AlertSystem alert = new AlertSystem();
    private final Logger logger = new Logger();
    private final ClimateHistory history = new ClimateHistory();

    public EcoPulse(Scanner sc) { this.sc = sc; }

    public void start() {
        int choice;
        do {
            System.out.println("\n                                  W E L C O M E   T O   \n");
            System.out.println("\u001B[95m             ███████╗ ██████╗ ██████╗ ██████╗ ██╗   ██╗██╗     ███████╗███████╗");
            System.out.println("\u001B[95m             ██╔════╝██╔════╝██╔═══██╗██╔══██╗██║   ██║██║     ██╔════╝██╔════╝");
            System.out.println("\u001B[95m             █████╗  ██║     ██║   ██║██████╔╝██║   ██║██║     █████╗  █████╗  ");
            System.out.println("\u001B[95m             ██╔══╝  ██║     ██║   ██║██╔═══╝ ██║   ██║██║     ╚════██╗██╔══╝  ");
            System.out.println("\u001B[95m             ███████╗╚██████╗╚██████╔╝██║     ╚██████╔╝███████╗███████║███████╗");
            System.out.println("\u001B[95m             ╚══════╝ ╚═════╝ ╚═════╝ ╚═╝      ╚═════╝ ╚══════╝╚══════╝╚══════╝");
            System.out.print("\u001B[0m");
            System.out.println("M E A S U R E .  M O N I T O R .  M A N A G E .  E C O P U L S E  H A S  Y O U  C O V E R E D !\n");

            System.out.println("1. Input Climate Data");
            System.out.println("2. View Current Climate Data");
            System.out.println("3. View Alerts");
            System.out.println("4. View Logs");
            System.out.println("5. View History");
            System.out.println("6. Back to Modules");
            System.out.print("Enter your choice: ");
            try { choice = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) { choice = -1; }

            switch (choice) {
                case 1 -> {
                    data.inputData(sc);
                    history.addHistory(data);
                    logger.addLog("Data input on " + LocalDateTime.now());
                    if (data.getTemperature() > 38) alert.setMessage("High temperature alert!");
                }
                case 2 -> data.displayInfo();
                case 3 -> alert.displayInfo();
                case 4 -> logger.displayInfo();
                case 5 -> history.displayInfo();
                case 6 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 6);
    }
}

// =================== MAIN SYSTEM ===================
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        QuakeGuard quakeGuard = new QuakeGuard(sc);
        WaterUsageManager waver = new WaterUsageManager(sc);
        TempTerra tempTerra = new TempTerra(sc);
        EcoPulse ecoPulse = new EcoPulse(sc);

        boolean running = true;
        while (running) {
            showStartMenu();
            int startChoice;
            try { startChoice = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) { startChoice = 0; }

            switch (startChoice) {
                case 1 -> showMainMenu(sc, quakeGuard, waver, tempTerra, ecoPulse);
                case 2 -> showHelp();
                case 3 -> {
                    System.out.println("\n     W H E R E  G E M E  I N N O V A T E S,  Q U W A T R O  P R O T E C T S  \n");
                    System.out.println("\u001B[33m████████╗██╗  ██╗ █████╗ ███╗   ██╗██╗  ██╗    ██╗   ██╗ ██████╗ ██╗   ██╗  ███╗");
                    System.out.println("\u001B[33m╚══██╔══╝██║  ██║██╔══██╗████╗  ██║██║ ██╔╝    ╚██╗ ██╔╝██╔═══██╗██║   ██║  ███║");
                    System.out.println("\u001B[33m   ██║   ███████║███████║██╔██╗ ██║█████╔╝      ╚████╔╝ ██║   ██║██║   ██║  ███║");
                    System.out.println("\u001B[33m   ██║   ██╔══██║██╔══██║██║╚██╗██║██╔═██╗        ██║   ██║   ██║██║   ██║  ╚══╝ ");
                    System.out.println("\u001B[33m   ██║   ██║  ██║██║  ██║██║ ╚████║██║  ██╗       ██║   ╚██████╔╝╚██████╔╝  ███║");
                    System.out.println("\u001B[33m   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝       ╚═╝    ╚═════╝  ╚═════╝   ╚══╝ ");
                    System.out.print("\u001B[0m");
                    System.out.println("  S T A Y  S A F E,  S T A Y  B R I G H T,  A N D  D O  W H A T' S  R I G H T !\n");
                    running = false;
                }
                default -> System.out.println("Invalid choice!");
            }
        }

        sc.close();
    }

    
    private static void showStartMenu() {

        System.out.println("\n");
        System.out.println("                           W E L C O M E  T O\n");
        System.out.println("\u001B[32m      ██████╗  ██╗   ██╗██╗    ██╗ █████╗ ████████╗██████╗  ██████╗  ");
        System.out.println("\u001B[32m     ██╔═══██╗ ██║   ██║██║    ██║██╔══██╗╚══██╔══╝██╔══██╗██╔═══██╗ ");
        System.out.println("\u001B[32m     ██║   ██║ ██║   ██║██║ █╗ ██║███████║   ██║   ██████╔╝██║   ██║ ");
        System.out.println("\u001B[32m     ██║██ ██║ ██║   ██║██║███╗██║██╔══██║   ██║   ██╔══██╗██║   ██║ ");
        System.out.println("\u001B[32m     ╚██████╔╝ ╚██████╔╝╚███╔███╔╝██║  ██║   ██║   ██║  ██║╚██████╔╝ ");
        System.out.println("\u001B[32m      ╚══▀▀═╝   ╚═════╝  ╚══╝╚══╝ ╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝ ╚═════╝  ");
        System.out.println("      - Y o u r  C o m p a s s  f o r  a  S a f e r  F u t u r e - ");
        System.out.print("\u001B[0m");
        System.out.println("                         Developed by: GEME");
        System.out.println("\n1. Start");
        System.out.println("2. Help");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void showHelp() {
        System.out.println("\n==== HELP ====");
        System.out.println("This a unified suite offering earthquake insights, water usage tracking,");
        System.out.println("environmental temperature monitoring, and real-time climate alert:");
        
        System.out.println("\n1. QuakeGuard - Earthquake Risk Info");
        System.out.println("   - Shows recent earthquakes and magnitudes.");
        System.out.println("   - Offers safety tips based on risk level.");
        System.out.println("   - Helps users understand local quake hazards.");

        System.out.println("\n2. WaVer - Water Usage Tracking");
        System.out.println("   - Tracks daily, weekly, and monthly water use.");
        System.out.println("   - Identifies high-usage patterns and conservation tips.");
        System.out.println("   - Sends alerts for excessive or unusual consumption.");

        System.out.println("\n3. TempTerra - Temperature & Environment Hub");
        System.out.println("   - Displays real-time temperature and conditions.");
        System.out.println("   - Explains heat index, humidity, and weather effects.");
        System.out.println("   - Gives health and safety tips during extreme heat.");

        System.out.println("\n4. EcoPulse - Real-Time Climate Monitoring");
        System.out.println("   - Sends real-time climate and environmental alerts.");
        System.out.println("   - Tracks weather pattern changes for user awareness.");
        System.out.println("   - Provides safety actions during climate-related events.");

        System.out.println("\nThis suite is crafted to inspire environmental awareness,");
        System.out.println("strengthen safety practices, and empower users to make smart");
        System.out.println("responsible, and well-informed decisions.");
    }

    private static void showMainMenu(Scanner sc, QuakeGuard quakeGuard, WaterUsageManager waver,
                                     TempTerra tempTerra, EcoPulse ecoPulse) {
        int choice;
        do {
            System.out.println("\n===== QuWaTrO Management Suite =====");
            System.out.println("1. Modules");
            System.out.println("2. Authors");
            System.out.println("3. About the Project");
            System.out.println("4. Exit to Main Menu");
            System.out.print("Enter your choice: ");
            try { choice = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) { choice = 0; }

            switch (choice) {
                case 1 -> showModulesMenu(sc, quakeGuard, waver, tempTerra, ecoPulse);
                case 2 -> showAuthorsMenu(sc);
                case 3 -> displayProjectInfo();
                case 4 -> System.out.println("\nReturning to Start Menu...");
                default -> System.out.println("\nInvalid choice!");
            }
        } while (choice != 4);
    }

    private static void showModulesMenu(Scanner sc, QuakeGuard quakeGuard, WaterUsageManager waver,
                                        TempTerra tempTerra, EcoPulse ecoPulse) {
        int moduleChoice;
        do {
            System.out.println("\n--- Modules ---");
            System.out.println("1. QuakeGuard (Earthquake Risk System)");
            System.out.println("2. WaVer (Water Usage Tracker)");
            System.out.println("3. TempTerra Knowledge Hub");
            System.out.println("4. EcoPulse Climate Monitor");
            System.out.println("5. Back to Main Menu");
            System.out.print("Select a module: ");
            try { moduleChoice = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) { moduleChoice = 0; }

            switch (moduleChoice) {
                case 1 -> quakeGuard.start();
                case 2 -> waver.start();
                case 3 -> tempTerra.start();
                case 4 -> ecoPulse.start();
                case 5 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid choice!");
            }
        } while (moduleChoice != 5);
    }

    private static void showAuthorsMenu(Scanner sc) {
        int authorChoice;
        do {
            System.out.println("\n--- Authors ---");
            System.out.println("1. DALISAY, Gelliel Ashley M.");
            System.out.println("2. DIAZ, Elyzah Kim C.");
            System.out.println("3. LABITAN, Mikko Ronce N.");
            System.out.println("4. UNTIVEROS, Edz Margaret");
            System.out.println("5. Back to Main Menu");
            System.out.print("Select an author: ");
            try { authorChoice = Integer.parseInt(sc.nextLine().trim()); } catch (Exception e) { authorChoice = 0; }

            switch (authorChoice) {
                case 1 -> System.out.println("\nLast Name:  DALISAY \r\n" + //
                                        "First Name: Gelliel Ashley\r\n" + //
                                        "Middle Initial: Macuha\r\n" + //
                                        "Email: gellielashley@gmail.com\r\n" + //
                                        "Contact Number: 09760145494\r\n" + //
                                        "Address: As-is Bauan Batangas \r\n" + //
                                        "Birthdate: April 1, 2006\r\n" + //
                                        "Age: 19\r\n" + //
                                        "Sex: Female\r\n" + //
                                        "Nationality: Filipino \r\n" + //
                                        "Religion: Roman Catholic");
                case 2 -> System.out.println("\nLast Name: DIAZ\r\n" + //
                                        "First Name: Elyzah \r\n" + //
                                        "Middle Initial: Cacao\r\n" + //
                                        "Email: elyzahdiaz9@gmail.com\r\n" + //
                                        "Contact Number: 09694566268\r\n" + //
                                        "Address: Talaibon Ibaan Batangas\r\n" + //
                                        "Birthdate: June 9, 2006\r\n" + //
                                        "Age: 19\r\n" + //
                                        "Sex: Female\r\n" + //
                                        "Nationality: Filipino\r\n" + //
                                        "Religion: Roman Catholic");
                case 3 -> System.out.println("\nLast Name: LABITAN \r\n" + //
                                        "First Name: Mikko Ronce  \r\n" + //
                                        "Middle Initial: Nambayan \r\n" + //
                                        "Email: mikkoronce@gmail.com\r\n" + //
                                        "Contact Number: +63 991 948 8324\r\n" + //
                                        "Address: Sorosoro Karsada, Batangas City \r\n" + //
                                        "Birthdate: May 03, 2006 \r\n" + //
                                        "Age: 19 \r\n" + //
                                        "Sex: Male\r\n" + //
                                        "Nationality: Filipino\r\n" + //
                                        "Religion: Roman Catholic");
                case 4 -> System.out.println("\nLast Name: UNTIVEROS \r\n" + //
                                        "First Name: Edz Margaret \r\n" + //
                                        "Middle Initial: Rosales \r\n" + //
                                        "Email: untiverosedzmargaret@gmail.com\r\n" + //
                                        "Contact Number: 09989221885\r\n" + //
                                        "Address: Tinga Itaas, Batangas City \r\n" + //
                                        "Birthdate: September 15, 2006 \r\n" + //
                                        "Age: 19 \r\n" + //
                                        "Sex: Female \r\n" + //
                                        "Nationality: Filipino \r\n" + //
                                        "Religion: Roman Catholic");
                case 5 -> System.out.println("\nReturning to Main Menu...");
                default -> System.out.println("\nInvalid choice!");
            }
        } while (authorChoice != 5);
    }

    private static void displayProjectInfo() {
        System.out.println("\n--- About the Project ---");
        System.out.println("This suite combines multiple environmental and disaster management tools:");
        System.out.println("\nQuakeGuard: A Real-Time Earthquake Risk Assessment System\r\n" + //
                        "\r\n" + //
                        "QuakeGuard is a Java console-based application that efficiently organizes and \r\n" + //
                        "retrieves earthquake risk data for different locations across the Philippines. \r\n" + //
                        "The system enables users to search, filter, and view data according to risk \r\n" + //
                        "levels, historical seismic activity, and proximity to fault lines. It also \r\n" + //
                        "features admin-level controls that allow authorized users to add, edit, or delete \r\n" + //
                        "records. Additionally, QuakeGuard provides preparedness tips tailored to each risk \r\n" + //
                        "level, helping users stay informed and ready for potential disasters. Through a \r\n" + //
                        "clear, text-based menu interface, the system promotes public awareness, disaster \r\n" + //
                        "readiness, and data-driven risk assessment using Java's structured logic and data \r\n" + //
                        "management features.\r\n" + //
                        "\r\n" + //
                        "WaVer: A Java Console Tool to Track and Reduce Household Water Waste\r\n" + //
                        "\r\n" + //
                        "WaVer is a Java-based console program designed to help users monitor and manage \r\n" + //
                        "daily water consumption. By logging household activities such as showering, laundry, \r\n" + //
                        "dishwashing, toilet use, and watering plants, the system records and analyzes total \r\n" + //
                        "water usage. It identifies patterns of high consumption, detects potential leaks, \r\n" + //
                        "and highlights peak usage times. Based on user data, WaVer provides personalized \r\n" + //
                        "water-saving suggestions to encourage sustainable practices and reduce utility costs. \r\n" + //
                        "The project demonstrates the practical use of Java arrays, conditionals, and file \r\n" + //
                        "handling, offering a simple yet impactful solution for environmental conservation at home.\r\n" + //
                        "\r\n" + //
                        "TempTerra Knowledge Hub: Understanding the Heat Index Crisis\r\n" + //
                        "\r\n" + //
                        "TempTerra is a Java console application focused on raising climate and temperature \r\n" + //
                        "awareness by calculating and analyzing the Heat Index from user-input temperature and \r\n" + //
                        "humidity. The program utilizes arrays and sorting/searching algorithms to store, organize, \r\n" + //
                        "and review past heat index records. It also logs each computation in a text file, allowing \r\n" + //
                        "users to track environmental trends over time. Beyond data processing, TempTerra presents \r\n" + //
                        "climate facts and educational insights about the effects of heatwaves and global warming, \r\n" + //
                        "encouraging users to be more informed and responsible toward environmental changes. This \r\n" + //
                        "project highlights the integration of Java computation, data handling, and user education \r\n" + //
                        "in one practical application.\r\n" + //
                        "\r\n" + //
                        "EcoPulse: Real-Time Climate Impact Monitor\r\n" + //
                        "\r\n" + //
                        "EcoPulse is a Java console-based monitoring system that records and evaluates environmental \r\n" + //
                        "data such as temperature, humidity, and rainfall. Using file management and conditional \r\n" + //
                        "logic, the program stores data entries with timestamps and triggers alerts for extreme \r\n" + //
                        "weather conditions including floods, heat waves, or droughts. Users can view all recorded \r\n" + //
                        "logs, track environmental trends, and receive safety reminders based on the data analysis. \r\n" + //
                        "With its intuitive text interface and automated alert system, EcoPulse demonstrates how \r\n" + //
                        "Java can be used for real-time environmental tracking, making it a valuable tool for \r\n" + //
                        "communities, students, and researchers interested in climate resilience and data-driven \r\n" + //
                        "awareness.");
    }
}
