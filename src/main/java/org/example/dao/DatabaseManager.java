package org.example.dao;

import org.example.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DatabaseManager {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static final String DB_URL = "jdbc:sqlite:boutique.db";

    public DatabaseManager() {
        createTables();
        logger.info("DatabaseManager ініціалізовано. БД: {}", DB_URL);
    }


    private void createTables() {
        String sql = """
                CREATE TABLE IF NOT EXISTS flowers (
                    id               INTEGER PRIMARY KEY AUTOINCREMENT,
                    flower_type      TEXT    NOT NULL,
                    price            REAL    NOT NULL,
                    stem_length      REAL    NOT NULL,
                    freshness_level  INTEGER NOT NULL,
                    image_path       TEXT,
                    stock_quantity   INTEGER NOT NULL DEFAULT 10,
                    has_thorns       INTEGER DEFAULT 0,
                    is_fragrant      INTEGER DEFAULT 0,
                    petal_color      TEXT,
                    bloom_count      INTEGER DEFAULT 0,
                    head_diameter    REAL    DEFAULT 0.0
                );
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            logger.debug("Таблиця flowers перевірена/створена.");
        } catch (SQLException e) {
            logger.error("Помилка створення таблиці: {}", e.getMessage(), e);
        }
    }


    public void addFlowerToDB(Flower flower) {

        String sql = """
                INSERT INTO flowers
                    (flower_type, price, stem_length, freshness_level, image_path,
                     stock_quantity, has_thorns, is_fragrant, petal_color, bloom_count, head_diameter)
                VALUES (?,?,?,?,?,?,?,?,?,?,?)
                """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, flower.getName());
            ps.setDouble(2, flower.getPrice());
            ps.setDouble(3, flower.getStemLength());
            ps.setInt(4, flower.getFreshnessLevel());
            ps.setString(5, flower.getImagePath());
            ps.setInt(6, flower.getStockQuantity());
            ps.setBoolean(7, flower instanceof Rose && ((Rose) flower).isHasThorns());
            ps.setBoolean(8, flower instanceof Lily && ((Lily) flower).isFragrant());
            ps.setString(9, flower instanceof Chrysanthemum
                    ? ((Chrysanthemum) flower).getPetalColor() : null);
            ps.setInt(10, flower instanceof Orchid
                    ? ((Orchid) flower).getBloomCount() : 0);
            ps.setDouble(11, flower instanceof Sunflower
                    ? ((Sunflower) flower).getHeadDiameter() : 0.0);

            ps.executeUpdate();
            logger.info("Квітку '{}' додано до БД.", flower.getName());
        } catch (SQLException e) {
            logger.error("Помилка додавання квітки '{}': {}", flower.getName(), e.getMessage(), e);
        }
    }


    public List<Flower> getAllFlowers() {

        List<Flower> flowers = new ArrayList<>();
        String sql = "SELECT * FROM flowers ORDER BY flower_type, price";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Flower f = mapRowToFlower(rs);
                if (f != null) flowers.add(f);
            }

            logger.debug("Отримано {} квітів з БД.", flowers.size());
        } catch (SQLException e) {
            logger.error("Помилка отримання квітів: {}", e.getMessage(), e);
        }
        return flowers;
    }


    public void updateFlowerStock(Flower flower) {

        String sql = """
                UPDATE flowers SET stock_quantity = ?
                WHERE flower_type = ? AND ABS(price - ?) < 0.001 AND ABS(stem_length - ?) < 0.001
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, flower.getStockQuantity());
            ps.setString(2, flower.getName());
            ps.setDouble(3, flower.getPrice());
            ps.setDouble(4, flower.getStemLength());
            ps.executeUpdate();
            logger.info("Склад '{}': {} шт.", flower.getName(), flower.getStockQuantity());
        } catch (SQLException e) {
            logger.error("Помилка оновлення складу '{}': {}", flower.getName(), e.getMessage(), e);
        }
    }


    private Flower mapRowToFlower(ResultSet rs) throws SQLException {

        String type      = rs.getString("flower_type");
        double price     = rs.getDouble("price");
        double stemLen   = rs.getDouble("stem_length");
        int freshness    = rs.getInt("freshness_level");
        String imagePath = rs.getString("image_path");
        int stock        = rs.getInt("stock_quantity");

        return switch (type) {
            case "Троянда"    -> new Rose(price, stemLen, freshness, imagePath,
                    rs.getBoolean("has_thorns"), stock);
            case "Тюльпан"    -> new Tulip(price, stemLen, freshness, imagePath, stock);
            case "Лілія"      -> new Lily(price, stemLen, freshness, imagePath,
                    rs.getBoolean("is_fragrant"), stock);
            case "Хризантема" -> new Chrysanthemum(price, stemLen, freshness, imagePath,
                    rs.getString("petal_color"), stock);
            case "Орхідея"    -> new Orchid(price, stemLen, freshness, imagePath,
                    rs.getInt("bloom_count"), stock);
            case "Соняшник"   -> new Sunflower(price, stemLen, freshness, imagePath,
                    rs.getDouble("head_diameter"), stock);
            default -> { logger.warn("Невідомий тип: {}", type); yield null; }
        };
    }
}