package org.example.dao;

import org.example.models.Flower;
import org.example.models.Rose;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseManagerTest {

    private static DatabaseManager dbManager;

    @BeforeAll
    static void setUp() {

        dbManager = new DatabaseManager();


        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:boutique.db");
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM flowers WHERE price = 999.0");
        } catch (Exception e) {
            System.err.println("Помилка очищення БД: " + e.getMessage());
        }

    }

    @Test
    @Order(1)
    void testAddAndGetAllFlowers() {

        int initialSize = dbManager.getAllFlowers().size();


        Rose testRose = new Rose(999.0, 99.0, 10, "test_rose.png", true, 50);

        dbManager.addFlowerToDB(testRose);

        List<Flower> flowers = dbManager.getAllFlowers();
        assertTrue(flowers.size() > initialSize, "Кількість квітів у БД мала збільшитись");


        Flower foundFlower = flowers.stream()
                .filter(f -> f.getName().equals("Троянда") && f.getPrice() == 999.0)
                .findFirst()
                .orElse(null);

        assertNotNull(foundFlower, "Тестова квітка має бути в базі");
        assertEquals(50, foundFlower.getStockQuantity());

    }

    @Test
    @Order(2)
    void testUpdateFlowerStock() {

        List<Flower> flowers = dbManager.getAllFlowers();

        Flower testFlower = flowers.stream()
                .filter(f -> f.getName().equals("Троянда") && f.getPrice() == 999.0)
                .findFirst()
                .orElse(null);

        assertNotNull(testFlower);


        testFlower.setStockQuantity(15);
        dbManager.updateFlowerStock(testFlower);


        List<Flower> updatedFlowers = dbManager.getAllFlowers();

        Flower updatedTestFlower = updatedFlowers.stream()
                .filter(f -> f.getName().equals("Троянда") && f.getPrice() == 999.0)
                .findFirst()
                .orElse(null);

        assertNotNull(updatedTestFlower);
        assertEquals(15, updatedTestFlower.getStockQuantity());

    }

}