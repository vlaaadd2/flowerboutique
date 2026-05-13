package org.example.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlowerTest {

    @Test
    void testRoseProperties() {
        Rose rose = new Rose(150.0, 60.0, 10, "rose.png", true, 5);
        assertEquals("Троянда", rose.getName());
        assertEquals(150.0, rose.getPrice(), 0.001);
        assertEquals(60.0, rose.getStemLength(), 0.001);
        assertEquals(10, rose.getFreshnessLevel());
        assertTrue(rose.isHasThorns());
        assertEquals(5, rose.getStockQuantity());
    }

    @Test
    void testTulipProperties() {
        Tulip tulip = new Tulip(80.0, 45.0, 7, "tulip.png", 10);
        assertEquals("Тюльпан", tulip.getName());
        assertEquals(10, tulip.getStockQuantity());
    }

    @Test
    void testLilyProperties() {
        Lily lily = new Lily(200.0, 70.0, 9, "lily.png", true, 4);
        assertEquals("Лілія", lily.getName());
        assertTrue(lily.isFragrant());
    }

    @Test
    void testChrysanthemumProperties() {
        Chrysanthemum ch = new Chrysanthemum(90.0, 50.0, 8, "ch.png", "Біла", 12);
        assertEquals("Хризантема", ch.getName());
        assertEquals("Біла", ch.getPetalColor());
    }

    @Test
    void testOrchidProperties() {
        Orchid orchid = new Orchid(350.0, 80.0, 10, "orchid.png", 5, 3);
        assertEquals("Орхідея", orchid.getName());
        assertEquals(5, orchid.getBloomCount());
    }

    @Test
    void testSunflowerProperties() {
        Sunflower sf = new Sunflower(60.0, 90.0, 9, "sf.png", 15.0, 15);
        assertEquals("Соняшник", sf.getName());
        assertEquals(15.0, sf.getHeadDiameter(), 0.001);
    }

    @Test
    void testIsAvailable() {
        Rose rose = new Rose(100.0, 50.0, 8, "rose.png", false, 1);
        assertTrue(rose.isAvailable());
        rose.decreaseStock();
        assertFalse(rose.isAvailable());
    }

    @Test
    void testDecreaseStockDoesNotGoBelowZero() {
        Rose rose = new Rose(100.0, 50.0, 8, "rose.png", false, 0);
        rose.decreaseStock();
        assertEquals(0, rose.getStockQuantity());
    }

    @Test
    void testIncreaseStock() {
        Tulip tulip = new Tulip(80.0, 45.0, 7, "tulip.png", 3);
        tulip.increaseStock();
        assertEquals(4, tulip.getStockQuantity());
    }

    @Test
    void testToString() {
        Rose rose = new Rose(150.0, 60.0, 10, "rose.png", true, 5);
        String str = rose.toString();
        assertTrue(str.contains("Троянда"));
        assertTrue(str.contains("60.0"));
        assertTrue(str.contains("150.0"));
    }
}