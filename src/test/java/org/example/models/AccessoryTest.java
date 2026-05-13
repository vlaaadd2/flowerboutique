package org.example.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccessoryTest {

    @Test
    void testAccessoryCreation() {
        Accessory accessory = new Accessory("Стрічка", 20.0);
        assertEquals("Стрічка", accessory.getName());
        assertEquals(20.0, accessory.getPrice(), 0.001);
    }

    @Test
    void testSetters() {
        Accessory accessory = new Accessory("Стрічка", 20.0);
        accessory.setName("Упаковка");
        accessory.setPrice(35.0);
        assertEquals("Упаковка", accessory.getName());
        assertEquals(35.0, accessory.getPrice(), 0.001);
    }

    @Test
    void testToString() {
        Accessory accessory = new Accessory("Стрічка", 20.0);
        assertTrue(accessory.toString().contains("Стрічка"));
        assertTrue(accessory.toString().contains("20.0"));
    }
}