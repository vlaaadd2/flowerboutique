package org.example.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.models.Flower;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class MainAppTest {

    private MainApp mainApp;

    @Start
    public void start(Stage stage) throws Exception {

        mainApp = new MainApp();
        mainApp.init();
        mainApp.start(stage);

    }

    @Test
    void testGuiInitializationAndAddFlower(FxRobot robot) {

        ListView<Flower> assortmentList = robot.lookup(".boutique-list").queryListView();
        assertNotNull(assortmentList);
        assertFalse(assortmentList.getItems().isEmpty(), "Асортимент з БД не має бути порожнім");


        robot.interact(() -> assortmentList.getSelectionModel().selectFirst());


        Button addBtn = robot.lookup(n -> n instanceof Button && ((Button) n).getText().contains("Додати")).queryButton();
        robot.clickOn(addBtn);


        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Label countLabel = robot.lookup(n -> n instanceof Label && ((Label) n).getText().equals("1 шт.")).queryAs(Label.class);
        assertNotNull(countLabel, "Лічильник букета мав оновитись до '1 шт.'");


        Button sortBtn = robot.lookup(n -> n instanceof Button && ((Button) n).getText().contains("Сортувати")).queryButton();
        robot.clickOn(sortBtn);
    }
}