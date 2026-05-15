package org.example.services;

import org.example.dao.DatabaseManager;
import org.example.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BouquetServiceTest {

    private BouquetService service;
    private DatabaseManager mockDb;
    private Bouquet bouquet;

    @BeforeEach
    void setUp() {
        mockDb  = Mockito.mock(DatabaseManager.class);
        service = new BouquetService(mockDb);
        bouquet = new Bouquet();
    }





    @Test void addAvailableFlowerSucceeds() {
        Rose r = new Rose(150, 60, 10, "rose.png", true, 5);
        assertTrue(service.addFlowerToBouquet(bouquet, r));
        assertEquals(1, bouquet.getFlowers().size());
        assertEquals(4, r.getStockQuantity());
        verify(mockDb).updateFlowerStock(r);
    }

    @Test void addOutOfStockFlowerFails() {
        Rose r = new Rose(150, 60, 10, "rose.png", true, 0);
        assertFalse(service.addFlowerToBouquet(bouquet, r));
        assertTrue(bouquet.getFlowers().isEmpty());
        verify(mockDb, never()).updateFlowerStock(any());
    }





    @Test void removeFlowerReturnsToStock() {
        Tulip t = new Tulip(80, 45, 7, "tulip.png", 5);
        bouquet.addFlower(t);
        t.decreaseStock();                       // склад = 4
        service.removeFlowerFromBouquet(bouquet, t);
        assertTrue(bouquet.getFlowers().isEmpty());
        assertEquals(5, t.getStockQuantity());   // повернули 1
        verify(mockDb).updateFlowerStock(t);
    }





    @Test void sortOrderIsDescending() {
        Rose  fresh = new Rose(150, 60, 10, "r.png", true, 5);
        Tulip mid   = new Tulip(80, 45, 7, "t.png", 5);
        Lily  stale = new Lily(200, 70, 3, "l.png", true, 4);
        bouquet.addFlower(stale); bouquet.addFlower(mid); bouquet.addFlower(fresh);

        service.sortFlowersByFreshness(bouquet);
        List<Flower> flowers = bouquet.getFlowers();

        assertEquals(10, flowers.get(0).getFreshnessLevel());
        assertEquals(7,  flowers.get(1).getFreshnessLevel());
        assertEquals(3,  flowers.get(2).getFreshnessLevel());
    }

    @Test void sortEmptyBouquetDoesNotThrow() {
        assertDoesNotThrow(() -> service.sortFlowersByFreshness(bouquet));
    }





    @Test void findReturnsMatchingFlowers() {
        bouquet.addFlower(new Rose(150, 60, 10, "r.png", true, 5));
        bouquet.addFlower(new Tulip(80, 45, 7, "t.png", 5));
        bouquet.addFlower(new Lily(200, 70, 9, "l.png", true, 4));

        List<Flower> found = service.findFlowersByStemLength(bouquet, 50, 65);
        assertEquals(1, found.size());
        assertEquals("Троянда", found.get(0).getName());
    }

    @Test void findReturnsMultiple() {
        bouquet.addFlower(new Rose(150, 60, 10, "r.png", true, 5));
        bouquet.addFlower(new Tulip(80, 55, 7, "t.png", 5));

        List<Flower> found = service.findFlowersByStemLength(bouquet, 50, 65);
        assertEquals(2, found.size());
    }

    @Test void findReturnsEmptyWhenNoMatch() {
        bouquet.addFlower(new Rose(150, 60, 10, "r.png", true, 5));
        assertTrue(service.findFlowersByStemLength(bouquet, 1, 10).isEmpty());
    }

    @Test void findIncludesExactBoundary() {
        bouquet.addFlower(new Tulip(80, 45, 7, "t.png", 5));
        assertEquals(1, service.findFlowersByStemLength(bouquet, 45, 45).size());
    }
}