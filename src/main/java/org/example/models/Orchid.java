package org.example.models;


public class Orchid extends Flower {

    private int bloomCount;

    public Orchid(double price, double stemLength, int freshnessLevel,
                  String imagePath, int bloomCount, int stockQuantity) {

        super("Орхідея", price, stemLength, freshnessLevel, imagePath, stockQuantity);
        this.bloomCount = bloomCount;

    }

    public int getBloomCount() {
        return bloomCount;
    }

    public void setBloomCount(int bloomCount) {
        this.bloomCount = bloomCount;
    }

}