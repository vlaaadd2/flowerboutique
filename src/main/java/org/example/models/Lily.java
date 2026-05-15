package org.example.models;


public class Lily extends Flower {

    private boolean fragrant;

    public Lily(double price, double stemLength, int freshnessLevel,
                String imagePath, boolean fragrant, int stockQuantity) {

        super("Лілія", price, stemLength, freshnessLevel, imagePath, stockQuantity);
        this.fragrant = fragrant;

    }

    public boolean isFragrant() {
        return fragrant;
    }

    public void setFragrant(boolean fragrant) {
        this.fragrant = fragrant;
    }

}