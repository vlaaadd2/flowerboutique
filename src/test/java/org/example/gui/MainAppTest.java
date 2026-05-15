package org.example.gui;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.example.models.Flower;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MainAppTest {

    private MainApp mainApp;

    @BeforeAll
    static void setUpAll() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:boutique.db");
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS flowers");
        } catch (Exception ignored) {}
    }

    @Start
    public void start(Stage stage) throws Exception {
        mainApp = new MainApp();
        mainApp.init();
        mainApp.start(stage);
    }

    @AfterEach
    void tearDown() {

        Platform.runLater(() -> {
            List<Window> windows = new ArrayList<>(Window.getWindows());

            for (Window window : windows) {

                if (window instanceof Stage) {
                    ((Stage) window).close();
                }

            }

        });

        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    @Order(1)
    void testAFullGuiFlow(FxRobot robot) {

        ListView<Flower> assortmentList = getAssortmentList(robot);
        assertNotNull(assortmentList, "Список асортименту не знайдено");

        Button addBtn = robot.lookup("➕  Додати до букета").queryButton();
        robot.moveTo(addBtn).moveTo(assortmentList);

        Button accBtn = robot.lookup(n -> n instanceof Button && ((Button) n).getText().contains("Атласна стрічка")).queryButton();
        robot.moveTo(accBtn).moveTo(assortmentList);

        robot.interact(() -> {
            Flower f1 = assortmentList.getItems().get(0);
            f1.setStockQuantity(3);
            Flower f2 = assortmentList.getItems().get(1);
            f2.setStockQuantity(0);
            assortmentList.refresh();
        });

        WaitForAsyncUtils.waitForFxEvents();


        robot.interact(() -> assortmentList.getSelectionModel().select(1));
        robot.clickOn(addBtn);
        WaitForAsyncUtils.waitForFxEvents();
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);

        robot.interact(() -> assortmentList.getSelectionModel().clearSelection());
        robot.clickOn(addBtn);
        WaitForAsyncUtils.waitForFxEvents();
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);

        robot.interact(() -> assortmentList.getSelectionModel().select(0));
        robot.clickOn(addBtn);
        robot.interact(() -> assortmentList.getSelectionModel().select(2));
        robot.clickOn(addBtn);

        robot.clickOn("🔃  Сортувати за свіжістю");

        ImageView canvasFlower = getCanvasImageView(robot);
        assertNotNull(canvasFlower, "Квітка мала з'явитись на полотні");
        robot.drag(canvasFlower).dropBy(50, 50);
        robot.clickOn(canvasFlower);
        robot.clickOn("🗑  Видалити вибраний елемент");

        robot.clickOn(accBtn);
        WaitForAsyncUtils.waitForFxEvents();
        ImageView canvasAccessory = getCanvasImageView(robot);
        assertNotNull(canvasAccessory, "Аксесуар мав з'явитись на полотні");
        robot.drag(canvasAccessory).dropBy(-30, 20);
        robot.clickOn(canvasAccessory);
        robot.clickOn("🗑  Видалити вибраний елемент");

        robot.clickOn("🔍  Пошук за довжиною стебла");
        TextField minF = robot.lookup(n -> n instanceof TextField && "Мін, см".equals(((TextField) n).getPromptText())).query();
        TextField maxF = robot.lookup(n -> n instanceof TextField && "Макс, см".equals(((TextField) n).getPromptText())).query();
        robot.clickOn(minF).write("10");
        robot.clickOn(maxF).write("100");
        robot.clickOn("Знайти");
        robot.press(KeyCode.ESCAPE).release(KeyCode.ESCAPE);

        robot.clickOn("🔍  Пошук за довжиною стебла");
        TextField minFInv = robot.lookup(n -> n instanceof TextField && "Мін, см".equals(((TextField) n).getPromptText())).query();
        robot.clickOn(minFInv).write("error");
        robot.clickOn("Знайти");
        robot.press(KeyCode.ESCAPE).release(KeyCode.ESCAPE);
    }

    @Test
    @Order(2)
    void testBExistingDatabaseAndInitialization(FxRobot robot) {
        ListView<Flower> assortmentList = getAssortmentList(robot);
        assertNotNull(assortmentList);
        assertFalse(assortmentList.getItems().isEmpty());
    }

    private ListView<Flower> getAssortmentList(FxRobot robot) {
        List<ListView> lists = robot.lookup(".boutique-list").queryAllAs(ListView.class).stream().collect(Collectors.toList());
        return lists.isEmpty() ? null : lists.get(0);
    }

    private ImageView getCanvasImageView(FxRobot robot) {
        return robot.lookup(n -> n instanceof ImageView).queryAllAs(ImageView.class).stream()
                .filter(n -> n.getParent() != null && n.getParent() instanceof Pane && !(n.getParent() instanceof Button))
                .findFirst().orElse(null);
    }
}