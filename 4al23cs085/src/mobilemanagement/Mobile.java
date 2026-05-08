package mobilemanagement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public final class Mobile {
    private static final AtomicInteger TOTAL_CREATED = new AtomicInteger(0);
    private static final DateTimeFormatter DISPLAY_TIME = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    private final int id;
    private final String brand;
    private final String model;
    private final String operatingSystem;
    private final int storageGb;
    private final double price;
    private final LocalDateTime createdAt;

    public Mobile() {
        this("Unknown", "Standard", "Android", 64, 0.0);
    }

    public Mobile(String brand, String model, String operatingSystem, int storageGb, double price) {
        this.id = TOTAL_CREATED.incrementAndGet();
        this.brand = clean(brand, "Unknown");
        this.model = clean(model, "Standard");
        this.operatingSystem = clean(operatingSystem, "Android");
        this.storageGb = Math.max(storageGb, 1);
        this.price = Math.max(price, 0.0);
        this.createdAt = LocalDateTime.now();
    }

    private static String clean(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value.trim();
    }

    public static int getTotalMobilesCreated() {
        return TOTAL_CREATED.get();
    }

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public int getStorageGb() {
        return storageGb;
    }

    public double getPrice() {
        return price;
    }

    public String getCreatedAtDisplay() {
        return createdAt.format(DISPLAY_TIME);
    }

    public String getDisplayName() {
        return brand + " " + model;
    }
}
