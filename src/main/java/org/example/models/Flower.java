package org.example.models;

/**
 * Абстрактний базовий клас для всіх квітів.
 * Інкапсулює загальні властивості: назву, ціну, довжину стебла,
 * рівень свіжості, шлях до зображення та кількість на складі.
 */
public abstract class Flower {

    private String name;
    private double price;
    private double stemLength;
    private int freshnessLevel;
    private String imagePath;
    private int stockQuantity;

    public Flower(String name, double price, double stemLength,
                  int freshnessLevel, String imagePath, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stemLength = stemLength;
        this.freshnessLevel = freshnessLevel;
        this.imagePath = imagePath;
        this.stockQuantity = stockQuantity;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getStemLength() { return stemLength; }
    public void setStemLength(double stemLength) { this.stemLength = stemLength; }

    public int getFreshnessLevel() { return freshnessLevel; }
    public void setFreshnessLevel(int freshnessLevel) { this.freshnessLevel = freshnessLevel; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int qty) { this.stockQuantity = qty; }

    public boolean isAvailable() { return stockQuantity > 0; }

    public void decreaseStock() {
        if (stockQuantity > 0) stockQuantity--;
    }

    public void increaseStock() {
        stockQuantity++;
    }

    @Override
    public String toString() {
        return name + "  |  " + stemLength + " см  |  св." + freshnessLevel
                + "/10  |  " + price + " грн  |  " + stockQuantity + " шт";
    }
}