package org.example.services;

import org.example.dao.DatabaseManager;
import org.example.models.Bouquet;
import org.example.models.Flower;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public class BouquetService {

    private static final Logger logger = LoggerFactory.getLogger(BouquetService.class);
    private final DatabaseManager dbManager;

    public BouquetService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }



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



    public void removeFlowerFromBouquet(Bouquet bouquet, Flower flower) {

        bouquet.removeFlower(flower);
        flower.increaseStock();


        dbManager.updateFlowerStock(flower);
        logger.info("Видалено '{}' з букета. Залишок: {} шт.",
                flower.getName(), flower.getStockQuantity());

    }



    public void sortFlowersByFreshness(Bouquet bouquet) {

        bouquet.getFlowers().sort(
                Comparator.comparingInt(Flower::getFreshnessLevel).reversed()
        );

        logger.info("Букет відсортовано за свіжістю.");
    }



    public List<Flower> findFlowersByStemLength(Bouquet bouquet, double minLength, double maxLength) {

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