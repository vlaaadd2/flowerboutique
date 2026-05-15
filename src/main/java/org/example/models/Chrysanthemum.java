package org.example.models;


public class Chrysanthemum extends Flower {

    private String petalColor;

    public Chrysanthemum(double price, double stemLength, int freshnessLevel,
                         String imagePath, String petalColor, int stockQuantity) {

        super("Хризантема", price, stemLength, freshnessLevel, imagePath, stockQuantity);
        this.petalColor = petalColor;

    }

    public String getPetalColor() {
        return petalColor;
    }

    public void setPetalColor(String petalColor) {
        this.petalColor = petalColor;
    }

}