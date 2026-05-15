package org.example.models;

import java.util.ArrayList;
import java.util.List;


public class Bouquet {

    private List<Flower> flowers;
    private List<Accessory> accessories;

    public Bouquet() {
        this.flowers = new ArrayList<>();
        this.accessories = new ArrayList<>();
    }

    public void addFlower(Flower flower) {
        flowers.add(flower);
    }

    public void removeFlower(Flower flower) {
        flowers.remove(flower);
    }

    public void addAccessory(Accessory accessory) {
        accessories.add(accessory);
    }


    public double getTotalPrice() {

        double total = 0;
        for (Flower f : flowers) total += f.getPrice();
        for (Accessory a : accessories) total += a.getPrice();
        return total;

    }

    public List<Flower> getFlowers() {
        return flowers;
    }

    public List<Accessory> getAccessories() {
        return accessories;
    }

}