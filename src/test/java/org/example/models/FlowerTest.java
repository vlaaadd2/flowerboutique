package org.example.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FlowerTest {

    @Test void roseProperties() {
        Rose r = new Rose(150, 60, 10, "rose.png", true, 5);
        assertEquals("Троянда", r.getName());
        assertEquals(150, r.getPrice(), 0.001);
        assertEquals(60, r.getStemLength(), 0.001);
        assertEquals(10, r.getFreshnessLevel());
        assertTrue(r.isHasThorns());
        assertEquals(5, r.getStockQuantity());
    }

    @Test void tulipProperties() {
        Tulip t = new Tulip(80, 45, 7, "tulip.png", 10);
        assertEquals("Тюльпан", t.getName());
        assertEquals(10, t.getStockQuantity());
    }

    @Test void lilyProperties() {
        Lily l = new Lily(200, 70, 9, "lily.png", true, 4);
        assertEquals("Лілія", l.getName());
        assertTrue(l.isFragrant());
    }

    @Test void chrysanthemumProperties() {
        Chrysanthemum c = new Chrysanthemum(90, 50, 8, "c.png", "Біла", 12);
        assertEquals("Хризантема", c.getName());
        assertEquals("Біла", c.getPetalColor());
    }

    @Test void orchidProperties() {
        Orchid o = new Orchid(350, 80, 10, "o.png", 5, 3);
        assertEquals("Орхідея", o.getName());
        assertEquals(5, o.getBloomCount());
    }

    @Test void sunflowerProperties() {
        Sunflower s = new Sunflower(60, 90, 9, "s.png", 15.0, 15);
        assertEquals("Соняшник", s.getName());
        assertEquals(15.0, s.getHeadDiameter(), 0.001);
    }

    @Test void isAvailableWhenStockPositive() {
        Rose r = new Rose(100, 50, 8, "rose.png", false, 1);
        assertTrue(r.isAvailable());
    }

    @Test void isNotAvailableWhenStockZero() {
        Rose r = new Rose(100, 50, 8, "rose.png", false, 0);
        assertFalse(r.isAvailable());
    }

    @Test void decreaseStockReducesBy1() {
        Rose r = new Rose(100, 50, 8, "rose.png", false, 3);
        r.decreaseStock();
        assertEquals(2, r.getStockQuantity());
    }

    @Test void decreaseStockDoesNotGoBelowZero() {
        Rose r = new Rose(100, 50, 8, "rose.png", false, 0);
        r.decreaseStock();
        assertEquals(0, r.getStockQuantity());
    }

    @Test void increaseStockAdds1() {
        Tulip t = new Tulip(80, 45, 7, "tulip.png", 3);
        t.increaseStock();
        assertEquals(4, t.getStockQuantity());
    }

    @Test void toStringContainsName() {
        Rose r = new Rose(150, 60, 10, "rose.png", true, 5);
        assertTrue(r.toString().contains("Троянда"));
    }
}