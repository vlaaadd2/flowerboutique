package org.example.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BouquetTest {

    private Bouquet bouquet;
    private Rose rose;
    private Tulip tulip;

    @BeforeEach
    void setUp() {
        bouquet = new Bouquet();
        rose  = new Rose(150.0, 60.0, 10, "rose.png", true, 5);
        tulip = new Tulip(80.0, 45.0, 7, "tulip.png", 10);
    }

    @Test
    void testAddFlower() {
        bouquet.addFlower(rose);
        assertEquals(1, bouquet.getFlowers().size());
    }

    @Test
    void testRemoveFlower() {
        bouquet.addFlower(rose);
        bouquet.addFlower(tulip);
        bouquet.removeFlower(rose);
        assertEquals(1, bouquet.getFlowers().size());
        assertFalse(bouquet.getFlowers().contains(rose));
    }

    @Test
    void testGetTotalPriceOnlyFlowers() {
        bouquet.addFlower(rose);
        bouquet.addFlower(tulip);
        assertEquals(230.0, bouquet.getTotalPrice(), 0.001);
    }

    @Test
    void testGetTotalPriceWithAccessory() {
        bouquet.addFlower(rose);
        bouquet.addAccessory(new Accessory("Стрічка", 20.0));
        assertEquals(170.0, bouquet.getTotalPrice(), 0.001);
    }

    @Test
    void testEmptyBouquetPrice() {
        assertEquals(0.0, bouquet.getTotalPrice(), 0.001);
    }
}