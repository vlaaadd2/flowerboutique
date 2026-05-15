package org.example.models;

/** Троянда. Додаткова властивість: наявність колючок. */
public class Rose extends Flower {

    private boolean hasThorns;

    public Rose(double price, double stemLength, int freshnessLevel,
                String imagePath, boolean hasThorns, int stockQuantity) {
        super("Троянда", price, stemLength, freshnessLevel, imagePath, stockQuantity);
        this.hasThorns = hasThorns;
    }

    public boolean isHasThorns() { return hasThorns; }
    public void setHasThorns(boolean hasThorns) { this.hasThorns = hasThorns; }
}