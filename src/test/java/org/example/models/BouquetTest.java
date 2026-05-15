package org.example.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BouquetTest {

    private Bouquet bouquet;
    private Rose    rose;
    private Tulip   tulip;

    @BeforeEach
    void setUp() {
        bouquet = new Bouquet();
        rose    = new Rose(150, 60, 10, "rose.png", true, 5);
        tulip   = new Tulip(80, 45, 7, "tulip.png", 10);
    }

    @Test void addFlowerIncreasesSize() {
        bouquet.addFlower(rose);
        assertEquals(1, bouquet.getFlowers().size());
    }

    @Test void removeFlowerDecreasesSize() {
        bouquet.addFlower(rose);
        bouquet.addFlower(tulip);
        bouquet.removeFlower(rose);
        assertEquals(1, bouquet.getFlowers().size());
        assertFalse(bouquet.getFlowers().contains(rose));
    }

    @Test void totalPriceFlowersOnly() {
        bouquet.addFlower(rose);
        bouquet.addFlower(tulip);
        assertEquals(230.0, bouquet.getTotalPrice(), 0.001);
    }

    @Test void totalPriceWithAccessory() {
        bouquet.addFlower(rose);
        bouquet.addAccessory(new Accessory("Стрічка", 20));
        assertEquals(170.0, bouquet.getTotalPrice(), 0.001);
    }

    @Test void emptyBouquetPriceIsZero() {
        assertEquals(0.0, bouquet.getTotalPrice(), 0.001);
    }

    @Test void addAccessoryAppearsInList() {
        Accessory a = new Accessory("Упаковка", 30);
        bouquet.addAccessory(a);
        assertEquals(1, bouquet.getAccessories().size());
        assertEquals("Упаковка", bouquet.getAccessories().get(0).getName());
    }
}