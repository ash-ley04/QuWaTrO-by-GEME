import java.io.*;
import java.util.*;

// ===== ABSTRACT CLASS (Abstraction) =====
abstract class UsageCategory {
    protected String name;
    protected float litersUsed;

    public UsageCategory(String name, float litersUsed) {
        this.name = name;
        this.litersUsed = litersUsed;
    }

    // Abstract method (implemented differently in subclasses)
    public abstract float calculateUsage();

    public String getName() {
        return name;
    }

    public float getLitersUsed() {
        return litersUsed;
    }

    public void setLitersUsed(float litersUsed) {
        this.litersUsed = litersUsed;
    }

    public void displayInfo() {
        System.out.println(name + ": " + litersUsed + " liters");
    }
}

// ===== SUBCLASSES (Inheritance + Polymorphism) =====
class Shower extends UsageCategory {
    public Shower(float litersUsed) {
        super("Shower", litersUsed);
    }

    @Override
    public float calculateUsage() {
        return litersUsed;
    }
}

class Laundry extends UsageCategory {
    public Laundry(float litersUsed) {
        super("Laundry", litersUsed);
    }

    @Override
    public float calculateUsage() {
        return litersUsed;
    }
}

class Dishwashing extends UsageCategory {
    public Dishwashing(float litersUsed) {
        super("Dishwashing", litersUsed);
    }

    @Override
    public float calculateUsage() {
        return litersUsed;
    }
}

class Toilet extends UsageCategory {
    public Toilet(float litersUsed) {
        super("Toilet", litersUsed);
    }

    @Override
    public float calculateUsage() {
        return litersUsed;
    }
}

class Irrigation extends UsageCategory {
    public Irrigation(float litersUsed) {
        super("Irrigation", litersUsed);
    }

    @Override
    public float calculateUsage() {
        return litersUsed;
    }
}

// ===== ENCAPSULATION + DATA MODEL =====
class WaterUsage {
    private String date;
    private UsageCategory[] categories;

    public WaterUsage(String date, UsageCategory[] categories) {
        this.date = date;
        this.categories = categories;
    }

    public String getDate() {
        return date;
    }

    public UsageCategory[] getCategories() {
        return categories;
    }

    public float getTotalUsage() {
        float total = 0;
        for (UsageCategory c : categories) {
            total += c.calculateUsage();
        }
        return total;
    }
}

// ===== MAIN MANAGEMENT CLASS =====
class WaterUsageManager {
    private static final String FILE_NAME = "usage.txt";
    private static final float HIGH_USAGE_THRESHOLD = 500.0f;
    private Scanner sc = new Scanner(System.in);

    public void displayMainMenu() {
        System.out.println("\n=== WaVer - Smart Water Usage Tracker ===");
        System.out.println("1. Log Daily Water Usage");
        System.out.println("2. View Usage Summary");
        System.out.println("3. Check for High Usage Days");
        System.out.println("4. View Water-Saving Tips");
        System.out.println("5. Exit");
        System.out.print("Enter your choice (1-5): ");
    }

    public void start() {
        int choice = 0;
        do {
            try {
                displayMainMenu();
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1 -> logDailyUsage();
                    case 2 -> viewUsageSummary();
                    case 3 -> checkHighUsage();
                    case 4 -> displayWaterSavingTips();
                    case 5 -> System.out.println("Exiting WaVer. Thank you for conserving water!");
                    default -> System.out.println("Invalid choice. Please enter 1–5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        } while (choice != 5);
    }

    private void logDailyUsage() {
        try {
            System.out.print("\nEnter today's date (YYYY-MM-DD): ");
            String date = sc.nextLine();

            UsageCategory[] categories = new UsageCategory[5];
            categories[0] = new Shower(getLitersInput("Enter shower water usage (liters): "));
            categories[1] = new Laundry(getLitersInput("Enter laundry water usage (liters): "));
            categories[2] = new Dishwashing(getLitersInput("Enter dishwashing water usage (liters): "));
            categories[3] = new Toilet(getLitersInput("Enter toilet water usage (liters): "));
            categories[4] = new Irrigation(getLitersInput("Enter irrigation/gardening water usage (liters): "));

            WaterUsage usage = new WaterUsage(date, categories);
            displayUsageData(usage);

            System.out.print("Is this information correct? (y/n): ");
            char confirm = sc.nextLine().toLowerCase().charAt(0);

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
                return Float.parseFloat(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void saveToFile(WaterUsage usage) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(usage.getDate());
            for (UsageCategory c : usage.getCategories()) {
                writer.write("," + c.getLitersUsed());
            }
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    private void viewUsageSummary() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            float grandTotal = 0;
            int count = 0;

            System.out.println("\n=== Water Usage Summary ===");
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String date = data[0];
                float[] liters = new float[5];
                for (int i = 0; i < 5; i++) {
                    liters[i] = Float.parseFloat(data[i + 1]);
                }

                UsageCategory[] categories = {
                    new Shower(liters[0]),
                    new Laundry(liters[1]),
                    new Dishwashing(liters[2]),
                    new Toilet(liters[3]),
                    new Irrigation(liters[4])
                };

                WaterUsage usage = new WaterUsage(date, categories);
                displayUsageData(usage);
                grandTotal += usage.getTotalUsage();
                count++;
            }

            if (count > 0) {
                System.out.println("\nTotal recorded days: " + count);
                System.out.println("Grand total usage: " + grandTotal + " liters");
                System.out.println("Average daily usage: " + (grandTotal / count) + " liters");
            } else {
                System.out.println("No data available.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("No usage data found.");
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
    }

    private void checkHighUsage() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean found = false;
            System.out.println("\n=== High Usage Analysis ===");

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String date = data[0];
                float total = 0;
                for (int i = 1; i <= 5; i++) {
                    total += Float.parseFloat(data[i]);
                }

                if (total > HIGH_USAGE_THRESHOLD) {
                    System.out.println("High usage on " + date + ": " + total + " liters");
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No high usage days found.");
            }

        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
    }

    private void displayUsageData(WaterUsage usage) {
        System.out.println("\nDate: " + usage.getDate());
        for (UsageCategory c : usage.getCategories()) {
            c.displayInfo();
        }
        System.out.println("Total: " + usage.getTotalUsage() + " liters");
    }

    private void displayWaterSavingTips() {
        System.out.println("\n=== Water-Saving Tips ===");
        System.out.println("1. Fix leaks promptly.");
        System.out.println("2. Take shorter showers.");
        System.out.println("3. Use low-flow faucets.");
        System.out.println("4. Run full loads for laundry and dishes.");
        System.out.println("5. Water plants early to reduce evaporation.");
        System.out.println("6. Reuse rainwater for irrigation.");
        System.out.println("7. Turn off taps while brushing.");
        System.out.println("8. Sweep instead of hosing outdoor areas.");
    }
}

// ===== MAIN CLASS =====
public class WaverApp {
    public static void main(String[] args) {
        WaterUsageManager manager = new WaterUsageManager();
        manager.start();
    }
}
