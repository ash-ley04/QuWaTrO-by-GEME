// QuakeGuard.java (Clean Console Version)
// CS 211 - Final Project | Java OOP Version (No Emoji Output)
// Author: (Your Name)
// Compile: javac QuakeGuard.java
// Run: java QuakeGuard

import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;

class InvalidRiskLevelException extends Exception {
    public InvalidRiskLevelException(String message) {
        super(message);
    }
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
    @Override
    public String getType() { return "City"; }
}

class ProvinceLocation extends Location {
    public ProvinceLocation(String name, String riskLevel, int historicalQuakes, double lastMagnitude, double distanceToFaultLineKm) {
        super(name, riskLevel, historicalQuakes, lastMagnitude, distanceToFaultLineKm);
    }
    @Override
    public String getType() { return "Province"; }
}

public class QuakeGuard {
    private ArrayList<Location> database = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private final String[] allowedRiskLevels = {"Low", "Moderate", "High"};

    public QuakeGuard() {
        seedSampleData();
    }

    private void seedSampleData() {
        database.add(new ProvinceLocation("Abra", "Moderate", 25, 6.3, 22.0));
        database.add(new ProvinceLocation("Albay", "High", 42, 6.9, 12.5));
        database.add(new CityLocation("Angeles City", "Moderate", 19, 6.0, 28.0));
        database.add(new CityLocation("Antipolo City", "High", 31, 6.7, 10.0));
        database.add(new ProvinceLocation("Antique", "Moderate", 22, 6.1, 35.0));
        database.add(new CityLocation("Bacolod City", "Moderate", 18, 5.8, 40.0));
        database.add(new CityLocation("Batangas City", "High", 38, 6.5, 14.0));
        database.add(new CityLocation("Cagayan de Oro City", "Low", 6, 4.9, 80.0));
        database.add(new CityLocation("Caloocan City", "High", 30, 6.6, 11.5));
        database.add(new CityLocation("Cebu City", "Moderate", 20, 6.0, 34.0));
        database.add(new CityLocation("Davao City", "Moderate", 18, 6.2, 30.0));
        database.add(new CityLocation("Dagupan City", "Moderate", 27, 6.4, 26.0));
        database.add(new CityLocation("Dasmariñas City", "Moderate", 21, 6.2, 20.0));
        database.add(new CityLocation("General Santos City", "Moderate", 15, 5.9, 37.0));
        database.add(new ProvinceLocation("Ilocos Region", "Moderate", 29, 6.3, 24.0));
        database.add(new CityLocation("Ilagan City", "Moderate", 17, 6.0, 32.0));
        database.add(new CityLocation("Kalibo City", "Low", 5, 4.8, 90.0));
        database.add(new CityLocation("Laoag City", "Low", 8, 5.0, 70.0));
        database.add(new CityLocation("Las Piñas City", "High", 33, 6.8, 13.5));
        database.add(new CityLocation("Legazpi City", "High", 45, 7.0, 10.0));
        database.add(new CityLocation("Manila", "High", 56, 7.1, 15.5));
        database.add(new CityLocation("Makati City", "High", 34, 6.6, 14.0));
        database.add(new CityLocation("Marikina City", "High", 36, 6.7, 9.0));
        database.add(new CityLocation("Masbate City", "High", 40, 6.8, 18.0));
        database.add(new CityLocation("Muntinlupa City", "High", 32, 6.5, 16.0));
        database.add(new CityLocation("Naga City", "Moderate", 23, 6.1, 25.0));
        database.add(new CityLocation("Olongapo City", "Moderate", 20, 6.2, 29.0));
        database.add(new CityLocation("Pagadian City", "Moderate", 16, 5.9, 38.0));
        database.add(new CityLocation("Parañaque City", "High", 31, 6.4, 17.0));
        database.add(new CityLocation("Pasig City", "High", 30, 6.5, 12.0));
        database.add(new CityLocation("Puerto Princesa City", "Low", 3, 4.6, 150.0));
        database.add(new CityLocation("Quezon City", "High", 35, 6.9, 11.0));
        database.add(new CityLocation("Roxas City", "Low", 4, 4.7, 110.0));
        database.add(new CityLocation("San Jose del Monte City", "Moderate", 22, 6.0, 27.0));
        database.add(new CityLocation("San Pablo City", "Moderate", 18, 6.1, 30.0));
        database.add(new CityLocation("Tacloban City", "Moderate", 20, 6.3, 28.0));
        database.add(new CityLocation("Tagbilaran City", "Moderate", 17, 6.0, 33.0));
        database.add(new CityLocation("Taguig City", "High", 28, 6.5, 14.0));
        database.add(new CityLocation("Tagum City", "Moderate", 15, 5.8, 36.0));
        database.add(new CityLocation("Tarlac City", "Moderate", 19, 6.1, 25.0));
        database.add(new CityLocation("Tuguegarao City", "Low", 10, 5.4, 65.0));
        database.add(new CityLocation("Valenzuela City", "High", 30, 6.4, 15.0));
        database.add(new CityLocation("Vigan City", "Moderate", 24, 6.2, 22.5));

        sortDatabase();
    }

    private String validateRisk(String input) throws InvalidRiskLevelException {
        for (String risk : allowedRiskLevels)
            if (risk.equalsIgnoreCase(input)) return risk;
        throw new InvalidRiskLevelException("Allowed: Low, Moderate, High only.");
    }

    private void addLocation() {
        try {
            System.out.print("\nEnter Location Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter Risk Level (Low/Moderate/High): ");
            String risk = validateRisk(scanner.nextLine());
            System.out.print("Historical Earthquakes: ");
            int quakes = Integer.parseInt(scanner.nextLine());
            System.out.print("Last Major Magnitude: ");
            double mag = Double.parseDouble(scanner.nextLine());
            System.out.print("Distance to Fault Line (km): ");
            double dist = Double.parseDouble(scanner.nextLine());
            database.add(new CityLocation(name, risk, quakes, mag, dist));
            sortDatabase();
            System.out.println("Location added successfully!");
        } catch (Exception e) {
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
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = scanner.nextLine().trim();

        Location loc = search(name);
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
            for (Location loc : database)
                if (loc.getRiskLevel().equalsIgnoreCase(risk))
                    System.out.println("- " + loc.getName());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
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

    private void sortDatabase() {
        database.sort(Comparator.comparing(l -> l.getName().toLowerCase()));
    }

    private void mainMenu() {
        int choice = 0;
        do {
            try {
                System.out.println("\n===== PH Earthquake Risk System (QuakeGuard Java OOP) =====");
                System.out.println("1. Search Location Info");
                System.out.println("2. Add New Location");
                System.out.println("3. View All Locations");
                System.out.println("4. Filter by Risk Level");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;
                choice = Integer.parseInt(input);

                switch (choice) {
                    case 1 -> searchLocation();
                    case 2 -> addLocation();
                    case 3 -> listAll();
                    case 4 -> filterByRisk();
                    case 5 -> System.out.println("Exiting... Stay safe!");
                    default -> System.out.println("Invalid choice!");
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        } while (choice != 5);
    }

    public static void main(String[] args) {
        new QuakeGuard().mainMenu();
    }
}
