package org.example.models;

/** Соняшник. Додаткова властивість: діаметр суцвіття (см). */
public class Sunflower extends Flower {

    private double headDiameter;

    public Sunflower(double price, double stemLength, int freshnessLevel,
                     String imagePath, double headDiameter, int stockQuantity) {
        super("Соняшник", price, stemLength, freshnessLevel, imagePath, stockQuantity);
        this.headDiameter = headDiameter;
    }

    public double getHeadDiameter() { return headDiameter; }
    public void setHeadDiameter(double d) { this.headDiameter = d; }
}