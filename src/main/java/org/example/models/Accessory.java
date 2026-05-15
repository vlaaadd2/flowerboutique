package org.example.models;


public class Accessory {

    private String name;
    private double price;
    private String imagePath;

    public Accessory(String name, double price) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " — " + price + " грн";
    }


    public String getImagePath() {
        return imagePath;
    }

}