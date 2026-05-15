package org.example.models;

/** Тюльпан. */
public class Tulip extends Flower {

    public Tulip(double price, double stemLength, int freshnessLevel,
                 String imagePath, int stockQuantity) {
        super("Тюльпан", price, stemLength, freshnessLevel, imagePath, stockQuantity);
    }
}