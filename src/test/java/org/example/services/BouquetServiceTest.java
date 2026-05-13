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

    // ── addFlowerToBouquet ────────────────────────────────────

    @Test
    void testAddFlowerToBouquetSuccess() {
        Rose rose = new Rose(150.0, 60.0, 10, "rose.png", true, 5);
        boolean result = service.addFlowerToBouquet(bouquet, rose);

        assertTrue(result);
        assertEquals(1, bouquet.getFlowers().size());
        assertEquals(4, rose.getStockQuantity());
        verify(mockDb, times(1)).updateFlowerStock(rose);
    }

    @Test
    void testAddFlowerToBouquetWhenOutOfStock() {
        Rose rose = new Rose(150.0, 60.0, 10, "rose.png", true, 0);
        boolean result = service.addFlowerToBouquet(bouquet, rose);

        assertFalse(result);
        assertTrue(bouquet.getFlowers().isEmpty());
        verify(mockDb, never()).updateFlowerStock(any());
    }

    // ── removeFlowerFromBouquet ───────────────────────────────

    @Test
    void testRemoveFlowerFromBouquet() {
        Tulip tulip = new Tulip(80.0, 45.0, 7, "tulip.png", 5);
        bouquet.addFlower(tulip);
        tulip.decreaseStock(); // імітуємо що додали

        service.removeFlowerFromBouquet(bouquet, tulip);

        assertTrue(bouquet.getFlowers().isEmpty());
        assertEquals(5, tulip.getStockQuantity()); // повернули 1
        verify(mockDb, times(1)).updateFlowerStock(tulip);
    }

    // ── sortFlowersByFreshness ────────────────────────────────

    @Test
    void testSortFlowersByFreshness() {
        Rose fresh  = new Rose(150.0, 60.0, 10, "rose.png", true, 5);
        Tulip stale = new Tulip(80.0, 45.0, 3,  "tulip.png", 5);
        Lily mid    = new Lily(200.0, 70.0, 7, "lily.png", true, 4);

        bouquet.addFlower(stale);
        bouquet.addFlower(mid);
        bouquet.addFlower(fresh);

        service.sortFlowersByFreshness(bouquet);

        List<Flower> flowers = bouquet.getFlowers();
        assertEquals(10, flowers.get(0).getFreshnessLevel());
        assertEquals(7,  flowers.get(1).getFreshnessLevel());
        assertEquals(3,  flowers.get(2).getFreshnessLevel());
    }

    @Test
    void testSortEmptyBouquet() {
        assertDoesNotThrow(() -> service.sortFlowersByFreshness(bouquet));
    }

    // ── findFlowersByStemLength ───────────────────────────────

    @Test
    void testFindFlowersByStemLength_Found() {
        bouquet.addFlower(new Rose(150.0, 60.0, 10, "rose.png", true, 5));
        bouquet.addFlower(new Tulip(80.0, 45.0, 7, "tulip.png", 5));
        bouquet.addFlower(new Lily(200.0, 70.0, 9, "lily.png", true, 4));

        List<Flower> result = service.findFlowersByStemLength(bouquet, 50.0, 65.0);

        assertEquals(1, result.size());
        assertEquals("Троянда", result.get(0).getName());
    }

    @Test
    void testFindFlowersByStemLength_MultipleFound() {
        bouquet.addFlower(new Rose(150.0, 60.0, 10, "rose.png", true, 5));
        bouquet.addFlower(new Tulip(80.0, 55.0, 7, "tulip.png", 5));
        bouquet.addFlower(new Lily(200.0, 70.0, 9, "lily.png", true, 4));

        List<Flower> result = service.findFlowersByStemLength(bouquet, 50.0, 65.0);

        assertEquals(2, result.size());
    }

    @Test
    void testFindFlowersByStemLength_NoneFound() {
        bouquet.addFlower(new Rose(150.0, 60.0, 10, "rose.png", true, 5));
        List<Flower> result = service.findFlowersByStemLength(bouquet, 1.0, 10.0);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindFlowersByStemLength_ExactBoundary() {
        bouquet.addFlower(new Tulip(80.0, 45.0, 7, "tulip.png", 5));
        List<Flower> result = service.findFlowersByStemLength(bouquet, 45.0, 45.0);
        assertEquals(1, result.size());
    }
}