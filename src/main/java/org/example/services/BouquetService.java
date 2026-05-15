package org.example.services;

import org.example.dao.DatabaseManager;
import org.example.models.Bouquet;
import org.example.models.Flower;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * BouquetService — бізнес-логіка для роботи з букетом:
 * додавання/видалення квітів (з оновленням складу),
 * сортування за свіжістю, пошук за довжиною стебла.
 */
public class BouquetService {

    private static final Logger logger = LoggerFactory.getLogger(BouquetService.class);
    private final DatabaseManager dbManager;

    public BouquetService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Додає квітку до букета. Зменшує склад і зберігає в БД.
     *
     * @return true — успішно; false — немає на складі
     */
    public boolean addFlowerToBouquet(Bouquet bouquet, Flower flower) {
        if (!flower.isAvailable()) {
            logger.warn("Квітка '{}' недоступна (склад = 0).", flower.getName());
            return false;
        }
        bouquet.addFlower(flower);
        flower.decreaseStock();
        dbManager.updateFlowerStock(flower);
        logger.info("Додано '{}' до букета. Залишок: {} шт.",
                flower.getName(), flower.getStockQuantity());
        return true;
    }

    /**
     * Видаляє квітку з букета. Повертає на склад і зберігає в БД.
     */
    public void removeFlowerFromBouquet(Bouquet bouquet, Flower flower) {
        bouquet.removeFlower(flower);
        flower.increaseStock();
        dbManager.updateFlowerStock(flower);
        logger.info("Видалено '{}' з букета. Залишок: {} шт.",
                flower.getName(), flower.getStockQuantity());
    }

    /**
     * Сортує квіти в букеті за рівнем свіжості (найсвіжіші — першими).
     */
    public void sortFlowersByFreshness(Bouquet bouquet) {
        bouquet.getFlowers().sort(
                Comparator.comparingInt(Flower::getFreshnessLevel).reversed()
        );
        logger.info("Букет відсортовано за свіжістю.");
    }

    /**
     * Повертає квіти з букета, чия довжина стебла входить у заданий діапазон.
     */
    public List<Flower> findFlowersByStemLength(Bouquet bouquet,
                                                double minLength,
                                                double maxLength) {
        List<Flower> result = new ArrayList<>();
        for (Flower f : bouquet.getFlowers()) {
            if (f.getStemLength() >= minLength && f.getStemLength() <= maxLength) {
                result.add(f);
            }
        }
        logger.info("Пошук [{}-{}] см: знайдено {} квіток.", minLength, maxLength, result.size());
        return result;
    }
}