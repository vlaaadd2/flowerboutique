package org.example.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccessoryTest {

    @Test void creationSetsFields() {
        Accessory a = new Accessory("Стрічка", 20);
        assertEquals("Стрічка", a.getName());
        assertEquals(20.0, a.getPrice(), 0.001);
    }

    @Test void setterName() {
        Accessory a = new Accessory("Стрічка", 20);
        a.setName("Упаковка");
        assertEquals("Упаковка", a.getName());
    }

    @Test void setterPrice() {
        Accessory a = new Accessory("Стрічка", 20);
        a.setPrice(35);
        assertEquals(35.0, a.getPrice(), 0.001);
    }

    @Test void toStringContainsName() {
        Accessory a = new Accessory("Стрічка", 20);
        assertTrue(a.toString().contains("Стрічка"));
    }
}