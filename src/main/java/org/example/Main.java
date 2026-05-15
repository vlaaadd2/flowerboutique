package org.example;

import javafx.application.Application;
import org.example.gui.MainApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Точка входу в додаток. */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("========== Запуск Flower Boutique ==========");
        Application.launch(MainApp.class, args);
    }
}